// Marcel Guzik
// grupa poniedzialek 14:30

package app.gui;

import app.map.Map;

import javax.swing.*;

public class WindowApp {
    static Map map;

    static JFrame frame = new JFrame("Map App");

    static JTextField nameText = new JTextField(25);
    static JTextField publisherText = new JTextField(25);
    static JTextField widthText = new JTextField(5);
    static JTextField heightText = new JTextField(5);
    static JTextField scaleText = new JTextField(6);

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            frame.setSize(350, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panel = new JPanel();
            panel.add(new JLabel("map name"));
            panel.add(nameText);
            panel.add(new JLabel("publisher"));
            panel.add(publisherText);
            panel.add(new JLabel("Width"));
            panel.add(widthText);
            panel.add(new JLabel("Height"));
            panel.add(heightText);
            panel.add(new JLabel("Scale"));
            panel.add(scaleText);

            JButton newButton = new JButton("new map");
            newButton.addActionListener(actionEvent -> {
                setMap(MapWindowDialog.changeMapData(frame, null));
            });
            panel.add(newButton);

            JButton changeButton = new JButton("change map");
            changeButton.addActionListener(actionEvent -> {
                setMap(MapWindowDialog.changeMapData(frame, map));
            });
            panel.add(changeButton);

            JButton loadButton = new JButton("load map");
            loadButton.addActionListener(actionEvent -> {
                loadMap();
            });
            panel.add(loadButton);

            JButton saveButton = new JButton("save map");
            saveButton.addActionListener(actionEvent -> {
                saveMap();
            });
            panel.add(saveButton);

            JMenuBar menuBar = new JMenuBar();
            JMenu fileMenu = new JMenu("File");
            var newItem = new JMenuItem("New");
            newItem.addActionListener(actionEvent -> {
                setMap(MapWindowDialog.changeMapData(frame, null));
            });
            var openItem = new JMenuItem("Open");
            openItem.addActionListener(actionEvent -> {
                loadMap();
            });
            var saveItem = new JMenuItem("Save");
            saveItem.addActionListener(actionEvent -> {
                saveMap();
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
            setMap(newMap);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    static void saveMap() {
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

    static void setMap(Map newMap) {
        if(newMap == null) {
            return;
        }
        map = newMap;
        nameText.setText(map.getName());
        publisherText.setText(map.getPublisher());
        widthText.setText(Integer.toString(map.getWidth()));
        heightText.setText(Integer.toString(map.getHeight()));
        scaleText.setText(Integer.toString(map.getScale()));
    }
}
