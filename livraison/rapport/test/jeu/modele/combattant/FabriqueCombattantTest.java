package jeu.modele.combattant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import jeu.OutilsTest;
import jeu.modele.config.Parametres;

public class FabriqueCombattantTest {

    private Parametres parametres = new Parametres();

    @Test
    public void chaqueTypeASonProfil() {
        Combattant tank = CatalogueFabriques.creer("tank", parametres).creer("T", 0);
        Combattant equilibre = CatalogueFabriques.creer("equilibre", parametres).creer("E", 1);
        Combattant artilleur = CatalogueFabriques.creer("artilleur", parametres).creer("A", 2);

        assertEquals(96, tank.getEnergie());
        assertEquals(60, equilibre.getEnergie());
        assertEquals(42, artilleur.getEnergie());
        assertEquals(3, tank.getMunitions(TypeArme.TIR));
        assertEquals(6, equilibre.getMunitions(TypeArme.TIR));
        assertEquals(12, artilleur.getMunitions(TypeArme.TIR));
        assertEquals("tank", tank.getType());
    }

    @Test
    public void typeInconnu() {
        assertThrows(IllegalArgumentException.class, () -> CatalogueFabriques.creer("dragon", parametres));
    }

    @Test
    public void consommerUneMunition() {
        Combattant c = OutilsTest.combattant("C", 0, 10, 1);
        assertTrue(c.consommerMunition(TypeArme.BOMBE));
        assertEquals(0, c.getMunitions(TypeArme.BOMBE));
        assertFalse(c.consommerMunition(TypeArme.BOMBE));
    }

    @Test
    public void leBouclierDureJusquaLaFinDuTourSuivant() {
        Combattant c = OutilsTest.combattant("C", 0, 10, 0);
        c.activerBouclier();
        assertTrue(c.aBouclierActif());
        c.finDeTour(); // fin du tour où il est levé
        assertTrue(c.aBouclierActif());
        c.finDeTour(); // fin du tour suivant
        assertFalse(c.aBouclierActif());
    }
}
