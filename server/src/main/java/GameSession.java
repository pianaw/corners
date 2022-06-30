import message.Message;
import message.TypeMessage;

public class GameSession extends Thread {

    private ClientHandler player1;
    private ClientHandler player2;
    private ClientHandler activePlayer;
    private byte[][] board;
    boolean isOnlyDoubleMove;
    private int lastMoveColumn;
    private int lastMoveRow;
    private boolean isFirstPlayerWon;
    private int moveCount; // TODO

    public GameSession(ClientHandler player1) {
        this.player1 = player1;

        this.board = new byte[][]{{0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, -1, -1, -1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 0, 0}};

        this.isOnlyDoubleMove = false;
        this.lastMoveColumn = -1;
        this.lastMoveRow = -1;
        this.moveCount = 0;
        this.isFirstPlayerWon = false;

    }

    public void addPlayer(ClientHandler player2) {
        if (this.player2 == null) {
            this.player2 = player2;
        }
    }

    @Override
    public void run() {
        System.out.println("New game session started");
        Message startMessage = new Message();

        startMessage.type = TypeMessage.START1;
        player1.sendMessage(startMessage);

        startMessage.type = TypeMessage.START2;
        player2.sendMessage(startMessage);

        this.activePlayer = player1;

        while (true) {
            Message message = activePlayer.readMessage();

            if (message.type.equals(TypeMessage.MOVE)) {
                byte typeOfMove = validateMove(message);
                if (typeOfMove == 1) {
                    message.type = TypeMessage.NEW_STATE;

                    player1.sendMessage(message);
                    player2.sendMessage(message);

                    message.type = TypeMessage.WAIT;
                    activePlayer.sendMessage(message);

                    changeActivePlayer();

                    message.type = TypeMessage.GO;
                    activePlayer.sendMessage(message);

                    this.moveCount += 1;
                }
                else if (typeOfMove == 2) {
                    message.type = TypeMessage.NEW_STATE;

                    player1.sendMessage(message);
                    player2.sendMessage(message);

                    message.type = TypeMessage.GO2;
                    activePlayer.sendMessage(message);

                    this.lastMoveColumn = message.targetCol;
                    this.lastMoveRow = message.targetRow;

                    this.moveCount += 1;
                } else {
                    message.type = TypeMessage.REJECT;
                    activePlayer.sendMessage(message);
                }

            }
            else if (message.type.equals(TypeMessage.PASS)) {
                message.type = TypeMessage.WAIT;
                activePlayer.sendMessage(message);

                changeActivePlayer();

                message.type = TypeMessage.GO;
                activePlayer.sendMessage(message);
            }
            else if (message.type.equals(TypeMessage.FAIL)) {
                message.type = TypeMessage.FAIL;
                activePlayer.sendMessage(message);

                changeActivePlayer();

                message.type = TypeMessage.WIN;
                activePlayer.sendMessage(message);

                this.gameDown();
            }

            if (isFirstPlayerWon && activePlayer == player1) {
                message.type = TypeMessage.WIN;
                player1.sendMessage(message);

                message.type = TypeMessage.FAIL;
                player2.sendMessage(message);
            }

            if (activePlayer == player2 && isVictory()) {
                if (isFirstPlayerWon) {
                    message.type = TypeMessage.DEAD_HEAT;
                    player1.sendMessage(message);
                    player2.sendMessage(message);
                }
                else {
                    message.type = TypeMessage.WIN;
                    player2.sendMessage(message);

                    message.type = TypeMessage.FAIL;
                    player1.sendMessage(message);
                }
            }

            else if (activePlayer == player1 && isVictory()) {
                isFirstPlayerWon = true;
            }
        }
    }

    private void changeActivePlayer() {
        if (this.activePlayer == this.player1) {
            activePlayer = player2;
        } else {
            activePlayer = player1;
        }

        this.lastMoveRow = -1;
        this.lastMoveColumn = -1;

        this.isOnlyDoubleMove = false;
    }

    private byte validateMove(Message message) {
        int deltaRow = Math.abs(message.sourceRow - message.targetRow);
        int deltaCol = Math.abs(message.sourceCol - message.targetCol);
        int direct = message.sourceRow - message.targetRow + message.sourceCol - message.targetCol < 0 ? -1 : 1;
        byte activeChecker = (byte) (activePlayer == player1 ? 1 : -1);
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
                        return 2;
                    }
                }
            } else if (deltaCol == 0 && deltaRow == 2) {
                if (board[message.sourceRow - direct][message.sourceCol] != 0) {
                    if ((this.lastMoveRow == -1 && this.lastMoveColumn == -1) || (this.lastMoveColumn == message.sourceCol && this.lastMoveRow == message.sourceRow)) {
                        board[message.sourceRow][message.sourceCol] = 0;
                        board[message.targetRow][message.targetCol] = activeChecker;

                        this.isOnlyDoubleMove = true;
                        return 2;
                    }
                }
            }

        }
        return 0;
    }

    public boolean isVictory() {
        if (this.activePlayer == player1) {
            return board[0][5] == 1 && board[0][6] == 1 && board[0][7] == 1 &&
                    board[1][5] == 1 && board[1][6] == 1 && board[1][7] == 1 &&
                    board[2][5] == 1 && board[2][6] == 1 && board[2][7] == 1;
        } else if (this.activePlayer == player2){
            return board[5][0] == -1 && board[6][0] == -1 && board[7][0] == -1 &&
                    board[5][1] == -1 && board[6][1] == -1 && board[7][1] == -1 &&
                    board[5][2] == -1 && board[6][2] == -1 && board[7][2] == -1;

        }
        return false;
    }

    public void gameDown() {
        player1.clientDown();
        player2.clientDown();

        this.interrupt();
        System.out.println(Thread.activeCount());
    }
}
