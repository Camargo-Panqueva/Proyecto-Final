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

        final Point[] directions = { new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0) };

        for (Point direction : directions) {
            Point objectivePoint = new Point(basePoint.x + direction.x, basePoint.y + direction.y);
            if (isInsideBoard(objectivePoint) && !isOccupiedOrBlocked(player.getPosition(), objectivePoint)) {
                possibleMovements.add(new Point(objectivePoint));
            }
        }

        return possibleMovements;
    }

    private boolean isInsideBoard(Point point) {
        return point.x >= 0 && point.x < this.gameModel.getBoard().getWidth() && point.y >= 0 && point.y < this.gameModel.getBoard().getHeight();
    }

    private boolean isOccupiedOrBlocked(Point playerPosition, Point objectivePoint) {
        return isOccupiedPoint(objectivePoint) || isBlockedPoint(playerPosition, objectivePoint) != null;
    }

    private boolean isOccupiedPoint(Point point) {
        for (Player player : this.gameModel.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private Wall isBlockedPoint(final Point initialPoint, final Point finalPoint) {

        final int xWall = initialPoint.x == finalPoint.x ? 2 * initialPoint.x : (2 * initialPoint.x) + (finalPoint.x - initialPoint.x);
        final int yWall = initialPoint.y == finalPoint.y ? 2 * initialPoint.y : (2 * initialPoint.y) + (finalPoint.y - initialPoint.y);

        if (!(yWall <= this.gameModel.getBoard().getWidth() * 2 - 2 && yWall <= this.gameModel.getBoard().getHeight() * 2 - 2 && xWall >= 0 && yWall >= 0)) {
            return null;
        }

        final WallData wallData = gameModel.getBoard().getWallData(xWall, yWall);

        if (wallData == null) {
            return null;
        }

        final UUID wallID = wallData.getWallId();

        return this.walls.get(wallID);
    }

    public void executeMove(Player player, Point moveTo) {
        player.setPosition(moveTo);
        this.nextTurn();
    }

    public void executePlaceWall(final Player player, final Wall wall, final ArrayList<Point> newWalls) {
        final UUID wallUuid = UUID.randomUUID();
        wall.setWallId(wallUuid);

        for (Point point : newWalls) {
            this.gameModel.getBoard().getBoardWalls()[point.x][point.y] = wall.getWallData();
        }
        player.addWallPlaced(wall.getWallData());
        this.walls.put(wallUuid, wall);
        this.gameModel.addWall(wallUuid, wall.getWallData());
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

}
