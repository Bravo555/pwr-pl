package app.gui;

import app.map.Map;
import app.map.PointOfInterest;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

class MapTable extends JScrollPane {
    private static final long serialVersionUID = 1L;

    private List<Map> list;
    private JTable table;
    private DefaultTableModel tableModel;

    public MapTable(List<Map> list) {
        this.list = list;
        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createTitledBorder("Lista map:"));

        String[] tableHeader = { "Name", "Publisher", "Width", "Height", "Scale", "Points of interest" };
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
        for (Map map : list) {
            if (map != null) {
                String[] row = {
                    map.getName(),
                    map.getPublisher(),
                    Integer.toString(map.getWidth()),
                    Integer.toString(map.getHeight()),
                    Integer.toString(map.getScale()),
                    Integer.toString(map.getPointsOfInterest().size())
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