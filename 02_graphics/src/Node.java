import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class Node implements Serializable {
    protected int x;
    protected int y;

    protected int r;

    private Color color;
    String name;


    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.r = 10;

        name = new String("?");

        this.color = Color.WHITE;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMouseOver(int mx, int my){
        return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
    }

    void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(x-r, y-r, 2*r, 2*r);
        g.setColor(Color.BLACK);
        g.drawOval(x-r, y-r, 2*r, 2*r);
        Font font = new Font("serif", Font.PLAIN, 16);
        FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(this.name, frc);
        g.drawString(this.name, this.x - (int)bounds.getWidth() / 2, this.y + (int)bounds.getHeight() / 4);
    }

    @Override
    public String toString(){
        return ("(" + x +", " + y + ", " + r + ")");
    }
}
