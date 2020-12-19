import java.awt.*;
import java.io.Serializable;

public class Edge implements Serializable {
    private Node n1;
    private Node n2;

    public Edge(Node n1, Node n2) {
        this.n1 = n1;
        this.n2 = n2;
    }

    public Node getN1() {
        return n1;
    }

    public Node getN2() {
        return n2;
    }

    public void draw(Graphics g) {
        g.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY());
    }

    public boolean isMouseOver(int mx, int my) {
        double numerator = ((n1.getY() - n2.getY())*mx + (n2.getX() - n1.getX())*my + n1.getX()*n2.getY() - n2.getX()*n1.getY());
        double denominator = Math.sqrt(Math.pow((n2.getX() - n1.getX()), 2) + Math.pow((n2.getY() - n1.getY()), 2));
        return (Math.abs((numerator/denominator)) < 4);
    }

    public void hover(Component component) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
}
