package jeu.modele.strategie.remplissage;

import java.util.List;
import java.util.Random;

import jeu.modele.combattant.Combattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Grille;

/**
 * Remplissage aléatoire : murs au hasard (sans couper la grille en morceaux), puis combattants et pastilles.
 */
public class RemplissageAleatoire implements StrategieRemplissage {

    public void remplir(Grille grille, List<Combattant> combattants, Parametres parametres, Random hasard) {
        // Des murs au hasard peuvent enfermer une zone de la grille (un combattant coincé ne pourrait
        // jamais rencontrer les autres). On retire donc tous les murs au hasard, et on recommence tant
        // que les cases libres ne sont pas toutes reliées. Au bout de 100 essais on abandonne les murs.
        boolean ok = false;
        int essai = 0;
        while (!ok && essai < 100) {
            // setMur(p, ...) écrase l'ancienne valeur, donc pas besoin de vider la grille entre deux essais
            for (Position p : grille.getPositions()) {
                grille.setMur(p, hasard.nextDouble() < parametres.getDensiteMurs());
            }
            ok = OutilsRemplissage.estConnexe(grille);
            essai++;
        }
        if (!ok) {
            // on n'a pas trouvé : tant pis, pas de murs
            for (Position p : grille.getPositions()) {
                grille.setMur(p, false);
            }
        }
        OutilsRemplissage.placerCombattants(grille, combattants, hasard);
        OutilsRemplissage.placerPastilles(grille, parametres.getNbPastilles(), parametres.getValeurPastille(), hasard);
    }
}
