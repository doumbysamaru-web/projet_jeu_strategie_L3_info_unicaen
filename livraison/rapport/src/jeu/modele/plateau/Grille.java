package jeu.modele.plateau;

import java.util.ArrayList;
import java.util.List;
import jeu.modele.combattant.Combattant;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;

/**
 * Grille de jeu : chaque case peut contenir un mur, une pastille, un explosif et un combattant.
 */
public class Grille {

    private int largeur;
    private int hauteur;
    // cases[x][y] : x = colonne, y = ligne, y vers le bas comme à l'écran (la ligne 0 est en haut)
    private Case[][] cases;

    public Grille(int largeur, int hauteur) {
        this.largeur = largeur;
        this.hauteur = hauteur;
        cases = new Case[largeur][hauteur];
        for (int x = 0; x < largeur; x++) {
            for (int y = 0; y < hauteur; y++) {
                cases[x][y] = new Case(); // chaque case est initialement vide (pas de mur, pas de pastille, pas d'explosif, pas de combattant)
            }
        }
    }

    public int getLargeur() {
        return largeur;
    }

    public int getHauteur() {
        return hauteur;
    }

    public boolean estDansGrille(Position p) {
        return p.getX() >= 0 && p.getX() < largeur 
            && p.getY() >= 0 && p.getY() < hauteur;
    }


    private Case caseEn(Position p) {
        return cases[p.getX()][p.getY()]; // renvoie la case de coordonnées p (x = colonne, y = ligne)
    }

    public boolean estMur(Position p) {
        return caseEn(p).mur;
    }

    public void setMur(Position p, boolean mur) {
        caseEn(p).mur = mur;
    }

   
    public int getPastille(Position p) {
        return caseEn(p).pastille; // Renvoie l'énergie de la pastille de la case (0 s'il n'y en a pas).
    }

    public void setPastille(Position p, int valeur) {
        caseEn(p).pastille = valeur;
    }

    /**
     * Enlève la pastille de la case et renvoie son énergie.
     */
    public int ramasserPastille(Position p) {
        int valeur = caseEn(p).pastille;
        caseEn(p).pastille = 0;
        return valeur;
    }

    public Explosif getExplosif(Position p) {
        return caseEn(p).explosif;
    }

    public void poserExplosif(Position p, Explosif e) {
        caseEn(p).explosif = e;
    }

    /**
     * Enlève l'explosif de la case et le renvoie (null s'il n'y en avait pas).
     */
    public Explosif retirerExplosif(Position p) {
        Explosif e = caseEn(p).explosif;
        caseEn(p).explosif = null;
        return e;
    }

    /**
     * Renvoie les positions de tous les explosifs de la grille.
     */
    public List<Position> positionsAvecExplosif() {
        List<Position> liste = new ArrayList<>();
        for (Position p : getPositions()) {
            if (caseEn(p).explosif != null) {
                liste.add(p);
            }
        }
        return liste;
    }

    public Combattant getOccupant(Position p) {
        return caseEn(p).occupant;
    }


    public boolean estLibre(Position p) {
        return estDansGrille(p) && !caseEn(p).mur && caseEn(p).occupant == null; 
        // l'ordre est important : on ne veut pas appeler caseEn(p) si p n'est pas dans la grille, sinon on risque un ArrayIndexOutOfBoundsException.
    }

    public boolean estVide(Position p) {
        return estLibre(p) && 
                caseEn(p).pastille == 0 && 
                caseEn(p).explosif == null; 
        // pas de mur, pas d'occupant, pas de pastille, pas d'explosif
    }

    
    // On ne vérifie pas que le combattant n'était pas déjà sur la grille : on peut le déplacer avec placerCombattant() ou deplacerCombattant().
    public void placerCombattant(Combattant c, Position p) { 
        if (!estLibre(p)) {
            throw new IllegalStateException("La case " + p + " n'est pas libre");
        }
        caseEn(p).occupant = c;
        c.setPosition(p);
    }

    /**
     * Enlève un combattant de la grille.
     */
    public void retirerCombattant(Combattant c) {
        Position p = c.getPosition();
        if (p != null && caseEn(p).occupant == c) {
            caseEn(p).occupant = null;
        }
    }

    /**
     * Déplace un combattant vers une case libre.
     */
    public void deplacerCombattant(Combattant c, Position destination) {
        retirerCombattant(c);
        placerCombattant(c, destination);
        // l'ordre est important : on enlève d'abord le combattant de sa case actuelle. 
    }

    /**
     * Renvoie les cases voisines (8 directions) qui sont dans la grille.
     */
    public List<Position> voisines(Position p) {
        List<Position> liste = new ArrayList<>();
        for (Direction d : Direction.values()) {
            Position v = p.voisine(d);
            if (estDansGrille(v)) {
                liste.add(v);
            }
        }
        return liste;
    }

    /**
     * Renvoie toutes les positions de la grille, ligne par ligne.
     */
    public List<Position> getPositions() {
        List<Position> liste = new ArrayList<>();
        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < largeur; x++) {
                liste.add(new Position(x, y));
            }
        }
        return liste;
    }
}
