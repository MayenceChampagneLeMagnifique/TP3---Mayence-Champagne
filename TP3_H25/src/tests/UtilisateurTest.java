package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import reseau.Utilisateur;

import static org.junit.jupiter.api.Assertions.*;

class UtilisateurTest {

    @AfterEach
    void tearDown() {
    }

    @Test
    void setUsername() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        u1.setUsername("PlumeLatraverse");
        assertEquals(u1.getUsername(), "PlumeLatraverse");
    }

    @Test
    void setPassword() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        u1.setPassword("JaimePasLaCoriandre56");
        assertEquals(u1.getPassword(), "JaimePasLaCoriandre56");
    }

    @Test
    void getUsername() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        assertEquals(u1.getUsername(), "BobBissonette");
    }

    @Test
    void getPassword() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        assertEquals(u1.getPassword(), "KetchupMoutarde123");
    }

    @Test
    void testToString() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        assertEquals(u1.toString(), "Username : " + u1.getUsername() + "(Mot de passe : " + u1.getPassword() + ")");
    }

    @Test
    void compareTo() {
        Utilisateur u1 = new Utilisateur("BobBissonette", "KetchupMoutarde123");
        Utilisateur u2 = new Utilisateur("PlumeLatraverse", "JaimeLaBiere123");
        assertEquals(u1.compareTo(u2), -1);
        assertEquals(u2.compareTo(u1), 1);
        assertEquals(u1.compareTo(u1), 0);
    }
}