package jeu.modele.combattant;

import jeu.modele.config.Parametres;

/**
 * Crée des tanks : 60 % d'énergie en plus mais moitié moins de munitions.
 */
public class FabriqueTank extends FabriqueCombattant {

    public FabriqueTank(Parametres parametres) {
        super(parametres);
    }

    public Combattant creer(String nom, int numero) {
        int energie = parametres.getEnergieInitiale() * 16 / 10;
        return new Combattant(nom, numero, "tank", energie, parametres.getMunitionsMines() / 2,
                parametres.getMunitionsBombes() / 2, parametres.getMunitionsTirs() / 2);
    }
}
