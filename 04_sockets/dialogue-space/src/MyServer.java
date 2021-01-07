/*
 *  Komunikator sieciowy
 *   - program serwera
 *
 *  Autor: Pawel Rogalinski
 *   Data: 1 grudnia 2017 r.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.util.Arrays;
import java.util.stream.Collectors;


class MyServer extends JFrame {
    static final int SERVER_PORT = 25000;
    private final HashSet<ClientThread> clients = new HashSet<>();
    private final JTextField messageField = new JTextField(20);
    private final JTextArea  textArea  = new JTextArea(15,18);

    public static void main(String [] args){
        new MyServer();
    }

    MyServer(){
//        super("Przestrzeń Dialogu Szymona Hołowni");
//        setSize(300,340);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JPanel panel = new JPanel();
//        JLabel clientLabel = new JLabel("Odbiorca:");
//        panel.add(clientLabel);
////        clientMenu.setPrototypeDisplayValue(new ClientThread());
////        panel.add(clientMenu);
//        JLabel messageLabel = new JLabel("Napisz:");
//        panel.add(messageLabel);
//        panel.add(messageField);
//        messageField.addActionListener(actionEvent -> {
//            ClientThread client = (ClientThread)clientMenu.getSelectedItem();
//            if (client != null) {
//                String message = messageField.getText();
//                printMessage(client.getName(), message);
//                client.sendMessage(message);
//            }
//        });
//        textArea.setLineWrap(true);
//        textArea.setWrapStyleWord(true);
//        JLabel textAreaLabel = new JLabel("Przestrzeń Dialogu:");
//        panel.add(textAreaLabel);
//        textArea.setEditable(false);
//        JScrollPane scroll = new JScrollPane(textArea,
//                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
//                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
//        panel.add(scroll);
//        setContentPane(panel);
//        setVisible(true);
        new Thread(() -> {
            boolean socket_created = false;

            try (ServerSocket serwer = new ServerSocket(SERVER_PORT)) {
                String host = InetAddress.getLocalHost().getHostName();
                System.out.println("Serwer zosta� uruchomiony na hoscie " + host);
                socket_created = true;

                while (true) {
                    Socket socket = serwer.accept();
                    if (socket != null) {

                        new ClientThread(this, socket);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                if (!socket_created) {
                    JOptionPane.showMessageDialog(null, "Gniazdko dla serwera nie mo�e by� utworzone");
                    System.exit(0);
                } else {
                    JOptionPane.showMessageDialog(null, "BLAD SERWERA: Nie mozna polaczyc sie z klientem ");
                }
            }
        }).start();
    }

    synchronized public void printMessage(String name, String message){
        ChatUtils.printMessage(name, message, textArea);
    }

    synchronized public HashSet<ClientThread> getClients() {
        return clients;
    }

    synchronized void addClient(ClientThread client){
        clients.add(client);
    }

    synchronized void removeClient(ClientThread client){
        clients.remove(client);
    }
}



class ClientThread implements Runnable {
    private Socket socket;
    private String name;
    private MyServer myServer;
    private ObjectOutputStream outputStream;
    private int serverPort;

    // UWAGA: Ten konstruktor tworzy nieaktywny obiekt ClientThread,
    // kt�ry posiada tylko nazw� prototypow�, potrzebn� dla
    // metody setPrototypeDisplayValue z klasy JComboBox
    ClientThread(){
        name = "#########################";
    }

    ClientThread(MyServer server, Socket socket) {
        myServer = server;
        this.socket = socket;
        new Thread(this).start();
    }

    public String getName(){ return name; }

    public String toString(){ return name; }

    public void sendMessage(String message){
        try {
            outputStream.writeObject(message);
            if (message.equals("exit")){
                myServer.removeClient(this);
                socket.close();
                socket = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {

        String message;
        try( ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream input = new ObjectInputStream(socket.getInputStream()))
        {
            outputStream = output;
            name = (String)input.readObject();
            serverPort = (Integer)input.readObject();
            System.out.println("polaczenie od: " + socket.getRemoteSocketAddress() + " imie: " + name + ", port = " + serverPort);
            myServer.addClient(this);

            var clients =
                myServer.getClients().stream()
                    .map(clientThread -> new Client(clientThread.socket.getInetAddress().getHostName(), clientThread.serverPort))
                    .filter(client -> client.serverPort != this.serverPort)
                    .collect(Collectors.toList());
            output.writeObject(clients);
            System.out.println("wysylam innych klientow: " + clients);

            while(true){
                message = (String)input.readObject();
                myServer.printMessage(this.name, message);
                if (message.equals("exit")){
                    myServer.removeClient(this);
                    break;
                }
            }
            socket.close();
            socket = null;
        } catch(Exception e) {
            myServer.removeClient(this);
        }
    }
}