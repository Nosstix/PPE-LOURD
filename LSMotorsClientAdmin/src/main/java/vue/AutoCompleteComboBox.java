package vue;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class AutoCompleteComboBox {

    public static <T> void enable(JComboBox<T> combo, List<T> original) {
        combo.setEditable(true);
        JTextField editor = (JTextField) combo.getEditor().getEditorComponent();

        editor.getDocument().addDocumentListener((SimpleDocumentListener) e ->
                SwingUtilities.invokeLater(() -> applyFilter(combo, editor.getText(), original))
        );

        editor.addActionListener(e -> combo.hidePopup());
    }

    private static <T> void applyFilter(JComboBox<T> combo, String text, List<T> original) {
        String q = text == null ? "" : text.trim().toLowerCase();
        List<T> filtered = new ArrayList<>();

        for (T item : original) {
            String s = String.valueOf(item).toLowerCase();
            if (q.isEmpty() || s.contains(q)) filtered.add(item);
        }

        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        for (T item : filtered) model.addElement(item);

        combo.setModel(model);
        combo.setSelectedItem(text);
        if (model.getSize() > 0) combo.showPopup();
    }
}
