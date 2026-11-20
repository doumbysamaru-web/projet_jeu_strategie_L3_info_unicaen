package jeu.modele.action;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;

/**
 * Déclenchement du bouclier, qui protège des tirs et des bombes jusqu'à la fin du tour suivant.
 */
public class ActivationBouclier implements Action {

    public boolean estPossible(VueJeu vue, InfosCombattant acteur) {
        return true;
    }

    // on retire d'abord le cout du bouclier : si le combattant n'a plus d'énergie, il est éliminé et le bouclier n'est pas activé.
    public void executer(Arene arene, Combattant acteur) {
        arene.journaliser(acteur.getNom() + " déclenche son bouclier");
        arene.depenserEnergie(acteur, arene.getParametres().getCoutBouclier());
        if (acteur.estVivant()) {
            acteur.activerBouclier();
        }
    }

    public String getDescription() {
        return "bouclier";
    }
}
