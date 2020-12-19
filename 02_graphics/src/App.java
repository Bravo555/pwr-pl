import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.List;

public class App extends JFrame {
    static String TITLE = "Graph App";
    static String AUTHOR = "Marcel Guzik";
    static String INSTRUCTION = "stuff";

    final private GraphPanel panel = new GraphPanel();

    public static void main(String[] args) {
        App app = new App();
        app.showBuiltinExample();
    }

    public App() {
        super(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600,600);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("File");
        JMenu menuGraph = new JMenu("Graph");
        JMenu menuHelp = new JMenu("Help");
        JMenuItem menuNew = new JMenuItem("New", KeyEvent.VK_N);
        JMenuItem menuSave = new JMenuItem("Save", KeyEvent.VK_S);
        JMenuItem menuOpen = new JMenuItem("Open", KeyEvent.VK_O);
        JMenuItem menuShowExample = new JMenuItem("Example", KeyEvent.VK_X);
        JMenuItem menuExit = new JMenuItem("Exit", KeyEvent.VK_E);
        JMenuItem menuListOfNodes = new JMenuItem("List of Nodes", KeyEvent.VK_L);
        JMenuItem menuListOfEdges = new JMenuItem("List of Edges", KeyEvent.VK_L);
        JMenuItem menuAuthor = new JMenuItem("Author", KeyEvent.VK_A);
        JMenuItem menuInstruction = new JMenuItem("Instruction", KeyEvent.VK_I);

        menuNew.addActionListener(actionEvent -> panel.setGraph(new Graph()));
        menuSave.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
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
                JOptionPane.showMessageDialog(this,  e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
            }
            panel.repaint();
        });
        menuOpen.addActionListener(actionEvent -> {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(new File("."));
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = fc.getSelectedFile();
            try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file))) {
                panel.setGraph((Graph) input.readObject());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
            } catch (ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "B��d", JOptionPane.ERROR_MESSAGE);
            }
            panel.repaint();
        });
        menuShowExample.addActionListener(actionEvent -> showBuiltinExample());
        menuExit.addActionListener(actionEvent -> System.exit(0));
        menuListOfNodes.addActionListener(actionEvent -> showListOfNodes(panel.getGraph()));
        menuListOfEdges.addActionListener(actionEvent -> showListOfEdges(panel.getGraph()));
        menuAuthor.addActionListener(actionEvent -> JOptionPane.showMessageDialog(this, AUTHOR, TITLE, JOptionPane.INFORMATION_MESSAGE));
        menuInstruction.addActionListener(actionEvent -> JOptionPane.showMessageDialog(this, INSTRUCTION, TITLE, JOptionPane.PLAIN_MESSAGE));

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

        setContentPane(panel);
        setVisible(true);
    }

    private void showBuiltinExample() {
        Graph graph = new Graph();

        Node n1 = new Node(100, 100);
        Node n2 = new Node(100, 200);
        n2.setColor(Color.CYAN);
        Node n3 = new Node(200, 100);
        n3.setR(20);
        Node n4 = new Node(200, 250);
        n4.setColor(Color.GREEN);
        n4.setR(30);

        graph.addNode(n1);
        graph.addNode(n2);
        graph.addNode(n3);
        graph.addNode(n4);

        graph.addEdge(new Edge(n1, n2));

        panel.setGraph(graph);
    }

    private void showListOfNodes(Graph graph) {
        List<Node> nodes = graph.getNodes();
        int i = 0;
        StringBuilder message = new StringBuilder("Nodes: " + nodes.size() + "\n");
        for (Node node: nodes) {
            message.append(node + "    ");
            if (++i % 5 == 0)
                message.append("\n");
        }
        JOptionPane.showMessageDialog(this, message, TITLE + "Nodes", JOptionPane.PLAIN_MESSAGE);
    }

    private void showListOfEdges(Graph graph) {
        List<Edge> edges = graph.getEdges();
        int i = 0;
        StringBuilder message = new StringBuilder("Edges: " + edges.size() + "\n");
        for (Edge edge: edges) {
            message.append(edge + "    ");
            if (++i % 5 == 0)
                message.append("\n");
        }
        JOptionPane.showMessageDialog(this, message, TITLE + "Edges", JOptionPane.PLAIN_MESSAGE);
    }
}
