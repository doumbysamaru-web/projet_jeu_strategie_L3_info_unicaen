package jeu.modele.partie;

import jeu.modele.acces.ProxyJoueur;
import jeu.modele.combattant.Combattant;
import jeu.modele.strategie.joueur.StrategieJoueur;

/**
 * Un joueur : son combattant, la stratégie qui le pilote et son proxy.
 */
public class Joueur {

    private Combattant combattant;
    private StrategieJoueur strategie;
    private ProxyJoueur vue;

    public Joueur(Combattant combattant, StrategieJoueur strategie, ProxyJoueur vue) {
        this.combattant = combattant;
        this.strategie = strategie;
        this.vue = vue;
    }

    public Combattant getCombattant() {
        return combattant;
    }

    public StrategieJoueur getStrategie() {
        return strategie;
    }

    /**
     * Le proxy du joueur, c'est-à-dire ce qu'il voit de la partie.
     */
    public ProxyJoueur getVue() {
        return vue;
    }

    public String toString() {
        return combattant.getNom() + " (" + combattant.getType() + ", " + strategie.getNom() + ")";
    }
}
