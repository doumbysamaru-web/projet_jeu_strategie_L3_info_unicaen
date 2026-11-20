package jeu.modele.partie;

import java.util.ArrayList;
import java.util.List;
import jeu.modele.acces.ProxyJoueur;
import jeu.modele.acces.VueJeu;
import jeu.modele.action.Action;
import jeu.modele.action.Arene;
import jeu.modele.action.Attente;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.InfosCombattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.Explosif;
import jeu.modele.plateau.Grille;
import jeu.modele.plateau.InfosExplosif;
import jeu.modele.strategie.joueur.StrategieJoueur;
import jeu.util.observateur.AbstractModeleEcoutable;

/**
 * Le modèle complet d'une partie : grille, joueurs et déroulement des tours.
 * Elle ne dépend d'aucune classe graphique.
 *
 * Cette classe joue trois rôles à la fois :
 *  VueJeu : on peut lire l'état de la partie (c'est la vue complète, sans rien cacher) ;
 *  Arene : les actions s'en servent pour modifier la partie (déplacer, infliger des dégâts...) ;
 *  AbstractModeleEcoutable : elle prévient les vues à la fin de chaque tour.
 */
public class Partie extends AbstractModeleEcoutable implements VueJeu, Arene {

    private static final int TAILLE_JOURNAL = 500;

    private Parametres parametres;
    private Grille grille;
    private OrdreDeJeu ordre;
    private List<Joueur> joueurs = new ArrayList<>();
    private List<String> journal = new ArrayList<>(); // journal des événements récents (taille limitée à TAILLE_JOURNAL)
    private int numeroTour = 0;
    private boolean terminee = false;
    private Combattant vainqueur = null;

    /**
     * Crée une partie sur une grille déjà remplie (les joueurs sont ajoutés ensuite).
     */
    public Partie(Parametres parametres, Grille grille, OrdreDeJeu ordre) {
        this.parametres = parametres;
        this.grille = grille;
        this.ordre = ordre;
    }

    /**
     * Ajoute un joueur dont le combattant est déjà placé sur la grille, et lui crée son proxy.
     */
    public Joueur ajouterJoueur(Combattant combattant, StrategieJoueur strategie) {
        if (combattant.getPosition() == null) {
            throw new IllegalArgumentException(combattant.getNom() + " n'est pas placé sur la grille");
        }
        Joueur joueur = new Joueur(combattant, strategie, new ProxyJoueur(this, combattant));
        joueurs.add(joueur);
        return joueur;
    }

    public List<Joueur> getJoueurs() {
        return joueurs;
    }

    /**
     * Joue un tour complet : chaque combattant agit, puis les bombes décomptent et les boucliers s'usent.
     */
    public void jouerTour() {
        if (terminee) {
            return; // sécurité : on ne joue pas de tours après la fin de la partie
        }
        numeroTour++;
        journaliser("--- Tour " + numeroTour + " ---");

        // L'ordre est calculé une seule fois au début du tour. Un joueur éliminé par quelqu'un
        // qui joue avant lui est quand même dans la liste, d'où le test estVivant() dans le if.
        for (Joueur joueur : ordre.ordonner(joueursEnJeu())) {
            if (!terminee && joueur.getCombattant().estVivant()) {
                Action action = choisirAction(joueur);
                action.executer(this, joueur.getCombattant());
                verifierSurvivants();
            }
        }

        // Une fois que tout le monde a joué : les bombes décomptent, les boucliers s'usent.
        // Une bombe peut tuer quelqu'un, d'où le deuxième verifierSurvivants().
        if (!terminee) {
            finDeTour();
            verifierSurvivants();
        }
        // Sécurité : sans cette limite, deux robots qui ne font qu'attendre joueraient pour toujours.
        if (!terminee && numeroTour >= parametres.getToursMax()) {
            terminerAuNombreDeTours();
        }
        // On prévient les vues (grille, table, journal...) une seule fois par tour, pas à chaque action.
        fireChangement();
    }


    public void jouerJusquALaFin() { // sécurité : on ne joue pas de tours après la fin de la partie
        while (!terminee) {
            jouerTour();
        }
    }

    
    // une stratégie qui plante (exception) est rattrapée ; 
    // une stratégie qui renvoie null est remplacée ; 
    // une action impossible est refusée. 
    // Dans les trois cas, le joueur « attend » et la partie continue
    private Action choisirAction(Joueur joueur) { 
        Combattant c = joueur.getCombattant();
        Action action;
        try {
            action = joueur.getStrategie().choisirAction(joueur.getVue());
        } catch (RuntimeException e) {
            journaliser("Erreur dans la stratégie de " + c.getNom() + " : " + e.getMessage());
            return new Attente();
        }
        if (action == null) {
            return new Attente();
        }
        if (!action.estPossible(this, c)) {
            journaliser(c.getNom() + " tente une action impossible (" + action.getDescription() + ") et perd son tour");
            return new Attente();
        }
        return action;
    }

    private void finDeTour() {
        // positionsAvecExplosif() renvoie une liste copiée : on peut donc retirer des explosifs
        // de la grille (dans declencherExplosion) pendant qu'on la parcourt.
        for (Position p : grille.positionsAvecExplosif()) {
            Explosif e = grille.getExplosif(p);
            if (e != null && e.decompter()) {
                declencherExplosion(p);
            }
        }
        for (Joueur j : joueurs) {
            j.getCombattant().finDeTour();
        }
    }

    /**
     * Fait exploser l'explosif de la case : les combattants de la zone perdent de l'énergie.
     */
    public void declencherExplosion(Position p) {
        // On enlève l'explosif de la grille avant d'appliquer les dégâts : il ne peut pas exploser
        // deux fois, et une explosion ne déclenche pas les autres explosifs

        Explosif e = grille.retirerExplosif(p);
        if (e == null) {
            return;
        }
        journaliser("Explosion d'une " + e.getTypeArme() + " de " + e.getProprietaire().getNom() + " en " + p);
        for (Position zone : e.zoneImpact(p, grille)) {
            Combattant cible = grille.getOccupant(zone);
            if (cible != null) {
                if (cible.aBouclierActif() && e.estArreteParBouclier()) {
                    journaliser(cible.getNom() + " est protégé par son bouclier");
                } else {
                    infligerDegats(cible, e.getDegats(parametres), "explosion");
                }
            }
        }
    }

    private List<Joueur> joueursEnJeu() {
        List<Joueur> liste = new ArrayList<>();
        for (Joueur j : joueurs) {
            if (j.getCombattant().estVivant()) {
                liste.add(j);
            }
        }
        return liste;
    }

    // la partie s'arrête quand il reste un seul combattant (ou aucun)
    private void verifierSurvivants() {
        if (terminee) {
            return;
        }
        List<Joueur> enJeu = joueursEnJeu();
        if (enJeu.size() == 1) {
            terminer(enJeu.get(0).getCombattant());
        } else if (enJeu.isEmpty()) {
            terminer(null);
        }
    }

    // au nombre maximal de tours, le plus riche en énergie gagne (personne en cas d'égalité)
    private void terminerAuNombreDeTours() {
        journaliser("Nombre maximal de tours atteint");
        Combattant meilleur = null;
        boolean egalite = false;
        for (Joueur j : joueursEnJeu()) {
            Combattant c = j.getCombattant();
            if (meilleur == null || c.getEnergie() > meilleur.getEnergie()) {
                meilleur = c;
                egalite = false;
            } else if (c.getEnergie() == meilleur.getEnergie()) {
                egalite = true;
            }
        }
        if (egalite) {
            terminer(null);
        } else {
            terminer(meilleur);
        }
    }

    private void terminer(Combattant gagnant) {
        terminee = true;
        vainqueur = gagnant;
        if (gagnant == null) {
            journaliser("Partie terminée : aucun vainqueur");
        } else {
            journaliser("Partie terminée : " + gagnant.getNom() + " remporte la partie !");
        }
    }

    // ---------- Méthodes de Arene (utilisées par les actions) ----------

    public Grille getGrille() {
        return grille;
    }

    // Le déplacement a deux effets de bord selon ce qu'il y a sur la case d'arrivée :
    // une pastille est ramassée, un explosif (mine ou bombe) explose tout de suite.
    public void deplacerCombattant(Combattant c, Position destination) {
        grille.deplacerCombattant(c, destination);
        int energie = grille.ramasserPastille(destination);
        if (energie > 0) {
            c.modifierEnergie(energie);
            journaliser(c.getNom() + " ramasse une pastille (+" + energie + ")");
        }
        if (grille.getExplosif(destination) != null) {
            declencherExplosion(destination);
        }
    }

    // infligerDegats() et depenserEnergie() sont séparés pour que les actions puissent infliger des dégâts sans payer le coût (ex: une explosion) ou payer le coût sans infliger de dégâts (ex: un tir raté).
    public void infligerDegats(Combattant cible, int degats, String cause) {
        if (!cible.estVivant()) {
            return;
        }
        cible.modifierEnergie(-degats);
        journaliser(cible.getNom() + " perd " + degats + " d'énergie (" + cause + ")");
        if (!cible.estVivant()) {
            grille.retirerCombattant(cible);
            journaliser(cible.getNom() + " est éliminé !");
        }
    }

    public void depenserEnergie(Combattant c, int cout) {
        if (cout <= 0 || !c.estVivant()) {
            return;
        }
        c.modifierEnergie(-cout);
        if (!c.estVivant()) {
            grille.retirerCombattant(c);
            journaliser(c.getNom() + " n'a plus d'énergie et est éliminé !");
        }
    }

    public void journaliser(String message) {
        journal.add(message);
        if (journal.size() > TAILLE_JOURNAL) {
            journal.remove(0);
        }
    }

    // ---------- Méthodes de VueJeu (la vue complète, sans filtre) ----------

    public Parametres getParametres() {
        return parametres;
    }

    public int getLargeur() {
        return grille.getLargeur();
    }

    public int getHauteur() {
        return grille.getHauteur();
    }

    public boolean estDansGrille(Position p) {
        return grille.estDansGrille(p);
    }

    public boolean estMur(Position p) {
        return grille.estDansGrille(p) && grille.estMur(p);
    }

    public int getPastille(Position p) {
        if (!grille.estDansGrille(p)) {
            return 0;
        }
        return grille.getPastille(p);
    }

    public InfosExplosif getExplosif(Position p) {
        if (!grille.estDansGrille(p)) {
            return null;
        }
        return grille.getExplosif(p);
    }

    public InfosCombattant getCombattant(Position p) {
        if (!grille.estDansGrille(p)) {
            return null;
        }
        return grille.getOccupant(p);
    }

    // Renvoie la liste des combattants (vue complète, sans filtre).
    public List<InfosCombattant> getCombattants() {
        List<InfosCombattant> liste = new ArrayList<>();
        for (Joueur j : joueurs) {
            liste.add(j.getCombattant());
        }
        return liste;
    }

    // Renvoie la liste des combattants encore vivants (vue complète, sans filtre).
    public List<InfosCombattant> getCombattantsVivants() {
        List<InfosCombattant> liste = new ArrayList<>();
        for (Joueur j : joueurs) {
            if (j.getCombattant().estVivant()) {
                liste.add(j.getCombattant());
            }
        }
        return liste;
    }

    public int getNumeroTour() {
        return numeroTour;
    }

    public boolean estTerminee() {
        return terminee;
    }

    public InfosCombattant getVainqueur() {
        return vainqueur;
    }

    public List<String> getJournal() {
        return new ArrayList<>(journal);
    }
}
