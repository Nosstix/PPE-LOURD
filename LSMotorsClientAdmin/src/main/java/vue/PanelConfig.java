package vue;

import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PanelConfig extends JPanel {

    private final JSpinner spMarge = new JSpinner(new SpinnerNumberModel(40.0, 0.0, 500.0, 0.5));
    private final JLabel lblActuel = new JLabel("-");
    private final JButton btCharger = new JButton("Charger");
    private final JButton btEnregistrer = new JButton("Enregistrer");

    public PanelConfig() {
        super(new BorderLayout(10,10));
        setBorder(BorderFactory.createEmptyBorder(12,12,12,12));
        setOpaque(false);

        JLabel title = new JLabel("Configuration (marge globale %)");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
        title.setForeground(Theme.ACCENT);
        add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(0,2,10,10));
        center.setOpaque(false);
        center.add(new JLabel("Marge (%)"));
        center.add(spMarge);
        center.add(new JLabel("Valeur actuelle"));
        center.add(lblActuel);
        add(center, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);
        actions.add(btCharger);
        actions.add(btEnregistrer);
        add(actions, BorderLayout.SOUTH);

        btCharger.addActionListener(this::onLoad);
        btEnregistrer.addActionListener(this::onSave);

        onLoad(null);
    }

    private void onLoad(ActionEvent e) {
        try {
            double v = Modele.getMargePct();
            lblActuel.setText(v + " %");
            spMarge.setValue(v);
        } catch (Exception ex) {
            UI.showError(this, "Impossible de charger la marge.", ex);
        }
    }

    private void onSave(ActionEvent e) {
        try {
            double pct = ((Number) spMarge.getValue()).doubleValue();
            Modele.setMargePct(pct);
            onLoad(null);
            UI.showInfo(this, "Marge mise à jour.");
        } catch (Exception ex) {
            UI.showError(this, "Impossible d'enregistrer la marge.", ex);
        }
    }
}
