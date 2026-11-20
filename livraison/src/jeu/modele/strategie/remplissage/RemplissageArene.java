package jeu.modele.strategie.remplissage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jeu.modele.combattant.Combattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Grille;

/**
 * Remplissage « arène » équitable : piliers réguliers, combattants dans les coins, pastilles symétriques.
 */
public class RemplissageArene implements StrategieRemplissage {

    public void remplir(Grille grille, List<Combattant> combattants, Parametres parametres, Random hasard) {
        int l = grille.getLargeur();
        int h = grille.getHauteur();

        // un pilier toutes les 3 cases, sans toucher les bords
        for (int x = 2; x < l - 2; x += 3) {
            for (int y = 2; y < h - 2; y += 3) {
                grille.setMur(new Position(x, y), true);
            }
        }

        // les 4 coins, puis le milieu des bords
        Position[] departs = {
            new Position(0, 0), new Position(l - 1, h - 1), new Position(l - 1, 0), new Position(0, h - 1),
            new Position(l / 2, 0), new Position(l / 2, h - 1), new Position(0, h / 2), new Position(l - 1, h / 2)
        };
        List<Combattant> restants = new ArrayList<>();
        for (int i = 0; i < combattants.size(); i++) {
            if (i < departs.length && grille.estVide(departs[i])) {
                grille.placerCombattant(combattants.get(i), departs[i]);
            } else {
                restants.add(combattants.get(i));
            }
        }
        OutilsRemplissage.placerCombattants(grille, restants, hasard);

        // pastilles par paires : une case et sa symétrique par rapport au centre
        List<Position> vides = OutilsRemplissage.casesVides(grille);
        Collections.shuffle(vides, hasard);
        int aPlacer = parametres.getNbPastilles();
        for (Position p : vides) {
            if (aPlacer > 0 && grille.estVide(p)) {
                grille.setPastille(p, parametres.getValeurPastille());
                aPlacer--;
                Position miroir = new Position(l - 1 - p.getX(), h - 1 - p.getY());
                if (aPlacer > 0 && grille.estVide(miroir)) {
                    grille.setPastille(miroir, parametres.getValeurPastille());
                    aPlacer--;
                }
            }
        }
    }
}
