import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/* 
 *  Program: Prosty edytor grafu
 *     Plik: GraphPanel.java
 *           
 *  Klasa GraphPanel implementuje podstawowe funkcjonalnoœci
 *  graficznego edytora grafu nieskierowanego.
 *  Klasa umo¿liwia:
 *     - rysowanie grafu w oknie,
 *     - obs³ugê zdarzeñ generowanych przez myszkê,
 *     - tworzenie i obs³ugê menu kontekstowych
 *       umo¿liwiaj¹cych wykonywanie operacji edycyjnych.  
 *            
 *    Autor: Mi³osz Skóra
 *     Data:  listopad 2020 r.
 */

public class GraphPanel extends JPanel
		implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = 1L;

	protected Graph graph;
	

	private int mouseX = 0;
	private int mouseY = 0;
	private boolean mouseButtonLeft = false;
	@SuppressWarnings("unused")
	private boolean mouseButtonRigth = false;
	protected int mouseCursor = Cursor.DEFAULT_CURSOR;
	
	protected Node nodeUnderCursor = null;
	
	protected Edge edgeUnderCursor = null;
	
	public GraphPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
	    setFocusable(true);
	    requestFocus();
	}

	public Graph getGraph() {
		return graph;
	}
	
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	
	private Node findNode(int mx, int my){
		for(Node node: graph.getNodes()){
			if (node.isMouseOver(mx, my)){
				return node;
			}
		}
		return null;
	}
	
	private Node findNode(MouseEvent event){
		return findNode(event.getX(), event.getY());
	}
	
	private Edge findEdge(int mx, int my){
		for(Edge edge: graph.getEdges()){
			if (edge.isMouseOver(mx, my)){
				return edge;
			}
		}
		return null;
	}
	
	private Edge findEdge(MouseEvent event){
		return findEdge(event.getX(), event.getY());
	}
	
	protected void setMouseCursor(MouseEvent event) {
		nodeUnderCursor = findNode(event);
		edgeUnderCursor = findEdge(event);
		if (nodeUnderCursor != null || edgeUnderCursor != null) {
			mouseCursor = Cursor.HAND_CURSOR;
		} else if (mouseButtonLeft) {
			mouseCursor = Cursor.MOVE_CURSOR;
		} else {
			mouseCursor = Cursor.DEFAULT_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(mouseCursor));
		mouseX = event.getX();
		mouseY = event.getY();
	}
	
	protected void setMouseCursor() {
		nodeUnderCursor = findNode(mouseX, mouseY);
		edgeUnderCursor = findEdge(mouseX, mouseY);
		if (nodeUnderCursor != null || edgeUnderCursor != null) {
			mouseCursor = Cursor.HAND_CURSOR;
		} else if (mouseButtonLeft) {
			mouseCursor = Cursor.MOVE_CURSOR;
		} else {
			mouseCursor = Cursor.DEFAULT_CURSOR;
		}
		setCursor(Cursor.getPredefinedCursor(mouseCursor));
	}
	
	private void moveNode(int dx, int dy, Node node){
		node.setX(node.getX()+dx);
		node.setY(node.getY()+dy);
	}
	
	private void moveAllNodes(int dx, int dy) {
		for (Node node : graph.getNodes()) {
			moveNode(dx, dy, node);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (graph==null) return;
		graph.draw(g);
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
	}

	@Override
	public void mouseEntered(MouseEvent event) {
	}

	@Override
	public void mouseExited(MouseEvent event) {
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if (event.getButton()==1) mouseButtonLeft = true;
		if (event.getButton()==3) mouseButtonRigth = true;
		setMouseCursor(event);
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if (event.getButton() == 1)
			mouseButtonLeft = false;
		if (event.getButton() == 3)
			mouseButtonRigth = false;
		setMouseCursor(event);
		if (event.getButton() == 3) {
			if (nodeUnderCursor != null) {
				createPopupMenu(event, nodeUnderCursor);
			} else if(edgeUnderCursor != null) {
				createPopupMenu(event, edgeUnderCursor);
			} else {
				createPopupMenu(event);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent event) {
		if (mouseButtonLeft) {
			if (nodeUnderCursor != null) {
				moveNode(event.getX() - mouseX, event.getY() - mouseY, nodeUnderCursor);
			} else {
				moveAllNodes(event.getX() - mouseX, event.getY() - mouseY);
			}
		}
		mouseX = event.getX();
		mouseY = event.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		setMouseCursor(event);
	}

	@Override
	public void keyPressed(KeyEvent event) {
		{  int dist;
	       if (event.isShiftDown()) dist = 10;
	                         else dist = 1;
			switch (event.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				moveAllNodes(-dist, 0);
				break;
			case KeyEvent.VK_RIGHT:
				moveAllNodes(dist, 0);
				break;
			case KeyEvent.VK_UP:
				moveAllNodes(0, -dist);
				break;
			case KeyEvent.VK_DOWN:
				moveAllNodes(0, dist);
				break;
			case KeyEvent.VK_DELETE:
				if (nodeUnderCursor != null) {
					graph.removeNode(nodeUnderCursor);
					nodeUnderCursor = null;
				}
				break;
			}
		}
		repaint();
		setMouseCursor();
	}

	@Override
	public void keyReleased(KeyEvent event) {
	}

	@Override
	public void keyTyped(KeyEvent event) {
		char znak=event.getKeyChar(); 
		if (nodeUnderCursor!=null){
		switch (znak) {
		case 'r':
			nodeUnderCursor.setColor(Color.RED);
			break;
		case 'g':
			nodeUnderCursor.setColor(Color.GREEN);
			break;
		case 'b':
			nodeUnderCursor.setColor(Color.BLUE);
			break;
		case '+':
			int r = nodeUnderCursor.getR()+10;
			nodeUnderCursor.setR(r);
			break;
		case '-':
			r = nodeUnderCursor.getR()-10;
			if (r>=10) nodeUnderCursor.setR(r);
			break;
		}
		repaint();
		setMouseCursor();
		}
	}	

	protected void createPopupMenu(MouseEvent event) {
        JMenuItem menuItem;
 
        JPopupMenu popup = new JPopupMenu();
        menuItem = new JMenuItem("Create new node");
          
		menuItem.addActionListener((action) -> {
			graph.addNode(new Node(event.getX(), event.getY()));
			repaint();
		});

		popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }
	
	protected void createPopupMenu(MouseEvent event, Node node) {
		JMenuItem menuItem;

		JPopupMenu popup = new JPopupMenu();
		menuItem = new JMenuItem("Change node color");
		
		menuItem.addActionListener((a) -> {
			Color newColor = JColorChooser.showDialog(
                    this,
                    "Choose Background Color",
                    node.getColor());
			if (newColor!=null){
				node.setColor(newColor);
			}
			repaint();
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Remove this node");
		 
		menuItem.addActionListener((action) -> {
			graph.removeNode(node);
			repaint();
		});
		popup.add(menuItem);
		popup.show(event.getComponent(), event.getX(), event.getY());
	}
	
	protected void createPopupMenu(MouseEvent event, Edge edge) {
		JMenuItem menuItem;

		JPopupMenu popup = new JPopupMenu();
		menuItem = new JMenuItem("Change edge color");
		
		menuItem.addActionListener((a) -> {
			Color newColor = JColorChooser.showDialog(
                    this,
                    "Choose Background Color",
                    edge.getColor());
			if (newColor!=null){
				edge.setColor(newColor);
			}
			repaint();
		});
		popup.add(menuItem);
		menuItem = new JMenuItem("Remove this edge");
		 
		menuItem.addActionListener((action) -> {
			graph.removeEdge(edge);
			repaint();
		});
		popup.add(menuItem);
		popup.show(event.getComponent(), event.getX(), event.getY());
	}

}
