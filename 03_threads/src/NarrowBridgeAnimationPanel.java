import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.awt.geom.AffineTransform;

public class NarrowBridgeAnimationPanel extends JPanel {
    List<Bus> buses;

    public NarrowBridgeAnimationPanel(List<Bus> buses) {
        this.buses = buses;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D)g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, 37, 700);
        g2d.fillRect(497, 0, 37, 700);
        g2d.setColor(Color.GRAY);
        g2d.fillRect(37, 0, 130, 700);
        g2d.fillRect(367, 0, 130, 700);
        g2d.setColor(Color.PINK);
        g2d.fillRect(167, 0, 35, 700);
        g2d.fillRect(332, 0, 35, 700);
        g2d.setColor(new Color(100, 100, 255));
        g2d.fillRect(202, 0, 130, 700);
        g2d.setColor(Color.GRAY);
        Font font = new Font("monospaced", Font.BOLD, 38);
        AffineTransform affineTransform = new AffineTransform();
        affineTransform.rotate(Math.toRadians(-90), 0, 0);
        Font rotatedFont = font.deriveFont(affineTransform);
        g2d.setFont(rotatedFont);
        g2d.drawString("PARKING", 28, 550);
        g2d.drawString("PARKING", 28, 250);
        g2d.drawString("PARKING", 528, 550);
        g2d.drawString("PARKING", 528, 250);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("BRIDGE", 278, 250);
        g2d.drawString("BRIDGE", 278, 550);
        g2d.setColor(Color.WHITE);
        g2d.drawString("GATE", 195, 200);
        g2d.drawString("GATE", 195, 385);
        g2d.drawString("GATE", 195, 570);
        g2d.drawString("GATE", 361, 200);
        g2d.drawString("GATE", 361, 385);
        g2d.drawString("GATE", 361, 570);
        g2d.drawString("ROAD", 110, 200);
        g2d.drawString("ROAD", 110, 385);
        g2d.drawString("ROAD", 110, 570);
        g2d.drawString("ROAD", 446, 200);
        g2d.drawString("ROAD", 446, 385);
        g2d.drawString("ROAD", 446, 570);

        drawBuses(g);
    }

    void drawBuses(Graphics g) {
        for(Bus bus: buses) {
            bus.draw(g);
        }
    }
}
