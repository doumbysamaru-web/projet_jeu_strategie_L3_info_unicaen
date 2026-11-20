package jeu.modele.combattant;

/**
 * Les types d'armes d'un combattant.
 */
public enum TypeArme {
    MINE,
    BOMBE,
    TIR;

    public String toString() {
        return name().toLowerCase();
    }
}
