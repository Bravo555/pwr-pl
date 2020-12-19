import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/* 
 *  Program: Prosty edytor grafu
 *     Plik: Node.java
 *           
 *  Klasa Node reprezentuje wêz³y grafu na p³aszczyŸnie. 
 *            
 *    Autor: Mi³osz Skóra
 *     Data:  listopad 2020 r.
 */

public class Node implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected int x;
	protected int y;
	
	protected int r;
	
	private Color color;
	
	private String nodeName;
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.r = 10;
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
	
	public void setXY(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}
	
	public String getNodeName() {
		return nodeName;
	}
	
	public void setNodeName(String newName) {
		this.nodeName = newName;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public boolean isMouseOver(int mx, int my){
		return (x-mx)*(x-mx)+(y-my)*(y-my)<=r*r;
	}

	void draw(Graphics g) {
		g.setColor(color);
		g.fillOval(x-r, y-r, 2*r, 2*r);
		g.setColor(Color.BLACK);
		g.drawOval(x-r, y-r, 2*r, 2*r);
		if(nodeName != null) {
			g.drawString(nodeName, x - 2*nodeName.length(), y+4);
		}
	}
	
	@Override
	public String toString(){
		return ("(" + x +", " + y + ", " + r + ")");
	}
	
}
