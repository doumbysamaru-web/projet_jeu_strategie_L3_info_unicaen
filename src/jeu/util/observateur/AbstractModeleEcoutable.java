package jeu.util.observateur;

import java.util.ArrayList;
import java.util.List;

/**
 * Modèle écoutable qui gère sa liste d'écouteurs.
 */
public abstract class AbstractModeleEcoutable implements ModeleEcoutable {

    private List<EcouteurModele> ecouteurs = new ArrayList<>();

    public void ajoutEcouteur(EcouteurModele e) {
        ecouteurs.add(e);
    }

    public void retraitEcouteur(EcouteurModele e) {
        ecouteurs.remove(e);
    }

    /**
     * Prévient tous les écouteurs que le modèle a changé.
     */
    // (protected : seule la classe qui hérite, ici Partie, décide quand elle prévient les écouteurs)
    protected void fireChangement() {
        for (EcouteurModele e : ecouteurs) {
            e.modeleMisAJour(this);
        }
    }
}
