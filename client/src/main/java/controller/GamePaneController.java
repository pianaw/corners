package controller;

import context.GameContext;
import gui.Board;
import gui.GamePane;
import gui.MenuPane;
import gui.Window;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import player.message.Message;
import player.message.Transmitter;
import player.message.TypeMessage;

import java.util.Optional;

public class GamePaneController {

    private GamePane pane;
    public GamePaneController(GamePane pane) {
        this.pane = pane;
    }

    public void checkState(Optional<Message> message1) {
        if (message1.isPresent()) {
            Message message = message1.get();
            if (message.type.equals(TypeMessage.WAIT)) {
                setPassButtonDisabled(true);
            }
            else if (message.type.equals(TypeMessage.GO)) {
                this.setMessage("Ваш ход");
            }
            else if (message.type.equals(TypeMessage.GO2)) {
                this.setMessage("Ходите повторно, если есть доступные ходы");
                setPassButtonDisabled(false);
            }
            else if (message.type.equals(TypeMessage.FAIL)) {
                this.setMessage("К сожалению, вы проиграли :(");
                Board.controller.setBoardDisabled(true);
                GameContext.gameDown();

                addExitEventListener();
            }
            else if (message.type.equals(TypeMessage.WIN)) {
                this.setMessage("Поздравляем! Вы одержали победу :)");
                Board.controller.setBoardDisabled(true);
                GameContext.gameDown();

                addExitEventListener();
            }
            else if (message.type.equals(TypeMessage.START1) || message.type.equals(TypeMessage.START2)) {
                HBox box = (HBox) this.pane.getBottom();
                box.getChildren().get(1).setDisable(false);
            }
            else if (message.type.equals(TypeMessage.DEAD_HEAT)) {
                this.setMessage("Игра закончена ничьей. Победила дружба :)");
                Board.controller.setBoardDisabled(true);
                GameContext.gameDown();

                addExitEventListener();
            }

            Board.controller.checkState(message);
        }
    }

    public void addPassEventListener(Button button) {
        button.setOnMouseClicked(event -> {
            Message message = new Message();
            message.type = TypeMessage.PASS;
            GameContext.manager.setNewState(message);

        });
    }

    public void addFailEventListener(Button button) {
        button.setOnMouseClicked(event -> {
            Message message = new Message();
            message.type = TypeMessage.FAIL;
            GameContext.manager.setNewState(message);
        });
    }

    public void setMessage(String message) {
        FlowPane textPane =  (FlowPane)pane.getCenter();
        Text text = (Text) textPane.getChildren().get(0);
        text.setText(message);
    }

    public void addExitEventListener() {
        Platform.runLater(() -> {
            HBox box = (HBox) pane.getBottom();

            AnchorPane pane1 = (AnchorPane) box.getChildren().get(0);
            AnchorPane pane2 = (AnchorPane) box.getChildren().get(1);

            pane1.getChildren().get(0).setVisible(false);

            Button button = new Button("В главное меню");
            button.setStyle("-fx-background-color: darkmagenta; -fx-text-fill: white; -fx-border-color: black; -fx-border-radius: 2px");

            button.setOnMouseClicked(event -> {
                Window.controller.setSize(400.0, 400.0);
                Scene scene = new Scene(new MenuPane(), 400.0, 400.0);
                Window.controller.setScene(scene);
            });

            pane2.getChildren().set(0, button);
        });
    }

    private void setPassButtonDisabled(boolean value) {
        HBox box = (HBox) this.pane.getBottom();
        AnchorPane anchorPane = (AnchorPane) box.getChildren().get(0);
        anchorPane.getChildren().get(0).setDisable(value);

    }

}
