package jeu.modele.combattant;

import jeu.modele.config.Parametres;

/**
 * Donne la fabrique correspondant à un type de combattant écrit dans le fichier de paramètres.
 */
public class CatalogueFabriques {

    /**
     * Renvoie la fabrique du type demandé (tank, artilleur ou equilibre).
     */
    public static FabriqueCombattant creer(String nom, Parametres parametres) {
        switch (nom) {
            case "tank":
                return new FabriqueTank(parametres);
            case "artilleur":
                return new FabriqueArtilleur(parametres);
            case "equilibre":
                return new FabriqueEquilibre(parametres);
            default:
                throw new IllegalArgumentException("Type de combattant inconnu : " + nom);
        }
    }
}
