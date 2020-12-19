// Marcel Guzik
// grupa poniedzialek 14:30

package app.gui;

import app.map.Map;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MapGroup {
    static List<Map> maps = new ArrayList<>();

    static JFrame frame = new JFrame("Map App");

    static JTextField nameText = new JTextField(25);
    static JTextField publisherText = new JTextField(25);
    static JTextField widthText = new JTextField(5);
    static JTextField heightText = new JTextField(5);
    static JTextField scaleText = new JTextField(6);

    static MapTable table = new MapTable(maps);

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            frame.setSize(450, 500);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel panel = new JPanel();

            panel.add(table);

            JButton newButton = new JButton("new map");
            newButton.addActionListener(actionEvent -> {
                Map newMap = MapManager.newMap(frame);
                if(newMap == null) {
                    return;
                }
                System.out.println(newMap.hashCode());
                addMap(newMap);
                table.refreshView();
            });
            panel.add(newButton);

            JButton changeButton = new JButton("change map");
            changeButton.addActionListener(actionEvent -> {
                int index = table.getSelectedIndex();
                if(index < 0) {
                    return;
                }
                Map selected = maps.get(index);
                MapManager.changeMap(frame, selected);
                table.refreshView();
            });
            panel.add(changeButton);

            JButton removeButton = new JButton("remove map");
            removeButton.addActionListener(actionEvent -> {
                int index = table.getSelectedIndex();
                if(index < 0) {
                    return;
                }
                maps.remove(index);
                table.refreshView();
            });
            panel.add(removeButton);

            JButton loadButton = new JButton("load map");
            loadButton.addActionListener(actionEvent -> {
                loadMap();
            });
            panel.add(loadButton);

            JButton saveButton = new JButton("save map");
            saveButton.addActionListener(actionEvent -> {
                int index = table.getSelectedIndex();
                if(index < 0) {
                    return;
                }
                Map selected = maps.get(index);
                saveMap(selected);
            });
            panel.add(saveButton);

            JButton sortBySizeButton = new JButton("Sort by size (default)");
            sortBySizeButton.addActionListener(actionEvent -> {
                maps.sort(null);
                table.refreshView();
            });
            panel.add(sortBySizeButton);

            JButton sortByNameButton = new JButton("Sort by name");
            sortByNameButton.addActionListener(actionEvent -> {
                maps.sort(Comparator.comparing((Map::getName)));
                maps.sort((m1, m2) -> (m1.getName().compareTo(m2.getName())));
                table.refreshView();
            });
            panel.add(sortByNameButton);


            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            var newItem = new JMenuItem("New");
            newItem.addActionListener(actionEvent -> {
                addMap(MapWindowDialog.changeMapData(frame, null));
            });
            var openItem = new JMenuItem("Open");
            openItem.addActionListener(actionEvent -> {
                loadMap();
            });
            var saveItem = new JMenuItem("Save");
            saveItem.addActionListener(actionEvent -> {
//                saveMap();
            });


            fileMenu.add(newItem);
            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.add(new JSeparator());
            var exitItem = new JMenuItem("Exit");
            exitItem.addActionListener(actionEvent -> {
                frame.dispose();
            });
            fileMenu.add(exitItem);

            menuBar.add(fileMenu);

            JMenu helpMenu = new JMenu("Help");
            var about = new JMenuItem("About");
            about.addActionListener(actionEvent -> {
                JOptionPane.showMessageDialog(frame, "Autor: Marcel Guzik");
            });
            helpMenu.add(about);
            menuBar.add(helpMenu);

            frame.setJMenuBar(menuBar);

            frame.setContentPane(panel);

            table.refreshView();
            frame.setVisible(true);
        });
    }

    static void loadMap() {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showOpenDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Map newMap = Map.loadFromFile(chooser.getSelectedFile());
            addMap(newMap);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void saveMap(Map map) {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showSaveDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            map.saveToFile(chooser.getSelectedFile());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void addMap(Map newMap) {
        if(newMap == null) {
            return;
        }
        maps.add(newMap);
        table.refreshView();
    }
}
