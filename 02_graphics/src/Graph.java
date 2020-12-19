import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Graph implements Serializable {
    private final List<Node> nodes;
    private final List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public void addNode(Node node){
        nodes.add(node);
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public void removeNode(Node node){
        edges.removeAll(edges.stream()
            .filter(edge -> edge.getN1() == node || edge.getN2() == node)
            .collect(Collectors.toList()));
        nodes.remove(node);
    }
    public void addEdge(Edge edge){
        edges.add(edge);
    }

    public List<Edge> getEdges(){
        return edges;
    }

    public void removeEdge(Edge edge){
        edges.remove(edge);
    }

    public void draw(Graphics g) {
        for(Edge edge: edges){
            edge.draw(g);
        }
        for(Node node: nodes) {
            node.draw(g);
        }
    }

}
