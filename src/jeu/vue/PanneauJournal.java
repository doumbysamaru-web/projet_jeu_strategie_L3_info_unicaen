package jeu.vue;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jeu.modele.acces.VueJeu;
import jeu.util.observateur.EcouteurModele;

/**
 * Affiche le journal des événements de la partie.
 */
public class PanneauJournal extends JPanel implements EcouteurModele {

    private VueJeu vue;
    private JTextArea texte = new JTextArea(12, 30);

    public PanneauJournal(VueJeu vue) {
        this.vue = vue;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Journal"));
        texte.setEditable(false);
        add(new JScrollPane(texte), BorderLayout.CENTER);
        vue.ajoutEcouteur(this);
        modeleMisAJour(vue);
    }

    /**
     * Désabonne le panneau de la partie.
     */
    public void detacher() {
        vue.retraitEcouteur(this);
    }

    public void modeleMisAJour(Object source) {
        List<String> journal = vue.getJournal();
        String s = "";
        // on n'affiche que les 100 dernières lignes
        int debut = Math.max(0, journal.size() - 100);
        for (int i = debut; i < journal.size(); i++) {
            s = s + journal.get(i) + "\n";
        }
        texte.setText(s);
        texte.setCaretPosition(texte.getDocument().getLength());
    }
}
