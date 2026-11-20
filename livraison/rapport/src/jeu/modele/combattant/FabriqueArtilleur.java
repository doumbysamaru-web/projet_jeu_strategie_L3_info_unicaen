package jeu.modele.combattant;

import jeu.modele.config.Parametres;

/**
 * Crée des artilleurs : 30 % d'énergie en moins mais deux fois plus de munitions.
 */
public class FabriqueArtilleur extends FabriqueCombattant {

    public FabriqueArtilleur(Parametres parametres) {
        super(parametres);
    }

    public Combattant creer(String nom, int numero) {
        int energie = parametres.getEnergieInitiale() * 7 / 10;
        return new Combattant(nom, numero, "artilleur", energie, parametres.getMunitionsMines() * 2,
                parametres.getMunitionsBombes() * 2, parametres.getMunitionsTirs() * 2);
    }
}
