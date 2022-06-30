package gui;

import controller.GamePaneController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GamePane extends BorderPane {

    public static GamePaneController controller;

    public GamePane() {
        this.setStyle("-fx-background-color: linear-gradient(#000000, #ffffff)");

        Text message = new Text("Настройка игры");
        message.setFill(Color.WHITE);

        FlowPane board = new FlowPane(message);
        board.setMaxWidth(400);
        board.getChildren().add(new Board());
        board.setAlignment(Pos.CENTER);

        HBox box = new HBox();
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-padding: 10px");

        Button button1 = new Button("Завершить ход");
        button1.setDisable(true);
        AnchorPane button1Pane = new AnchorPane(button1);
        button1Pane.setStyle("-fx-padding: 5px");
        button1.setStyle("-fx-background-color: darkmagenta; -fx-text-fill: white; -fx-border-color: black; -fx-border-radius: 2px");

        Button button2 = new Button("Cдаться");
        button2.setDisable(true);
        AnchorPane button2Pane = new AnchorPane(button2);
        button2Pane.setStyle("-fx-padding: 5px");
        button2.setStyle("-fx-background-color: darkmagenta; -fx-text-fill: white; -fx-bordvm13jhcler-color: black; -fx-border-radius: 2px");

        box.getChildren().add(button1Pane);
        box.getChildren().add(button2Pane);

        this.setBottom(box);
        this.setCenter(board);

        controller = new GamePaneController(this);
        controller.addPassEventListener(button1);
        controller.addFailEventListener(button2);
    }
}
