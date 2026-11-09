package jeu.modele.combattant;

import jeu.modele.geometrie.Position;

/**
 * Accès en lecture seule à un combattant (utilisé par les vues et les stratégies).
 */
public interface InfosCombattant {

    String getNom();

    /**
     * Numéro du combattant dans la partie (0, 1, 2...).
     */
    int getNumero();

    /**
     * Type du combattant : tank, artilleur ou equilibre.
     */
    String getType();

    int getEnergie();

    Position getPosition();

    int getMunitions(TypeArme arme);

    /**
     * Vrai tant que l'énergie est positive.
     */
    boolean estVivant();

    boolean aBouclierActif();
}
