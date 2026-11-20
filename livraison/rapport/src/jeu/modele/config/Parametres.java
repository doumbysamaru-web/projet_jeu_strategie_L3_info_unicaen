package jeu.modele.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.Properties;

/**
 * Paramètres du jeu : valeurs par défaut, éventuellement remplacées par le fichier jeu.properties.
 */
public class Parametres {

    // Valeurs par défaut (le nom de la clé dans le fichier est indiqué à droite)
    public static final int LARGEUR = 12;              // largeur
    public static final int HAUTEUR = 10;              // hauteur
    public static final String REMPLISSAGE = "aleatoire"; // remplissage : aleatoire ou arene
    public static final double DENSITE_MURS = 0.12;    // densiteMurs
    public static final int NB_PASTILLES = 6;          // nbPastilles
    public static final int VALEUR_PASTILLE = 15;      // valeurPastille
    public static final int ENERGIE_INITIALE = 60;     // energieInitiale
    public static final int MUNITIONS_MINES = 3;       // munitionsMines
    public static final int MUNITIONS_BOMBES = 3;      // munitionsBombes
    public static final int MUNITIONS_TIRS = 6;        // munitionsTirs
    public static final int COUT_DEPLACEMENT = 1;      // coutDeplacement
    public static final int COUT_BOUCLIER = 3;         // coutBouclier
    public static final int COUT_TIR = 1;              // coutTir
    public static final int COUT_POSE = 1;             // coutPose
    public static final int DEGATS_TIR = 12;           // degatsTir
    public static final int DEGATS_MINE = 20;          // degatsMine
    public static final int DEGATS_BOMBE = 18;         // degatsBombe
    public static final int PORTEE_TIR = 5;            // porteeTir
    public static final int DELAI_BOMBE = 3;           // delaiBombe
    public static final String ORDRE = "aleatoire";    // ordre : fixe ou aleatoire
    public static final int TOURS_MAX = 300;           // toursMax
    public static final String GRAINE = "";            // graine (vide = parties toutes différentes)
    public static final String JOUEURS =               // joueurs
            "Alpha:tank:chasseur;Bravo:artilleur:prudent;Charlie:equilibre:aleatoire;Delta:equilibre:chasseur";

    private int largeur;
    private int hauteur;
    private String remplissage;
    private double densiteMurs;
    private int nbPastilles;
    private int valeurPastille;
    private int energieInitiale;
    private int munitionsMines;
    private int munitionsBombes;
    private int munitionsTirs;
    private int coutDeplacement;
    private int coutBouclier;
    private int coutTir;
    private int coutPose;
    private int degatsTir;
    private int degatsMine;
    private int degatsBombe;
    private int porteeTir;
    private int delaiBombe;
    private boolean ordreAleatoire;
    private int toursMax;
    private String graine;
    private List<ConfigurationJoueur> joueurs;

    /**
     * Crée les paramètres par défaut.
     */
    public Parametres() {
        this(new Properties());
    }

    /**
     * Crée les paramètres à partir de propriétés ; une clé absente garde sa valeur par défaut.
     */
    public Parametres(Properties prop) {
        largeur = lireEntier(prop, "largeur", LARGEUR);
        hauteur = lireEntier(prop, "hauteur", HAUTEUR);
        remplissage = lireTexte(prop, "remplissage", REMPLISSAGE).toLowerCase();
        densiteMurs = lireReel(prop, "densiteMurs", DENSITE_MURS);
        nbPastilles = lireEntier(prop, "nbPastilles", NB_PASTILLES);
        valeurPastille = lireEntier(prop, "valeurPastille", VALEUR_PASTILLE);
        energieInitiale = lireEntier(prop, "energieInitiale", ENERGIE_INITIALE);
        munitionsMines = lireEntier(prop, "munitionsMines", MUNITIONS_MINES);
        munitionsBombes = lireEntier(prop, "munitionsBombes", MUNITIONS_BOMBES);
        munitionsTirs = lireEntier(prop, "munitionsTirs", MUNITIONS_TIRS);
        coutDeplacement = lireEntier(prop, "coutDeplacement", COUT_DEPLACEMENT);
        coutBouclier = lireEntier(prop, "coutBouclier", COUT_BOUCLIER);
        coutTir = lireEntier(prop, "coutTir", COUT_TIR);
        coutPose = lireEntier(prop, "coutPose", COUT_POSE);
        degatsTir = lireEntier(prop, "degatsTir", DEGATS_TIR);
        degatsMine = lireEntier(prop, "degatsMine", DEGATS_MINE);
        degatsBombe = lireEntier(prop, "degatsBombe", DEGATS_BOMBE);
        porteeTir = lireEntier(prop, "porteeTir", PORTEE_TIR);
        delaiBombe = lireEntier(prop, "delaiBombe", DELAI_BOMBE);
        ordreAleatoire = lireTexte(prop, "ordre", ORDRE).equals("aleatoire");
        toursMax = lireEntier(prop, "toursMax", TOURS_MAX);
        graine = lireTexte(prop, "graine", GRAINE);
        joueurs = ConfigurationJoueur.lireListe(lireTexte(prop, "joueurs", JOUEURS));

        if (largeur < 3 || hauteur < 3) {
            throw new IllegalArgumentException("La grille doit faire au moins 3x3");
        }
        if (joueurs.size() < 2) {
            throw new IllegalArgumentException("Il faut au moins 2 joueurs");
        }
    }

    /**
     * Lit les paramètres dans un fichier .properties.
     */
    public static Parametres depuisFichier(String nomFichier) throws IOException {
        Properties prop = new Properties();
        Reader lecteur = new InputStreamReader(new FileInputStream(nomFichier), "UTF-8");
        prop.load(lecteur);
        lecteur.close();
        return new Parametres(prop);
    }

    private static String lireTexte(Properties prop, String cle, String defaut) {
        String valeur = prop.getProperty(cle);
        if (valeur == null || valeur.trim().isEmpty()) {
            return defaut;
        }
        return valeur.trim();
    }

    private static int lireEntier(Properties prop, String cle, int defaut) {
        String valeur = lireTexte(prop, cle, null);
        if (valeur == null) {
            return defaut;
        }
        try {
            return Integer.parseInt(valeur);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Paramètre " + cle + " : entier attendu au lieu de " + valeur);
        }
    }

    private static double lireReel(Properties prop, String cle, double defaut) {
        String valeur = lireTexte(prop, cle, null);
        if (valeur == null) {
            return defaut;
        }
        try {
            return Double.parseDouble(valeur);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Paramètre " + cle + " : nombre attendu au lieu de " + valeur);
        }
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public String getRemplissage() {
        return remplissage;
    }

    public double getDensiteMurs() {
        return densiteMurs;
    }

    public int getNbPastilles() {
        return nbPastilles;
    }

    public int getValeurPastille() {
        return valeurPastille;
    }

    public int getEnergieInitiale() {
        return energieInitiale;
    }

    public int getMunitionsMines() {
        return munitionsMines;
    }

    public int getMunitionsBombes() {
        return munitionsBombes;
    }

    public int getMunitionsTirs() {
        return munitionsTirs;
    }

    public int getCoutDeplacement() {
        return coutDeplacement;
    }

    public int getCoutBouclier() {
        return coutBouclier;
    }

    public int getCoutTir() {
        return coutTir;
    }

    public int getCoutPose() {
        return coutPose;
    }

    public int getDegatsTir() {
        return degatsTir;
    }

    public int getDegatsMine() {
        return degatsMine;
    }

    public int getDegatsBombe() {
        return degatsBombe;
    }

    public int getPorteeTir() {
        return porteeTir;
    }

    public int getDelaiBombe() {
        return delaiBombe;
    }

    public boolean isOrdreAleatoire() {
        return ordreAleatoire;
    }

    public int getToursMax() {
        return toursMax;
    }

    /**
     * Renvoie la graine du hasard, ou une chaîne vide si elle n'est pas fixée.
     */
    public String getGraine() {
        return graine;
    }

    public List<ConfigurationJoueur> getJoueurs() {
        return joueurs;
    }
}
