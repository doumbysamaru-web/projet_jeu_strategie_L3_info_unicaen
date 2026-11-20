package jeu.modele.strategie.joueur;

import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;

/**
 * Stratégie d'un joueur robot (pattern Strategy) : choisit une action à partir de la vue du joueur.
 */
public interface StrategieJoueur {

    /**
     * Choisit l'action à jouer ce tour-ci.
     */
    Action choisirAction(VueJoueur vue);

    /**
     * Nom de la stratégie (aleatoire, chasseur, prudent).
     */
    String getNom();
}
