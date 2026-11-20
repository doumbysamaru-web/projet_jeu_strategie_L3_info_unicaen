package jeu.modele.plateau;

import jeu.modele.combattant.Combattant;

/**
 * Contenu d'une case (utilisée seulement par la classe Grille) visible seulement dans le package jeu.modele.plateau.
 */
class Case {
    boolean mur;
    int pastille; // énergie de la pastille, 0 s'il n'y en a pas
    Explosif explosif;
    Combattant occupant;
}
