package jeu;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.swing.SwingUtilities;

import jeu.console.VueConsole;
import jeu.controleur.ControleurPartie;
import jeu.modele.config.Parametres;
import jeu.modele.partie.CreateurPartie;
import jeu.modele.partie.Partie;
import jeu.modele.tournoi.Tournoi;


public class Main {

    public static void main(String[] args) throws IOException {
        // les paramètres sont lus dans jeu.properties s'il existe dans le répertoire courant
        Parametres parametres;
        if (new File("jeu.properties").exists()) {
            parametres = Parametres.depuisFichier("jeu.properties");
        } else {
            System.out.println("jeu.properties introuvable : paramètres par défaut");
            parametres = new Parametres();
        }

        if (args.length > 0 && args[0].equals("--console")) {
            lancerConsole(parametres);
        } else if (args.length > 0 && args[0].equals("--tournoi")) {
            int nbParties = 100;
            if (args.length > 1) {
                nbParties = Integer.parseInt(args[1]);
            }
            lancerTournoi(parametres, nbParties);
        } else {
            final Parametres p = parametres;
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    new ControleurPartie(p).nouvellePartie();
                }
            });
        }
    }

    // une partie affichée dans le terminal, avec le même modèle que l'interface graphique
    private static void lancerConsole(Parametres parametres) {
        Partie partie = CreateurPartie.creer(parametres, CreateurPartie.creerGenerateur(parametres));
        VueConsole console = new VueConsole(partie);
        partie.ajoutEcouteur(console);
        console.afficher();
        partie.jouerJusquALaFin();
    }

    // beaucoup de parties sans affichage pour comparer les stratégies
    private static void lancerTournoi(Parametres parametres, int nbParties) {
        System.out.println("Tournoi de " + nbParties + " parties");
        Map<String, Integer> victoires = new Tournoi(parametres).lancer(nbParties);
        for (String joueur : victoires.keySet()) {
            int n = victoires.get(joueur);
            System.out.println("  " + joueur + " : " + n + " victoires (" + (100 * n / nbParties) + " %)");
        }
    }
}
