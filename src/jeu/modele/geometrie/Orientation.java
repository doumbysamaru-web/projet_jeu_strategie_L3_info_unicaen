package jeu.modele.geometrie;

/**
 * Orientation d'un tir : horizontale ou verticale.
 */
public enum Orientation {
    HORIZONTALE,
    VERTICALE;

    /**
     * Renvoie les deux sens dans lesquels part le tir.
     * Un tir est horizontal ou vertical, et peut donc partir dans deux directions.
     */
    public Direction[] getSens() {
        if (this == HORIZONTALE) {
            return new Direction[] { Direction.GAUCHE, Direction.DROITE };
        } else {
            return new Direction[] { Direction.HAUT, Direction.BAS };
        }
    }
}
