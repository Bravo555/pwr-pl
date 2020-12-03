import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphPanel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    protected Graph graph;

    private int mouseX = 0;
    private int mouseY = 0;

    private boolean mouseButtonLeft = false;
    private boolean mouseButtonRight = false;

    protected Node nodeUnderCursor;
    protected int mouseCursor = Cursor.DEFAULT_CURSOR;

    public GraphPanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setFocusable(true);
        requestFocus();
    }

    void setGraph(Graph graph) {
        this.graph = graph;
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
        if (event.getButton()==3) mouseButtonRight = true;
        setMouseCursor(event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        if (event.getButton() == 1)
            mouseButtonLeft = false;
        if (event.getButton() == 3)
            mouseButtonRight = false;
        setMouseCursor(event);
        if (event.getButton() == 3) {
            if (nodeUnderCursor != null) {
                createPopupMenu(event, nodeUnderCursor);
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

    protected void setMouseCursor(MouseEvent event) {
        nodeUnderCursor = findNode(event);
        if (nodeUnderCursor != null) {
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
        if (nodeUnderCursor != null) {
            mouseCursor = Cursor.HAND_CURSOR;
        } else if (mouseButtonLeft) {
            mouseCursor = Cursor.MOVE_CURSOR;
        } else {
            mouseCursor = Cursor.DEFAULT_CURSOR;
        }
        setCursor(Cursor.getPredefinedCursor(mouseCursor));
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

        // Implementacja s�uchacza zdarze� za pomoc� wyra�enia Lambda
        menuItem.addActionListener((action) -> {
            graph.removeNode(node);
            repaint();
        });

        popup.add(menuItem);
        popup.show(event.getComponent(), event.getX(), event.getY());
    }


    private Node findNode(MouseEvent event){
        return findNode(event.getX(), event.getY());
    }

    private Node findNode(int mx, int my){
        return graph.getNodes().stream()
                .filter(node -> node.isMouseOver(mx, my))
                .findFirst()
                .orElse(null);
    }

    private void moveNode(int dx, int dy, Node node){
        node.setX(node.getX()+dx);
        node.setY(node.getY()+dy);
    }

    // TODO: fix with matrix transformations
    private void moveAllNodes(int dx, int dy) {
        for (Node node : graph.getNodes()) {
            moveNode(dx, dy, node);
        }
    }
}
