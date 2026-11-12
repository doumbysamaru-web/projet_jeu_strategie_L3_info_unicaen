package jeu.modele.partie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jeu.modele.combattant.CatalogueFabriques;
import jeu.modele.combattant.Combattant;
import jeu.modele.config.ConfigurationJoueur;
import jeu.modele.config.Parametres;
import jeu.modele.plateau.Grille;
import jeu.modele.strategie.joueur.CatalogueStrategies;
import jeu.modele.strategie.joueur.StrategieJoueur;
import jeu.modele.strategie.remplissage.CatalogueRemplissages;

/**
 * Construit une partie prête à jouer à partir des paramètres.
 */
public class CreateurPartie {

    /**
     * Crée le générateur aléatoire (avec la graine des paramètres si elle est donnée).
     */
    public static Random creerGenerateur(Parametres parametres) {
        if (parametres.getGraine().isEmpty()) {
            return new Random();
        }
        return new Random(Long.parseLong(parametres.getGraine()));
    }


    // Crée une partie : combattants, stratégies, grille remplie et ordre de jeu.
    
    // Chaque stratégie et l'ordre de jeu reçoivent leur propre Random, créé à partir de "hasard".
    // Comme ça, avec la même graine on obtient exactement la même partie, et une stratégie qui
    // tire plus de nombres aléatoires qu'une autre ne change pas ce que reçoivent les autres.
    public static Partie creer(Parametres parametres, Random hasard) {
        List<Combattant> combattants = new ArrayList<>();
        List<StrategieJoueur> strategies = new ArrayList<>();
        int numero = 0;
        for (ConfigurationJoueur config : parametres.getJoueurs()) {
            Combattant c = CatalogueFabriques.creer(config.getType(), parametres).creer(config.getNom(), numero);
            combattants.add(c);
            strategies.add(CatalogueStrategies.creer(config.getStrategie(), new Random(hasard.nextLong())));
            numero++;
        }

        // la stratégie de remplissage pose les murs, les pastilles ET place les combattants
        Grille grille = new Grille(parametres.getLargeur(), parametres.getHauteur());
        CatalogueRemplissages.creer(parametres.getRemplissage()).remplir(grille, combattants, parametres, hasard);

        OrdreDeJeu ordre;
        if (parametres.isOrdreAleatoire()) {
            ordre = new OrdreAleatoire(new Random(hasard.nextLong()));
        } else {
            ordre = new OrdreFixe();
        }

        Partie partie = new Partie(parametres, grille, ordre);
        for (int i = 0; i < combattants.size(); i++) {
            partie.ajouterJoueur(combattants.get(i), strategies.get(i));
        }
        return partie;
    }
}
