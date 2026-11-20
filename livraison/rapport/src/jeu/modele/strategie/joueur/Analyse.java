package jeu.modele.strategie.joueur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jeu.modele.acces.VueJeu;
import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;
import jeu.modele.action.Deplacement;
import jeu.modele.action.PoseExplosif;
import jeu.modele.action.Tir;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Orientation;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.InfosExplosif;
import jeu.modele.plateau.Visibilite;

/**
 * Méthodes utiles aux stratégies des robots (chemins, lignes de tir, dangers).
 */
public class Analyse {

    /**
     * Renvoie les combattants vivants autres que le joueur.
     */
    public static List<InfosCombattant> ennemis(VueJoueur vue) {
        List<InfosCombattant> liste = new ArrayList<>();
        for (InfosCombattant c : vue.getCombattantsVivants()) {
            if (c != vue.getMoi()) {
                liste.add(c);
            }
        }
        return liste;
    }

    /**
     * Vrai si un tir parti de "depuis" peut toucher "vers" (alignés, assez près, pas de mur entre les deux).
     */
    public static boolean ligneDeTirLibre(VueJeu vue, Position depuis, Position vers) {
        // trois conditions pour pouvoir tirer : alignés, assez près (même règle que dans Tir), et pas de mur entre
        if (depuis.equals(vers) || !depuis.estAligneeAvec(vers)) {
            return false;
        }
        if (depuis.distanceManhattan(vers) >= vue.getParametres().getPorteeTir()) {
            return false;
        }
        Direction sens;
        if (depuis.getX() == vers.getX()) {
            if (vers.getY() > depuis.getY()) {
                sens = Direction.BAS;
            } else {
                sens = Direction.HAUT;
            }
        } else {
            if (vers.getX() > depuis.getX()) {
                sens = Direction.DROITE;
            } else {
                sens = Direction.GAUCHE;
            }
        }
        Position p = depuis.voisine(sens);
        while (!p.equals(vers)) {
            if (vue.estMur(p)) {
                return false;
            }
            p = p.voisine(sens);
        }
        return true;
    }

    /**
     * Renvoie un ennemi sans bouclier que l'on peut toucher depuis cette case, ou null.
     */
    public static InfosCombattant ennemiEnLigneDeMire(VueJoueur vue, Position depuis) {
        for (InfosCombattant e : ennemis(vue)) {
            if (!e.aBouclierActif() && ligneDeTirLibre(vue, depuis, e.getPosition())) {
                return e;
            }
        }
        return null;
    }

    /**
     * Vrai si un ennemi qui a encore des tirs peut tirer sur cette case.
     */
    public static boolean estSousLeFeu(VueJoueur vue, Position p) {
        for (InfosCombattant e : ennemis(vue)) {
            if (e.getMunitions(TypeArme.TIR) > 0 && ligneDeTirLibre(vue, e.getPosition(), p)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Vrai si une bombe visible qui explose à la fin du tour touchera cette case.
     */
    public static boolean estMenaceeParBombe(VueJeu vue, Position p) {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Position q = new Position(p.getX() + dx, p.getY() + dy);
                if (vue.estDansGrille(q)) {
                    InfosExplosif e = vue.getExplosif(q);
                    if (e != null && e.getTypeArme() == TypeArme.BOMBE && e.getCompteARebours() <= 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Vrai si on peut passer sur la case sans danger connu.
     */
    public static boolean estTraversable(VueJeu vue, Position p) {
        return vue.estDansGrille(p) && !vue.estMur(p) && vue.getCombattant(p) == null && vue.getExplosif(p) == null;
    }

    /**
     * Parcours en largeur depuis la case de départ : renvoie les cases atteignables de la plus
     * proche à la plus lointaine, et remplit premierPas avec la direction du premier pas pour y aller.
     *
     * Comment ça marche : "ordre" sert de file. On traite les cases dans l'ordre où elles ont été
     * ajoutées, donc d'abord celles à 1 pas, puis à 2 pas, etc. C'est ce qui garantit que la première
     * case qui convient est la plus proche. "premierPas" sert aussi à savoir si une case a déjà été vue.
     */
    private static List<Position> parcours(VueJeu vue, Position depart, Map<Position, Direction> premierPas) {
        List<Position> ordre = new ArrayList<>();
        ordre.add(depart);
        premierPas.put(depart, null);
        int i = 0;
        while (i < ordre.size()) {
            Position p = ordre.get(i);
            i++;
            for (Direction d : Direction.cardinales()) {
                Position v = p.voisine(d);
                if (!premierPas.containsKey(v) && estTraversable(vue, v)) {
                    // Le premier pas d'une case = celui de la case d'où on vient. Seules les cases
                    // voisines du départ ont leur propre direction (celle qu'on vient de prendre).
                    if (p.equals(depart)) {
                        premierPas.put(v, d);
                    } else {
                        premierPas.put(v, premierPas.get(p));
                    }
                    ordre.add(v);
                }
            }
        }
        ordre.remove(0); // on enlève la case de départ
        return ordre;
    }

    /**
     * Direction du premier pas vers la pastille la plus proche, ou null.
     */
    public static Direction versPastille(VueJoueur vue) {
        Map<Position, Direction> premierPas = new HashMap<>();
        for (Position p : parcours(vue, vue.getMoi().getPosition(), premierPas)) {
            if (vue.getPastille(p) > 0) {
                return premierPas.get(p);
            }
        }
        return null;
    }

    /**
     * Direction du premier pas vers la case la plus proche d'où l'on peut tirer sur un ennemi, ou null.
     */
    public static Direction versPositionDeTir(VueJoueur vue) {
        Map<Position, Direction> premierPas = new HashMap<>();
        for (Position p : parcours(vue, vue.getMoi().getPosition(), premierPas)) {
            if (ennemiEnLigneDeMire(vue, p) != null) {
                return premierPas.get(p);
            }
        }
        return null;
    }

    /**
     * Direction du premier pas vers une case collée à un ennemi, ou null.
     */
    public static Direction versEnnemi(VueJoueur vue) {
        Map<Position, Direction> premierPas = new HashMap<>();
        for (Position p : parcours(vue, vue.getMoi().getPosition(), premierPas)) {
            for (InfosCombattant e : ennemis(vue)) {
                if (e.getPosition().distanceManhattan(p) == 1) {
                    return premierPas.get(p);
                }
            }
        }
        return null;
    }

    /**
     * Renvoie un déplacement vers une case qui n'est pas menacée par une bombe, ou null.
     */
    public static Action fuiteDesBombes(VueJoueur vue, Random hasard) {
        List<Action> refuges = new ArrayList<>();
        for (Direction d : Direction.cardinales()) {
            Position p = vue.getMoi().getPosition().voisine(d);
            if (estTraversable(vue, p) && !estMenaceeParBombe(vue, p)) {
                refuges.add(new Deplacement(d));
            }
        }
        if (refuges.isEmpty()) {
            return null;
        }
        return refuges.get(hasard.nextInt(refuges.size()));
    }

    /**
     * Renvoie une pose possible sur la case voisine la plus proche de la cible, ou null.
     */
    public static Action poseVers(VueJoueur vue, TypeArme type, Position cible, Visibilite visibilite) {
        Action meilleure = null;
        int meilleureDistance = 1000;
        for (Direction d : Direction.values()) {
            Action pose = new PoseExplosif(type, d, visibilite);
            int distance = vue.getMoi().getPosition().voisine(d).distanceChebyshev(cible);
            if (pose.estPossible(vue, vue.getMoi()) && distance < meilleureDistance) {
                meilleure = pose;
                meilleureDistance = distance;
            }
        }
        return meilleure;
    }

    /**
     * Renvoie le tir qui vise la cible (elle doit être alignée avec le joueur).
     */
    public static Action tirVers(VueJoueur vue, InfosCombattant cible) {
        if (cible.getPosition().getY() == vue.getMoi().getPosition().getY()) {
            return new Tir(Orientation.HORIZONTALE);
        }
        return new Tir(Orientation.VERTICALE);
    }

    /**
     * Vrai si le joueur peut payer ce coût sans tomber à 0 d'énergie.
     */
    public static boolean peutPayer(VueJoueur vue, int cout) {
        return vue.getMoi().getEnergie() > cout;
    }
}
