package jeu.controleur;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import jeu.modele.config.Parametres;
import jeu.modele.partie.CreateurPartie;
import jeu.modele.partie.Joueur;
import jeu.modele.partie.Partie;
import jeu.vue.FenetreJoueur;
import jeu.vue.FenetrePrincipale;

/**
 * Contrôleur : crée la partie et les fenêtres, et réagit aux boutons de la fenêtre principale.
 */
public class ControleurPartie {

    private static final int DELAI_LECTURE_AUTO = 400; // en millisecondes

    private Parametres parametres;
    private Random hasard;
    private Timer minuterie;
    private Partie partie;
    private FenetrePrincipale fenetre;
    private List<FenetreJoueur> fenetresJoueurs = new ArrayList<>();

    public ControleurPartie(Parametres parametres) {
        this.parametres = parametres;
        this.hasard = CreateurPartie.creerGenerateur(parametres);
        // en lecture auto, le Timer joue un tour toutes les 400 ms
        minuterie = new Timer(DELAI_LECTURE_AUTO, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tourSuivant();
            }
        });
    }

    /**
     * Lance une nouvelle partie et ouvre ses fenêtres.
     */
    public void nouvellePartie() {
        minuterie.stop();
        fermerFenetres();

        partie = CreateurPartie.creer(parametres, new Random(hasard.nextLong()));
        fenetre = new FenetrePrincipale(partie);

        // On relie les boutons de la fenêtre aux méthodes du contrôleur. C'est le contrôleur qui connaît
        // la fenêtre et pas l'inverse : la fenêtre ne sait pas ce que font ses boutons (MVC).
        fenetre.ajouterEcouteurSuivant(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tourSuivant();
            }
        });
        fenetre.ajouterEcouteurAuto(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (fenetre.isLectureAuto()) {
                    minuterie.start();
                } else {
                    minuterie.stop();
                }
            }
        });
        fenetre.ajouterEcouteurNouvellePartie(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                nouvellePartie();
            }
        });
        fenetre.ajouterEcouteurVuesJoueurs(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                afficherVuesJoueurs(fenetre.isVuesJoueurs());
            }
        });

        fenetre.setLocation(0, 0);
        fenetre.setVisible(true);

        // Une fenêtre par joueur, rangées sur deux colonnes à droite de la fenêtre principale.
        // On lui donne j.getVue() (le proxy du joueur) et non la partie : c'est ce qui l'empêche
        // d'afficher les mines et bombes invisibles des autres.
        int i = 0;
        for (Joueur j : partie.getJoueurs()) {
            FenetreJoueur f = new FenetreJoueur(j.getVue(), j.getStrategie().getNom());
            int x = fenetre.getWidth() + (i % 2) * (f.getWidth() + 5);
            int y = (i / 2) * (f.getHeight() + 5);
            f.setLocation(x, y);
            fenetresJoueurs.add(f);
            i++;
        }
        afficherVuesJoueurs(true);
    }

    private void tourSuivant() {
        partie.jouerTour();
        if (partie.estTerminee()) {
            minuterie.stop();
            fenetre.setLectureAuto(false);
        }
    }

    private void afficherVuesJoueurs(boolean visibles) {
        for (FenetreJoueur f : fenetresJoueurs) {
            f.setVisible(visibles);
        }
        fenetre.toFront();
    }

    private void fermerFenetres() {
        for (FenetreJoueur f : fenetresJoueurs) {
            f.fermer();
        }
        fenetresJoueurs.clear();
        if (fenetre != null) {
            fenetre.fermer();
        }
    }
}
