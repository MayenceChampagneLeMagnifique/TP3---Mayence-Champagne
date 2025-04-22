package reseau;

import java.io.*;
import java.util.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * La classe {@code ReseauBuilder} fournit des utilitaires pour
 * charger un réseau {@link CivixNet} à partir d'un fichier JSON,
 * ainsi que pour le sérialiser et le désérialiser.
 * <p>
 * Cette classe est utilisée pour initialiser un réseau à partir d'un fichier
 * ou pour en sauvegarder l'état sur disque.
 * </p>
 */
public class ReseauBuilder implements Serializable {

    /**
     * Charge un objet {@link CivixNet} à partir d'un fichier JSON.
     * <p>
     * Le fichier doit contenir une structure JSON avec un tableau d'utilisateurs,
     * chacun ayant un nom, un mot de passe, et une liste d'abonnements.
     * </p>
     * <p>
     * Exemple de structure attendue :
     * <pre>
     * {
     *   "utilisateurs": [
     *     {
     *       "username": "alice",
     *       "password": "420-SF2_H25_limoilou",
     *       "abonnements": ["bob", "charlie"]
     *     },
     *     ...
     *   ]
     * }
     * </pre>
     *
     * @param cheminFichier le chemin absolu ou relatif vers le fichier JSON
     * @return un objet {@link CivixNet} reconstruit à partir du fichier
     * @throws Exception si le fichier est introuvable, mal formé ou si une erreur d'E/S survient
     */
    public static CivixNet chargerDepuisJSON(String cheminFichier) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        CivixNet reseau = new CivixNet();

        try (FileReader reader = new FileReader(cheminFichier)) {
            JsonNode root = mapper.readTree(reader);
            JsonNode utilisateursNode = root.get("utilisateurs");

            if (utilisateursNode != null && utilisateursNode.isArray()) {

                for (JsonNode u : utilisateursNode) {
                    String username = u.get("username").asText();
                    String password = u.get("password").asText();
                    reseau.ajouterUtilisateur(username, password);
                }

                for (JsonNode u : utilisateursNode) {
                    String username = u.get("username").asText();
                    Utilisateur utilisateur = reseau.obtenirUtilisateurAPartirDuUsername(username);
                    List<Utilisateur> abonnements = new ArrayList<>();
                    JsonNode abonnementsNode = u.get("abonnements");

                    if (abonnementsNode != null && abonnementsNode.isArray()) {
                        for (JsonNode ab : abonnementsNode) {
                            String abos = ab.asText();
                            Utilisateur abonnement = reseau.obtenirUtilisateurAPartirDuUsername(abos);
                            abonnements.add(abonnement);
                        }
                    }

                    reseau.ajouterAbonnements(utilisateur, abonnements);
                }
            } else {
                throw new Exception("L'utilisateur n'existe pas");
            }
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier : " + e.getMessage());
        }
        return reseau;
    }

    /**
     * Sérialise un objet {@link CivixNet} et l'écrit dans un fichier `.ser`.
     *
     * @param reseau  l'objet {@link CivixNet} à sauvegarder
     * @param pathOut le répertoire de sortie (chemin terminé par un slash)
     * @throws IOException si une erreur d'écriture survient
     */
    public static void serialise(CivixNet reseau, String pathOut) throws IOException {
        FileOutputStream fos = new FileOutputStream(pathOut);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(reseau);
        oos.close();
    }

    /**
     * Désérialise un objet {@link CivixNet} à partir d’un fichier `.ser`.
     *
     * @param inputFile le chemin vers le fichier de sérialisation
     * @return l'objet {@link CivixNet} restauré
     * @throws IOException            si une erreur de lecture survient
     * @throws ClassNotFoundException si la classe {@link CivixNet} n’est pas trouvée
     */
    public static CivixNet deserialise(String inputFile) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(inputFile);
        ObjectInputStream ois = new ObjectInputStream(fis);
        return (CivixNet) ois.readObject();
    }
}
