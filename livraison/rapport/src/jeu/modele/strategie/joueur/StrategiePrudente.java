package jeu.modele.strategie.joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;
import jeu.modele.action.ActivationBouclier;
import jeu.modele.action.Attente;
import jeu.modele.action.Deplacement;
import jeu.modele.action.PoseExplosif;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Visibilite;

/**
 * Stratégie défensive : se protège, ramasse les pastilles, pose des mines et ne tire que si un ennemi passe devant.
 */
public class StrategiePrudente implements StrategieJoueur {

    private Random hasard;

    public StrategiePrudente(Random hasard) {
        this.hasard = hasard;
    }

    public Action choisirAction(VueJoueur vue) {
        InfosCombattant moi = vue.getMoi();
        Position ici = moi.getPosition();
        Parametres p = vue.getParametres();

        // une bombe va exploser à côté
        if (Analyse.estMenaceeParBombe(vue, ici)) {
            Action fuite = Analyse.fuiteDesBombes(vue, hasard);
            if (fuite != null && Analyse.peutPayer(vue, p.getCoutDeplacement())) {
                return fuite;
            }
            if (!moi.aBouclierActif() && Analyse.peutPayer(vue, p.getCoutBouclier())) {
                return new ActivationBouclier();
            }
        }

        // un ennemi est dans la ligne de mire
        InfosCombattant cible = Analyse.ennemiEnLigneDeMire(vue, ici);
        if (cible != null && moi.getMunitions(TypeArme.TIR) > 0 && Analyse.peutPayer(vue, p.getCoutTir())) {
            return Analyse.tirVers(vue, cible);
        }

        // un ennemi peut nous tirer dessus : on se cache ou on lève le bouclier
        if (Analyse.estSousLeFeu(vue, ici)) {
            Action abri = abriHorsDuFeu(vue);
            if (abri != null && Analyse.peutPayer(vue, p.getCoutDeplacement())) {
                return abri;
            }
            if (!moi.aBouclierActif() && Analyse.peutPayer(vue, p.getCoutBouclier())) {
                return new ActivationBouclier();
            }
        }

        // on reprend de l'énergie
        if (moi.getEnergie() < p.getEnergieInitiale() && Analyse.peutPayer(vue, p.getCoutDeplacement())) {
            Direction d = Analyse.versPastille(vue);
            if (d != null) {
                return new Deplacement(d);
            }
        }

        // une fois sur quatre, on pose une mine invisible à côté de soi
        if (moi.getMunitions(TypeArme.MINE) > 0 && Analyse.peutPayer(vue, p.getCoutPose()) && hasard.nextInt(4) == 0) {
            Direction d = Direction.values()[hasard.nextInt(8)];
            Action mine = new PoseExplosif(TypeArme.MINE, d, Visibilite.PRIVEE);
            if (mine.estPossible(vue, moi)) {
                return mine;
            }
        }
        return new Attente();
    }

    // déplacement vers une case voisine où aucun ennemi ne peut tirer, ou null
    private Action abriHorsDuFeu(VueJoueur vue) {
        List<Action> abris = new ArrayList<>();
        for (Direction d : Direction.cardinales()) {
            Position q = vue.getMoi().getPosition().voisine(d);
            if (Analyse.estTraversable(vue, q) && !Analyse.estSousLeFeu(vue, q) && !Analyse.estMenaceeParBombe(vue, q)) {
                abris.add(new Deplacement(d));
            }
        }
        if (abris.isEmpty()) {
            return null;
        }
        return abris.get(hasard.nextInt(abris.size()));
    }

    public String getNom() {
        return "prudent";
    }
}
