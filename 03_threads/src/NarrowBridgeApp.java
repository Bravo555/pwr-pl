import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class NarrowBridgeApp extends JFrame {

    private static final String APP_TITLE = "Narrow Bridge App";

    private final JTextArea messageLog = new JTextArea();
    JTextField queueTextField = new JTextField(20);
    JTextField onBridgeTextField = new JTextField(20);
    JComboBox<String> trafficType = new JComboBox<>();
    JSlider intensitySlider = new JSlider(0, 5000, 2000);

    NarrowBridge bridge = new NarrowBridge();


    public static void main(String[] args) {
        new NarrowBridgeApp();
    }

    NarrowBridgeApp() {
        super(APP_TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1000, 600);

        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));

        DefaultCaret caret = (DefaultCaret)messageLog.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        JPanel status = new JPanel();
        status.setLayout(new BoxLayout(status, BoxLayout.Y_AXIS));
//        status.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel trafficPanel = new JPanel();
//        trafficPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        trafficPanel.add(new JLabel("Ograniczenie ruchu"));
        trafficType.addItem("Ruch ograniczony (max 1 bus)");
        trafficType.addItem("Bez ograniczeń");
        trafficType.addItem("Ruch dwukierunkowy (max 3 busy)");
        trafficType.addItem("Ruch jednokierunkowy (max 3 busy)");
        trafficPanel.add(trafficType);
        bridge.setTrafficTypeComboBox(trafficType);
        status.add(trafficPanel);

        JPanel intensityPanel = new JPanel();
//        intensityPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        intensityPanel.add(new JLabel("Natężenie ruchu"));
        intensityPanel.add(intensitySlider);
        status.add(intensityPanel);

        JPanel dirPanel = new JPanel();
//        dirPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        dirPanel.add(new JLabel("Kierunek ruchu"));
        dirPanel.add(new JSlider());
        status.add(dirPanel);

        JPanel onBridge = new JPanel();
//        onBridge.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        onBridge.add(new JLabel("Na moście"));
        onBridgeTextField.setEditable(false);
        onBridge.add(onBridgeTextField);
        status.add(onBridge);

        JPanel queue = new JPanel();
//        queue.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        queue.add(new JLabel("Kolejka"));
        queueTextField.setEditable(false);
        queue.add(queueTextField);
        status.add(queue);


        JScrollPane msgWrapper = new JScrollPane(messageLog);
        msgWrapper.setPreferredSize(new Dimension(0, 600));
        status.add(msgWrapper);

        root.add(status);
        JPanel animation = new NarrowBridgeAnimationPanel(bridge.getBuses());
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                animation.repaint();
            }
        });
        animation.setPreferredSize(new Dimension(400, Integer.MAX_VALUE));
        root.add(animation);

        setContentPane(root);


        bridge.setMessageLogArea(messageLog);
        bridge.setOnBridgeTextField(onBridgeTextField);
        bridge.setQueueTextField(queueTextField);

        setVisible(true);

        // Zadaniem tej pętli jest tworzenie kolejnych busów,
        // które mają przewozić przez most pasażerów:
        // Przerwy pomiędzy kolejnymi busami są generowane losowo.
        while (true) {
            Bus bus = new Bus(bridge);
            new Thread(bus).start();

            // Przerwa przed utworzeniem kolejnego busa
            try {
                // Parametr TRAFFIC określa natężenie ruchu busów.
                // Może przyjmować wartości z przedziału [0, 5000]
                //    0 - bardzo małe natżenie (nowy bus co 5500 ms)
                // 5000 - bardzo duże natżenie (nowy bus co 500 ms )
                int traffic = intensitySlider.getValue();
                Thread.sleep(5500 - traffic);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

enum BusDirection {
    EAST,
    WEST;

    @Override
    public String toString() {
        return switch (this) {
            case EAST -> "W";
            case WEST -> "Z";
        };
    }
}

class NarrowBridge {
    List<Bus> allBuses = new LinkedList<>();
    List<Bus> busesWaiting = new LinkedList<>();
    List<Bus> busesOnTheBridge = new LinkedList<>();


    public JTextArea messageLog;
    private JTextField onBridgeTextField;
    private JTextField queueTextField;
    JComboBox<String> trafficType;
    BusDirection curDir;
    int nobuses;

    int counter = 0;

    public List<Bus> getBuses() {
        return allBuses;
    }

    public void setMessageLogArea(JTextArea area) {
        messageLog = area;
    }

    public void setOnBridgeTextField(JTextField field) {
        onBridgeTextField = field;
    }

    public void setQueueTextField(JTextField field) {
        queueTextField = field;
    }

    public void setTrafficTypeComboBox(JComboBox<String> trafficType) {
        this.trafficType = trafficType;
    }

    void printBridgeInfo(Bus bus, String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("Bus[").append(bus.id).append("->").append(bus.dir).append("]  ");
        sb.append(message).append("\n");
        messageLog.append(sb.toString());
    }

    synchronized void getOnTheBridge(Bus bus){
        synchronized(bus) {
//            bus.time = System.currentTimeMillis();
//            bus.state = BusState.GET_ON_BRIDGE;
        }

        boolean print_Message = true;

        while(true) {
            String selOpt = (String)trafficType.getSelectedItem();

                if(selOpt.equals("Ruch dwukierunkowy (max 3 busy)")) {
                    if (this.busesOnTheBridge.size() < 3) {
                        break;
                    }
                }
                else if(selOpt.equals("Ruch jednokierunkowy (max 3 busy)")) {
                    if (this.busesOnTheBridge.isEmpty() && this.busesWaiting.isEmpty()) {
                        this.nobuses = 0;
                        break;
                    }

                    if (this.busesOnTheBridge.isEmpty()) {
                        if (bus.dir != this.curDir || bus.dir == this.curDir && this.nobuses < 10) {
                            break;
                        }
                    } else if (bus.dir == this.curDir && this.nobuses < 10 && this.busesOnTheBridge.size() < 3) {
                        break;
                    }
                }
                else if(selOpt.equals("Ruch ograniczony (max 1 bus)")) {
                    if (this.busesOnTheBridge.isEmpty()) {
                        break;
                    }
                }
                else {
                    break;
                }

            this.busesWaiting.add(bus);
            if (print_Message) {
                this.printBridgeInfo(bus, "CZEKA NA WJAZD");
                queueTextField.setText(busesWaiting.stream().map(b -> (b.id + " ")).collect(Collectors.joining()));
                print_Message = false;
            }

            try {
                this.wait();
            } catch (InterruptedException var4) {
            }

            this.busesWaiting.remove(bus);
        }

        if (this.curDir == bus.dir) {
            ++this.nobuses;
        } else {
            this.curDir = bus.dir;
            this.nobuses = 1;
        }

        this.busesOnTheBridge.add(bus);
        onBridgeTextField.setText(busesOnTheBridge.stream().map(b -> (b.id + " ")).collect(Collectors.joining()));
        this.printBridgeInfo(bus, "WJEŻDŻA NA MOST");
    }

    synchronized void getOffTheBridge(Bus bus) {
        busesOnTheBridge.remove(bus);
        printBridgeInfo(bus, "OPUSZCZA MOST");
        onBridgeTextField.setText(busesOnTheBridge.stream().map(b -> (b.id + " ")).collect(Collectors.joining()));
        notify();
    }

}

