package jeu.modele.acces;

import java.util.List;

import jeu.modele.combattant.InfosCombattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.InfosExplosif;
import jeu.util.observateur.ModeleEcoutable;

/**
 * Accès en lecture à une partie, commun à la partie complète et aux proxys des joueurs.
 */
public interface VueJeu extends ModeleEcoutable {

    int getLargeur();

    int getHauteur();

    boolean estDansGrille(Position p);

    /**
     * Vrai si la case est un mur (faux en dehors de la grille).
     */
    boolean estMur(Position p);

    /**
     * Énergie de la pastille de la case, 0 s'il n'y en a pas.
     */
    int getPastille(Position p);

    /**
     * Explosif visible sur la case, ou null.
     */
    InfosExplosif getExplosif(Position p);

    /**
     * Combattant présent sur la case, ou null.
     */
    InfosCombattant getCombattant(Position p);

    /**
     * Tous les combattants, y compris ceux qui sont éliminés.
     */
    List<InfosCombattant> getCombattants();

    /**
     * Les combattants encore en jeu.
     */
    List<InfosCombattant> getCombattantsVivants();

    int getNumeroTour();

    boolean estTerminee();

    /**
     * Le gagnant, ou null s'il n'y en a pas (encore).
     */
    InfosCombattant getVainqueur();

    Parametres getParametres();

    /**
     * Les derniers messages de la partie.
     */
    List<String> getJournal();
}
