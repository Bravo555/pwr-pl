package app.gui;

import app.map.PointOfInterest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class PoiTable extends JScrollPane {
    private static final long serialVersionUID = 1L;

    private List<PointOfInterest> list;
    private JTable table;
    private DefaultTableModel tableModel;

    public PoiTable(List<PointOfInterest> list) {
        this.list = list;
        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("Lista punktow:"));

        String[] tableHeader = { "Nazwa", "x", "y" };
        tableModel = new DefaultTableModel(tableHeader, 0);
        table = new JTable(tableModel) {

            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        setViewportView(table);
    }

    void refreshView() {
        tableModel.setRowCount(0);
        for (PointOfInterest poi : list) {
            if (poi != null) {
                String[] row = {
                    poi.getName(),
                    Float.toString(poi.getX()),
                    Float.toString(poi.getY()),
                };
                tableModel.addRow(row);
            }
        }
    }

    int getSelectedIndex() {
        int index = table.getSelectedRow();
        if (index<0) {
            JOptionPane.showMessageDialog(this, "Zadana grupa nie jest zaznaczona.", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
        return index;
    }

}