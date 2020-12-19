import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

/* 
 *  Program: Prosty edytor grafu
 *     Plik: Edge.java
 *           
 *  Klasa Edge reprezentuje krawêdzie grafu na p³aszczyŸnie. 
 *            
 *    Autor: Mi³osz Skóra
 *     Data:  listopad 2020 r.
 */

public class Edge implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	protected Node node1;
	protected Node node2;
	
	private Color color;

	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public boolean isMouseOver(int mx, int my){
		double numerator = ((node1.getY() - node2.getY())*mx + (node2.getX() - node1.getX())*my + node1.getX()*node2.getY() - node2.getX()*node1.getY());
		double denominator = Math.sqrt(Math.pow((node2.getX() - node1.getX()), 2) + Math.pow((node2.getY() - node1.getY()), 2));
		return (Math.abs((numerator/denominator)) < 3);
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	void draw(Graphics g) {
		g.setColor(color);
		g.drawLine(node1.getX(), node1.getY(),node2.getX(), node2.getY());
	}
	
	@Override
	public String toString(){
		return ("(" + node1.toString() + ")" + "==>" + "(" + node2.toString() + ")");
	}
}
