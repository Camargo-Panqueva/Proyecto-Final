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

        final Point objetivePoint = new Point();
        Wall wall;
        //TODO : REFACTOR ME PLS
        if (basePoint.y != height - 1) {
            objetivePoint.move(basePoint.x, basePoint.y + 1);

            wall = this.isBlockedPoint(player.getPosition(), objetivePoint);
            if (wall == null) {
                possibleMovements.add(new Point(objetivePoint));
            } else if (wall.getWallData().getOwner() != player) {//TODO : ally walls
                possibleMovements.add(new Point(objetivePoint));
            }
        }
        if (basePoint.x != width - 1) {
            objetivePoint.move(basePoint.x + 1, basePoint.y);

            wall = this.isBlockedPoint(player.getPosition(), objetivePoint);
            if (wall == null) {
                possibleMovements.add(new Point(objetivePoint));
            } else if (wall.getWallData().getOwner() != player) {//TODO : ally walls
                possibleMovements.add(new Point(objetivePoint));
            }
        }

        if (basePoint.y != 0) {
            objetivePoint.move(basePoint.x, basePoint.y - 1);

            wall = this.isBlockedPoint(player.getPosition(), objetivePoint);
            if (wall == null) {
                possibleMovements.add(new Point(objetivePoint));
            } else if (wall.getWallData().getOwner() != player) {//TODO : ally walls
                possibleMovements.add(new Point(objetivePoint));
            }
        }

        if (basePoint.x != 0) {
            objetivePoint.move(basePoint.x - 1, basePoint.y);

            wall = this.isBlockedPoint(player.getPosition(), objetivePoint);
            if (wall == null) {
                possibleMovements.add(new Point(objetivePoint));
            } else if (wall.getWallData().getOwner() != player) {//TODO : ally walls
                possibleMovements.add(new Point(objetivePoint));
            }
        }

        return possibleMovements;
    }

    private boolean isOccupiedPoint(Point point) {
        return false;
    }

    private Wall isBlockedPoint(final Point initialPoint, final Point finalPoint) {

        final int xWall = initialPoint.x == finalPoint.x ? 2 * initialPoint.x - 1 : 2 * (initialPoint.x + (finalPoint.x - initialPoint.x)) - 1;
        final int yWall = initialPoint.y == finalPoint.y ? 2 * initialPoint.y - 1 : 2 * (initialPoint.y + (finalPoint.y - initialPoint.y)) - 1;

        if(!(yWall <= this.gameModel.getBoard().getWidth() * 2 - 2 && yWall <= this.gameModel.getBoard().getHeight() * 2 - 2 && xWall >= 0 && yWall >= 0)){
            return null;
        }

        System.out.println(xWall + " " + yWall);

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

}
