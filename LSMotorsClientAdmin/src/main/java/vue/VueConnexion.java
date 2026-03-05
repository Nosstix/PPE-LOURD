package vue;

import controleur.User;
import modele.Modele;

import javax.swing.*;
import java.awt.*;

public class VueConnexion extends JFrame {

    private final JTextField txtEmail = new JTextField(24);
    private final JPasswordField txtMdp = new JPasswordField(24);
    private final JButton btConnexion = new JButton("Connexion");
    private final JLabel lblStatus = new JLabel(" ");

    public VueConnexion() {
        super("LS Motors - Connexion Admin");

        Image icon = Assets.logoWindowIcon();
        if (icon != null) setIconImage(icon);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 340);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        root.setOpaque(true);
        root.setBackground(Theme.PANEL);

        JLabel title = new JLabel("Connexion Console Admin");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 20f));
        title.setForeground(Theme.ACCENT);

        JLabel sub = new JLabel("Accès réservé aux comptes admin");
        sub.setForeground(Theme.MUTED);

        JPanel top = new JPanel(new GridLayout(0, 1, 6, 6));
        top.setOpaque(true);
        top.setBackground(Theme.PANEL);
        top.add(title);
        top.add(sub);

        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));
        form.setOpaque(true);
        form.setBackground(Theme.PANEL);

        form.add(label("Email"));
        form.add(txtEmail);

        form.add(label("Mot de passe"));
        form.add(txtMdp);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(true);
        actions.setBackground(Theme.PANEL);
        actions.add(btConnexion);

        lblStatus.setForeground(Theme.MUTED);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(true);
        bottom.setBackground(Theme.PANEL);
        bottom.add(lblStatus, BorderLayout.WEST);
        bottom.add(actions, BorderLayout.EAST);

        root.add(top, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        root.add(bottom, BorderLayout.SOUTH);

        setContentPane(root);

        getRootPane().setDefaultButton(btConnexion);
        btConnexion.addActionListener(e -> doLogin());

        Style.applyToWindow(this);

        setVisible(true);
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(Theme.FG);
        return l;
    }

    private void setBusy(boolean busy, String msg) {
        btConnexion.setEnabled(!busy);
        txtEmail.setEnabled(!busy);
        txtMdp.setEnabled(!busy);
        lblStatus.setText(msg == null ? " " : msg);
        setCursor(busy ? Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR) : Cursor.getDefaultCursor());
    }

    private void doLogin() {
        final String email = txtEmail.getText().trim();
        final String mdp = new String(txtMdp.getPassword()).trim();

        if (email.isEmpty() || mdp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email et mot de passe obligatoires.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        setBusy(true, "Connexion en cours...");

        new SwingWorker<User, Void>() {
            @Override
            protected User doInBackground() {
                return Modele.selectWhereUser(email, mdp);
            }

            @Override
            protected void done() {
                try {
                    User u = get();

                    setBusy(false, " ");

                    if (u == null) {
                        JOptionPane.showMessageDialog(VueConnexion.this,
                                "Identifiants incorrects.",
                                "Connexion refusée",
                                JOptionPane.WARNING_MESSAGE);
                        txtMdp.setText("");
                        txtMdp.requestFocus();
                        return;
                    }

                    if (!"admin".equalsIgnoreCase(u.getRole())) {
                        JOptionPane.showMessageDialog(VueConnexion.this,
                                "Ce compte n'a pas le rôle admin.",
                                "Accès refusé",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    dispose();
                    new VueGenerale(u);

                } catch (Exception ex) {
                    setBusy(false, " ");
                    JOptionPane.showMessageDialog(VueConnexion.this,
                            "Erreur de connexion.\n" + ex.getMessage(),
                            "Erreur",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
}
