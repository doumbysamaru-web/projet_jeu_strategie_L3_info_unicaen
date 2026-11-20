package jeu.modele.plateau;

import java.util.ArrayList;
import java.util.List;

import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;

/**
 * Mine : explose quand un combattant marche dessus et ne touche que sa case.
 */
public class Mine extends Explosif {

    public Mine(Combattant proprietaire, Visibilite visibilite) {
        super(proprietaire, visibilite);
    }

    public TypeArme getTypeArme() {
        return TypeArme.MINE;
    }

    public List<Position> zoneImpact(Position centre, Grille grille) {
        List<Position> zone = new ArrayList<>();
        zone.add(centre);
        return zone;
    }

    public int getDegats(Parametres parametres) {
        return parametres.getDegatsMine();
    }

    // le bouclier protège des tirs et des bombes, pas des mines
    public boolean estArreteParBouclier() {
        return false;
    }
}
