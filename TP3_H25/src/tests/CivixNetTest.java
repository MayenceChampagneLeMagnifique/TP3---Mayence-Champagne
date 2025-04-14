package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reseau.CivixNet;
import reseau.Utilisateur;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CivixNetTest {

    private CivixNet reseau;
    private Utilisateur alice, bob, clara, david;

    @BeforeEach
    void setUp() {
        reseau = new CivixNet();
        reseau.ajouterUtilisateur("Alice", "MotDePasse12345");
        reseau.ajouterUtilisateur("Bob", "SecurePass45678");
        reseau.ajouterUtilisateur("Clara", "TestPass78901");
        reseau.ajouterUtilisateur("David", "AlphaPass99999");

        alice = reseau.obtenirUtilisateurAPartirDuUsername("Alice");
        bob = reseau.obtenirUtilisateurAPartirDuUsername("Bob");
        clara = reseau.obtenirUtilisateurAPartirDuUsername("Clara");
        david = reseau.obtenirUtilisateurAPartirDuUsername("David");
    }

    @Test
    void testAjouterUtilisateurEtGetUtilisateurs() {
        assertEquals(4, reseau.getUtilisateurs().size());
        assertTrue(reseau.getUtilisateurs().containsKey(alice));
        assertTrue(reseau.getUtilisateurs().get(alice).isEmpty());
    }

    @Test
    void testAjouterAbonnement() {
        reseau.ajouterAbonnement(alice, bob);
        assertTrue(reseau.getUtilisateurs().get(alice).contains(bob));
    }

    @Test
    void testRetirerAbonnement() {
        reseau.ajouterAbonnement(alice, clara);
        reseau.retirerAbonnement(alice, clara);
        assertFalse(reseau.getUtilisateurs().get(alice).contains(clara));
    }

    @Test
    void testAjouterEtRetirerAbonnements() {
        List<Utilisateur> abonnements = List.of(bob, clara, alice);
        reseau.ajouterAbonnements(david, abonnements);
        assertTrue(reseau.getUtilisateurs().get(david).containsAll(abonnements));

        reseau.retirerAbonnements(david, List.of(clara, alice));
        assertTrue(reseau.getUtilisateurs().get(david).contains(bob));
        assertFalse(reseau.getUtilisateurs().get(david).contains(clara));
    }

    @Test
    void testObtenirUtilisateurAPartirDuUsername() {
        Utilisateur u = reseau.obtenirUtilisateurAPartirDuUsername("Bob");
        assertEquals("Bob", u.getUsername());
    }

    @Test
    void testObtenirUtilisateurInexistant() {
        assertThrows(RuntimeException.class, () -> reseau.obtenirUtilisateurAPartirDuUsername("Inconnu"));
    }

    @Test
    void testAbonnementMutuel() {
        reseau.ajouterAbonnement(alice, bob);
        reseau.ajouterAbonnement(bob, alice);
        reseau.ajouterAbonnement(bob, david);
        assertTrue(reseau.abonnementMutuel(alice, bob));
        assertFalse(reseau.abonnementMutuel(bob, david));
    }

    @Test
    void testPropagationFausseInformation() {
        reseau.ajouterAbonnement(alice, bob);    // Alice -> Bob
        reseau.ajouterAbonnement(bob, clara);    // Bob -> Clara
        reseau.ajouterAbonnement(clara, david);  // Clara -> David (niveau 3)

        ArrayList<Utilisateur> result = reseau.propagationFausseInformationRecursive("Alice");
        List<String> noms = result.stream().map(Utilisateur::getUsername).toList();

        assertTrue(noms.contains("Alice"));
        assertTrue(noms.contains("Bob"));
        assertTrue(noms.contains("Clara"));
        assertFalse(noms.contains("David")); // Trop loin
        assertEquals(3, noms.size());

        // Vérifie l'ordre alphabétique inverse
        assertEquals(List.of("Clara", "Bob", "Alice"), noms);
    }

    @Test
    void testPropagationAvecCycle() {
        reseau.ajouterAbonnement(alice, bob);       // Alice -> Bob
        reseau.ajouterAbonnement(bob, alice);       // Bob -> Alice (cycle)
        reseau.ajouterAbonnement(bob, clara);       // Bob -> Clara

        ArrayList<Utilisateur> result = reseau.propagationFausseInformationRecursive("Alice");
        List<String> noms = result.stream().map(Utilisateur::getUsername).toList();

        assertTrue(noms.contains("Alice"));
        assertTrue(noms.contains("Bob"));
        assertTrue(noms.contains("Clara"));
        assertEquals(3, noms.size());

        assertEquals(List.of("Clara", "Bob", "Alice"), noms);
    }

    @Test
    void testPropagationUtilisateurSansAbonnements() {
        // Aucun abonnement
        ArrayList<Utilisateur> result = reseau.propagationFausseInformationRecursive("David");
        List<String> noms = result.stream().map(Utilisateur::getUsername).toList();

        assertEquals(1, noms.size());
        assertTrue(noms.contains("David"));
    }

    @Test
    void testPropagationAvecDoublonsPotentiels() {
        reseau.ajouterAbonnement(alice, bob);       // Alice -> Bob
        reseau.ajouterAbonnement(alice, clara);     // Alice -> Clara
        reseau.ajouterAbonnement(bob, clara);       // Bob -> Clara (déjà ajoutée)
        reseau.ajouterAbonnement(clara, david);     // Clara -> David

        ArrayList<Utilisateur> result = reseau.propagationFausseInformationRecursive("Alice");
        List<String> noms = result.stream().map(Utilisateur::getUsername).toList();

        assertTrue(noms.contains("Alice"));
        assertTrue(noms.contains("Bob"));
        assertTrue(noms.contains("Clara"));

        // Vérifie l'ordre alphabétique inverse sans doublons
        assertEquals(List.of("Clara", "Bob", "Alice"), noms);
        assertEquals(3, noms.size());
    }

    @Test
    void testToString() {
        reseau.ajouterAbonnement(alice, bob);
        reseau.ajouterAbonnement(alice, clara);

        String output = reseau.toString();
        assertTrue(output.contains("Alice suit : Bob, Clara") || output.contains("Alice suit : Clara, Bob"));
        assertTrue(output.contains("Bob suit : aucun"));
    }
}
