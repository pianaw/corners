import message.Message;
import message.TypeMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpServer extends Thread {
    private static final int DEFAULT_PORT = 1028;
    private ServerSocket serverSocket;

    public TcpServer() {
        try {
            this.serverSocket = new ServerSocket(DEFAULT_PORT);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = new Message();
                message.type = TypeMessage.WAIT;

                Socket socket1 = serverSocket.accept();
                ClientHandler client1 = subscribe(socket1);
                client1.sendMessage(message);
                GameSession gameSession = new GameSession(subscribe(socket1));

                Socket socket2 = serverSocket.accept();
                ClientHandler client2 = subscribe(socket1);
                client2.sendMessage(message);
                gameSession.addPlayer(subscribe(socket2));
                gameSession.start();
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public ClientHandler subscribe(Socket socket) {
        return new ClientHandler(this, socket);
    }
}

