package jeu.util.observateur;

/**
 * Modèle auquel des écouteurs peuvent s'abonner (pattern Observer).
 */
public interface ModeleEcoutable {

    void ajoutEcouteur(EcouteurModele e);
    void retraitEcouteur(EcouteurModele e);
}
