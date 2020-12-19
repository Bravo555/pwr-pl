import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/* 
 *  Program: Prosty edytor grafu
 *     Plik: Graph.java
 *           
 *  Klasa Graph reprezentuje graf na p³aszczyŸnie. 
 *            
 *    Autor: Mi³osz Skóra
 *     Data:  listopad 2020 r.
 */

public class Graph implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private List<Node> nodes;
	
	private List<Edge> edges;
	
	public Graph() {
		this.nodes = new ArrayList<Node>();	
		this.edges = new ArrayList<Edge>();
	}
	
	public void addNode(Node node){
		nodes.add(node);
	}
	
	public void removeNode(Node node){
		nodes.remove(node);
	}
	
	public Node[] getNodes(){
		Node [] array = new Node[0];
		return nodes.toArray(array);
	}
	
	public void addEdge(Edge edge){
		edges.add(edge);
	}
	
	public void removeEdge(Edge edge){
		edges.remove(edge);
	}
	
	public Edge[] getEdges(){
		Edge [] array = new Edge[0];
		return edges.toArray(array);
	}
	
	public void draw(Graphics g){
		for(Edge edge: edges) {
			edge.draw(g);
		}
		for(Node node: nodes) {
			node.draw(g);
		}
	}

}
