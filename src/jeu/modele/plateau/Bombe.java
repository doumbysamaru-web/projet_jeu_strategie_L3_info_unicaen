package jeu.modele.plateau;

import java.util.List;

import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;

/**
 * Bombe : explose après un compte à rebours (ou si on marche dessus) et touche sa case et les 8 voisines.
 */
public class Bombe extends Explosif {

    private int compteARebours;

    public Bombe(Combattant proprietaire, Visibilite visibilite, int compteARebours) {
        super(proprietaire, visibilite);
        this.compteARebours = compteARebours;
    }

    public TypeArme getTypeArme() {
        return TypeArme.BOMBE;
    }

    public int getCompteARebours() {
        return compteARebours;
    }

    // Appelée par la partie à la fin de chaque tour. Une bombe posée avec delaiBombe = 3 explose
    // donc à la fin du 3e tour qui suit sa pose (ou avant si quelqu'un marche dessus).
    public boolean decompter() {
        compteARebours--;
        return compteARebours <= 0;
    }

    public List<Position> zoneImpact(Position centre, Grille grille) {
        List<Position> zone = grille.voisines(centre);
        zone.add(centre);
        return zone;
    }

    public int getDegats(Parametres parametres) {
        return parametres.getDegatsBombe();
    }

    public boolean estArreteParBouclier() {
        return true;
    }
}
