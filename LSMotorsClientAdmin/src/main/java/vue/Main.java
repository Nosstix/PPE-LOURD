package vue;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Theme.apply();
        SwingUtilities.invokeLater(VueConnexion::new);
    }
}
