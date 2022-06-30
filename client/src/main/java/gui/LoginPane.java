package gui;

import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

public class LoginPane extends BorderPane {

    public LoginPane() {
        this.setStyle("-fx-background-color: linear-gradient(#000000, #ffffff)");

        FlowPane pane = new FlowPane();
        pane.setAlignment(Pos.CENTER);
    }
}
