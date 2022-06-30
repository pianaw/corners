package ai;

import context.GameContext;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import player.message.Message;
import player.message.TypeMessage;

import java.util.*;

/**
 * @author Diana.Monzhosova
 * Simple algorithm of imitating Player's steps for "Corners" game
 */
public class AIPlayer {

    public byte color;
    public List<Point> currentCoordinates;
    public List<Point> targetCoordinates;
    private byte[][] board;
    private Point synchronisedPoint;

    public AIPlayer(Color color) {
        this.color = (byte) (color == Color.WHITE ? 1 : -1);
        this.currentCoordinates = new Stack<>();
        this.targetCoordinates = new Stack<>();

        if (this.color == -1) {
            for (byte i = 0; i < 3; i++) {
                for (byte j = 5; j < 8; j++) {
                    currentCoordinates.add(new Point(j, i));
                }
            }
            for (byte i = 5; i < 8; i++) {
                for (byte j = 0; j < 3; j++) {
                    targetCoordinates.add(new Point(j, i));
                }
            }
        } else {
            for (byte i = 5; i < 8; i++) {
                for (byte j = 0; j < 3; j++) {
                    currentCoordinates.add(new Point(j, i));
                }
            }
            for (byte i = 5; i < 8; i++) {
                for (byte j = 0; j < 3; j++) {
                    targetCoordinates.add(new Point(j, i));
                }
            }
        }
        this.board = GameContext.manager.board;
        makeAim();
    }

    private void makeAim() {
        Collections.shuffle(currentCoordinates);

        currentCoordinates.forEach(point -> point.targetPoint = targetCoordinates.remove(0));
    }

    public void move() {
        Point point;
        byte x;
        byte y;
        if (this.synchronisedPoint == null) {
            Collections.shuffle(currentCoordinates);

            x = currentCoordinates.get(0).currentX;
            y = currentCoordinates.get(0).currentY;

            point = makeRoute(currentCoordinates.get(0));
        } else {
            x = this.synchronisedPoint.currentX;
            y = this.synchronisedPoint.currentY;

            point = makeRoute(this.synchronisedPoint);
        }
        if (this.synchronisedPoint == null) {
            if (point != null) {
                Message message = new Message();
                message.type = TypeMessage.NEW_STATE;

                message.sourceRow = (int) y;
                message.sourceCol = (int) x;

                message.targetRow = (int) point.currentY;
                message.targetCol = (int) point.currentX;

                GameContext.manager.checkNewState(message);
            }
            else {
                move();
            }
        }
        else {
            Message message = new Message();
            if (point != null) {
                message.type = TypeMessage.WAIT;

                message.sourceRow = (int) y;
                message.sourceCol = (int) x;

                message.targetRow = (int) point.currentY;
                message.targetCol = (int) point.currentX;

                GameContext.manager.checkNewState(message);
                Platform.runLater(this::move);
            } else {
                message.type = TypeMessage.PASS;

                this.synchronisedPoint = null;
                GameContext.manager.checkNewState(message);
            }
        }
    }

    public Point makeRoute(Point point) {
        if (isVerticalDoubleMovePermittedFor(point)) {
            board[point.currentY][point.currentX] = 0;
            point.currentY -= 2 * color;
            this.synchronisedPoint = point;
        } else if (isHorizontalDoubleMovePermittedFor(point)) {
            board[point.currentY][point.currentX] = 0;
            point.currentX += 2 * color;
            this.synchronisedPoint = point;
        } else if (this.synchronisedPoint == null && isVerticalMovePermittedFor(point)) {
            board[point.currentY][point.currentX] = 0;
            point.currentY -= color;
        } else if (this.synchronisedPoint == null && isHorizontalMovePermittedFor(point)) {
            board[point.currentY][point.currentX] = 0;
            point.currentX += color;
        } else {
            this.checkStation(point);
            return null;
        }

        board[point.currentY][point.currentX] = color;
        return point;
    }

    public void checkStation(Point point) {
        if (point.targetPoint.currentX == 0) {
            if (point.currentX == 3) {
                Optional<Point> point2 = currentCoordinates.stream().filter(x -> x.currentX == 1 && x.currentY == point.currentY).findAny();

                point2.ifPresent(value -> exchangeTargetCoordinates(point, value));
            }
        }
        else if (point.targetPoint.currentY == 7) {
            if (point.currentY == 4) {
                Optional<Point> point2 = currentCoordinates.stream().filter(x -> x.currentY == 6 &&  x.currentX == point.currentX).findAny();

                point2.ifPresent(value -> exchangeTargetCoordinates(point, value));
            }
        }
    }

    public void exchangeTargetCoordinates(Point point1, Point point2) {
        byte x = point1.currentX;
        byte y = point1.currentY;

        point1.currentX = point2.currentX;
        point1.currentY = point2.currentY;

        point2.currentX = x;
        point2.currentY = y;
    }

    public boolean isVerticalMovePermittedFor(Point point) {

        byte y = (byte) (point.currentY - color);

        if (y >= 0 && y < 8 && y <= point.targetPoint.currentY) {
            return board[y][point.currentX] == 0;
        }

        return false;
    }

    public boolean isVerticalDoubleMovePermittedFor(Point point) {
        byte y = (byte) (point.currentY - 2 * color);

        if (y >= 0 && y < 8 && y <= point.targetPoint.currentY) {
            return board[y + color][point.currentX] != 0 && board[y][point.currentX] == 0;
        }

        return false;
    }

    public boolean isHorizontalMovePermittedFor(Point point) {
        byte x = (byte) (point.currentX + color);

        if (x >= 0 && x < 8 && x >= point.targetPoint.currentX) {
            return board[point.currentY][x] == 0;
        }

        return false;
    }

    public boolean isHorizontalDoubleMovePermittedFor(Point point) {
        byte x = (byte) (point.currentX + 2 * color);

        if (x >= 0 && x < 8 && x >= point.targetPoint.currentX) {
            return board[point.currentY][x - color] != 0 && board[point.currentY][x] == 0;
        }

        return false;
    }
}
