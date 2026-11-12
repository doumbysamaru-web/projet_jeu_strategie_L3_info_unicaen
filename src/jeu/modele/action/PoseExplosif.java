package jeu.modele.action;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Bombe;
import jeu.modele.plateau.Mine;
import jeu.modele.plateau.Visibilite;

/**
 * Pose d'une mine ou d'une bombe sur l'une des 8 cases voisines.
 */
public class PoseExplosif implements Action {

    private TypeArme type; // MINE ou BOMBE
    private Direction direction;
    private Visibilite visibilite;

    public PoseExplosif(TypeArme type, Direction direction, Visibilite visibilite) {
        this.type = type;
        this.direction = direction;
        this.visibilite = visibilite;
    }

    // "vue" peut être la partie (vue complète) ou le proxy d'un joueur. Avec le proxy, un explosif
    // invisible d'un adversaire n'apparaît pas : getExplosif renvoie null et la case semble vide.
    // C'est pour ça que la partie refait ce test avec la vue complète avant d'exécuter l'action.
    public boolean estPossible(VueJeu vue, InfosCombattant acteur) {
        Position cible = acteur.getPosition().voisine(direction);
        if (acteur.getMunitions(type) <= 0 || !vue.estDansGrille(cible)) {
            return false;
        }
        // la case doit être vide
        return !vue.estMur(cible) && vue.getCombattant(cible) == null
                && vue.getPastille(cible) == 0 && vue.getExplosif(cible) == null;
    }

    public void executer(Arene arene, Combattant acteur) {
        Position cible = acteur.getPosition().voisine(direction);
        acteur.consommerMunition(type);
        if (type == TypeArme.MINE) {
            arene.getGrille().poserExplosif(cible, new Mine(acteur, visibilite));
        } else {
            arene.getGrille().poserExplosif(cible, new Bombe(acteur, visibilite, arene.getParametres().getDelaiBombe()));
        }
        // pour une pose invisible on ne donne pas la case, le journal est lu par tout le monde
        if (visibilite == Visibilite.PUBLIQUE) {
            arene.journaliser(acteur.getNom() + " pose une " + type + " en " + cible);
        } else {
            arene.journaliser(acteur.getNom() + " pose une " + type + " invisible");
        }
        arene.depenserEnergie(acteur, arene.getParametres().getCoutPose());
    }

    public String getDescription() {
        return "pose d'une " + type + " vers " + direction;
    }
}
