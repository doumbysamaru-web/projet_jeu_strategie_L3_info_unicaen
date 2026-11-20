package jeu.modele.action;

import java.util.ArrayList;
import java.util.List;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Orientation;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Grille;

/**
 * Tir horizontal ou vertical : touche les combattants à une distance inférieure à la portée,
 * dans les deux sens, jusqu'au premier mur.
 */
public class Tir implements Action {

    private Orientation orientation;

    public Tir(Orientation orientation) {
        this.orientation = orientation;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public boolean estPossible(VueJeu vue, InfosCombattant acteur) {
        return acteur.getMunitions(TypeArme.TIR) > 0;
    }

    public void executer(Arene arene, Combattant acteur) {
        acteur.consommerMunition(TypeArme.TIR);
        if (orientation == Orientation.HORIZONTALE) {
            arene.journaliser(acteur.getNom() + " tire horizontalement");
        } else {
            arene.journaliser(acteur.getNom() + " tire verticalement");
        }

        // On cherche d'abord tous les combattants touchés, et seulement après on applique les dégâts
        // (un combattant éliminé est retiré de la grille, on ne veut pas changer la grille en la parcourant).
        Grille grille = arene.getGrille();
        int portee = arene.getParametres().getPorteeTir();
        List<Combattant> touches = new ArrayList<>();
        // getSens() donne les deux sens de l'orientation : gauche et droite, ou haut et bas
        for (Direction sens : orientation.getSens()) {
            Position p = acteur.getPosition();
            // "distance < portee" et pas "<=" : le sujet dit "à un nombre de cases inférieur à d"
            for (int distance = 1; distance < portee; distance++) {
                p = p.voisine(sens);
                // un mur (ou le bord) arrête le tir dans ce sens, mais pas dans l'autre sens
                if (!grille.estDansGrille(p) || grille.estMur(p)) {
                    break;
                }
                if (grille.getOccupant(p) != null) {
                    touches.add(grille.getOccupant(p));
                }
            }
        }

        for (Combattant cible : touches) {
            if (cible.aBouclierActif()) {
                arene.journaliser(cible.getNom() + " est protégé par son bouclier");
            } else {
                arene.infligerDegats(cible, arene.getParametres().getDegatsTir(), "tir de " + acteur.getNom());
            }
        }
        arene.depenserEnergie(acteur, arene.getParametres().getCoutTir());
    }

    public String getDescription() {
        return "tir";
    }
}
