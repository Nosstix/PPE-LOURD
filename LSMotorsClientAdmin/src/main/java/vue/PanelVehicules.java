package vue;

import controleur.SimpleItem;
import controleur.Vehicule;
import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class PanelVehicules extends PanelBase {

    private final JTextField txtNomModele = new JTextField(22);
    private final JTextField txtPrix = new JTextField(12);

    private final JComboBox<SimpleItem> cbMarque = new JComboBox<>();
    private final JComboBox<SimpleItem> cbCategorie = new JComboBox<>();

    private List<SimpleItem> marques;
    private List<SimpleItem> categories;

    private final JButton btAjouter = new JButton("Ajouter");
    private final JButton btModifier = new JButton("Modifier");
    private final JButton btSupprimer = new JButton("Supprimer");

    public PanelVehicules() {
        super();

        JPanel left = new JPanel(new BorderLayout(10,10));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        left.setOpaque(false);

        JLabel t = new JLabel("Gestion des véhicules");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 18f));
        t.setForeground(Theme.ACCENT);
        left.add(t, BorderLayout.NORTH);

        JPanel form = UI.form(
                new JLabel("Nom / Modèle"), txtNomModele,
                new JLabel("Marque"), cbMarque,
                new JLabel("Catégorie"), cbCategorie,
                new JLabel("Prix catalogue"), txtPrix
        );
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

        initTable();
        loadCombos();
        onRefresh(null);
    }

    private void initTable() {
        setTable(new String[]{"ID","Nom/Modèle","Prix","Marque","Catégorie","idMarque","idCategorie"}, new Object[0][0]);
        hideColumn(5);
        hideColumn(6);
    }

    private void hideColumn(int modelIndex) {
        try {
            int viewIndex = table.convertColumnIndexToView(modelIndex);
            table.getColumnModel().getColumn(viewIndex).setMinWidth(0);
            table.getColumnModel().getColumn(viewIndex).setMaxWidth(0);
            table.getColumnModel().getColumn(viewIndex).setWidth(0);
        } catch (Exception ignored) {}
    }

    private void loadCombos() {
        try {
            marques = Modele.selectAllMarquesList();
            categories = Modele.selectAllCategoriesList();

            DefaultComboBoxModel<SimpleItem> m1 = new DefaultComboBoxModel<>();
            for (SimpleItem s : marques) m1.addElement(s);
            cbMarque.setModel(m1);

            DefaultComboBoxModel<SimpleItem> m2 = new DefaultComboBoxModel<>();
            for (SimpleItem s : categories) m2.addElement(s);
            cbCategorie.setModel(m2);

            AutoCompleteComboBox.enable(cbMarque, marques);
            AutoCompleteComboBox.enable(cbCategorie, categories);

        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger marques/catégories.", ex);
        }
    }

    private void onRefresh(ActionEvent e) {
        try {
            setTable(new String[]{"ID","Nom/Modèle","Prix","Marque","Catégorie","idMarque","idCategorie"}, Modele.selectAllVehicules());
            hideColumn(5);
            hideColumn(6);
            // si tu as ajouté une marque/catégorie depuis un autre onglet
            loadCombos();
        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger les véhicules.", ex);
        }
    }

    private void onAdd(ActionEvent e) {
        try {
            Vehicule v = readForm(0);
            Modele.insertVehicule(v);
            clearForm();
            onRefresh(null);
            UI.showInfo(this, "Véhicule ajouté.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible d'ajouter le véhicule.", ex);
        }
    }

    private void onUpdate(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                UI.showError(this, "Sélectionne un véhicule.", null);
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            Vehicule v = readForm(id);
            Modele.updateVehicule(v);
            onRefresh(null);
            UI.showInfo(this, "Véhicule modifié.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de modifier le véhicule.", ex);
        }
    }

    private void onDelete(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                UI.showError(this, "Sélectionne un véhicule.", null);
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            if (UI.confirm(this, "Supprimer le véhicule ID " + id + " ?") != JOptionPane.YES_OPTION) return;

            Modele.deleteVehicule(id);
            clearForm();
            onRefresh(null);
            UI.showInfo(this, "Véhicule supprimé.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de supprimer le véhicule.", ex);
        }
    }

    private Vehicule readForm(int id) {
        String nomModele = txtNomModele.getText().trim();
        String prixS = txtPrix.getText().trim();

        if (nomModele.isEmpty() || prixS.isEmpty()) {
            throw new IllegalArgumentException("Nom/Modèle et prix obligatoires.");
        }

        double prix = Double.parseDouble(prixS.replace(",", "."));

        int idMarque = resolveId(cbMarque.getSelectedItem(), marques);
        int idCategorie = resolveId(cbCategorie.getSelectedItem(), categories);
        if (idMarque <= 0 || idCategorie <= 0) {
            throw new IllegalArgumentException("Marque/Catégorie invalide (sélectionne dans la liste).");
        }

        return new Vehicule(id, nomModele, prix, idMarque, idCategorie);
    }

    private int resolveId(Object selected, List<SimpleItem> list) {
        if (selected instanceof SimpleItem) return ((SimpleItem) selected).getId();
        String text = String.valueOf(selected).trim();
        for (SimpleItem s : list) {
            if (s.getLibelle().equalsIgnoreCase(text)) return s.getId();
        }
        return -1;
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtNomModele.setText(String.valueOf(table.getValueAt(row, 1)));
        txtPrix.setText(String.valueOf(table.getValueAt(row, 2)));

        int idMarque = (int) table.getValueAt(row, 5);
        int idCategorie = (int) table.getValueAt(row, 6);
        selectById(cbMarque, marques, idMarque);
        selectById(cbCategorie, categories, idCategorie);
    }

    private void selectById(JComboBox<SimpleItem> combo, List<SimpleItem> list, int id) {
        for (SimpleItem s : list) {
            if (s.getId() == id) {
                combo.setSelectedItem(s);
                return;
            }
        }
    }

    private void clearForm() {
        txtNomModele.setText("");
        txtPrix.setText("");
        if (cbMarque.getItemCount() > 0) cbMarque.setSelectedIndex(0);
        if (cbCategorie.getItemCount() > 0) cbCategorie.setSelectedIndex(0);
    }
}
