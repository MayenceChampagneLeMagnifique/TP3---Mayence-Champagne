package reseau;

import java.io.Serializable;
import java.util.*;

/**
 * La classe {@code CivixNet} représente un réseau social simplifié où les utilisateurs
 * peuvent s'abonner à d'autres utilisateurs et interagir avec eux.
 * <p>
 * Chaque utilisateur est représenté par un objet {@link Utilisateur}, et les relations
 * d'abonnement sont gérées dans une carte associant chaque utilisateur à un ensemble
 * d'autres utilisateurs qu'il suit.
 * </p>
 */
public class CivixNet {

    /**
     * La carte représentant les utilisateurs et leurs abonnements.
     * La clé est un utilisateur, et la valeur est l'ensemble des utilisateurs qu'il suit.
     */
    private TreeMap<Utilisateur, Set<Utilisateur>> utilisateurs;

    /**
     * Constructeur par défaut. Initialise un réseau vide.
     */
    public CivixNet() {
        this.utilisateurs = new TreeMap<>();
    }

    /**
     * Retourne la carte des utilisateurs du réseau.
     *
     * @return une map représentant les utilisateurs et leurs abonnements
     */
    public Map<Utilisateur, Set<Utilisateur>> getUtilisateurs() {
        return utilisateurs;
    }

    /**
     * Ajoute un nouvel utilisateur au réseau.
     *
     * @param username le nom d'utilisateur
     * @param password le mot de passe associé
     * @throws IllegalArgumentException si le nom ou le mot de passe est invalide
     */
    public void ajouterUtilisateur(String username, String password) {
        Utilisateur newUser = new Utilisateur(username, password);
        if (username == null || password == null) {
            throw new IllegalArgumentException("Le nom ou le mot de passe est invalide.");
        }

        this.utilisateurs.put(newUser, new HashSet<Utilisateur>());
    }

    /**
     * Abonne un utilisateur à un autre.
     *
     * @param compte           l'utilisateur qui souhaite suivre
     * @param nouvelAbonnement l'utilisateur à suivre
     */
    public void ajouterAbonnement(Utilisateur compte, Utilisateur nouvelAbonnement) {
        if (utilisateurs.containsKey(compte)) {
            utilisateurs.get(compte).add(nouvelAbonnement);
        }
    }

    /**
     * Retire un abonnement pour un utilisateur donné.
     *
     * @param compte             l'utilisateur qui arrête de suivre
     * @param abonnementARetirer l'utilisateur à ne plus suivre
     */
    public void retirerAbonnement(Utilisateur compte, Utilisateur abonnementARetirer) {
        utilisateurs.get(compte).remove(abonnementARetirer);
    }

    /**
     * Abonne un utilisateur à une liste d'autres utilisateurs.
     *
     * @param compte              l'utilisateur qui souhaite suivre d'autres comptes
     * @param nouveauxAbonnements la liste des nouveaux abonnements
     */
    public void ajouterAbonnements(Utilisateur compte, List<Utilisateur> nouveauxAbonnements) {
        utilisateurs.get(compte).addAll(nouveauxAbonnements);
    }

    /**
     * Retire une liste d'abonnements pour un utilisateur donné.
     *
     * @param compte              l'utilisateur concerné
     * @param abonnementsARetirer la liste des abonnements à supprimer
     */
    public void retirerAbonnements(Utilisateur compte, List<Utilisateur> abonnementsARetirer) {
        utilisateurs.get(compte).removeAll(abonnementsARetirer);
    }

    /**
     * Recherche un utilisateur dans le réseau à partir de son nom.
     *
     * @param username le nom d'utilisateur recherché
     * @return l'objet {@link Utilisateur} correspondant
     * @throws RuntimeException si l'utilisateur n'existe pas
     */
    public Utilisateur obtenirUtilisateurAPartirDuUsername(String username) {
        for (Utilisateur u : utilisateurs.keySet()) {

                if (u.getUsername().equals(username)) {
                    return u;
                }
        }

        throw new RuntimeException("Utilisateur introuvable");
    }

    /**
     * Vérifie si deux utilisateurs sont mutuellement abonnés.
     *
     * @param u1 le premier utilisateur
     * @param u2 le second utilisateur
     * @return {@code true} si u1 suit u2, sinon {@code false}
     */
    public boolean abonnementMutuel(Utilisateur u1, Utilisateur u2) {

        if (utilisateurs.containsKey(u1) && utilisateurs.containsKey(u2)) {
            return utilisateurs.get(u1).contains(u2) && utilisateurs.get(u2).contains(u1);
        }
        return false;
    }

    /**
     * Retourne la liste des utilisateurs affectés par une fausse information initiée par un utilisateur donné,
     * à l'aide d'une approche récursive. Cette méthode appelle la méthode privée propagerRecursive.
     * <p>
     * La propagation se fait jusqu'à deux niveaux de connexions :
     * - niveau 0 : l'utilisateur initial
     * - niveau 1 : ses abonnés directs
     * - niveau 2 : les abonnés de ses abonnés
     * <p>
     * Les doublons sont éliminés, et le résultat est retourné trié en ordre alphabétique inverse.
     *
     * @param username le nom d'utilisateur de la personne ayant lancé la fausse information
     * @return une liste triée en ordre alphabétique inverse des utilisateurs affectés sans doublons
     */
    public ArrayList<Utilisateur> propagationFausseInformationRecursive(String username) {
        Utilisateur u = obtenirUtilisateurAPartirDuUsername(username);
        Set<Utilisateur> affectes = new HashSet<>();

        propagerRecursive(u, 0, 2, affectes);
        ArrayList<Utilisateur> pesteBubonique = new ArrayList<>(affectes); // :)
        pesteBubonique.sort((u1, u2) -> u2.getUsername().compareTo(u1.getUsername()));

        return pesteBubonique;
    }

    /**
     * Méthode auxiliaire récursive qui propage la fausse information dans le réseau.
     *
     * @param courant   l'utilisateur actuellement traité dans la récursion
     * @param niveau    niveau actuel de la récursion (0 pour l'utilisateur initial)
     * @param maxNiveau niveau maximal de propagation autorisé (ex. : 2)
     * @param affectes  ensemble cumulatif des utilisateurs affectés par la propagation
     */
    private void propagerRecursive(Utilisateur courant, int niveau, int maxNiveau, Set<Utilisateur> affectes) {
        if (niveau > maxNiveau || affectes.contains(courant)) {
            return;
        }
        affectes.add(courant);

        for (Utilisateur abonnement : utilisateurs.get(courant)) {
            propagerRecursive(abonnement, niveau++, maxNiveau, affectes);
        }
    }


    /**
     * Retourne une représentation textuelle du réseau.
     *
     * @return une chaîne de caractères listant les utilisateurs et leurs abonnements
     */
    @Override
    public String toString() {
        String str = "=== Réseau CivixNet ===";
        for (Utilisateur u : utilisateurs.keySet()) {
            str += "\n" + u.getUsername() + " suit : ";
            for (Utilisateur abonnement : utilisateurs.get(u)) {

                if (abonnement != null) {
                    str += "aucun";
                }
                str += abonnement.getUsername() + ", ";
            }
        }

        return str;
    }
}
