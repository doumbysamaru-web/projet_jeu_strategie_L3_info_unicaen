package jeu.modele.acces;

import jeu.modele.combattant.InfosCombattant;

/**
 * Ce que voit un joueur de la partie.
 */
public interface VueJoueur extends VueJeu {

    /**
     * Le combattant de ce joueur.
     */
    InfosCombattant getMoi();
}
