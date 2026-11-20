package jeu.modele.tournoi;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import jeu.modele.config.Parametres;
import jeu.modele.partie.CreateurPartie;
import jeu.modele.partie.Joueur;
import jeu.modele.partie.Partie;

/**
 * Joue beaucoup de parties sans affichage pour comparer les stratégies des robots.
 */
public class Tournoi {

    private Parametres parametres;

    public Tournoi(Parametres parametres) {
        this.parametres = parametres;
    }

    /**
     * Joue nbParties parties et renvoie le nombre de victoires de chaque joueur ("aucun vainqueur" compris).
     */
    public Map<String, Integer> lancer(int nbParties) {
        Map<String, Integer> victoires = new LinkedHashMap<>();
        Random hasard = CreateurPartie.creerGenerateur(parametres);
        for (int i = 0; i < nbParties; i++) {
            Partie partie = CreateurPartie.creer(parametres, new Random(hasard.nextLong()));
            if (i == 0) {
                for (Joueur j : partie.getJoueurs()) {
                    victoires.put(j.toString(), 0);
                }
                victoires.put("aucun vainqueur", 0);
            }
            partie.jouerJusquALaFin();

            String gagnant = "aucun vainqueur";
            for (Joueur j : partie.getJoueurs()) {
                if (j.getCombattant() == partie.getVainqueur()) {
                    gagnant = j.toString();
                }
            }
            victoires.put(gagnant, victoires.get(gagnant) + 1);
        }
        return victoires;
    }
}
