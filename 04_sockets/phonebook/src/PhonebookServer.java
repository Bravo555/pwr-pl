import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

class PhonebookServer {
    static Phonebook phonebook = new Phonebook();
    static final int PORT = 2137;
    static boolean serverRunning = true;

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.printf("Listening on port %d...\n", PORT);

            while(serverRunning) {
                Socket socket = serverSocket.accept();
                System.out.println("New connection");

                ObjectOutputStream tx = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream rx = new ObjectInputStream(socket.getInputStream());

                new Thread(() -> {
                    boolean connected = true;
                    while(connected) {
                        try {
                            PhonebookCommand command = (PhonebookCommand) rx.readObject();
                            PhonebookOpResult result;

                            if (command instanceof PhonebookCommand.Close) {
                                serverRunning = false;
                                serverSocket.close();
                                System.out.println("New connections are no longer accepted");
                                result = new PhonebookOpResult.Ok();
                            } else if (command instanceof PhonebookCommand.Bye) {
                                connected = false;
                                result = new PhonebookOpResult.Ok();
                            } else {
                                result = phonebook.applyCommand(command);
                            }
                            tx.writeObject(result);
                        } catch (SocketException e) {
                            // do nothing. only appears when we close the server socket while accepting new connections
                            // idk if there is any way to handle it without raising this exception
                        } catch (EOFException e) {
                            System.out.println("Client disconnected prematurely");
                            connected = false;
                        } catch (IOException | ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Connection closed");
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}