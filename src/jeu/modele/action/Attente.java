package jeu.modele.action;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;

/**
 * Ne rien faire pendant ce tour (pour économiser son énergie), ça coût rien d'attendre. :)
 */
public class Attente implements Action {

    public boolean estPossible(VueJeu vue, InfosCombattant acteur) {
        return true;
    }

    public void executer(Arene arene, Combattant acteur) {
        arene.journaliser(acteur.getNom() + " attend");
    }

    public String getDescription() {
        return "attente";
    }
}
