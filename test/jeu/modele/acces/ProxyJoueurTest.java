package jeu.modele.acces;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jeu.OutilsTest;
import jeu.OutilsTest.StrategieScriptee;
import jeu.modele.action.PoseExplosif;
import jeu.modele.combattant.Combattant;
import jeu.modele.combattant.TypeArme;
import jeu.modele.config.Parametres;
import jeu.modele.geometrie.Direction;
import jeu.modele.geometrie.Position;
import jeu.modele.partie.Partie;
import jeu.modele.plateau.Bombe;
import jeu.modele.plateau.Grille;
import jeu.modele.plateau.Mine;
import jeu.modele.plateau.Visibilite;

public class ProxyJoueurTest {

    private Parametres parametres = new Parametres();

    @Test
    public void unExplosifInvisibleNEstVuQueParSonPoseur() {
        Grille grille = new Grille(5, 5);
        Combattant a = OutilsTest.combattant("A", 0, 100, 3);
        Combattant b = OutilsTest.combattant("B", 1, 100, 3);
        OutilsTest.placer(grille, a, 0, 0);
        OutilsTest.placer(grille, b, 4, 4);
        Partie partie = OutilsTest.partie(parametres, grille, a, new StrategieScriptee(), b, new StrategieScriptee());
        Position mine = new Position(2, 2);
        Position bombe = new Position(3, 3);
        grille.poserExplosif(mine, new Mine(a, Visibilite.PRIVEE));
        grille.poserExplosif(bombe, new Bombe(b, Visibilite.PUBLIQUE, 3));

        VueJoueur vueA = partie.getJoueurs().get(0).getVue();
        VueJoueur vueB = partie.getJoueurs().get(1).getVue();

        assertNotNull(partie.getExplosif(mine)); // la partie voit tout
        assertNotNull(vueA.getExplosif(mine));   // A voit sa mine
        assertNull(vueB.getExplosif(mine));      // B ne la voit pas
        assertNotNull(vueA.getExplosif(bombe));  // la bombe publique est vue par tous
        assertNotNull(vueB.getExplosif(bombe));
        assertSame(a, vueA.getMoi());
        assertEquals(2, vueA.getCombattantsVivants().size());
    }

    @Test
    public void unePoseSurUnPiegeInvisibleEchoue() {
        Grille grille = new Grille(5, 5);
        Combattant a = OutilsTest.combattant("A", 0, 100, 3);
        Combattant b = OutilsTest.combattant("B", 1, 100, 3);
        OutilsTest.placer(grille, a, 0, 0);
        OutilsTest.placer(grille, b, 2, 1);
        PoseExplosif pose = new PoseExplosif(TypeArme.MINE, Direction.GAUCHE, Visibilite.PUBLIQUE);
        Partie partie = OutilsTest.partie(parametres, grille, a, new StrategieScriptee(), b, new StrategieScriptee(pose));
        grille.poserExplosif(new Position(1, 1), new Mine(a, Visibilite.PRIVEE));

        VueJoueur vueB = partie.getJoueurs().get(1).getVue();
        assertTrue(pose.estPossible(vueB, b));     // B ne voit pas la mine de A
        assertFalse(pose.estPossible(partie, b));  // mais la partie la voit

        partie.jouerTour();
        assertEquals(3, b.getMunitions(TypeArme.MINE));
        assertSame(a, grille.getExplosif(new Position(1, 1)).getProprietaire());
    }
}
