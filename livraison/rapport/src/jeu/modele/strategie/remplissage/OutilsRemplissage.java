package jeu.modele.strategie.remplissage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import jeu.modele.combattant.Combattant;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Grille;

/**
 * Méthodes communes aux deux remplissages.
 */
class OutilsRemplissage {

    /**
     * Vrai si toutes les cases sans mur communiquent entre elles (parcours en largeur).
     */
    static boolean estConnexe(Grille grille) {
        List<Position> libres = new ArrayList<>();
        for (Position p : grille.getPositions()) {
            if (!grille.estMur(p)) {
                libres.add(p);
            }
        }
        if (libres.isEmpty()) {
            return false;
        }
        // Parcours en largeur depuis la première case libre. Si on atteint autant de cases qu'il y en a
        // de libres, aucune n'est isolée derrière des murs. Même principe que Analyse.parcours.
        List<Position> vues = new ArrayList<>();
        vues.add(libres.get(0));
        int i = 0;
        while (i < vues.size()) {
            Position p = vues.get(i);
            i++;
            for (Direction d : Direction.cardinales()) {
                Position v = p.voisine(d);
                if (grille.estDansGrille(v) && !grille.estMur(v) && !vues.contains(v)) {
                    vues.add(v);
                }
            }
        }
        return vues.size() == libres.size();
    }

    /**
     * Place les combattants sur des cases vides en essayant de les éloigner les uns des autres.
     */
    static void placerCombattants(Grille grille, List<Combattant> combattants, Random hasard) {
        List<Position> dejaPlaces = new ArrayList<>();
        for (Combattant c : combattants) {
            List<Position> vides = casesVides(grille);
            if (vides.isEmpty()) {
                throw new IllegalStateException("Grille trop petite pour placer tous les combattants");
            }
            Collections.shuffle(vides, hasard);
            // parmi 20 cases au hasard, on prend la plus loin des combattants déjà placés
            Position meilleure = vides.get(0);
            int meilleureDistance = -1;
            for (int k = 0; k < 20 && k < vides.size(); k++) {
                Position candidate = vides.get(k);
                int distance = 1000;
                for (Position autre : dejaPlaces) {
                    distance = Math.min(distance, candidate.distanceManhattan(autre));
                }
                if (distance > meilleureDistance) {
                    meilleure = candidate;
                    meilleureDistance = distance;
                }
            }
            grille.placerCombattant(c, meilleure);
            dejaPlaces.add(meilleure);
        }
    }

    /**
     * Pose des pastilles sur des cases vides tirées au hasard.
     */
    static void placerPastilles(Grille grille, int nombre, int valeur, Random hasard) {
        List<Position> vides = casesVides(grille);
        Collections.shuffle(vides, hasard);
        for (int i = 0; i < nombre && i < vides.size(); i++) {
            grille.setPastille(vides.get(i), valeur);
        }
    }

    /**
     * Renvoie les cases sans mur, combattant, pastille ni explosif.
     */
    static List<Position> casesVides(Grille grille) {
        List<Position> vides = new ArrayList<>();
        for (Position p : grille.getPositions()) {
            if (grille.estVide(p)) {
                vides.add(p);
            }
        }
        return vides;
    }
}
