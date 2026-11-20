package jeu.modele.geometrie;

/**
 * Les 4 directions autour d'une case (les 4 premières servent aux déplacements). d
 */
public enum Direction {
    HAUT(0, -1),
    BAS(0, 1),
    GAUCHE(-1, 0),
    DROITE(1, 0);

    private int dx;
    private int dy;

    private Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    
    public boolean estCardinale() {
        return dx == 0 || dy == 0;
    }

    
    public static Direction[] cardinales() {
        return new Direction[] { HAUT, BAS, GAUCHE, DROITE };
    }

    public String toString() {
        return name().toLowerCase();
    }
}
