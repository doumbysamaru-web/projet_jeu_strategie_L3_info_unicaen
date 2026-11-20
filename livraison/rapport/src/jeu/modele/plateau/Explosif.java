package jeu.modele.plateau;

import java.util.List;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;

/**
 * Arme posée sur la grille (mine ou bombe).
 */
public abstract class Explosif implements InfosExplosif {

    private Combattant proprietaire;
    private Visibilite visibilite;

    public Explosif(Combattant proprietaire, Visibilite visibilite) {
        this.proprietaire = proprietaire;
        this.visibilite = visibilite;
    }

    public InfosCombattant getProprietaire() {
        return proprietaire;
    }

    public Visibilite getVisibilite() {
        return visibilite;
    }

    // Un explosif public se voit toujours ; un explosif privé seulement par son poseur
    // (on compare avec == car on veut le même objet, pas juste un combattant qui lui ressemble).
    public boolean estVisiblePar(InfosCombattant c) {
        return visibilite == Visibilite.PUBLIQUE || c == proprietaire;
    }

    public int getCompteARebours() {
        return -1; // par défaut, pas de compte à rebours (mine ou bombe posée mais pas encore armée)
    }

    /**
     * Fait avancer la minuterie d'un tour ; renvoie vrai si l'explosif doit exploser.
     */
    public boolean decompter() {
        return false;
    }

    /**
     * Renvoie les cases touchées par l'explosion.
     */
    public abstract List<Position> zoneImpact(Position centre, Grille grille);

    /**
     * Renvoie l'énergie perdue par chaque combattant touché.
     */
    public abstract int getDegats(Parametres parametres);

    /**
     * Vrai si le bouclier protège de cette explosion.
     */
    public abstract boolean estArreteParBouclier();
}
