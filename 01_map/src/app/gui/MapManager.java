package app.gui;

import app.map.Map;
import app.map.MapException;
import app.map.PointOfInterest;

import javax.swing.*;
import java.awt.*;

public class MapManager extends JDialog {
    Map map;

    JTextField nameText = new JTextField(30);
    JTextField publisherText = new JTextField(30);
    JTextField widthText = new JTextField(5);
    JTextField heightText = new JTextField(5);
    JTextField scaleText = new JTextField(6);

    PoiTable table;

     public MapManager(Window parent, Map map) {
         super(parent, ModalityType.DOCUMENT_MODAL);
         this.map = map;
         table = new PoiTable(map.getPointsOfInterest());
         this.setSize(400, 500);
         this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
         setTitle("New map");

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

         panel.add(table);


         JButton loadButton = new JButton("Load POI from file");
         loadButton.addActionListener(actionEvent -> {
             loadPoi();
             table.refreshView();
             System.out.println();
         });
         panel.add(loadButton);

         JButton saveButton = new JButton("Save POI to file");
         saveButton.addActionListener(actionEvent -> {
             int index = table.getSelectedIndex();
             if(index < 0) {
                 return;
             }
             PointOfInterest selected = map.getPointsOfInterest().get(index);
             savePoi(selected);
         });
         panel.add(saveButton);

         JButton removeButton = new JButton("Remove POI");
         removeButton.addActionListener(actionEvent -> {
             int index = table.getSelectedIndex();
             if(index < 0) {
                 return;
             }
             map.getPointsOfInterest().remove(index);
             table.refreshView();
         });
         panel.add(removeButton);

         JButton addPoiButton = new JButton("add POI");
         addPoiButton.addActionListener(actionEvent -> {
             PointOfInterest newPoi = PoiDialog.newPoi(this);
             if(newPoi == null) {
                 return;
             }
             map.addPointOfInterest(newPoi);
             table.refreshView();
         });
         panel.add(addPoiButton);


         JMenuBar menuBar = new JMenuBar();
         JMenu fileMenu = new JMenu("File");
         var newItem = new JMenuItem("New");
         newItem.addActionListener(actionEvent -> {
             setMap(MapWindowDialog.changeMapData(this, null));
         });
         var openItem = new JMenuItem("Open");
         openItem.addActionListener(actionEvent -> {
             loadMap();
         });
         var saveItem = new JMenuItem("Save");
         saveItem.addActionListener(actionEvent -> {
             saveMap();
         });

         JPanel controlButtons = new JPanel();
         JButton okButton = new JButton("OK");
         okButton.addActionListener(actionEvent -> {
             // commit changes to map
             try {
                 map.setName(nameText.getText());
                 map.setPublisher(publisherText.getText());
                 int width = Integer.parseInt(widthText.getText());
                 int height = Integer.parseInt(heightText.getText());
                 map.setDimensions(width, height);
                 map.setScale(Integer.parseInt(scaleText.getText()));

                 // close
                 dispose();
             } catch (MapException e) {
                 JOptionPane.showMessageDialog(this, "Wrong name. Try again or cancel");
             }
         });
         controlButtons.add(okButton);

         JButton cancelButton = new JButton("Cancel");
         cancelButton.addActionListener(actionEvent -> {
             this.map = null;
             dispose();
         });
         controlButtons.add(cancelButton);
         panel.add(controlButtons);
         // close

         dispose();


         fileMenu.add(newItem);
         fileMenu.add(openItem);
         fileMenu.add(saveItem);
         fileMenu.add(new JSeparator());
         var exitItem = new JMenuItem("Exit");
         exitItem.addActionListener(actionEvent -> {
             this.dispose();
         });
         fileMenu.add(exitItem);

         menuBar.add(fileMenu);

         JMenu helpMenu = new JMenu("Help");
         var about = new JMenuItem("About");
         about.addActionListener(actionEvent -> {
             JOptionPane.showMessageDialog(this, "Autor: Marcel Guzik");
         });
         helpMenu.add(about);
         menuBar.add(helpMenu);

         this.setJMenuBar(menuBar);

         this.setContentPane(panel);

         setMap(map);    // yes, this is a stupid hack
         table.refreshView();
         this.setVisible(true);
    }

    void loadMap() {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showOpenDialog(this);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            Map newMap = Map.loadFromFile(chooser.getSelectedFile());
            setMap(newMap);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    void saveMap() {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showSaveDialog(this);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            map.saveToFile(chooser.getSelectedFile());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }

    void setMap(Map newMap) {
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

    public static Map newMap(Window parent) {
         Map map = new Map();
         MapManager mapManager = new MapManager(parent, map);

         return mapManager.map;
    }

    public static Map changeMap(Window parent, Map map) {
         MapManager mapManager = new MapManager(parent, map);
         return mapManager.map;
    }


    void savePoi(PointOfInterest poi) {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showSaveDialog(this);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            poi.saveToFile(chooser.getSelectedFile());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
     }

    void loadPoi() {
        JFileChooser chooser = new JFileChooser("./");
        int status = chooser.showOpenDialog(this);
        if(status != JFileChooser.APPROVE_OPTION) {
            return;
        }

        try {
            PointOfInterest poi = PointOfInterest.loadFromFile(chooser.getSelectedFile());
            map.getPointsOfInterest().add(poi);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
