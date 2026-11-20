package jeu.modele.partie;

import java.util.ArrayList;
import java.util.List;

/**
 * Ordre fixe : toujours l'ordre du fichier de paramètres.
 */
public class OrdreFixe implements OrdreDeJeu {

    public List<Joueur> ordonner(List<Joueur> joueurs) {
        return new ArrayList<>(joueurs);
    }
}
