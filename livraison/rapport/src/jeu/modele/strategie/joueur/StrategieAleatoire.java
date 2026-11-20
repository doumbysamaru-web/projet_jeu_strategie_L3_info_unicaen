package jeu.modele.strategie.joueur;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jeu.modele.acces.VueJoueur;
import jeu.modele.action.Action;
import jeu.modele.action.ActivationBouclier;
import jeu.modele.action.Attente;
import jeu.modele.action.Deplacement;
import jeu.modele.action.PoseExplosif;
import jeu.modele.action.Tir;
import jeu.modele.combattant.TypeArme;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Orientation;
import jeu.modele.plateau.Visibilite;

/**
 * Stratégie aléatoire : tire au hasard un type d'action, puis une action possible de ce type.
 */
public class StrategieAleatoire implements StrategieJoueur {

    private Random hasard;

    public StrategieAleatoire(Random hasard) {
        this.hasard = hasard;
    }

    public Action choisirAction(VueJoueur vue) {
        // on range les actions possibles par type, sinon les poses (32 possibilités) seraient presque toujours choisies
        List<Action> deplacements = new ArrayList<>();
        for (Direction d : Direction.cardinales()) {
            ajouterSiPossible(deplacements, new Deplacement(d), vue);
        }
        List<Action> poses = new ArrayList<>();
        TypeArme[] explosifs = { TypeArme.MINE, TypeArme.BOMBE };
        for (TypeArme type : explosifs) {
            for (Direction d : Direction.values()) {
                ajouterSiPossible(poses, new PoseExplosif(type, d, Visibilite.PUBLIQUE), vue);
                ajouterSiPossible(poses, new PoseExplosif(type, d, Visibilite.PRIVEE), vue);
            }
        }
        List<Action> tirs = new ArrayList<>();
        ajouterSiPossible(tirs, new Tir(Orientation.HORIZONTALE), vue);
        ajouterSiPossible(tirs, new Tir(Orientation.VERTICALE), vue);
        List<Action> bouclier = new ArrayList<>();
        bouclier.add(new ActivationBouclier());
        List<Action> attente = new ArrayList<>();
        attente.add(new Attente());

        // On ne garde que les catégories qui ont au moins une action possible (sinon nextInt(0) planterait).
        // Bouclier et attente sont toujours possibles, donc la liste n'est jamais vide.
        List<List<Action>> categories = new ArrayList<>();
        if (!deplacements.isEmpty()) {
            categories.add(deplacements);
        }
        if (!poses.isEmpty()) {
            categories.add(poses);
        }
        if (!tirs.isEmpty()) {
            categories.add(tirs);
        }
        categories.add(bouclier);
        categories.add(attente);

        List<Action> categorie = categories.get(hasard.nextInt(categories.size()));
        return categorie.get(hasard.nextInt(categorie.size()));
    }

    private void ajouterSiPossible(List<Action> liste, Action action, VueJoueur vue) {
        if (action.estPossible(vue, vue.getMoi())) {
            liste.add(action);
        }
    }

    public String getNom() {
        return "aleatoire";
    }
}
