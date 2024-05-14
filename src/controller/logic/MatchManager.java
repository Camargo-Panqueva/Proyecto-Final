package controller.logic;

import controller.wall.Wall;
import controller.wall.WallManager;
import model.GameModel;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallData;

import java.awt.*;
import java.util.*;

public class MatchManager {
    private final GameModel model;
    private final HashMap<UUID, Wall> walls;
    private final int[][] abstractBoard;
    private int indexCurrentIndex;

    public MatchManager(final GameModel gameModel) {
        this.model = gameModel;
        this.walls = new HashMap<>();
        this.abstractBoard = new int[gameModel.getBoard().getWidth() * 2 - 1][gameModel.getBoard().getHeight() * 2 - 1];

        if (!gameModel.getWalls().isEmpty()) {
            WallManager wallManager = new WallManager();
            for (WallData wallData : this.model.getWalls().values()) {
                this.walls.put(wallData.getWallId(), wallManager.getWallInstance(wallData));
            }
        }

        this.model.setPlayerInTurn(0);
        this.indexCurrentIndex = 0;
    }

    public ArrayList<Point> getPossibleMovements(final Player player) {
        final ArrayList<Point> possibleMovements = new ArrayList<>();
        final Point basePoint = new Point(player.getPosition());

        final Point[] directions = {new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

        for (Point direction : directions) {
            Point objectivePoint = new Point(basePoint.x + direction.x, basePoint.y + direction.y);
            if (isInsideBoard(objectivePoint) && !isOccupiedOrBlocked(player.getPosition(), objectivePoint)) {
                possibleMovements.add(new Point(objectivePoint));
            }
        }

        return possibleMovements;
    }

    private boolean isInsideBoard(Point point) {
        return point.x >= 0 && point.x < this.model.getBoard().getWidth() && point.y >= 0 && point.y < this.model.getBoard().getHeight();
    }

    private boolean isOccupiedOrBlocked(Point playerPosition, Point objectivePoint) {
        return isOccupiedPoint(objectivePoint) || isBlockedPoint(playerPosition, objectivePoint) != null;
    }

    private boolean isOccupiedPoint(Point point) {
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private Wall isBlockedPoint(final Point initialPoint, final Point finalPoint) {

        final int xWall = initialPoint.x == finalPoint.x ? 2 * initialPoint.x : (2 * initialPoint.x) + (finalPoint.x - initialPoint.x);
        final int yWall = initialPoint.y == finalPoint.y ? 2 * initialPoint.y : (2 * initialPoint.y) + (finalPoint.y - initialPoint.y);

        if (!(yWall <= this.model.getBoard().getWidth() * 2 - 2 && yWall <= this.model.getBoard().getHeight() * 2 - 2 && xWall >= 0 && yWall >= 0)) {
            return null;
        }

        final WallData wallData = model.getBoard().getWallData(xWall, yWall);

        if (wallData == null) {
            return null;
        }

        final UUID wallID = wallData.getWallId();

        return this.walls.get(wallID);
    }

    public void executeMove(Player player, Point moveTo) {
        player.setPosition(moveTo);
        //Win Condition
        if (player.getXWinPosition() == player.getPosition().x || player.getYWinPosition() == player.getPosition().y) {
            this.model.setMatchState(GameModel.MatchState.WINNER);
            this.model.setWinningPlayer(player);
        }
        this.nextTurn();
    }

    public void executePlaceWall(final Player player, final Wall wall, final ArrayList<Point> newWalls) {
        final UUID wallUuid = UUID.randomUUID();
        wall.setWallId(wallUuid);

        for (Point point : newWalls) {
            this.model.getBoard().getBoardWalls()[point.x][point.y] = wall.getWallData();
        }
        player.addWallPlaced(wall.getWallData());
        this.walls.put(wallUuid, wall);
        this.model.addWall(wallUuid, wall.getWallData());
        this.nextTurn();
    }

    public boolean isABlockerWall(final ArrayList<Point> newWalls) {

        final int height = this.model.getBoard().getHeight() * 2 - 1;
        final int width = this.model.getBoard().getWidth() * 2 - 1;

        final int[][] wantedBoard = new int[width][height];

        for (int x = 0; x < width; x++) { //Abstract the values for the algorithm
            for (int y = 0; y < height; y++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    wantedBoard[x][y] = 0;
                } else if (this.model.getBoard().getWallData(x, y) == null) {
                    wantedBoard[x][y] = 1;
                } else {
                    wantedBoard[x][y] = 0;
                }
            }
        }
        for (Point point : newWalls) { //Add the new wall
            wantedBoard[point.x][point.y] = 0;
        }

        int islandCount = 0;

        final boolean[][] visited = new boolean[width][height];

        final Point[] directions = {new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (islandCount > 1) {
                    return true;
                }

                if (wantedBoard[y][x] == 1 && !visited[y][x]) { //start the search from the first 1
                    //bfs
                    visited[y][x] = true;

                    ArrayDeque<Point> deque = new ArrayDeque<>();

                    deque.add(new Point(x, y));

                    while (!deque.isEmpty()) {
                        Point currPoint = new Point(deque.pop());

                        for (Point direction : directions) {
                            int dirY = currPoint.y + direction.y;
                            int dirX = currPoint.x + direction.x;
                            if (dirY < height && dirX < width && dirY >= 0 && dirX >= 0 && wantedBoard[dirY][dirX] == 1 && !visited[dirY][dirX]) {
                                deque.add(new Point(dirX, dirY));
                                visited[dirY][dirX] = true;
                            }
                        }
                    }

                    islandCount++;
                }
            }
        }
        return false;
    }

    private void AIMove() {

    }

    private void nextTurn() {

        this.indexCurrentIndex++;

        if (!this.model.getPlayers().containsKey(this.indexCurrentIndex)) {
            this.indexCurrentIndex = 0;
            this.model.setPlayerInTurn(0);
            return;
        }

        this.model.setPlayerInTurn(this.indexCurrentIndex);

        //hardcode, the IA always is the player 2
        if (this.model.getGameModeManager().getCurrentGameMode() == GameModes.NORMAL_PLAYER_IA && this.indexCurrentIndex == 1) {

        }
    }

    public void printMatrix(int[][] matrix) {
        System.out.println(" ________matrix________ ");
        if (matrix == null) {
            System.out.println("Null");
        }
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < Objects.requireNonNull(matrix)[0].length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                if (matrix[x][y] == 0) {
                    sb.append("0").append(" ");
                } else {
                    sb.append("1").append(" ");
                }

            }
            sb.append("\n");
            System.out.println(sb);
            ;
        }
    }

}
