package jeu.vue;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import jeu.modele.acces.VueJeu;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.InfosExplosif;
import jeu.modele.plateau.Visibilite;
import jeu.util.observateur.EcouteurModele;

/**
 * Dessine la grille vue par une VueJeu : la partie complète ou le proxy d'un joueur.
 */
public class PanneauGrille extends JPanel implements EcouteurModele {

    /** Couleur de chaque combattant, selon son numéro. */
    private static final Color[] COULEURS = {
        new Color(47, 111, 219), new Color(224, 115, 31), new Color(142, 68, 173), new Color(22, 160, 133),
        new Color(214, 60, 113), new Color(122, 139, 30)
    };

    private VueJeu vue;
    private InfosCombattant moi; // combattant encadré en jaune (null pour la vue complète)
    private int tailleCase;

    // "vue" est soit la partie complète (fenêtre principale), soit le proxy d'un joueur (fenêtre du joueur).
    // Le panneau dessine ce que cette vue lui répond, il ne sait pas lequel des deux il reçoit.
    public PanneauGrille(VueJeu vue, InfosCombattant moi, int tailleCase) {
        this.vue = vue;
        this.moi = moi;
        this.tailleCase = tailleCase;
        vue.ajoutEcouteur(this);
    }

    /**
     * Renvoie la couleur d'un combattant.
     */
    public static Color couleurDe(InfosCombattant c) {
        return COULEURS[c.getNumero() % COULEURS.length];
    }

    /**
     * Désabonne le panneau de la partie.
     */
    public void detacher() {
        vue.retraitEcouteur(this);
    }

    // Appelée par la partie à la fin de chaque tour : repaint() redemande paintComponent() à Swing.
    public void modeleMisAJour(Object source) {
        repaint();
    }

    public Dimension getPreferredSize() {
        return new Dimension(vue.getLargeur() * tailleCase, vue.getHauteur() * tailleCase);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int t = tailleCase;
        g.setFont(new Font("SansSerif", Font.BOLD, t / 3));
        for (int x = 0; x < vue.getLargeur(); x++) {
            for (int y = 0; y < vue.getHauteur(); y++) {
                Position p = new Position(x, y);
                int px = x * t;
                int py = y * t;

                // fond de la case
                if (vue.estMur(p)) {
                    g.setColor(Color.DARK_GRAY);
                } else {
                    g.setColor(new Color(245, 240, 230));
                }
                g.fillRect(px, py, t, t);
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect(px, py, t, t);

                // pastille
                if (vue.getPastille(p) > 0) {
                    g.setColor(new Color(46, 158, 91));
                    g.fillOval(px + t / 4, py + t / 4, t / 2, t / 2);
                }

                // Mine ou bombe (couleur plus claire si elle est invisible pour les autres).
                // Dans une fenêtre de joueur, getExplosif renvoie null pour les explosifs cachés :
                // seuls ceux qu'il a le droit de voir passent dans ce if.
                InfosExplosif e = vue.getExplosif(p);
                if (e != null) {
                    boolean invisible = e.getVisibilite() == Visibilite.PRIVEE;
                    if (e.getTypeArme() == TypeArme.MINE) {
                        if (invisible) {
                            g.setColor(Color.PINK);
                        } else {
                            g.setColor(Color.RED);
                        }
                        g.fillRect(px + t / 4, py + t / 4, t / 2, t / 2);
                        g.setColor(Color.WHITE);
                        g.drawString("M", px + t / 2 - t / 9, py + t / 2 + t / 8);
                    } else {
                        if (invisible) {
                            g.setColor(Color.GRAY);
                        } else {
                            g.setColor(Color.BLACK);
                        }
                        g.fillOval(px + t / 5, py + t / 5, t * 3 / 5, t * 3 / 5);
                        g.setColor(Color.WHITE);
                        g.drawString("" + e.getCompteARebours(), px + t / 2 - t / 10, py + t / 2 + t / 8);
                    }
                }

                // combattant
                InfosCombattant c = vue.getCombattant(p);
                if (c != null) {
                    if (c.aBouclierActif()) {
                        g.setColor(Color.CYAN);
                        g.fillOval(px + 1, py + 1, t - 2, t - 2);
                    }
                    if (c == moi) {
                        // cadre jaune autour du joueur de cette fenêtre
                        g.setColor(Color.YELLOW);
                        g.drawRect(px + 1, py + 1, t - 2, t - 2);
                        g.drawRect(px + 2, py + 2, t - 4, t - 4);
                    }
                    g.setColor(couleurDe(c));
                    g.fillOval(px + t / 8, py + t / 8, t * 3 / 4, t * 3 / 4);
                    g.setColor(Color.WHITE);
                    g.drawString(c.getNom().substring(0, 1), px + t / 2 - t / 10, py + t / 2 + t / 8);
                }
            }
        }
    }
}
