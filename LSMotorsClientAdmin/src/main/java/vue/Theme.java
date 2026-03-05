package vue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.util.Enumeration;

public class Theme {

    public static final Color BG      = new Color(14, 14, 18);
    public static final Color PANEL   = new Color(20, 20, 26);
    public static final Color CARD    = new Color(26, 26, 34);

    public static final Color FG      = new Color(235, 235, 235);
    public static final Color MUTED   = new Color(170, 170, 170);

    public static final Color ACCENT  = new Color(212, 175, 55); // doré

    public static final Color BTN_BG      = new Color(34, 34, 44);
    public static final Color BTN_BG_DIS  = new Color(28, 28, 36);
    public static final Color BTN_FG_DIS  = new Color(130, 130, 130);

    public static final Color BORDER   = new Color(55, 55, 70);
    public static final Color TABLE_BG = new Color(18, 18, 24);
    public static final Color SEL_BG   = new Color(55, 55, 75);

    public static void apply() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}

        // Police plus grande partout
        Font base = new Font("SansSerif", Font.PLAIN, 14);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object k = keys.nextElement();
            Object v = UIManager.get(k);
            if (v instanceof Font) UIManager.put(k, base);
        }

        Border line = BorderFactory.createLineBorder(BORDER, 1);
        Border empty = BorderFactory.createEmptyBorder(6, 8, 6, 8);

        put("control", PANEL);
        put("Panel.background", PANEL);
        put("Viewport.background", PANEL);
        put("ScrollPane.background", PANEL);
        put("TabbedPane.background", BG);
        put("TabbedPane.foreground", FG);

        put("Label.foreground", FG);

        put("TextField.background", CARD);
        put("TextField.foreground", FG);
        put("TextField.caretForeground", FG);
        put("TextField.border", line);

        put("PasswordField.background", CARD);
        put("PasswordField.foreground", FG);
        put("PasswordField.caretForeground", FG);
        put("PasswordField.border", line);

        put("TextArea.background", CARD);
        put("TextArea.foreground", FG);
        put("TextArea.caretForeground", FG);
        put("TextArea.border", line);

        put("ComboBox.background", CARD);
        put("ComboBox.foreground", FG);
        put("ComboBox.border", line);
        put("ComboBox.selectionBackground", SEL_BG);
        put("ComboBox.selectionForeground", FG);

        put("List.background", CARD);
        put("List.foreground", FG);
        put("List.selectionBackground", SEL_BG);
        put("List.selectionForeground", FG);

        put("Button.background", BTN_BG);
        put("Button.foreground", FG);
        put("Button.border", BorderFactory.createCompoundBorder(line, empty));
        put("Button.focus", new ColorUIResource(new Color(0,0,0,0)));
        put("Button.disabledText", BTN_FG_DIS);

        put("Table.background", TABLE_BG);
        put("Table.foreground", FG);
        put("Table.selectionBackground", SEL_BG);
        put("Table.selectionForeground", FG);
        put("Table.gridColor", BORDER);

        put("TableHeader.background", CARD);
        put("TableHeader.foreground", FG);
        put("TableHeader.border", line);

        put("OptionPane.background", PANEL);
        put("OptionPane.messageForeground", FG);

        put("ToolTip.background", CARD);
        put("ToolTip.foreground", FG);
        put("ToolTip.border", line);
    }

    private static void put(String key, Object value) {
        UIManager.put(key, value instanceof Color ? new ColorUIResource((Color) value) : value);
    }
}
