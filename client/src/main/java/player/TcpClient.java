package player;

import gui.GamePane;
import player.message.Transmitter;

import java.io.IOException;
import java.net.Socket;

public class TcpClient extends Thread {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1028;
    public static Socket socket;

    public TcpClient() {
        try {
            socket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
        } catch (IOException e) {
            GamePane.controller.setMessage("Не удалось подключиться к серверу");
            GamePane.controller.addExitEventListener();
        }
    }

    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException ignored) {
        }
        while (true) {
            GamePane.controller.checkState(Transmitter.read());
        }
    }

}
