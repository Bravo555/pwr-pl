package app.gui;

import app.map.MapException;
import app.map.PointOfInterest;

import javax.swing.*;
import java.awt.*;

public class PoiDialog extends JDialog  {
    PointOfInterest poi;

    PoiDialog(Window parent, PointOfInterest poi) {
        super(parent, ModalityType.DOCUMENT_MODAL);
        this.poi = poi;
        setSize(220, 200);
        setTitle("New point of interest");

        JPanel panel = new JPanel();

        JLabel nameLabel = new JLabel("Name: ");
        JTextField nameText = new JTextField(16);

        JLabel xLabel = new JLabel("x: ");
        JTextField xText = new JTextField(6);

        JLabel yLabel = new JLabel("y: ");
        JTextField yText = new JTextField(6);

        panel.add(nameLabel);
        panel.add(nameText);
        panel.add(xLabel);
        panel.add(xText);
        panel.add(yLabel);
        panel.add(yText);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(actionEvent -> {
            // commit changes to list
            try {
                poi.setName(nameText.getText());
                poi.setX(Float.parseFloat(xText.getText()));
                poi.setY(Float.parseFloat(yText.getText()));

                // close
                dispose();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Wrong. Try again or cancel");
            }
        });
        panel.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(actionEvent -> {
            this.poi = null;
            dispose();
        });
        panel.add(cancelButton);

        setContentPane(panel);
        setVisible(true);
    }

    static PointOfInterest newPoi(Window parent) {
        PoiDialog poiDialog = new PoiDialog(parent, new PointOfInterest("name", 0.0f, 0.0f));
        return poiDialog.poi;
    }
}
