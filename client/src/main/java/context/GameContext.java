package context;

import javafx.scene.paint.Color;
import manager.Manager;
import ai.AIPlayer;
import player.TcpClient;

public class GameContext {

    public static TcpClient player;
    public static Manager manager;
    public static boolean isOnlineGame = false;

    public static void gameUp() {
        if (isOnlineGame) {
            player = new TcpClient();
            player.start();
            manager = new Manager();
        }
        else {
            manager = new Manager();
            manager.ai = new AIPlayer(Color.BLACK);
        }
    }

    public static void gameDown() {
        if (isOnlineGame) {
            player.interrupt();
            isOnlineGame = false;
        } else {
            manager.ai = null;
        }
    }
}
