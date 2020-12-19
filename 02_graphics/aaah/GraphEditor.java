import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/* 
 *  Program: Prosty edytor grafu
 *     Plik: GraphEditor.java
 *           
 *  Klasa GraphEditor implementuje okno g³ówne
 *  dla prostego graficznego edytora grafu.  
 *            
 *    Autor: Mi³osz Skóra
 *     Data:  listopad 2020 r.
 */

public class GraphEditor extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	private static final String APP_AUTHOR = "Autor: Mi³osz Skóra\n  Data: listopad 2020";
	private static final String APP_TITLE = "Edytor grafów";
	
	private static final String APP_INSTRUCTION =
			"                  O P I S   P R O G R A M U \n\n" + 
	        "Aktywna klawisze:\n" +
			"   strza³ki ==> przesuwanie wszystkich kó³ i krawêdzi\n" +
			"   SHIFT + strza³ki ==> szybkie przesuwanie wszystkich kó³ i krawêdzi\n\n" +
			"ponadto gdy kursor wskazuje ko³o:\n" +
			"   DEL   ==> kasowanie ko³a\n" +
			"   +, -   ==> powiêkszanie, pomniejszanie ko³a\n" +
			"   r,g,b ==> zmiana koloru ko³a\n\n" +
			"Operacje myszka:\n" +
			"   przeci¹ganie ==> przesuwanie wszystkich kó³\n" +
			"   PPM ==> tworzenie nowego ko³a w niejscu kursora\n" +
	        "ponadto gdy kursor wskazuje ko³o:\n" +
	        "   przeci¹ganie ==> przesuwanie ko³a\n" +
			"   PPM ==> zmiana koloru ko³a\n" +
	        "                   lub usuwanie ko³a\n"+
	        "ponadto gdy kursor wskazuje krawêdŸ:\n" +
			"   PPM ==> zmiana koloru krawêdzi\n" +
	        "                   lub usuwanie krawêdzi\n";
	
	
	public static void main(String[] args) {
		new GraphEditor();
	}
	
	final JFileChooser fc = new JFileChooser();

	private JMenuBar menuBar = new JMenuBar();
	private JMenu menuFile = new JMenu("File");
	private JMenu menuGraph = new JMenu("Graph");
	private JMenu menuHelp = new JMenu("Help");
	private JMenuItem menuNew = new JMenuItem("New", KeyEvent.VK_N);
	private JMenuItem menuSave = new JMenuItem("Save", KeyEvent.VK_S);
	private JMenuItem menuOpen = new JMenuItem("Open", KeyEvent.VK_O);
	private JMenuItem menuShowExample = new JMenuItem("Example", KeyEvent.VK_X);
	private JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_E);
	private JMenuItem menuListOfNodes = new JMenuItem("List of Nodes", KeyEvent.VK_L);
	private JMenuItem menuListOfEdges = new JMenuItem("List of Edges", KeyEvent.VK_L);
	private JMenuItem menuAuthor = new JMenuItem("Author", KeyEvent.VK_A);
	private JMenuItem menuInstruction = new JMenuItem("Instruction", KeyEvent.VK_I);
	
	private GraphPanel panel = new GraphPanel();
	
	
	public GraphEditor() {
		super(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400,400);
		setLocationRelativeTo(null);
		setContentPane(panel);
		createMenu();
		showBuildinExample();
		setVisible(true);
	}

	private void showListOfNodes(Graph graph) {
		Node array[] = graph.getNodes();
		int i = 0;
		StringBuilder message = new StringBuilder("Liczba wêz³ów: " + array.length + "\n");
		for (Node node : array) {
			message.append(node + "    ");
			if (++i % 5 == 0)
				message.append("\n");
		}
		JOptionPane.showMessageDialog(this, message, APP_TITLE + " - Lista wêz³ów", JOptionPane.PLAIN_MESSAGE);
	}
	
	private void showListOfEdges(Graph graph) {
		Edge array[] = graph.getEdges();
		int i = 0;
		StringBuilder message = new StringBuilder("Liczba krawêdzi: " + array.length + "\n");
		for (Edge edge: array) {
			message.append(edge + "    ");
			if (++i % 5 == 0)
				message.append("\n");
		}
		JOptionPane.showMessageDialog(this, message, APP_TITLE + " - Lista krawêdzi", JOptionPane.PLAIN_MESSAGE);
	}

	private void createMenu() {
		menuNew.addActionListener(this);
		menuSave.addActionListener(this);
		menuOpen.addActionListener(this);
		menuShowExample.addActionListener(this);
		menuExit.addActionListener(this);
		menuListOfNodes.addActionListener(this);
		menuListOfEdges.addActionListener(this);
		menuAuthor.addActionListener(this);
		menuInstruction.addActionListener(this);
		
		menuFile.setMnemonic(KeyEvent.VK_F);
		menuFile.add(menuNew);
		menuFile.add(menuSave);
		menuFile.add(menuOpen);
		menuFile.addSeparator();
		menuFile.add(menuExit);
		
		menuGraph.setMnemonic(KeyEvent.VK_G);
		menuGraph.add(menuShowExample);
		menuGraph.addSeparator();
		menuGraph.add(menuListOfNodes);
		menuGraph.add(menuListOfEdges);
		
		menuHelp.setMnemonic(KeyEvent.VK_H);
		menuHelp.add(menuInstruction);
		menuHelp.add(menuAuthor);
		
		menuBar.add(menuFile);
		menuBar.add(menuGraph);
		menuBar.add(menuHelp);
		setJMenuBar(menuBar);
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		if (source == menuNew) {
			panel.setGraph(new Graph());
		}
		if(source == menuSave) {
			fc.setCurrentDirectory(new File("."));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showSaveDialog(this);
			if(returnVal != JFileChooser.APPROVE_OPTION) {
				return;
			}
			File file = fc.getSelectedFile();
			try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file))) {
				output.writeObject(panel.getGraph());
			} catch(IOException e) {
				JOptionPane.showMessageDialog(this,  e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}
		if(source == menuOpen) {
			fc.setCurrentDirectory(new File("."));
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int returnVal = fc.showOpenDialog(this);
		    if(returnVal != JFileChooser.APPROVE_OPTION) {
		    	return;
		    }
		    File file = fc.getSelectedFile();
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
				panel.setGraph((Graph) input.readObject());
			} catch(IOException e) {
				JOptionPane.showMessageDialog(this,  e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			} catch(ClassNotFoundException e) {
				JOptionPane.showMessageDialog(this,  e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
			}
		}
		if (source == menuShowExample) {
			showBuildinExample();
		}
		if (source == menuListOfNodes) {
			showListOfNodes(panel.getGraph());
		}
		if (source == menuListOfEdges) {
			showListOfEdges(panel.getGraph());
		}
		if (source == menuAuthor) {
			JOptionPane.showMessageDialog(this, APP_AUTHOR, APP_TITLE, JOptionPane.INFORMATION_MESSAGE);
		}
		if (source == menuInstruction) {
			JOptionPane.showMessageDialog(this, APP_INSTRUCTION, APP_TITLE, JOptionPane.PLAIN_MESSAGE);
		}
		if (source == menuExit) {
			System.exit(0);
		}
		repaint();
	}

	private void showBuildinExample() {
		Graph graph = new Graph();

		Node n1 = new Node(100, 100);
		Node n2 = new Node(100, 200);
		n2.setColor(Color.CYAN);
		Node n3 = new Node(200, 100);
		n3.setR(20);
		Node n4 = new Node(200, 250);
		n4.setColor(Color.GREEN);
		n4.setR(30);
		Edge e1 = new Edge(n1,n2);

		graph.addNode(n1);
		graph.addNode(n2);
		graph.addNode(n3);
		graph.addNode(n4);
		graph.addEdge(e1);
		panel.setGraph(graph);
	}
}
