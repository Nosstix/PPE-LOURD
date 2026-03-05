package vue;

import controleur.SimpleItem;
import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelMarques extends PanelBase {

    private final JTextField txtNom = new JTextField(22);
    private final JButton btAjouter = new JButton("Ajouter");
    private final JButton btModifier = new JButton("Modifier");
    private final JButton btSupprimer = new JButton("Supprimer");

    public PanelMarques() {
        super();

        JPanel left = new JPanel(new BorderLayout(10,10));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        left.setOpaque(false);

        JLabel t = new JLabel("Gestion des marques");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 18f));
        t.setForeground(Theme.ACCENT);
        left.add(t, BorderLayout.NORTH);

        JPanel form = UI.form(new JLabel("Nom"), txtNom);
        left.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(0,1,8,8));
        actions.setOpaque(false);
        actions.add(btAjouter);
        actions.add(btModifier);
        actions.add(btSupprimer);
        left.add(actions, BorderLayout.SOUTH);

        add(left, BorderLayout.WEST);
        add(actionsBar(), BorderLayout.SOUTH);

        btRefresh.addActionListener(this::onRefresh);
        btAjouter.addActionListener(this::onAdd);
        btModifier.addActionListener(this::onUpdate);
        btSupprimer.addActionListener(this::onDelete);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            fillFormFromSelection();
        });

        setTable(new String[]{"ID", "Nom"}, new Object[0][0]);
        onRefresh(null);
    }

    private void onRefresh(ActionEvent e) {
        try {
            setTable(new String[]{"ID", "Nom"}, Modele.selectAllMarques());
        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger les marques.", ex);
        }
    }

    private void onAdd(ActionEvent e) {
        try {
            String lib = txtNom.getText().trim();
            if (lib.isEmpty()) throw new IllegalArgumentException("Nom obligatoire");
            Modele.insertMarque(new SimpleItem(0, lib));
            txtNom.setText("");
            onRefresh(null);
            UI.showInfo(this, "Marque ajoutée.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible d'ajouter la marque.", ex);
        }
    }

    private void onUpdate(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) { UI.showError(this, "Sélectionne une marque.", null); return; }
            int id = (int) table.getValueAt(row, 0);
            String lib = txtNom.getText().trim();
            if (lib.isEmpty()) throw new IllegalArgumentException("Nom obligatoire");
            Modele.updateMarque(new SimpleItem(id, lib));
            onRefresh(null);
            UI.showInfo(this, "Marque modifiée.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de modifier la marque.", ex);
        }
    }

    private void onDelete(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) { UI.showError(this, "Sélectionne une marque.", null); return; }
            int id = (int) table.getValueAt(row, 0);
            if (UI.confirm(this, "Supprimer la marque ID " + id + " ?") != JOptionPane.YES_OPTION) return;
            Modele.deleteMarque(id);
            txtNom.setText("");
            onRefresh(null);
            UI.showInfo(this, "Marque supprimée.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de supprimer la marque.", ex);
        }
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtNom.setText(String.valueOf(table.getValueAt(row, 1)));
    }
}
