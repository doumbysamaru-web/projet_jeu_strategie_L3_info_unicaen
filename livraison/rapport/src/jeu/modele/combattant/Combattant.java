package jeu.modele.combattant;

import java.util.HashMap;
import java.util.Map;

import jeu.modele.geometrie.Position;

/**
 * Un combattant : énergie, munitions, position et bouclier.
 */
public class Combattant implements InfosCombattant {

    private String nom;
    private int numero;
    private String type;
    private int energie;
    private Position position;
    private Map<TypeArme, Integer> munitions = new HashMap<>();
    private int toursBouclier; // nombre de fins de tour avant que le bouclier tombe

    public Combattant(String nom, int numero, String type, int energie, int mines, int bombes, int tirs) {
        this.nom = nom;
        this.numero = numero;
        this.type = type;
        this.energie = energie;
        munitions.put(TypeArme.MINE, mines);
        munitions.put(TypeArme.BOMBE, bombes);
        munitions.put(TypeArme.TIR, tirs);
    }

    public String getNom() {
        return nom;
    }

    public int getNumero() {
        return numero;
    }

    public String getType() {
        return type;
    }

    public int getEnergie() {
        return energie;
    }

    public Position getPosition() {
        return position;
    }

    public int getMunitions(TypeArme arme) {
        return munitions.get(arme);
    }

    public boolean estVivant() {
        return energie > 0;
    }

    public boolean aBouclierActif() {
        return toursBouclier > 0;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Ajoute de l'énergie (ou en retire si delta est négatif).
     */
    public void modifierEnergie(int delta) {
        energie = energie + delta;
    }

    /**
     * Utilise une munition ; renvoie faux s'il n'en reste plus.
     */
    public boolean consommerMunition(TypeArme arme) {
        int reste = munitions.get(arme);
        if (reste <= 0) {
            return false;
        }
        munitions.put(arme, reste - 1);
        return true;
    }

    /**
     * Lève le bouclier jusqu'à la fin du tour suivant.
     */
    public void activerBouclier() {
        toursBouclier = 2; // il tombe après deux fins de tour : celle-ci et la suivante
    }

    /**
     * Appelée à chaque fin de tour pour user le bouclier.
     */
    public void finDeTour() {
        if (toursBouclier > 0) {
            toursBouclier--;
        }
    }

    public String toString() {
        return nom;
    }
}
