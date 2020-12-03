import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    static String TITLE = "Vicky 2 economy sim";

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
        panel.setGraph(graph);
    }
}
