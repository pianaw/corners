package gui;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class Checker extends Pane {

    public Color color;
    private ImageView view;

    public Checker(Color color) {
        this.color = color;

        view = new ImageView();
        view.fitHeightProperty().setValue(35);
        view.fitWidthProperty().setValue(35);

        if (color.equals(Color.WHITE)){
            view.setImage(new Image("images/white-checker.png"));

        }
        else if(color.equals(Color.BLACK)) {
            view.setImage(new Image("images/black-checker.png"));
        }

        this.getChildren().add(view);

    }

}
