package app.gui;

import app.map.Map;
import app.map.MapException;

import javax.swing.*;
import java.awt.*;

public class MapWindowDialog extends JDialog {
    private Map map;

    public MapWindowDialog(Window parent, Map map) {
        super(parent, ModalityType.DOCUMENT_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(340, 250);
        setLocationRelativeTo(parent);
        this.map = map;

        JLabel nameLabel = new JLabel("Name");
        JLabel publisherLabel = new JLabel("Publisher");
        JLabel widthLabel = new JLabel("Width");
        JLabel heightLabel = new JLabel("Height");
        JLabel scaleLabel = new JLabel("Scale");

        JTextField nameText = new JTextField(25);
        JTextField publisherText = new JTextField(25);
        JTextField widthText = new JTextField(5);
        JTextField heightText = new JTextField(5);
        JTextField scaleText = new JTextField(6);

        if(map == null) {
            setTitle("New map");
        } else {
            setTitle("Map " + map.getName());
            nameText.setText(map.getName());
            publisherText.setText(map.getPublisher());
            widthText.setText(Integer.toString(map.getWidth()));
            heightText.setText(Integer.toString(map.getHeight()));
        }

        JPanel panel = new JPanel();

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(publisherLabel);
        panel.add(publisherText);
        panel.add(widthLabel);
        panel.add(widthText);
        panel.add(heightLabel);
        panel.add(heightText);
        panel.add(scaleLabel);
        panel.add(scaleText);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            String name = nameText.getText();
            String publisher = publisherText.getText();
            int width, height, scale;
            try {
                width = Integer.parseInt(widthText.getText());
                height = Integer.parseInt(heightText.getText());
                scale = Integer.parseInt(scaleText.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                if(this.map == null) {
                    this.map = new Map(width, height, scale, name, publisher);
                } else {
                    this.map.setName(name);
                    this.map.setPublisher(publisher);
                    this.map.setDimensions(width, height);
                    this.map.setScale(scale);
                }
                dispose();
            } catch(MapException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(okButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionEvent -> {
            dispose();
        });
        panel.add(cancelButton);

        setContentPane(panel);

        setVisible(true);
    }

    public static Map changeMapData(Window parent, Map map) {
        MapWindowDialog dialog = new MapWindowDialog(parent, map);
        return dialog.map;
    }
}
