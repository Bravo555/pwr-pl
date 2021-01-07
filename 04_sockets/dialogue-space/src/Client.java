import java.io.Serializable;

public class Client implements Serializable {
    public String hostname;
    public int serverPort;

    public Client(String hostname, int serverPort) {
        this.hostname = hostname;
        this.serverPort = serverPort;
    }

    @Override
    public String toString() {
        return String.format("{ hostname: %s, port: %d }", hostname, serverPort);
    }
}
