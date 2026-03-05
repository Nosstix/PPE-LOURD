package vue;

import controleur.User;

import javax.swing.*;
import java.awt.*;

public class PanelAccueil extends JPanel {

    public PanelAccueil(User admin) {
        super(new BorderLayout(6, 6));
        setBorder(BorderFactory.createEmptyBorder(4,10,4,10));
        setOpaque(true);
        setBackground(Theme.PANEL);

        // ===== HEADER (logo + titre) : reste fixe en haut =====
        JLabel wide = new JLabel();
        wide.setHorizontalAlignment(SwingConstants.CENTER);

        // Tu gardes ta taille de logo
        ImageIcon wideIco = Assets.scaledWideLogoKeepRatio(1200, 350);
        if (wideIco != null) wide.setIcon(wideIco);

        JPanel wideWrap = new JPanel(new BorderLayout());
        wideWrap.setOpaque(true);
        wideWrap.setBackground(Theme.PANEL);
        wideWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wideWrap.add(wide, BorderLayout.CENTER);

        JLabel title = new JLabel("Console Admin LS Motors");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 26f));
        title.setForeground(Theme.ACCENT);

        JLabel sub = new JLabel("Connecté : " + admin.getNom() + " " + admin.getPrenom() + " (" + admin.getEmail() + ")");
        sub.setForeground(Theme.MUTED);

        JPanel header = new JPanel(new BorderLayout(0,2));
        header.setOpaque(true);
        header.setBackground(Theme.PANEL);

        JPanel textHeader = new JPanel(new GridLayout(0,1,6,6));
        textHeader.setOpaque(true);
        textHeader.setBackground(Theme.PANEL);
        textHeader.add(title);
        textHeader.add(sub);

        header.add(textHeader, BorderLayout.NORTH);
        header.add(wideWrap, BorderLayout.CENTER);

        add(header, BorderLayout.NORTH);

        // ===== CONTENU (blocs) : scrollable =====
        JPanel content = new JPanel(new BorderLayout(18,18));
        content.setOpaque(true);
        content.setBackground(Theme.PANEL);

        // Colonne gauche
        JPanel left = new JPanel(new BorderLayout(12,12));
        left.setOpaque(true);
        left.setBackground(Theme.PANEL);
        left.setPreferredSize(new Dimension(380, 10)); // un poil plus large

        left.add(card("Démarrage rapide",
                "• Chaque onglet te permet de : ajouter, modifier, supprimer et afficher les données.\n" +
                        "• Utilisateurs : gérer les comptes et leurs rôles.\n" +
                        "• Catégories / Marques : gérer les listes utilisées par les véhicules.\n" +
                        "• Véhicules : gérer le catalogue (marque et catégorie).\n" +
                        "• Ventes : consulter l’historique hebdomadaire (global et par employé).\n" +
                        "• Config : définir le pourcentage de marge (%) de l’entreprise."
        ), BorderLayout.CENTER);

        // Grille des cartes à droite
        JPanel right = new JPanel(new GridLayout(0,2,12,12));
        right.setOpaque(true);
        right.setBackground(Theme.PANEL);

        right.add(smallCard("👤 Utilisateurs",
                "Ajouter / modifier / supprimer\nGérer les rôles\nPseudo Discord"));
        right.add(smallCard("🏷️ Catégories & Marques",
                "Ajouter / modifier / supprimer\nRecherche instantanée"));
        right.add(smallCard("🚗 Véhicules",
                "Ajouter / modifier / supprimer\nRecherche + auto-complétion\nMarque et catégorie"));
        right.add(smallCard("📊 Ventes",
                "Historique hebdo global\nHistorique hebdo par employé"));
        right.add(smallCard("⚙️ Configuration",
                "Définir la marge globale (%)\nAppliquée au système"));
        right.add(smallCard("🔎 Recherche",
                "Barre de recherche\nFiltre les tableaux en direct"));

        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.CENTER);

        // ScrollPane sur le contenu (si écran trop petit, ça scroll au lieu de couper)
        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Theme.PANEL);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scroll, BorderLayout.CENTER);
    }

    private JPanel card(String title, String body) {
        JPanel p = new JPanel(new BorderLayout(10,10));
        p.setOpaque(true);
        p.setBackground(Theme.CARD);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));

        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 16f));
        t.setForeground(Theme.ACCENT);

        JTextArea txt = new JTextArea(body);
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setOpaque(false);
        txt.setForeground(Theme.FG);
        txt.setFont(txt.getFont().deriveFont(14f));

        p.add(t, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    private JPanel smallCard(String title, String body) {
        JPanel p = new JPanel(new BorderLayout(8,8));
        p.setOpaque(true);
        p.setBackground(Theme.CARD);

        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.BORDER, 1),
                BorderFactory.createEmptyBorder(12,12,12,12)
        ));

        JLabel t = new JLabel(title);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 15f));
        t.setForeground(Theme.ACCENT);

        JTextArea txt = new JTextArea(body);
        txt.setEditable(false);
        txt.setOpaque(false);
        txt.setForeground(Theme.FG);
        txt.setFont(txt.getFont().deriveFont(13f));

        p.add(t, BorderLayout.NORTH);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }
}