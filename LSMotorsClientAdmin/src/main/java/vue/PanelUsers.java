package vue;

import controleur.User;
import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelUsers extends PanelBase {

    private final JTextField txtNom = new JTextField(16);
    private final JTextField txtPrenom = new JTextField(16);
    private final JTextField txtEmail = new JTextField(22);
    private final JPasswordField txtMdp = new JPasswordField(22);
    private final JComboBox<String> cbRole = new JComboBox<>(new String[]{"admin", "employe", "joueur"});
    private final JTextField txtDiscord = new JTextField(18);

    private final JButton btAjouter = new JButton("Ajouter");
    private final JButton btModifier = new JButton("Modifier");
    private final JButton btSupprimer = new JButton("Supprimer");

    public PanelUsers() {
        super();

        JPanel left = new JPanel(new BorderLayout(10,10));
        left.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        left.setOpaque(false);

        JLabel t = new JLabel("Gestion des utilisateurs");
        t.setFont(t.getFont().deriveFont(Font.BOLD, 18f));
        t.setForeground(Theme.ACCENT);
        left.add(t, BorderLayout.NORTH);

        JPanel form = UI.form(
                new JLabel("Nom"), txtNom,
                new JLabel("Prénom"), txtPrenom,
                new JLabel("Email"), txtEmail,
                new JLabel("Mot de passe"), txtMdp,
                new JLabel("Rôle"), cbRole,
                new JLabel("DiscordPseudo"), txtDiscord
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
        onRefresh(null);
    }

    private void initTable() {
        setTable(new String[]{"ID", "Nom", "Prénom", "Email", "Rôle", "Discord"}, new Object[0][0]);
    }

    private void onRefresh(ActionEvent e) {
        try {
            setTable(new String[]{"ID", "Nom", "Prénom", "Email", "Rôle", "Discord"}, Modele.selectAllUsers());
        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger les utilisateurs.", ex);
        }
    }

    private void onAdd(ActionEvent e) {
        try {
            User u = readForm(0, true);
            Modele.insertUser(u);
            clearForm();
            onRefresh(null);
            UI.showInfo(this, "Utilisateur ajouté.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible d'ajouter l'utilisateur.", ex);
        }
    }

    private void onUpdate(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                UI.showError(this, "Sélectionne un utilisateur dans la liste.", null);
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            boolean updatePassword = new String(txtMdp.getPassword()).trim().length() > 0;

            User u = readForm(id, updatePassword);
            Modele.updateUser(u, updatePassword);
            txtMdp.setText("");

            onRefresh(null);
            UI.showInfo(this, "Utilisateur modifié.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de modifier l'utilisateur.", ex);
        }
    }

    private void onDelete(ActionEvent e) {
        try {
            int row = table.getSelectedRow();
            if (row < 0) {
                UI.showError(this, "Sélectionne un utilisateur.", null);
                return;
            }
            int id = (int) table.getValueAt(row, 0);

            if (UI.confirm(this, "Supprimer l'utilisateur ID " + id + " ?") != JOptionPane.YES_OPTION) return;

            Modele.deleteUser(id);
            clearForm();
            onRefresh(null);
            UI.showInfo(this, "Utilisateur supprimé.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible de supprimer l'utilisateur.", ex);
        }
    }

    private User readForm(int id, boolean passwordRequired) {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String mdp = new String(txtMdp.getPassword()).trim();
        String role = (String) cbRole.getSelectedItem();
        String discord = txtDiscord.getText().trim();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role == null || role.isEmpty()) {
            throw new IllegalArgumentException("Nom/Prénom/Email/Rôle obligatoires.");
        }
        if (passwordRequired && mdp.isEmpty()) {
            throw new IllegalArgumentException("Mot de passe obligatoire (création). Pour modifier sans changer le mot de passe, laisse le champ vide.");
        }

        return new User(id, nom, prenom, email, mdp.isEmpty() ? null : mdp, role, discord.isEmpty() ? null : discord);
    }

    private void fillFormFromSelection() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtNom.setText(String.valueOf(table.getValueAt(row, 1)));
        txtPrenom.setText(String.valueOf(table.getValueAt(row, 2)));
        txtEmail.setText(String.valueOf(table.getValueAt(row, 3)));
        cbRole.setSelectedItem(String.valueOf(table.getValueAt(row, 4)));
        Object d = table.getValueAt(row, 5);
        txtDiscord.setText(d == null ? "" : String.valueOf(d));
        txtMdp.setText("");
    }

    private void clearForm() {
        txtNom.setText("");
        txtPrenom.setText("");
        txtEmail.setText("");
        txtMdp.setText("");
        cbRole.setSelectedIndex(2);
        txtDiscord.setText("");
    }
}
