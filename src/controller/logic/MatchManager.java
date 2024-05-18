package controller.logic;

import controller.wall.Wall;
import controller.wall.WallManager;
import model.GameModel;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallData;
import util.ConcurrentLoop;

import java.awt.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class MatchManager {
    private final GameModel model;
    private final HashMap<UUID, Wall> walls;
    private final int[][] abstractBoard;
    private int indexCurrentIndex;

    private Instant secondCount;

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

        this.startTimer();

        this.model.setTurnCount((short) 0);
        this.model.setPlayerInTurn(0);
        this.indexCurrentIndex = 0;

        this.triggerActionBeforeTurn();
    }

    public void lookForwardMoves(final Player fromPlayer, final ArrayList<Point> directions, final ArrayList<Point> possibleMovements, final Player playerLooking) {
        final Point basePoint = new Point(fromPlayer.getPosition());

        for (Point direction : directions) {
            Point objectivePoint = new Point(basePoint.x + direction.x, basePoint.y + direction.y);
            if (!isInsideBoard(objectivePoint) || isABlockerWall(fromPlayer.getPosition(), objectivePoint, playerLooking)) {
            } else if (isOccupiedPoint(objectivePoint)) {
                final ArrayList<Point> jumpedPlayerMoves = new ArrayList<>();

                final Point blockerPlayer = new Point(this.getPlayer(objectivePoint).getPosition());

                for (Point dir : directions) {
                    if ((dir.x == 1 && direction.x == -1) || (dir.y == 1 && direction.y == -1 || ((dir.x == -1 && direction.x == 1) || (dir.y == -1 && direction.y == 1)))) {
                    } else if (getBlockerWall(blockerPlayer, new Point(blockerPlayer.x + dir.x, blockerPlayer.y + dir.y)) != null) {
                    } else {
                        jumpedPlayerMoves.add(new Point(dir));
                    }
                }
                this.lookForwardMoves(this.getPlayer(objectivePoint), jumpedPlayerMoves, possibleMovements, playerLooking);
            } else {
                possibleMovements.add(new Point(objectivePoint));
            }
        }
    }

    public ArrayList<Point> getPossibleMovements(final Player player) {
        final ArrayList<Point> possibleMoves = new ArrayList<>();
        final ArrayList<Point> basicDirections = new ArrayList<>();
        basicDirections.add(new Point(0, 1));
        basicDirections.add(new Point(1, 0));
        basicDirections.add(new Point(0, -1));
        basicDirections.add(new Point(-1, 0));
        this.lookForwardMoves(player, basicDirections, possibleMoves, player);
        return possibleMoves.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
    }

    private Player getPlayer(Point point) {
        return this.model.getPlayers().values().stream().filter(p -> p.getPosition().equals(point)).findAny().orElse(null);
    }

    private boolean isInsideBoard(Point point) {
        return point.x >= 0 && point.x < this.model.getBoard().getWidth() && point.y >= 0 && point.y < this.model.getBoard().getHeight();
    }

    private boolean isABlockerWall(Point playerPosition, Point objectivePoint, Player player) {
        final Wall wall = getBlockerWall(playerPosition, objectivePoint);
        if (wall == null) {
            return false;
        } else if (wall.getIsAlly() && wall.getOwner().equals(player)) {
            return false;
        }
        return true;
    }

    private boolean isOccupiedPoint(Point point) {
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private Wall getBlockerWall(final Point initialPoint, final Point finalPoint) {

        final int xWall = initialPoint.x == finalPoint.x ? 2 * initialPoint.x : (2 * initialPoint.x) + (finalPoint.x - initialPoint.x);
        final int yWall = initialPoint.y == finalPoint.y ? 2 * initialPoint.y : (2 * initialPoint.y) + (finalPoint.y - initialPoint.y);

        if (!(xWall <= this.model.getBoard().getWidth() * 2 - 2 && yWall <= this.model.getBoard().getHeight() * 2 - 2 && xWall >= 0 && yWall >= 0)) {
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

        wall.setOwner(player);
        player.addWallPlaced(wall.getWallData());
        this.walls.put(wallUuid, wall);
        this.model.addWall(wallUuid, wall.getWallData());
        this.nextTurn();
    }

    public Wall executeDeleteWall(UUID wallId) {
        final Wall wall = this.walls.get(wallId);
        if (wall == null) {
            return null;
        }
        int boardWallX;
        int boardWallY;
        for (int x = 0; x < wall.getWidth(); x++) {
            for (int y = 0; y < wall.getHeight(); y++) {

                boardWallX = wall.getPositionOnBoard().x + x;
                boardWallY = wall.getPositionOnBoard().y + y;
                final WallData wallDataPosition = this.model.getBoard().getBoardWalls()[boardWallX][boardWallY];

                if (wallDataPosition == wall.getWallData()) {
                    model.getBoard().getBoardWalls()[boardWallX][boardWallY] = null;
                }
            }
        }

        wall.getOwner().removeWallPlaced(wall.getWallData());
        wall.setOwner(null);
        model.getWalls().remove(wall.getWallId());
        return this.walls.remove(wall.getWallId());
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

        final Point[] directions = {new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

        final HashSet<Player> playersThatGoalIsReachable = new HashSet<>();

        for (Player player : this.model.getPlayers().values()) {

            final boolean[][] visited = new boolean[width][height];


            final int x = player.getPosition().x * 2;
            final int y = player.getPosition().y * 2;

            //bfs
            visited[x][y] = true;

            ArrayDeque<Point> deque = new ArrayDeque<>();

            deque.add(new Point(x, y));

            while (!deque.isEmpty()) {
                final Point currPoint = new Point(deque.pop());

                if ((player.getYWinPosition() != -1 && currPoint.y == player.getYWinPosition() * 2) || (player.getXWinPosition() != -1 && currPoint.x == player.getXWinPosition() * 2)) {
                    playersThatGoalIsReachable.add(player); // the player reached his goal
                    break;
                }

                for (Point direction : directions) {
                    final int dirY = currPoint.y + direction.y;
                    final int dirX = currPoint.x + direction.x;
                    if (dirY < height && dirX < width && dirY >= 0 && dirX >= 0 && wantedBoard[dirX][dirY] == 1 && !visited[dirX][dirY]) { //TODO : ally walls are blockers?
                        deque.add(new Point(dirX, dirY)); // Save the point for searching here later
                        visited[dirX][dirY] = true;
                    }
                }

            }
        }

        return playersThatGoalIsReachable.size() != this.model.getPlayerCount(); //if at least one player misses his goal -> true
    }

    private void AIMove() {

    }

    private void startTimer() {
        ConcurrentLoop clockCurrentTurn = new ConcurrentLoop(this::clockPerTurn, 10, "Time Limit per Turn");
        this.secondCount = Instant.now();
        clockCurrentTurn.start();
    }

    private void newTime() {
        Player playerInTurn = this.model.getPlayers().get(model.getPlayerInTurnId());
        playerInTurn.setTimePlayed(0);
        this.secondCount = Instant.now();
    }

    private void nextTurn() {

        this.triggerActionsAfterTurn();

        this.newTime();

        this.indexCurrentIndex++;

        if (!this.model.getPlayers().containsKey(this.indexCurrentIndex)) {
            this.indexCurrentIndex = 0;
            this.model.setPlayerInTurn(0);

        } else {

            this.model.setPlayerInTurn(this.indexCurrentIndex);

            //hardcode, the IA always is the player 2
            if (this.model.getGameModeManager().getCurrentGameMode() == GameModes.NORMAL_PLAYER_IA && this.indexCurrentIndex == 1) {

            }
        }

        this.model.setTurnCount((short) (model.getTurnCount() + 1));

        this.triggerActionBeforeTurn();

    }

    private void triggerActionBeforeTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtStartTurn(this));
    }

    private void triggerActionsAfterTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtFinishTurn(this));
    }

    public short getTurnCount() {
        return this.model.getTurnCount();
    }

    private void clockPerTurn() {
        final Player playerInTurn = this.model.getPlayers().get(model.getPlayerInTurnId());
        playerInTurn.setTimePlayed((int) Duration.between(this.secondCount, Instant.now()).getSeconds());

        if (playerInTurn.getTimePlayed() >= this.model.getGameModeManager().getBaseParameters().timeLimitPerPlayer) {
            this.nextTurn();
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
        }
        System.out.println(sb);
    }

}
