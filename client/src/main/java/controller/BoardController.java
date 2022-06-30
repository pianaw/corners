package controller;

import context.GameContext;
import gui.Board;
import gui.Cell;
import gui.Checker;
import gui.GamePane;
import javafx.application.Platform;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import player.message.Message;
import player.message.TypeMessage;

public class BoardController {

    private Cell[][] boardArr;
    private Cell onHand = null;
    private Board board;

    public BoardController(Cell[][] boardArr, Board board) {
        this.boardArr = boardArr;
        this.board = board;
    }

    public void addMovementEventListener(Cell cell) {
            cell.setOnMouseClicked(event -> {
                if (cell.containsChecker() && onHand == null) {
                    cell.addLightEffect();
                    onHand = cell;
                } else if (cell == onHand) {
                    onHand.removeLightEffect();
                    onHand = null;
                } else if (!cell.containsChecker() && onHand != null) {
                    onHand.removeLightEffect();

                    Message message = new Message();
                    message.type = TypeMessage.MOVE;
                    message.sourceCol = onHand.getColumn();
                    message.sourceRow = onHand.getRow();
                    message.targetCol = cell.getColumn();
                    message.targetRow = cell.getRow();

                    GameContext.manager.setNewState(message);

                    onHand = null;
                }
            });
    }


    public void checkState(Message message) {
        if (message.type.equals(TypeMessage.NEW_STATE)) {
            System.out.println("i got new state of the game");
            Checker checker = (Checker) boardArr[message.sourceRow][message.sourceCol].getChecker();

            Platform.runLater(() -> {
                boardArr[message.targetRow][message.targetCol].addChecker(checker);
                boardArr[message.sourceRow][message.sourceCol].removeChecker();
            });
        }
        else if (message.type.equals(TypeMessage.WAIT)) {
            System.out.println("i'm waiting");
            GamePane.controller.setMessage("Ждём соперника...");
            Platform.runLater(() -> setBoardDisabled(true));
        }
        else if (message.type.equals(TypeMessage.GO)) {
            GamePane.controller.setMessage("Ваш ход");
            Platform.runLater(() -> setBoardDisabled(false));
        }
        else if (message.type.equals(TypeMessage.REJECT)) {
            GamePane.controller.setMessage("Некорректный ход...");
            System.out.println("server rejected to make this move");
        }
        else if (message.type.equals(TypeMessage.START1)) {
            GamePane.controller.setMessage("Второй игрок подключился к игре. Ваш ход");
            System.out.println("I'm started");

            Platform.runLater(() -> setBoardDisabled(false));
        }
        else if (message.type.equals(TypeMessage.START2)) {
            System.out.println("i'm waiting");
            GamePane.controller.setMessage("Вы присоединились к игре. Ждите хода соперника");
            board.setRotate(180);
            Platform.runLater(() -> setBoardDisabled(true));
        }
    }

    public void setBoardDisabled(boolean state) {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                this.boardArr[i][j].setDisable(state);
            }
        }
    }

    public void addCheckers(int home_height, int home_weight) {
        for (int i = 0; i < home_height; i++) {
            for (int j = Board.BOARD_SIZE - home_weight; j < Board.BOARD_SIZE; j++) {
                this.boardArr[i][j].addChecker(new Checker(Color.BLACK));
            }
        }

        for (int i = Board.BOARD_SIZE - home_height; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < home_weight; j++) {
                this.boardArr[i][j].addChecker(new Checker(Color.WHITE));
            }
        }
    }

    public void visualize() {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                Cell cell = new Cell(50, Color.SEAGREEN);
                cell.setColumn(i);
                cell.setRow(j);

                board.add(cell, i, j);


                boardArr[j][i] = cell;

                addMovementEventListener(cell);
            }
        }

        board.setGridLinesVisible(true);
        board.setEffect(new InnerShadow(100, Color.BLACK));

        board.setStyle("-fx-border-width: 2px; -fx-border-color: black");
    }
}
