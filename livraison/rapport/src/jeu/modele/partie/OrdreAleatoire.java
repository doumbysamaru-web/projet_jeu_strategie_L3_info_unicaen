package jeu.modele.partie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Ordre tiré au hasard à chaque tour.
 */
public class OrdreAleatoire implements OrdreDeJeu {

    private Random hasard;

    public OrdreAleatoire(Random hasard) {
        this.hasard = hasard;
    }

    public List<Joueur> ordonner(List<Joueur> joueurs) {
        List<Joueur> ordre = new ArrayList<>(joueurs);
        Collections.shuffle(ordre, hasard);
        return ordre;
    }
}
