package gui;

import controller.WindowController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window extends Application {

    public Stage stage;
    public static WindowController controller;
    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("Corners2D - ClassicVersion");
        stage.setWidth(400);
        stage.setHeight(400);
        stage.setResizable(false);
        stage.centerOnScreen();

        MenuPane pane = new MenuPane();
        Scene scene = new Scene(pane, 800, 600);
        stage.setScene(scene);

        controller = new WindowController(this);
        stage.show();
    }
}
