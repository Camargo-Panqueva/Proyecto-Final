package controller.logic;

import controller.wall.Wall;
import controller.wall.WallManager;
import model.GameModel;
import model.player.Player;
import model.wall.WallData;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MatchManager {
    private final GameModel gameModel;
    private final HashMap<UUID, Wall> walls;
    private int indexCurrentIndex;

    public MatchManager(final GameModel gameModel) {
        this.gameModel = gameModel;
        this.walls = new HashMap<>();

        if (!gameModel.getWalls().isEmpty()) {
            WallManager wallManager = new WallManager();
            for (WallData wallData : this.gameModel.getWalls().values()) {
                this.walls.put(wallData.getWallId(), wallManager.getWallInstance(wallData));
            }
        }

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

    public void executeMove(Player player, Point moveTo) {
        player.setPosition(moveTo);
        this.nextTurn();
    }

    public void executePlaceWall(final Player player, final Wall wall, final ArrayList<Point> newWalls) {
        final UUID wallUuid = UUID.randomUUID();
        wall.getWallData().setWallId(wallUuid);

        for (Point point : newWalls) {
            this.gameModel.getBoard().getBoardWalls()[point.y][point.x] = wall.getWallData();
        }
        player.addWallPlaced(wall.getWallData());
        this.walls.put(wallUuid, wall);
        this.nextTurn();
    }

    private void nextTurn() {

        this.indexCurrentIndex++;

        if (!this.gameModel.getPlayers().containsKey(this.indexCurrentIndex)) {
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
