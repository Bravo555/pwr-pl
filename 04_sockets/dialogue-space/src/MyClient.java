/*
 *  Komunikator sieciowy
 *   - program klienta
 *
 *  Autor: Pawel Rogalinski
 *   Data: 1 grudnia 2017 r.
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

// TODO: don't show 2nd dialog box if cancelled first one

class MyClient extends JFrame {
    public static void main(String[] args) {
        String name;
        String host;

        host = JOptionPane.showInputDialog("Podaj adres serwera");
        name = JOptionPane.showInputDialog("Podaj nazwe klienta");
        if (name != null && !name.equals("")) {
            new MyClient(name, host);
        }
    }

    private final JTextField messageField = new JTextField(20);
    private final JTextArea  textArea     = new JTextArea(15,18);

    static final int SERVER_PORT = 25000;
    private String serverHost;
    private Socket socket;
    private ServerSocket serverSocket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    MyClient(String name, String host) {
        super(name);
        this.serverHost = host;

        try {
            serverSocket = new ServerSocket(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        setSize(300, 310);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                try {
                    outputStream.close();
                    inputStream.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void windowClosed(WindowEvent event) {
                windowClosing(event);
            }
        });
        JPanel panel = new JPanel();
        JLabel messageLabel = new JLabel("Napisz:");
        JLabel textAreaLabel = new JLabel("Dialog:");
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        panel.add(messageLabel);
        panel.add(messageField);
        messageField.addActionListener(actionEvent -> {
            try {
                String message = messageField.getText();
                outputStream.writeObject(message);
                printMessage(name, message);
                if (message.equals("exit")) {
                    inputStream.close();
                    outputStream.close();
                    socket.close();
                    setVisible(false);
                    dispose();
                }
            } catch(IOException e) {
                System.out.println("Wyjatek klienta "+e);
            }
        });
        panel.add(textAreaLabel);
        JScrollPane scroll_bars = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scroll_bars);
        setContentPane(panel);
        setVisible(true);
        new Thread(() -> {
            if (serverHost.equals("")) {
                serverHost = "localhost";
            }
            try{
                socket = new Socket(serverHost, SERVER_PORT);
                inputStream = new ObjectInputStream(socket.getInputStream());
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(name);
                outputStream.writeObject(serverSocket.getLocalPort());

                List<Client> clients = (List<Client>)inputStream.readObject();
                if(!clients.isEmpty()) {
                    Client peer = clients.get(0);
                    Socket peerSocket = new Socket(peer.hostname, peer.serverPort);

                    var o = new ObjectOutputStream(peerSocket.getOutputStream());

                    o.writeObject(name);
                    o.writeObject("dzien dobry nazywam sie " + name);
                    printMessage("me", "wysłano powitanie");
                }
            } catch(IOException e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta nie moze byc utworzone");
                setVisible(false);
                dispose();
                return;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                while(true) {
                    String message = (String)inputStream.readObject();
                    printMessage("SERVER", message);
                    if(message.equals("exit")){
                        inputStream.close();
                        outputStream.close();
                        socket.close();
                        setVisible(false);
                        dispose();
                        break;
                    }
                }
            } catch(Exception e){
                JOptionPane.showMessageDialog(null, "Polaczenie sieciowe dla klienta zostalo przerwane");
                setVisible(false);
                dispose();
            }
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    Socket peer = serverSocket.accept();
                    var in = new ObjectInputStream(peer.getInputStream());
                    new Thread(() -> {
                        while (true) {
                            try {
                                System.out.println("ah shit");
                                String peerName = (String) in.readObject();
                                String msg = (String) in.readObject();
                                printMessage(peerName, msg);
                            } catch (IOException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    synchronized public void printMessage(String name, String message){
        ChatUtils.printMessage(name, message, textArea);
    }
}
