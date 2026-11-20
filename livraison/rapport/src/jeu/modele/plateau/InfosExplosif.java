package jeu.modele.plateau;

import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;

/**
 * Accès en lecture seule à une mine ou une bombe posée sur la grille.
 */
public interface InfosExplosif {


    TypeArme getTypeArme(); // Type d'arme (mine ou bombe)

    Visibilite getVisibilite();

    InfosCombattant getProprietaire();

    int getCompteARebours(); // Nombre de tours avant l'explosion, ou -1 pour une mine.

    boolean estVisiblePar(InfosCombattant c); // Vrai si ce combattant a le droit de voir l'explosif.
}
