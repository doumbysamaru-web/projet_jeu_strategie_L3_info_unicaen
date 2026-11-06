package jeu.modele.geometrie;

/**
 * Coordonnées (x = colonne, y = ligne) d'une case de la grille.
 */
public class Position {

    private int x; // colonne
    private int y; // ligne

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Renvoie la case voisine dans une direction.
     */
    public Position voisine(Direction d) {
        return new Position(x + d.getDx(), y + d.getDy());
    }

    /**
     * Distance en nombre de déplacements (4 directions). savoir où se deplacer
     */
    public int distanceManhattan(Position p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y);
    }

    /**
     * Distance en nombre de pas dans les 8 directions, diagonale comprises. (Impact bombe)
     */
    public int distanceChebyshev(Position p) {
        return Math.max(Math.abs(x - p.x), Math.abs(y - p.y));
    }

    /**
     * Vrai si les deux positions sont sur la même ligne ou la même colonne.
     * Permet de savoir l'impact pour les tirs
     */
    public boolean estAligneeAvec(Position p) {
        return x == p.x || y == p.y;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Position)) {
            return false;
        }
        Position p = (Position) o;
        return x == p.x && y == p.y;
    }

    public int hashCode() {
        return 31 * x + y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
