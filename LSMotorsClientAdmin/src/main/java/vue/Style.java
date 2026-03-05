package vue;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class Style {

    private static final Border LINE = BorderFactory.createLineBorder(Theme.BORDER, 1);
    private static final Border BTN_BORDER = BorderFactory.createCompoundBorder(
            LINE, BorderFactory.createEmptyBorder(8, 10, 8, 10)
    );

    public static void applyToWindow(Window w) {
        if (w == null) return;
        if (w instanceof JFrame f) {
            f.getContentPane().setBackground(Theme.BG);
        }
        applyRecursive(w);
    }

    private static void applyRecursive(Component c) {
        if (c instanceof JComponent jc) {

            if (jc instanceof JPanel) {
                jc.setOpaque(true);
                jc.setBackground(Theme.PANEL);
            }

            if (jc instanceof JButton b) {
                b.setUI(new BasicButtonUI());
                b.setOpaque(true);
                b.setContentAreaFilled(true);
                b.setBorderPainted(true);

                b.setBackground(b.isEnabled() ? Theme.BTN_BG : Theme.BTN_BG_DIS);
                b.setForeground(b.isEnabled() ? Theme.FG : Theme.BTN_FG_DIS);

                b.setBorder(BTN_BORDER);
                b.setFocusPainted(false);
                b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            if (jc instanceof JTextField t) {
                t.setOpaque(true);
                t.setBackground(Theme.CARD);
                t.setForeground(Theme.FG);
                t.setCaretColor(Theme.FG);
                t.setBorder(LINE);
            }

            if (jc instanceof JPasswordField p) {
                p.setOpaque(true);
                p.setBackground(Theme.CARD);
                p.setForeground(Theme.FG);
                p.setCaretColor(Theme.FG);
                p.setBorder(LINE);
            }

            if (jc instanceof JTextArea a) {
                a.setOpaque(true);
                a.setBackground(Theme.CARD);
                a.setForeground(Theme.FG);
                a.setCaretColor(Theme.FG);
                a.setBorder(LINE);
            }

            if (jc instanceof JComboBox<?> cb) {
                cb.setUI(new BasicComboBoxUI());
                cb.setOpaque(true);
                cb.setBackground(Theme.CARD);
                cb.setForeground(Theme.FG);
                cb.setBorder(LINE);
            }

            // ✅ FIX NAVBAR (JTabbedPane) : forçage du rendu (sinon Windows LAF fait n'importe quoi)
            if (jc instanceof JTabbedPane tabs) {
                tabs.setOpaque(true);
                tabs.setBackground(Theme.BG);
                tabs.setForeground(Theme.FG);
                tabs.setBorder(BorderFactory.createEmptyBorder());

                tabs.setUI(new BasicTabbedPaneUI() {
                    @Override
                    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex,
                                                      int x, int y, int w, int h, boolean isSelected) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(isSelected ? Theme.CARD : Theme.PANEL);
                        g2.fillRect(x, y, w, h);
                        g2.dispose();
                    }

                    @Override
                    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                                  int x, int y, int w, int h, boolean isSelected) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(Theme.BORDER);
                        g2.drawRect(x, y, w, h);
                        g2.dispose();
                    }

                    @Override
                    protected void paintText(Graphics g, int tabPlacement, Font font,
                                             FontMetrics metrics, int tabIndex,
                                             String title, Rectangle textRect, boolean isSelected) {
                        g.setFont(font);
                        g.setColor(isSelected ? Theme.ACCENT : Theme.FG);
                        g.drawString(title, textRect.x, textRect.y + metrics.getAscent());
                    }

                    @Override
                    protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                                       Rectangle[] rects, int tabIndex,
                                                       Rectangle iconRect, Rectangle textRect,
                                                       boolean isSelected) {
                        // pas de focus blanc dégueu
                    }
                });
            }

            if (jc instanceof JScrollPane sp) {
                sp.getViewport().setBackground(Theme.PANEL);
                sp.setBorder(LINE);
            }

            if (jc instanceof JTable table) {
                table.setBackground(Theme.TABLE_BG);
                table.setForeground(Theme.FG);
                table.setGridColor(Theme.BORDER);
                table.setSelectionBackground(Theme.SEL_BG);
                table.setSelectionForeground(Theme.FG);

                JTableHeader th = table.getTableHeader();
                if (th != null) {
                    th.setDefaultRenderer(new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                                       boolean hasFocus, int row, int column) {
                            super.getTableCellRendererComponent(t, value, false, false, row, column);
                            setOpaque(true);
                            setBackground(Theme.CARD);
                            setForeground(Theme.FG);
                            setBorder(LINE);
                            return this;
                        }
                    });
                    th.setBackground(Theme.CARD);
                    th.setForeground(Theme.FG);
                }

                table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                                   boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                        setOpaque(true);
                        if (isSelected) {
                            setBackground(Theme.SEL_BG);
                            setForeground(Theme.FG);
                        } else {
                            setBackground(Theme.TABLE_BG);
                            setForeground(Theme.FG);
                        }
                        return this;
                    }
                });
            }
        }

        if (c instanceof Container ct) {
            for (Component child : ct.getComponents()) applyRecursive(child);
        }
    }
}