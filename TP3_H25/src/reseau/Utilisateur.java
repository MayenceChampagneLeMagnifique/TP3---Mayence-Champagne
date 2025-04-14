package reseau;

public class Utilisateur implements Comparable<Utilisateur> {
    private String username;
    private String password;

    /**
     * Constructeur
     *
     * @param username
     * @param password
     */
    public Utilisateur(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    /**
     * Permet de modifier le nom d'utilisateur selon les règles de validerUsername
     *
     * @param newUsername
     */
    public void setUsername(String newUsername) {
        if (validerUsername(newUsername)) {
            this.username = newUsername;
        }
    }

    /**
     * Permet de modifier le mot de passe selon les règles de validerPassword
     *
     * @param newPassword
     */
    public void setPassword(String newPassword) {
        if (validerPassword(newPassword)) {
            this.password = newPassword;
        }
    }

    /**
     * Permet d'accéder au nom d'utilisateur
     *
     * @return le nom d'utilisateur
     */
    public String getUsername() {
        return username;
    }

    /**
     * Permet d'accéder au mot de passe
     *
     * @return le mot de passe
     */
    public String getPassword() {
        return password;
    }

    /**
     * Permet de valider le nom d'utilisateur
     *
     * @param username
     * @return true si le nom d'utilisateur est inférieur à 15 caractères
     */
    private boolean validerUsername(String username) {
        return username.length() <= 15;
    }

    /**
     * Permet de valider si le mot de passe est supérieur à 12 caractères et contient au moins un chiffre, une lettre minuscule et une lettre majuscule
     *
     * @param password
     * @return true si le mot de passe est valide
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
     * Permet de visualiser le nom d'utilisateur et le mot de passe de l'utilisateur
     *
     * @return le nom d'utilisateur et le mot de passe
     */
    @Override
    public String toString() {
        return "Username : " + getUsername() + "(Mot de passe : " + getPassword() + ")";
    }

    /**
     * Permet de comparer deux utilisateurs par leur nom d'utilisateur en ordre alphabétique
     *
     * @param u (un utilisateur)
     * @return
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
