package jeu.modele.partie;

import java.util.List;

/**
 * Ordre dans lequel les joueurs jouent pendant un tour (pattern Strategy).
 */
public interface OrdreDeJeu {


    List<Joueur> ordonner(List<Joueur> joueurs); 
    // renvoie les joueurs dans l'ordre où ils vont jouer (la liste renvoyée peut être différente de la liste passée en paramètre, mais doit contenir les mêmes joueurs)
}
