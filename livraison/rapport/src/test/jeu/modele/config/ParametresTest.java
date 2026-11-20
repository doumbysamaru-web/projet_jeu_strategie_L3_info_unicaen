package jeu.modele.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

import jeu.OutilsTest;

public class ParametresTest {

    @Test
    public void valeursParDefaut() {
        Parametres p = new Parametres();
        assertEquals(Parametres.LARGEUR, p.getLargeur());
        assertEquals(Parametres.PORTEE_TIR, p.getPorteeTir());
        assertEquals(4, p.getJoueurs().size());
    }

    @Test
    public void uneCleRemplaceLaValeurParDefaut() {
        Parametres p = OutilsTest.parametres("largeur", "20", "ordre", "fixe");
        assertEquals(20, p.getLargeur());
        assertFalse(p.isOrdreAleatoire());
    }

    @Test
    public void lectureDesJoueurs() {
        List<ConfigurationJoueur> joueurs = ConfigurationJoueur.lireListe("A:tank:Chasseur; B:artilleur:aleatoire");
        assertEquals(2, joueurs.size());
        assertEquals("A", joueurs.get(0).getNom());
        assertEquals("tank", joueurs.get(0).getType());
        assertEquals("chasseur", joueurs.get(0).getStrategie());
    }

    @Test
    public void valeursIncorrectes() {
        assertThrows(IllegalArgumentException.class, () -> OutilsTest.parametres("largeur", "abc"));
        assertThrows(IllegalArgumentException.class, () -> OutilsTest.parametres("joueurs", "Seul:tank:chasseur"));
        assertThrows(IllegalArgumentException.class, () -> ConfigurationJoueur.lireListe("A:tank"));
    }
}
