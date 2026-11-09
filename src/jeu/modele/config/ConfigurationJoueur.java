package jeu.modele.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Un joueur décrit dans le fichier de paramètres : nom, type de combattant et stratégie.
 */
public class ConfigurationJoueur {

    private String nom;
    private String type;
    private String strategie;

    public ConfigurationJoueur(String nom, String type, String strategie) {
        this.nom = nom;
        this.type = type;
        this.strategie = strategie;
    }

    public String getNom() {
        return nom;
    }

    public String getType() {
        return type;
    }

    public String getStrategie() {
        return strategie;
    }

    /**
     * Lit une liste de joueurs écrite sous la forme "nom:type:strategie;nom:type:strategie".
     */
    public static List<ConfigurationJoueur> lireListe(String texte) {
        List<ConfigurationJoueur> liste = new ArrayList<>();
        String[] morceaux = texte.split(";");
        for (String morceau : morceaux) {
            if (morceau.trim().isEmpty()) {
                continue;
            }
            String[] champs = morceau.split(":");
            if (champs.length != 3) {
                throw new IllegalArgumentException("Joueur mal écrit : " + morceau + " (attendu nom:type:strategie)");
            }
            String nom = champs[0].trim();
            String type = champs[1].trim().toLowerCase();
            String strategie = champs[2].trim().toLowerCase();
            liste.add(new ConfigurationJoueur(nom, type, strategie));
        }
        return liste;
    }

    public String toString() {
        return nom + ":" + type + ":" + strategie;
    }
}
