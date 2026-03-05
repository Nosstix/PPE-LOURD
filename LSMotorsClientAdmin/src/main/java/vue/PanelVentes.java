package vue;

import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelVentes extends JPanel {

    private final JTable tableGlobal = new JTable();
    private final JTable tableEmploye = new JTable();

    private final JButton btRefresh = new JButton("Rafraîchir");

    public PanelVentes() {
        super(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setOpaque(false);

        JLabel title = new JLabel("Historique des ventes (hebdomadaire)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(Theme.ACCENT);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(title, BorderLayout.WEST);
        top.add(btRefresh, BorderLayout.EAST);
        add(top, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setResizeWeight(0.5);
        split.setTopComponent(wrap("Global", tableGlobal));
        split.setBottomComponent(wrap("Par employé", tableEmploye));
        add(split, BorderLayout.CENTER);

        btRefresh.addActionListener(this::onRefresh);
        onRefresh(null);
    }

    private JPanel wrap(String label, JTable t) {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setOpaque(false);
        JLabel l = new JLabel(label);
        l.setForeground(Theme.MUTED);
        p.add(l, BorderLayout.NORTH);
        t.setFillsViewportHeight(true);
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        return p;
    }

    private void onRefresh(ActionEvent e) {
        try {
            Object[][] g = Modele.ventesGlobalHebdo();
            tableGlobal.setModel(new javax.swing.table.DefaultTableModel(
                    g,
                    new String[]{"Semaine(YYYYWW)", "Début", "Total", "Nb ventes"}
            ));

            Object[][] p = Modele.ventesParEmployeHebdo();
            tableEmploye.setModel(new javax.swing.table.DefaultTableModel(
                    p,
                    new String[]{"ID employé", "Employé", "Semaine(YYYYWW)", "Total", "Nb ventes"}
            ));
        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger les ventes.", ex);
        }
    }
}
