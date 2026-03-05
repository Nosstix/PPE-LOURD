package vue;

import javax.swing.*;
import java.awt.*;

public class UI {

    public static void showError(Component parent, String msg, Exception e) {
        String text = msg + (e != null ? "\n\n" + e.getMessage() : "");
        JOptionPane.showMessageDialog(parent, text, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public static void showInfo(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    public static int confirm(Component parent, String msg) {
        return JOptionPane.showConfirmDialog(parent, msg, "Confirmer", JOptionPane.YES_NO_OPTION);
    }

    public static JPanel form(Object... labelThenField) {
        JPanel p = new JPanel(new GridLayout(0,2,10,10));
        p.setOpaque(false);
        for (int i=0;i<labelThenField.length;i+=2) {
            p.add((Component)labelThenField[i]);
            p.add((Component)labelThenField[i+1]);
        }
        return p;
    }
}
