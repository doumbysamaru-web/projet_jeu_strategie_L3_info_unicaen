package jeu.modele.strategie.joueur;

import java.util.Random;

/**
 * Donne la stratégie correspondant à un nom écrit dans le fichier de paramètres.
 */
public class CatalogueStrategies {

    /**
     * Crée la stratégie demandée (aleatoire, chasseur ou prudent).
     */
    public static StrategieJoueur creer(String nom, Random hasard) {
        switch (nom) {
            case "aleatoire":
                return new StrategieAleatoire(hasard);
            case "chasseur":
                return new StrategieChasseur(hasard);
            case "prudent":
                return new StrategiePrudente(hasard);
            default:
                throw new IllegalArgumentException("Stratégie inconnue : " + nom);
        }
    }
}
