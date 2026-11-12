package jeu.modele.combattant;

import jeu.modele.config.Parametres;

/**
 * Crée des combattants équilibrés (valeurs de référence des paramètres).
 */
public class FabriqueEquilibre extends FabriqueCombattant {

    public FabriqueEquilibre(Parametres parametres) {
        super(parametres);
    }

    public Combattant creer(String nom, int numero) {
        return new Combattant(nom, numero, "equilibre", parametres.getEnergieInitiale(),
                parametres.getMunitionsMines(), parametres.getMunitionsBombes(), parametres.getMunitionsTirs());
    }
}
