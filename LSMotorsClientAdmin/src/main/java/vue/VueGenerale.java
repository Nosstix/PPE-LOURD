package vue;

import controleur.User;

import javax.swing.*;
import java.awt.*;

public class VueGenerale extends JFrame {

    public VueGenerale(User admin) {
        super("LS Motors - Console Admin");

        Image icon = Assets.logoWindowIcon();
        if (icon != null) setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1280, 760);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setOpaque(true);
        root.setBackground(Theme.BG);
        root.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(true);
        tabs.setBackground(Theme.BG);
        tabs.setForeground(Theme.FG);
        tabs.setBorder(BorderFactory.createEmptyBorder());

        tabs.addTab("Accueil", new PanelAccueil(admin));
        tabs.addTab("Utilisateurs", new PanelUsers());
        tabs.addTab("Catégories", new PanelCategories());
        tabs.addTab("Marques", new PanelMarques());
        tabs.addTab("Véhicules", new PanelVehicules());
        tabs.addTab("Ventes", new PanelVentes());
        tabs.addTab("Config", new PanelConfig());

        root.add(tabs, BorderLayout.CENTER);
        setContentPane(root);

        Style.applyToWindow(this);

        setVisible(true);
    }
}
