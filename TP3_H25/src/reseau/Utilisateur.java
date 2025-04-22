/**
 * @author Mayence Champagne
 * <p>
 * ***Javadoc complétée par ChatGPT***
 */


package reseau;

/**
 * La classe {@code Utilisateur} représente un utilisateur avec un nom d'utilisateur (username)
 * et un mot de passe (password). Elle inclut des mécanismes de validation pour s'assurer
 * que les identifiants respectent certaines contraintes de sécurité.
 * <p>
 * Elle permet également de comparer les utilisateurs par ordre alphabétique de leur nom d'utilisateur.
 * </p>
 */
public class Utilisateur implements Comparable<Utilisateur> {
    private String username;
    private String password;

    /**
     * Constructeur de la classe {@code Utilisateur}.
     *
     * @param username le nom d'utilisateur à affecter (doit contenir 15 caractères ou moins)
     * @param password le mot de passe à affecter (doit contenir au moins 12 caractères,
     *                 avec au moins une majuscule, une minuscule et un chiffre)
     */
    public Utilisateur(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * Modifie le nom d'utilisateur si celui-ci est valide selon les règles de validation.
     *
     * @param newUsername le nouveau nom d'utilisateur à définir
     *                    (maximum 15 caractères)
     */
    public void setUsername(String newUsername) {
        if (validerUsername(newUsername)) {
            this.username = newUsername;
        }
    }

    /**
     * Modifie le mot de passe si celui-ci est valide selon les règles de validation.
     *
     * @param newPassword le nouveau mot de passe à définir
     *                    (au moins 12 caractères, incluant une lettre majuscule, une minuscule et un chiffre)
     */
    public void setPassword(String newPassword) {
        if (validerPassword(newPassword)) {
            this.password = newPassword;
        }
    }

    /**
     * Retourne le nom d'utilisateur de l'utilisateur.
     *
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retourne le mot de passe de l'utilisateur.
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }

    /**
     * Vérifie si le nom d'utilisateur est valide.
     *
     * @param username le nom d'utilisateur à valider
     * @return {@code true} si le nom d'utilisateur contient au maximum 15 caractères,
     *         {@code false} sinon
     */
    private boolean validerUsername(String username) {
        return username.length() <= 15;
    }

    /**
     * Vérifie si le mot de passe est valide.
     * Un mot de passe valide doit contenir au moins 12 caractères,
     * une lettre majuscule, une lettre minuscule et un chiffre.
     *
     * @param password le mot de passe à valider
     * @return {@code true} si le mot de passe est conforme, {@code false} sinon
     */
    private boolean validerPassword(String password) {
        if (password.length() < 12) {
            return false;
        }

        int chiffre = 0;
        int minuscule = 0;
        int majuscule = 0;

        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                chiffre++;
            } else if (Character.isLowerCase(c)) {
                minuscule++;
            } else if (Character.isUpperCase(c)) {
                majuscule++;
            }
        }

        return chiffre != 0 && minuscule != 0 && majuscule != 0;
    }

    /**
     * Retourne une représentation textuelle de l'utilisateur, incluant
     * son nom d'utilisateur et son mot de passe.
     *
     * @return une chaîne contenant les informations de l'utilisateur
     */
    @Override
    public String toString() {
        return "Username : " + getUsername() + " (Mot de passe : " + getPassword() + ")";
    }

    /**
     * Compare cet utilisateur à un autre utilisateur par leur nom d'utilisateur
     * en ignorant la casse. L'ordre est alphabétique.
     *
     * @param u l'utilisateur à comparer
     * @return -1 si ce nom d'utilisateur est antérieur à {@code u}, 1 s'il est postérieur,
     *         0 s'ils sont équivalents (en ignorant la casse)
     */
    @Override
    public int compareTo(Utilisateur u) {
        int compare;
        compare = this.username.compareToIgnoreCase(u.username);
        if (compare < 0) return -1;
        if (compare > 0) return 1;

        return 0;
    }
}
