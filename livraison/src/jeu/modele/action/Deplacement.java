package jeu.modele.action;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;

/**
 * Déplacement d'une case vers le haut, le bas, la gauche ou la droite.
 */
public class Deplacement implements Action {

    private Direction direction;

    public Deplacement(Direction direction) {
        if (!direction.estCardinale()) {
            throw new IllegalArgumentException("On ne peut pas se déplacer en diagonale");
        }
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean estPossible(VueJeu vue, InfosCombattant acteur) {
        Position destination = acteur.getPosition().voisine(direction);
        return vue.estDansGrille(destination) && !vue.estMur(destination) && vue.getCombattant(destination) == null;
    }

    
    public void executer(Arene arene, Combattant acteur) {
        Position destination = acteur.getPosition().voisine(direction);
        arene.journaliser(acteur.getNom() + " se déplace vers " + direction);
        arene.depenserEnergie(acteur, arene.getParametres().getCoutDeplacement());
        // on paie d'abord : si ce déplacement épuise l'énergie, le combattant est éliminé et ne bouge pas
        if (acteur.estVivant()) {
            arene.deplacerCombattant(acteur, destination);
        }
    }

    public String getDescription() {
        return "déplacement vers " + direction;
    }
}
