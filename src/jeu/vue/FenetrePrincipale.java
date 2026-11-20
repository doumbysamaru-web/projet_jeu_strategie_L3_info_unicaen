package jeu.vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;

import jeu.modele.acces.VueJeu;
import jeu.util.observateur.EcouteurModele;

/**
 * Frame principale : toute la grille (y compris les explosifs invisibles), la table des combattants,
 * le journal et les boutons.
 */
public class FenetrePrincipale extends JFrame implements EcouteurModele {

    private VueJeu vue;
    private PanneauGrille panneauGrille;
    private ModeleTableCombattants modeleTable;
    private PanneauJournal panneauJournal;

    private JButton boutonSuivant = new JButton("Tour suivant");
    private JToggleButton boutonAuto = new JToggleButton("Lecture auto");
    private JButton boutonNouvelle = new JButton("Nouvelle partie");
    private JToggleButton boutonVuesJoueurs = new JToggleButton("Vues des joueurs", true);
    private JLabel statut = new JLabel();

    public FenetrePrincipale(VueJeu vue) {
        super("Jeu de stratégie - vue complète");
        this.vue = vue;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panneauGrille = new PanneauGrille(vue, null, 50);
        modeleTable = new ModeleTableCombattants(vue);
        panneauJournal = new PanneauJournal(vue);

        JScrollPane scrollTable = new JScrollPane(new JTable(modeleTable));
        scrollTable.setPreferredSize(new Dimension(450, 130));
        JPanel droite = new JPanel(new GridLayout(2, 1));
        droite.add(scrollTable);
        droite.add(panneauJournal);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        boutons.add(boutonSuivant);
        boutons.add(boutonAuto);
        boutons.add(boutonNouvelle);
        boutons.add(boutonVuesJoueurs);
        boutons.add(statut);

        add(panneauGrille, BorderLayout.CENTER);
        add(droite, BorderLayout.EAST);
        add(boutons, BorderLayout.SOUTH);

        vue.ajoutEcouteur(this);
        modeleMisAJour(vue);
        pack();
    }

    public void modeleMisAJour(Object source) {
        if (vue.estTerminee()) {
            boutonSuivant.setEnabled(false);
            boutonAuto.setEnabled(false);
            if (vue.getVainqueur() == null) {
                statut.setText("Partie terminée au tour " + vue.getNumeroTour() + " : aucun vainqueur");
            } else {
                statut.setText(vue.getVainqueur().getNom() + " gagne au tour " + vue.getNumeroTour() + " !");
            }
        } else {
            statut.setText("Tour " + vue.getNumeroTour() + " - " + vue.getCombattantsVivants().size() + " combattants en jeu");
        }
    }

    // Les boutons sont reliés au contrôleur par ces méthodes : la vue ne connaît pas le contrôleur.

    public void ajouterEcouteurSuivant(ActionListener l) {
        boutonSuivant.addActionListener(l);
    }

    public void ajouterEcouteurAuto(ActionListener l) {
        boutonAuto.addActionListener(l);
    }

    public void ajouterEcouteurNouvellePartie(ActionListener l) {
        boutonNouvelle.addActionListener(l);
    }

    public void ajouterEcouteurVuesJoueurs(ActionListener l) {
        boutonVuesJoueurs.addActionListener(l);
    }

    public boolean isLectureAuto() {
        return boutonAuto.isSelected();
    }

    public void setLectureAuto(boolean b) {
        boutonAuto.setSelected(b);
    }

    public boolean isVuesJoueurs() {
        return boutonVuesJoueurs.isSelected();
    }

    /**
     * Ferme la fenêtre et la désabonne de la partie.
     */
    public void fermer() {
        vue.retraitEcouteur(this);
        panneauGrille.detacher();
        modeleTable.detacher();
        panneauJournal.detacher();
        dispose();
    }
}
