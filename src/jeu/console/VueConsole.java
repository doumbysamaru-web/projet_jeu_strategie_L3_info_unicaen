package jeu.console;

import java.util.List;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.InfosExplosif;
import jeu.modele.plateau.Visibilite;
import jeu.util.observateur.EcouteurModele;

/**
 * Affichage de la partie en mode texte : montre que le modèle marche aussi sans Swing.
 * Légende : # mur, + pastille, M/m mine visible/invisible, B/b bombe visible/invisible, lettre = combattant.
 */
public class VueConsole implements EcouteurModele {

    private VueJeu vue;
    private int lignesAffichees = 0;

    public VueConsole(VueJeu vue) {
        this.vue = vue;
    }

    public void modeleMisAJour(Object source) {
        afficher();
    }

    /**
     * Affiche les nouveaux messages du journal, la grille et l'état des combattants.
     */
    public void afficher() {
        List<String> journal = vue.getJournal();
        if (lignesAffichees > journal.size()) {
            lignesAffichees = 0;
        }
        for (int i = lignesAffichees; i < journal.size(); i++) {
            System.out.println(journal.get(i));
        }
        lignesAffichees = journal.size();

        for (int y = 0; y < vue.getHauteur(); y++) {
            String ligne = "";
            for (int x = 0; x < vue.getLargeur(); x++) {
                ligne = ligne + symbole(new Position(x, y)) + " ";
            }
            System.out.println(ligne);
        }
        for (InfosCombattant c : vue.getCombattants()) {
            String s = "  " + c.getNom() + " (" + c.getType() + ") énergie " + c.getEnergie()
                    + ", mines " + c.getMunitions(TypeArme.MINE) + ", bombes " + c.getMunitions(TypeArme.BOMBE)
                    + ", tirs " + c.getMunitions(TypeArme.TIR);
            if (!c.estVivant()) {
                s = s + " [éliminé]";
            } else if (c.aBouclierActif()) {
                s = s + " [bouclier]";
            }
            System.out.println(s);
        }
        System.out.println();
    }

    private char symbole(Position p) {
        if (vue.estMur(p)) {
            return '#';
        }
        InfosCombattant c = vue.getCombattant(p);
        if (c != null) {
            return c.getNom().toUpperCase().charAt(0);
        }
        InfosExplosif e = vue.getExplosif(p);
        if (e != null) {
            boolean visible = e.getVisibilite() == Visibilite.PUBLIQUE;
            if (e.getTypeArme() == TypeArme.MINE) {
                if (visible) {
                    return 'M';
                }
                return 'm';
            } else {
                if (visible) {
                    return 'B';
                }
                return 'b';
            }
        }
        if (vue.getPastille(p) > 0) {
            return '+';
        }
        return '.';
    }
}
