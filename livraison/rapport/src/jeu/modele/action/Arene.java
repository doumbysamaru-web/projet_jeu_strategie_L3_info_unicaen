package jeu.modele.action;

import jeu.modele.combattant.Combattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Grille;

/**
 * Ce qu'une action peut modifier dans la partie (implémentée par Partie).
 */
public interface Arene {

    Grille getGrille();

    Parametres getParametres();

    void deplacerCombattant(Combattant c, Position destination);

    void infligerDegats(Combattant cible, int degats, String cause);

    void depenserEnergie(Combattant c, int cout);


    void journaliser(String message); // pour le journal de la partie (ex: "Alpha tire sur Bravo avec un missile")
}
