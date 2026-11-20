package jeu.modele.action;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;

/**
 * Une action jouée par un combattant pendant son tour (pattern Command).
 */
public interface Action {

    boolean estPossible(VueJeu vue, InfosCombattant acteur); // d'après la vue du combattant, est-ce possible?

    void executer(Arene arene, Combattant acteur);

    String getDescription(); // pour le journal de la partie (ex: "Alpha tire sur Bravo avec un missile")
}
