package manager;

import ai.AIPlayer;
import context.GameContext;
import gui.GamePane;
import player.message.Message;
import player.message.Transmitter;
import player.message.TypeMessage;

import java.util.Optional;

public class Manager {

    public byte[][] board;
    private boolean isOnlyDoubleMove;
    private int lastMoveColumn;
    private int lastMoveRow;
    public AIPlayer ai;

    public Manager() {
        this.board = new byte[][]{{0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0}};

        this.isOnlyDoubleMove = false;
        this.lastMoveRow = -1;
        this.lastMoveColumn = -1;
    }

    public void setNewState(Message message) {
        replyTo(message);
    }

    private void replyTo(Message message) {
        if (GameContext.isOnlineGame) {
            Transmitter.send(message);
            return;
        }

        if (message.type.equals(TypeMessage.MOVE)) {
            byte typeOfMove = isValidate(message);

            if (typeOfMove != 0) {

                message.type = TypeMessage.NEW_STATE;
                GamePane.controller.checkState(Optional.of(message));

                if (typeOfMove == 1) {
                    message.type = TypeMessage.GO;
                    GamePane.controller.checkState(Optional.of(message));
                    message.type = TypeMessage.WAIT;
                    GamePane.controller.checkState(Optional.of(message));
                    ai.move();
                    return;
                } else if (typeOfMove == 2) {
                    message.type = TypeMessage.GO2;
                    GamePane.controller.checkState(Optional.of(message));
                }

            } else {
                message.type = TypeMessage.REJECT;
                GamePane.controller.checkState(Optional.of(message));
            }
        }
        if (message.type.equals(TypeMessage.PASS)) {
            if (GameContext.isOnlineGame) {
                Transmitter.send(message);

            } else {
                this.isOnlyDoubleMove = false;
                this.lastMoveColumn = -1;
                this.lastMoveRow = -1;
                message.type = TypeMessage.WAIT;
                GamePane.controller.checkState(Optional.of(message));
                ai.move();
            }
        }
    }

    private byte isValidate(Message message) {
        int deltaRow = Math.abs(message.sourceRow - message.targetRow);
        int deltaCol = Math.abs(message.sourceCol - message.targetCol);
        int direct = message.sourceRow - message.targetRow + message.sourceCol - message.targetCol < 0 ? -1 : 1;
        byte activeChecker = (byte) (ai.color == 1 ? -1 : 1);
        if (board[message.sourceRow][message.sourceCol] == activeChecker && board[message.targetRow][message.targetCol] == 0) {
            if (!isOnlyDoubleMove && (deltaCol == 0 && deltaRow == 1 || deltaCol == 1 && deltaRow == 0)) {
                board[message.sourceRow][message.sourceCol] = 0;
                board[message.targetRow][message.targetCol] = activeChecker;

                return 1;
            } else if (deltaCol == 2 && deltaRow == 0) {
                if (board[message.sourceRow][message.sourceCol - direct] != 0) {
                    if ((this.lastMoveRow == -1 && this.lastMoveColumn == -1) || (this.lastMoveColumn == message.sourceCol && this.lastMoveRow == message.sourceRow)) {
                        board[message.sourceRow][message.sourceCol] = 0;
                        board[message.targetRow][message.targetCol] = activeChecker;

                        this.isOnlyDoubleMove = true;
                        this.lastMoveColumn = message.targetCol;
                        this.lastMoveRow = message.targetRow;
                        return 2;
                    }
                }
            } else if (deltaCol == 0 && deltaRow == 2) {
                if (board[message.sourceRow - direct][message.sourceCol] != 0) {
                    if ((this.lastMoveRow == -1 && this.lastMoveColumn == -1) || (this.lastMoveColumn == message.sourceCol && this.lastMoveRow == message.sourceRow)) {
                        board[message.sourceRow][message.sourceCol] = 0;
                        board[message.targetRow][message.targetCol] = activeChecker;

                        this.isOnlyDoubleMove = true;
                        this.lastMoveColumn = message.targetCol;
                        this.lastMoveRow = message.targetRow;
                        return 2;
                    }
                }
            }
        }
        return 0;
    }

    public void checkNewState(Message message) {
        if (message.type.equals(TypeMessage.NEW_STATE)) {
            GamePane.controller.checkState(Optional.of(message));

            byte state = gameState();
            if (state == 0) {
                message.type = TypeMessage.GO;
            } else if (state == 1) {
                message.type = TypeMessage.WIN;
            } else if (state == 2) {
                message.type = TypeMessage.FAIL;
            } else {
                message.type = TypeMessage.DEAD_HEAT;
            }

            GamePane.controller.checkState(Optional.of(message));
        } else if (message.type.equals(TypeMessage.PASS)) {

            byte state = gameState();

            if (state == 0) {
                message.type = TypeMessage.GO;
            } else if (state == 1) {
                message.type = TypeMessage.WIN;
            } else if (state == 2) {
                message.type = TypeMessage.FAIL;
            } else {
                message.type = TypeMessage.DEAD_HEAT;
            }

            GamePane.controller.checkState(Optional.of(message));
        } else if (message.type.equals(TypeMessage.WAIT)) {
            message.type = TypeMessage.NEW_STATE;
            GamePane.controller.checkState(Optional.of(message));
        }
    }

    public byte gameState() {
        byte result = 0;
        if (board[0][5] == 1 && board[0][6] == 1 && board[0][7] == 1 &&
                board[1][5] == 1 && board[1][6] == 1 && board[1][7] == 1 &&
                board[2][5] == 1 && board[2][6] == 1 && board[2][7] == 1) {
            result += 1;
        }
        if (board[5][0] == -1 && board[6][0] == -1 && board[7][0] == -1 &&
                board[5][1] == -1 && board[6][1] == -1 && board[7][1] == -1 &&
                board[5][2] == -1 && board[6][2] == -1 && board[7][2] == -1) {
            result += 2;
        }

        return result;
    }
}
