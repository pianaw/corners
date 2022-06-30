package controller;

import gui.Window;
import javafx.scene.Scene;

public class WindowController {

    private final Window window;

    public WindowController(Window win) {
        window = win;
    }

    public void setScene(Scene pane) {
        window.stage.setScene(pane);
        window.stage.centerOnScreen();
    }

    public void setSize(double width, double height) {
        window.stage.setHeight(height);
        window.stage.setWidth(width);
    }
}
