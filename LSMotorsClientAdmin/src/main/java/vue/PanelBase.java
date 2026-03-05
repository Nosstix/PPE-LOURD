package vue;

import controleur.Tableau;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.regex.Pattern;

public abstract class PanelBase extends JPanel {

    protected JTable table = new JTable();
    protected Tableau model = new Tableau(new Object[0][0], new String[0]);
    protected JButton btRefresh = new JButton("Rafraîchir");

    protected final JTextField txtSearch = new JTextField(24);
    private TableRowSorter<Tableau> sorter;

    public PanelBase() {
        super(new BorderLayout(10,10));

        setOpaque(true);
        setBackground(Theme.PANEL);

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.setOpaque(true);
        top.setBackground(Theme.PANEL);

        JLabel lab = new JLabel("Recherche :");
        lab.setForeground(Theme.FG);
        top.add(lab);
        top.add(txtSearch);
        add(top, BorderLayout.NORTH);

        table.setModel(model);
        table.setFillsViewportHeight(true);

        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Theme.PANEL);
        add(sp, BorderLayout.CENTER);

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { applySearch(); }
            @Override public void removeUpdate(DocumentEvent e) { applySearch(); }
            @Override public void changedUpdate(DocumentEvent e) { applySearch(); }
        });
    }

    protected JPanel actionsBar(JComponent... extra) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        p.setOpaque(false);
        for (JComponent c : extra) p.add(c);
        p.add(btRefresh);
        return p;
    }

    protected void setTable(String[] entetes, Object[][] donnees) {
        if (model.getColumnCount() == 0) {
            model = new Tableau(donnees, entetes);
            table.setModel(model);
            sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
        } else {
            model.setDonnees(donnees);
        }
        applySearch();
    }

    private void applySearch() {
        if (sorter == null) return;
        String q = txtSearch.getText().trim();
        if (q.isEmpty()) {
            sorter.setRowFilter(null);
            return;
        }
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(q)));
    }
}
