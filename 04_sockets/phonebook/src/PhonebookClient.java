import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class PhonebookClient {
    static final String HOST = "localhost";
    static final int PORT = 2137;

    public static void main(String[] args) {
        try {
            var socket = new Socket(HOST, PORT);

            var rx = new ObjectInputStream(socket.getInputStream());
            var tx = new ObjectOutputStream(socket.getOutputStream());

            var s = new Scanner(System.in);

            boolean connected = true;

            while(connected) {
                System.out.print("> ");
                String input = s.nextLine();
                try {
                    var command = PhonebookCommand.parse(input);
                    tx.writeObject(command);
                    var response = (PhonebookOpResult) rx.readObject();
                    System.out.println(response.toString());

                    if (command instanceof PhonebookCommand.Bye && response instanceof PhonebookOpResult.Ok) {
                        connected = false;
                    }
                } catch (EOFException | SocketException e) {
                    System.out.println("Server has disconnected.");
                    return;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            socket.close();
        } catch (ConnectException e) {
            System.out.println("Can't connect: server refused connection");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
