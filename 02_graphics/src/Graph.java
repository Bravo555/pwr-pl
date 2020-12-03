import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Node> nodes;

    public Graph() {
        this.nodes = new ArrayList<Node>();
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public void removeNode(Node node){
        nodes.remove(node);
    }

    public void draw(Graphics g){
        for(Node node : nodes){
            node.draw(g);
        }
    }

}
