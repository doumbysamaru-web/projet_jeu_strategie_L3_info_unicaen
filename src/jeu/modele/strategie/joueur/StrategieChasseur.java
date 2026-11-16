package jeu.modele.strategie.joueur;

import java.util.Random;

import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;
import jeu.modele.action.ActivationBouclier;
import jeu.modele.action.Attente;
import jeu.modele.action.Deplacement;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Visibilite;

/**
 * Stratégie offensive : fuit les bombes, tire dès que possible et va chercher les ennemis.
 */
public class StrategieChasseur implements StrategieJoueur {

    private Random hasard;

    public StrategieChasseur(Random hasard) {
        this.hasard = hasard;
    }

    // On teste les situations dans l'ordre d'importance et on renvoie la première action qui convient :
    // survivre d'abord, ensuite tirer, ensuite reprendre de l'énergie, enfin s'approcher des ennemis.
    // Avant chaque action on vérifie avec peutPayer() qu'on ne se tue pas avec son propre coût.
    public Action choisirAction(VueJoueur vue) {
        InfosCombattant moi = vue.getMoi();
        Position ici = moi.getPosition();
        Parametres p = vue.getParametres();

        // 1. une bombe va exploser à côté : on s'éloigne, sinon on lève le bouclier
        if (Analyse.estMenaceeParBombe(vue, ici)) {
            Action fuite = Analyse.fuiteDesBombes(vue, hasard);
            if (fuite != null && Analyse.peutPayer(vue, p.getCoutDeplacement())) {
                return fuite;
            }
            if (!moi.aBouclierActif() && Analyse.peutPayer(vue, p.getCoutBouclier())) {
                return new ActivationBouclier();
            }
        }

        // 2. un ennemi est dans la ligne de mire : on tire
        InfosCombattant cible = Analyse.ennemiEnLigneDeMire(vue, ici);
        if (cible != null && moi.getMunitions(TypeArme.TIR) > 0 && Analyse.peutPayer(vue, p.getCoutTir())) {
            return Analyse.tirVers(vue, cible);
        }

        // 3. peu d'énergie : on va vers une pastille
        if (moi.getEnergie() < p.getEnergieInitiale() / 3 && Analyse.peutPayer(vue, p.getCoutDeplacement())) {
            Direction d = Analyse.versPastille(vue);
            if (d != null) {
                return new Deplacement(d);
            }
        }

        // 4. un ennemi est tout près : une fois sur deux on pose une bombe invisible vers lui
        if (moi.getMunitions(TypeArme.BOMBE) > 0 && Analyse.peutPayer(vue, p.getCoutPose()) && hasard.nextInt(2) == 0) {
            for (InfosCombattant e : Analyse.ennemis(vue)) {
                if (e.getPosition().distanceChebyshev(ici) <= 2) {
                    Action pose = Analyse.poseVers(vue, TypeArme.BOMBE, e.getPosition(), Visibilite.PRIVEE);
                    if (pose != null) {
                        return pose;
                    }
                }
            }
        }

        // 5. sinon on se rapproche : d'une case de tir si on a des tirs, sinon de l'ennemi
        if (!Analyse.peutPayer(vue, p.getCoutDeplacement())) {
            return new Attente();
        }
        if (moi.getMunitions(TypeArme.TIR) > 0) {
            Direction d = Analyse.versPositionDeTir(vue);
            if (d != null) {
                return new Deplacement(d);
            }
        }
        Direction d = Analyse.versEnnemi(vue);
        if (d != null) {
            return new Deplacement(d);
        }
        return new Attente();
    }

    public String getNom() {
        return "chasseur";
    }
}
