package jeu.modele.strategie.remplissage;

import java.util.List;
import java.util.Random;

import jeu.modele.combattant.Combattant;
import jeu.modele.config.Parametres;
import jeu.modele.plateau.Grille;

/**
 * Façon de remplir la grille au départ : murs, pastilles et position des combattants (pattern Strategy).
 */
public interface StrategieRemplissage {

    /**
     * Remplit une grille vide et y place les combattants.
     */
    void remplir(Grille grille, List<Combattant> combattants, Parametres parametres, Random hasard);
}
