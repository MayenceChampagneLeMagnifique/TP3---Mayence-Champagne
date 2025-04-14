package reseau;

import java.io.*;

/**
 * La classe {@code ReseauBuilder} fournit des utilitaires pour
 * charger un réseau {@link CivixNet} à partir d'un fichier JSON,
 * ainsi que pour le sérialiser et le désérialiser.
 * <p>
 * Cette classe est utilisée pour initialiser un réseau à partir d'un fichier
 * ou pour en sauvegarder l'état sur disque.
 * </p>
 */
public class ReseauBuilder {

    /**
     * Charge un objet {@link CivixNet} à partir d'un fichier JSON.
     * <p>
     * Le fichier doit contenir une structure JSON avec un tableau d'utilisateurs,
     * chacun ayant un nom, un mot de passe, et une liste d'abonnements.
     * </p>
     *
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
        // TODO: Compléter cette méthode
        return null;
    }

    /**
     * Sérialise un objet {@link CivixNet} et l'écrit dans un fichier `.ser`.
     *
     * @param reseau  l'objet {@link CivixNet} à sauvegarder
     * @param pathOut le répertoire de sortie (chemin terminé par un slash)
     * @throws IOException si une erreur d'écriture survient
     */
    public static void serialise(CivixNet reseau, String pathOut) throws IOException {
        // TODO: Compléter cette méthode
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
        // TODO: Compléter cette méthode
        return null;
    }
}
