package gui;

import controller.MenuPaneController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

public class MenuPane extends BorderPane {

    public static MenuPaneController controller;

    public MenuPane() {
        VBox box = new VBox();
        this.setStyle("-fx-background-color: linear-gradient(#000000, #ffffff)");

        Button button1 = new Button("Онлайн игра");
        button1.setPrefSize(200,20);
        button1.setStyle("-fx-background-color: seagreen; -fx-text-fill: white; -fx-border-color: black; -fx-border-radius: 2px");
        AnchorPane paneButton1 = new AnchorPane(button1);
        paneButton1.setStyle("-fx-padding: 5px");

        Button button2 = new Button("Игра с ИИ");
        button2.setPrefSize(200,20);
        button2.setStyle("-fx-background-color: seagreen; -fx-text-fill: white; -fx-border-color: black; -fx-border-radius: 2px");
        AnchorPane paneButton2 = new AnchorPane(button2);
        paneButton2.setStyle("-fx-padding: 5px");

        box.getChildren().add(paneButton1);
        box.getChildren().add(paneButton2);

        FlowPane flowPane = new FlowPane(box);
        this.setCenter(flowPane);
        flowPane.setAlignment(Pos.CENTER);
        box.setAlignment(Pos.CENTER);

        controller = new MenuPaneController(this);
    }
}
