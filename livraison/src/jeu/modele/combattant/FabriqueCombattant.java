package jeu.modele.combattant;

import jeu.modele.config.Parametres;

/**
 * Fabrique de combattants (pattern Factory Method) : chaque sous-classe crée un type de combattant.
 */
public abstract class FabriqueCombattant {

    protected Parametres parametres;

    public FabriqueCombattant(Parametres parametres) {
        this.parametres = parametres;
    }

    /**
     * Crée un combattant de ce type.
     */
    public abstract Combattant creer(String nom, int numero);
}
