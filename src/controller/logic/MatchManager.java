package controller.logic;

import model.GameModel;
import model.player.Player;

import java.awt.*;
import java.util.ArrayList;

public class MatchManager {
    private final GameModel gameModel;
    private int indexCurrentIndex;

    public MatchManager(final GameModel gameModel) {
        this.gameModel = gameModel;
        this.gameModel.setPlayerInTurn(0);
        this.indexCurrentIndex = 0;
    }

    public ArrayList<Point> getPossibleMovements(final Player player) {

        final ArrayList<Point> possibleMovements = new ArrayList<>();

        final Point basePoint = new Point(player.getPosition());

        final int width = this.gameModel.getBoard().getWidth();
        final int height = this.gameModel.getBoard().getHeight();

        if (basePoint.y != height - 1) {
            possibleMovements.add(new Point(basePoint.x, basePoint.y + 1));
        }
        if (basePoint.x != width - 1) {
            possibleMovements.add(new Point(basePoint.x + 1, basePoint.y));
        }
        if (basePoint.y != 0) {
            possibleMovements.add(new Point(basePoint.x, basePoint.y - 1));
        }
        if (basePoint.x != 0) {
            possibleMovements.add(new Point(basePoint.x - 1, basePoint.y));
        }

        return possibleMovements;
    }

    public void executeMove(Player player, Point moveTo){
        player.setPosition(moveTo);
        this.nextTurn();
    }

    private void nextTurn() {

        this.indexCurrentIndex++;

        if (!this.gameModel.getPlayers().containsKey(this.indexCurrentIndex)){
            this.indexCurrentIndex = 0;
            this.gameModel.setPlayerInTurn(0);
            return;
        }

        this.gameModel.setPlayerInTurn(this.indexCurrentIndex);
    }

    private boolean isOccupiedPoint(Point point) {
        return false;
    }
}
