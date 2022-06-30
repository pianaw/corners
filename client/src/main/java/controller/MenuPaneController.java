package controller;

import context.GameContext;
import gui.GamePane;
import gui.MenuPane;
import gui.Window;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import player.message.Message;
import player.message.TypeMessage;

import java.util.Optional;

public class MenuPaneController {

    public MenuPane pane;
    public MenuPaneController(MenuPane menu) {
        pane = menu;

        FlowPane flowPane = (FlowPane) pane.getChildren().get(0);
        VBox box = (VBox) flowPane.getChildren().get(0);

        AnchorPane anchorPane1 = (AnchorPane) box.getChildren().get(0);

        AnchorPane anchorPane2 = (AnchorPane) box.getChildren().get(1);

        addPlayOnlineListener((Button) anchorPane1.getChildren().get(0));
        addPlayOfflineListener((Button)anchorPane2.getChildren().get(0));
    }

    public void addPlayOnlineListener(Button button) {
        button.setOnMouseClicked(event -> {
            Scene scene = new Scene(new GamePane(), 800, 600);
            Window.controller.setSize(800.0, 600.0);
            Window.controller.setScene(scene);

            GameContext.isOnlineGame = true;
            GameContext.gameUp();
        });
    }

    public void addPlayOfflineListener(Button button) {
        button.setDisable(false);
        button.setOnMouseClicked(event -> {
            Scene scene = new Scene(new GamePane(), 800, 600);
            Window.controller.setSize(800.0, 600.0);
            Window.controller.setScene(scene);

            Message message = new Message();
            message.type = TypeMessage.GO;
            GamePane.controller.checkState(Optional.of(message));

            GameContext.gameUp();
        });
    }
}
