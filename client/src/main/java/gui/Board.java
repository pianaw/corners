package gui;

import controller.BoardController;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class Board extends GridPane {

    public static final byte BOARD_SIZE = 8;
    public static BoardController controller;

    public Board() {
        int cell_size = 50; //TODO: расширить возможности выбора пользователем характеристик доски
        int home_height = 3;
        int home_weight = 3;

        for (int i = 0; i < BOARD_SIZE; i++) {
            this.getColumnConstraints().add(new ColumnConstraints(cell_size));
            this.getRowConstraints().add(new RowConstraints(cell_size));
        }

        controller = new BoardController(new Cell[BOARD_SIZE][BOARD_SIZE], this);
        controller.visualize();

        controller.addCheckers(home_height, home_weight);
    }
}
