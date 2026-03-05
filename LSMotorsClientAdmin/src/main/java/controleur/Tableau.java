package controleur;

import javax.swing.table.AbstractTableModel;

public class Tableau extends AbstractTableModel {
    private Object[][] donnees;
    private String[] entetes;

    public Tableau(Object[][] donnees, String[] entetes) {
        this.donnees = donnees == null ? new Object[0][0] : donnees;
        this.entetes = entetes == null ? new String[0] : entetes;
    }

    @Override
    public int getRowCount() { return donnees.length; }

    @Override
    public int getColumnCount() { return entetes.length; }

    @Override
    public String getColumnName(int column) { return entetes[column]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return donnees[rowIndex][columnIndex];
    }

    public Object getValueAtRowCol(int row, int col) {
        return getValueAt(row, col);
    }

    public void setDonnees(Object[][] nouvellesDonnees) {
        this.donnees = nouvellesDonnees == null ? new Object[0][0] : nouvellesDonnees;
        fireTableDataChanged();
    }
}
