package app;

import reseau.CivixNet;
import reseau.ReseauBuilder;
import reseau.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

/**
 * Cette classe représente l'interface utilisateur graphique pour afficher le réseau social CivixNet
 * avec des utilisateurs sous forme de nœuds et leurs connexions sous forme de flèches unidirectionnelles.
 * Elle hérite de JPanel et gère l'affichage du réseau, le positionnement des nœuds, les connexions, et l'interaction avec l'utilisateur.
 */
public class CivixNetSwingUI extends JPanel {

    private final CivixNet reseau;  // Réseau social contenant les utilisateurs et leurs abonnements
    private final Map<Utilisateur, Point> positions;  // Cartographie des utilisateurs et leurs positions sur le panneau
    private Utilisateur utilisateurSelectionne = null;  // Utilisateur actuellement sélectionné par l'utilisateur

    /**
     * Constructeur de la classe CivixNetSwingUI.
     * @param reseau Le réseau social à afficher.
     */
    public CivixNetSwingUI(CivixNet reseau) {
        this.reseau = reseau;
        this.positions = new HashMap<>();
        this.setPreferredSize(new Dimension(800, 600));  // Taille préférée du panneau
        genererPositionsEnCercle();  // Générer une disposition des utilisateurs en cercle
        setupMouseListener();  // Ajouter un écouteur pour la sélection d'utilisateur
    }

    /**
     * Génère une disposition circulaire pour les utilisateurs afin qu'ils soient répartis uniformément sur le panneau.
     */
    private void genererPositionsEnCercle() {
        int centerX = getPreferredSize().width / 2;
        int centerY = getPreferredSize().height / 2;
        int radius = 250;  // Rayon du cercle
        int total = reseau.getUtilisateurs().size();
        int index = 0;

        // Positionner chaque utilisateur sur le cercle
        for (Utilisateur u : reseau.getUtilisateurs().keySet()) {
            double angle = 2 * Math.PI * index / total;
            int x = (int) (centerX + radius * Math.cos(angle));
            int y = (int) (centerY + radius * Math.sin(angle));
            positions.put(u, new Point(x, y));
            index++;
        }
    }

    /**
     * Configure un écouteur de souris pour gérer les clics sur les utilisateurs.
     * Lorsqu'un utilisateur est sélectionné, les informations le concernant sont affichées.
     */
    private void setupMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                for (Map.Entry<Utilisateur, Point> entry : positions.entrySet()) {
                    Point p = entry.getValue();
                    if (p.distance(e.getPoint()) < 30) {  // Si l'utilisateur est cliqué (distance au centre < 30)
                        utilisateurSelectionne = entry.getKey();
                        repaint();  // Repeindre le panneau avec l'utilisateur sélectionné
                        return;
                    }
                }
                utilisateurSelectionne = null;  // Aucune sélection
                repaint();  // Repeindre sans sélection
            }
        });
    }

    /**
     * Fonction qui détermine le symbole de la direction d'une connexion.
     * @param from Point de départ de la connexion.
     * @param to Point d'arrivée de la connexion.
     * @param isBidirectional Indique si la connexion est bidirectionnelle.
     * @return Un symbole de direction sous forme de chaîne ("^", "v", "<", ">").
     */
    private String getDirectionSymbol(Point from, Point to, boolean isBidirectional) {
        int dx = to.x - from.x;
        int dy = to.y - from.y;

        if (Math.abs(dx) > Math.abs(dy)) {
            // Connexion horizontale
            if (isBidirectional)
                return "< >";
            return (dx > 0) ? ">" : "<"; // Flèches vers la droite ou la gauche
        } else if (Math.abs(dy) > Math.abs(dx)) {
            // Connexion verticale
            if (isBidirectional)
                return "^v";
            return (dy > 0) ? "v" : "^"; // Flèches vers le bas ou vers le haut
        } else {
            // Connexion diagonale (petite tolérance)
            if (isBidirectional)
                return "< >";
            return (dx > 0) ? ">" : "<"; // Si la direction est entre gauche et droite
        }
    }

    /**
     * Méthode de dessin de l'interface graphique.
     * Elle dessine les utilisateurs sous forme de cercles et les connexions sous forme de flèches.
     * Affiche également les informations sur l'utilisateur sélectionné.
     * @param g Objet Graphics utilisé pour dessiner.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Dessiner les connexions
        for (Map.Entry<Utilisateur, Set<Utilisateur>> entry : reseau.getUtilisateurs().entrySet()) {
            Utilisateur from = entry.getKey();
            Point fromP = positions.get(from);
            for (Utilisateur to : entry.getValue()) {
                Point toP = positions.get(to);

                // Vérifier si la connexion est bidirectionnelle
                boolean isBidirectional = reseau.abonnementMutuel(to, from);

                // Dessiner la ligne de connexion
                g2d.setColor(Color.GRAY);
                g2d.drawLine(fromP.x, fromP.y, toP.x, toP.y);

                // Ajouter un symbole pour indiquer la direction de la connexion
                String symbol = getDirectionSymbol(fromP, toP, isBidirectional);

                // Déplacer le symbole pour éviter le chevauchement
                Font font = new Font("Arial", Font.PLAIN, 14);
                g2d.setFont(font);
                g2d.setColor(Color.RED);
                g2d.drawString(symbol, (fromP.x + toP.x) / 2, (fromP.y + toP.y) / 2);
            }
        }

        // Dessiner les utilisateurs
        for (Utilisateur u : reseau.getUtilisateurs().keySet()) {
            Point p = positions.get(u);
            g2d.setColor(u.equals(utilisateurSelectionne) ? Color.ORANGE : Color.CYAN);
            g2d.fillOval(p.x - 30, p.y - 30, 60, 60);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(p.x - 30, p.y - 30, 60, 60);
            g2d.drawString(u.getUsername(), p.x - 20, p.y + 5);
        }

        // Infos sur l'utilisateur sélectionné
        if (utilisateurSelectionne != null) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("Utilisateur : " + utilisateurSelectionne.getUsername(), 20, 20);
            g2d.drawString("Mot de passe : " + utilisateurSelectionne.getPassword(), 20, 40);
        }
    }

    /**
     * Méthode principale pour lancer l'application graphique.
     * @param args Arguments de la ligne de commande.
     * @throws Exception Si le fichier JSON est invalide.
     */
    public static void main(String[] args) throws Exception {
        CivixNet reseau = ReseauBuilder.chargerDepuisJSON("C:\\Users\\Boome\\OneDrive\\Bureau\\TP3---Mayence-Champagne\\TP3_H25\\src\\donnees\\reseau.json");

        JFrame frame = new JFrame("CivixNet - Réseau Social");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new CivixNetSwingUI(reseau));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
