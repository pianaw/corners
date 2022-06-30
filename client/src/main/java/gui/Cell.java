package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.Glow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class Cell extends StackPane {

    public Color color;
    private int size;
    private int column;
    private int row;

    public Cell(int size, Color color) {
        this.setBackground(new Background(new BackgroundFill(color, new CornerRadii(0, false), new Insets(0))));
        this.setHeight(size);
        this.setWidth(size);

        this.setAlignment(Pos.CENTER);

        this.setPadding(new Insets(7));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void addChecker(Checker checker) {
        if (!containsChecker()) {
            this.getChildren().add(checker);
        }
    }

    public void removeChecker() {
        if (this.containsChecker()) {
            this.getChildren().remove(0);
        }
    }

    public Node getChecker() {
        if (this.containsChecker()) {
            return this.getChildren().get(0);
        }
        return null;
    }

    public boolean containsChecker() {
        return this.getChildren().size() != 0;
    }

    public void addLightEffect() {
        this.setEffect(new Glow());
    }

    public void removeLightEffect() {
        this.setEffect(null);
    }
}
