package jeu.modele.acces;

import java.util.List;

import jeu.modele.combattant.InfosCombattant;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Position;
import jeu.modele.plateau.InfosExplosif;
import jeu.util.observateur.EcouteurModele;

/**
 * Proxy d'un joueur (pattern Proxy) : tout est délégué à la vraie partie, sauf les explosifs
 * invisibles posés par les autres joueurs, qui sont cachés.
 *
 * Le proxy et la Partie implémentent la même interface VueJeu, donc le code qui les utilise
 * (les stratégies, PanneauGrille...) ne sait pas lequel des deux il a. Il y a un proxy par joueur
 * et rien n'est copié : le proxy garde juste une référence vers la partie ("reel") et vers le
 * combattant du joueur ("moi").
 */
public class ProxyJoueur implements VueJoueur {

    private VueJeu reel;
    private InfosCombattant moi;

    public ProxyJoueur(VueJeu reel, InfosCombattant moi) {
        this.reel = reel;
        this.moi = moi;
    }

    public InfosCombattant getMoi() {
        return moi;
    }

    // La seule méthode qui ne fait pas qu'appeler reel : si l'explosif existe mais que "moi" n'a pas
    // le droit de le voir (invisible et posé par quelqu'un d'autre), on répond comme s'il n'y avait rien.
    public InfosExplosif getExplosif(Position p) {
        InfosExplosif e = reel.getExplosif(p);
        if (e != null && e.estVisiblePar(moi)) {
            return e;
        }
        return null;
    }

    public int getLargeur() {
        return reel.getLargeur();
    }

    public int getHauteur() {
        return reel.getHauteur();
    }

    public boolean estDansGrille(Position p) {
        return reel.estDansGrille(p);
    }

    public boolean estMur(Position p) {
        return reel.estMur(p);
    }

    public int getPastille(Position p) {
        return reel.getPastille(p);
    }

    public InfosCombattant getCombattant(Position p) {
        return reel.getCombattant(p);
    }

    public List<InfosCombattant> getCombattants() {
        return reel.getCombattants();
    }

    public List<InfosCombattant> getCombattantsVivants() {
        return reel.getCombattantsVivants();
    }

    public int getNumeroTour() {
        return reel.getNumeroTour();
    }

    public boolean estTerminee() {
        return reel.estTerminee();
    }

    public InfosCombattant getVainqueur() {
        return reel.getVainqueur();
    }

    public Parametres getParametres() {
        return reel.getParametres();
    }

    public List<String> getJournal() {
        return reel.getJournal();
    }

    public void ajoutEcouteur(EcouteurModele e) {
        reel.ajoutEcouteur(e);
    }

    public void retraitEcouteur(EcouteurModele e) {
        reel.retraitEcouteur(e);
    }
}
