package jeu.util.observateur;

/**
 * Objet prévenu quand un modèle change (pattern Observer).
 */
public interface EcouteurModele {

    void modeleMisAJour(Object source);
}
