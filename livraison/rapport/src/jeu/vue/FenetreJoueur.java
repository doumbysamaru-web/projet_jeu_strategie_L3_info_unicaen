package jeu.vue;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

import jeu.modele.acces.VueJoueur;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.util.observateur.EcouteurModele;

/**
 * Frame d'un joueur : elle dessine son proxy, donc seulement ce que ce joueur peut voir.
 */
public class FenetreJoueur extends JFrame implements EcouteurModele {

    private VueJoueur vue;
    private PanneauGrille panneauGrille;
    private JLabel etat = new JLabel();

    public FenetreJoueur(VueJoueur vue, String nomStrategie) {
        super("Vue de " + vue.getMoi().getNom() + " (" + vue.getMoi().getType() + ", " + nomStrategie + ")");
        this.vue = vue;
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        panneauGrille = new PanneauGrille(vue, vue.getMoi(), 28);
        etat.setOpaque(true);
        etat.setBackground(PanneauGrille.couleurDe(vue.getMoi()));
        etat.setForeground(Color.WHITE);

        add(etat, BorderLayout.NORTH);
        add(panneauGrille, BorderLayout.CENTER);

        vue.ajoutEcouteur(this);
        modeleMisAJour(vue);
        pack();
    }

    public void modeleMisAJour(Object source) {
        InfosCombattant moi = vue.getMoi();
        if (!moi.estVivant()) {
            etat.setText(" " + moi.getNom() + " : éliminé");
        } else {
            String s = " " + moi.getNom() + " : énergie " + moi.getEnergie() + " | mines " + moi.getMunitions(TypeArme.MINE)
                    + " | bombes " + moi.getMunitions(TypeArme.BOMBE) + " | tirs " + moi.getMunitions(TypeArme.TIR);
            if (moi.aBouclierActif()) {
                s = s + " | bouclier";
            }
            etat.setText(s);
        }
    }

    /**
     * Ferme la fenêtre et la désabonne de la partie.
     */
    public void fermer() {
        vue.retraitEcouteur(this);
        panneauGrille.detacher();
        dispose();
    }
}
