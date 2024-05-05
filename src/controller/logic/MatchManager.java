package controller.logic;

import model.GameModel;
import model.player.Player;

import java.awt.*;
import java.util.ArrayList;

public class MatchManager {
    private final GameModel gameModel;

    public MatchManager(final GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameModel.setPlayerInTurn(0);
    }

    public ArrayList<Point> getPossibleMovements(final Player player) {

        final ArrayList<Point> possibleMovements = new ArrayList<>();

        final Point basePoint = new Point(player.getPosition());

        final int wight = this.gameModel.getBoard().getWidth();
        final int height = this.gameModel.getBoard().getHeight();

        if (basePoint.y != height - 1) {
            possibleMovements.add(new Point(basePoint.x, basePoint.y - 1));
        }
        if (basePoint.x != wight - 1) {
            possibleMovements.add(new Point(basePoint.x - 1, basePoint.y));
        }
        if (basePoint.y != 0) {
            possibleMovements.add(new Point(basePoint.x, basePoint.y + 1));
        }
        if (basePoint.x != 0) {
            possibleMovements.add(new Point(basePoint.x + 1, basePoint.y));
        }

        return possibleMovements;
    }

    private boolean isOccupiedPoint(Point point) {
        return false;
    }

    private void movePlayer(Player player) {

    }
}
