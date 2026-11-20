package jeu;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;
import jeu.modele.action.Attente;
import jeu.modele.combattant.Combattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.partie.OrdreFixe;
import jeu.modele.partie.Partie;
import jeu.modele.plateau.Grille;
import jeu.modele.strategie.joueur.StrategieJoueur;

/**
 * Petites méthodes pour préparer les situations de test.
 */
public class OutilsTest {

    /**
     * Crée des paramètres à partir de paires clé, valeur.
     */
    public static Parametres parametres(String... clesValeurs) {
        Properties prop = new Properties();
        for (int i = 0; i + 1 < clesValeurs.length; i += 2) {
            prop.setProperty(clesValeurs[i], clesValeurs[i + 1]);
        }
        return new Parametres(prop);
    }

    /**
     * Crée un combattant avec le même nombre de munitions de chaque type.
     */
    public static Combattant combattant(String nom, int numero, int energie, int munitions) {
        return new Combattant(nom, numero, "test", energie, munitions, munitions, munitions);
    }

    /**
     * Place un combattant en (x, y).
     */
    public static void placer(Grille grille, Combattant c, int x, int y) {
        grille.placerCombattant(c, new Position(x, y));
    }

    /**
     * Crée une partie à ordre fixe avec deux combattants déjà placés.
     */
    public static Partie partie(Parametres p, Grille grille, Combattant a, StrategieJoueur sa,
            Combattant b, StrategieJoueur sb) {
        Partie partie = new Partie(p, grille, new OrdreFixe());
        partie.ajouterJoueur(a, sa);
        partie.ajouterJoueur(b, sb);
        return partie;
    }

    /**
     * Stratégie de test qui joue une liste d'actions donnée, puis attend.
     */
    public static class StrategieScriptee implements StrategieJoueur {
        private List<Action> actions = new ArrayList<>();

        public StrategieScriptee(Action... liste) {
            for (Action a : liste) {
                actions.add(a);
            }
        }

        public Action choisirAction(VueJoueur vue) {
            if (actions.isEmpty()) {
                return new Attente();
            }
            return actions.remove(0);
        }

        public String getNom() {
            return "script";
        }
    }
}
