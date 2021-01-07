import javax.swing.*;

public class ChatUtils {
    synchronized static public void printMessage(String name, String message, JTextArea textArea) {
        textArea.append(String.format("[%s]: %s\n", name, message));
    }
}
