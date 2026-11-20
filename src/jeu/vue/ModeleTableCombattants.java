package jeu.vue;

import javax.swing.table.AbstractTableModel;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.util.observateur.EcouteurModele;

/**
 * Modèle de la JTable des combattants : adapte la partie pour la JTable et se met à jour à chaque tour.
 */
public class ModeleTableCombattants extends AbstractTableModel implements EcouteurModele {

    private String[] colonnes = { "Nom", "Type", "Énergie", "Mines", "Bombes", "Tirs", "Bouclier", "État" };
    private VueJeu vue;

    public ModeleTableCombattants(VueJeu vue) {
        this.vue = vue;
        vue.ajoutEcouteur(this);
    }

    /**
     * Désabonne la table de la partie.
     */
    public void detacher() {
        vue.retraitEcouteur(this);
    }

    // fireTableDataChanged() dit à la JTable que les données ont changé : elle appelle alors
    // getValueAt() pour chaque cellule et se redessine toute seule.
    public void modeleMisAJour(Object source) {
        fireTableDataChanged();
    }

    public int getRowCount() {
        return vue.getCombattants().size();
    }

    public int getColumnCount() {
        return colonnes.length;
    }

    public String getColumnName(int colonne) {
        return colonnes[colonne];
    }

    public Object getValueAt(int ligne, int colonne) {
        InfosCombattant c = vue.getCombattants().get(ligne);
        switch (colonne) {
            case 0:
                return c.getNom();
            case 1:
                return c.getType();
            case 2:
                return Math.max(0, c.getEnergie());
            case 3:
                return c.getMunitions(TypeArme.MINE);
            case 4:
                return c.getMunitions(TypeArme.BOMBE);
            case 5:
                return c.getMunitions(TypeArme.TIR);
            case 6:
                if (c.aBouclierActif()) {
                    return "actif";
                }
                return "";
            default:
                if (!c.estVivant()) {
                    return "éliminé";
                }
                if (c == vue.getVainqueur()) {
                    return "vainqueur";
                }
                return "en jeu";
        }
    }
}
