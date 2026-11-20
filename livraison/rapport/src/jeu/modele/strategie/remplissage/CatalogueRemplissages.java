package jeu.modele.strategie.remplissage;

/**
 * Donne le remplissage correspondant à un nom écrit dans le fichier de paramètres.
 */
public class CatalogueRemplissages {

    /**
     * Crée le remplissage demandé (aleatoire ou arene).
     */
    public static StrategieRemplissage creer(String nom) {
        switch (nom) {
            case "aleatoire":
                return new RemplissageAleatoire();
            case "arene":
                return new RemplissageArene();
            default:
                throw new IllegalArgumentException("Remplissage inconnu : " + nom);
        }
    }
}
