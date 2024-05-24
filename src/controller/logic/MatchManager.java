package controller.logic;

import controller.cells.DobleTurnCell;
import controller.cells.ReturnCell;
import controller.cells.TeleportCell;
import controller.wall.Wall;
import model.GameModel;
import model.cell.CellType;
import model.difficulty.DifficultyType;
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
    private final HashMap<Player, AIPlayer> aiPlayers;
    private final Deque<Player> extraTurns = new ArrayDeque<>();
    private Instant secondCount;

    public MatchManager(final GameModel gameModel) {
        this.model = gameModel;
        this.walls = new HashMap<>();

        this.aiPlayers = new HashMap<>();

        this.model.getPlayers().values().stream().filter(Player::getIsAI).forEach(player -> this.aiPlayers.put(player, new AIPlayer(player, this)));

        if (this.model.getDifficulty().getDifficultyType() != DifficultyType.NORMAL) {
            this.startTimer();
        }

        //TODO : if start AI player???

        this.triggerActionBeforeTurn();
    }

    public void lookForwardMoves(final Player fromPlayer, ArrayList<Point> directions, final ArrayList<Point> possibleMovements, final Player playerLooking) {

        final Point basePoint = new Point(fromPlayer.getPosition());

        for (Point direction : new ArrayList<>(directions)) {
            Point objectivePoint = new Point(basePoint.x + direction.x, basePoint.y + direction.y);

            if (this.model.getBoard().getCellType(playerLooking.getPosition()) == CellType.TELEPORT && basePoint.equals(playerLooking.getPosition())) {
                for (Point tpDir : new TeleportCell().getTeleportPoints()) {
                    final Point tpPoint = new Point(playerLooking.getPosition().x + tpDir.x, playerLooking.getPosition().y + tpDir.y);
                    if (isValidPoint(tpPoint) && !isOccupiedPoint(tpPoint) && containsWinnerPosition(this.getIslandBFS(getAbstractBoardFor(playerLooking), tpPoint), playerLooking)) {
                        possibleMovements.add(new Point(tpPoint));
                    }
                }
            }

            if (!isValidPoint(objectivePoint) || isBlocker(fromPlayer.getPosition(), objectivePoint, playerLooking)) {
                continue;
            }

            if (isOccupiedPoint(objectivePoint)) {
                final ArrayList<Point> jumpedPlayerMoves = new ArrayList<>();

                final Point blockerPlayer = new Point(this.getPlayer(objectivePoint).getPosition());

                for (Point dir : directions) {
                    if (!isOppositeDirection(dir, direction) && !isBlocker(blockerPlayer, objectivePoint, playerLooking)) {
                        jumpedPlayerMoves.add(new Point(dir));
                    }
                }
                this.lookForwardMoves(this.getPlayer(objectivePoint), jumpedPlayerMoves, possibleMovements, playerLooking);
            } else {
                possibleMovements.add(new Point(objectivePoint));
            }
        }
    }

    private boolean isOppositeDirection(Point dir, Point playerDirection) {
        return (dir.x == 1 && playerDirection.x == -1) || (dir.x == -1 && playerDirection.x == 1) ||
                (dir.y == 1 && playerDirection.y == -1) || (dir.y == -1 && playerDirection.y == 1);
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
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return player;
            }
        }
        return null;
    }

    private boolean isValidPoint(Point point) {
        final int width = model.getBoard().getWidth();
        final int height = model.getBoard().getHeight();
        return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
    }

    private boolean isBlocker(Point playerPosition, Point objectivePoint, Player player) {
        final Wall wall = this.getPotentialBlockerWall(playerPosition, objectivePoint);
        if (wall == null) {
            return false;
        } else return !(wall.getIsAlly() && wall.getOwner().equals(player));
    }

    public boolean isOccupiedPoint(Point point) {
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }

    private Wall getPotentialBlockerWall(final Point initialPoint, final Point finalPoint) {

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

    public void movePlayer(Player player, final Point moveTo, final boolean isAPlayer) {
        player.setPosition(moveTo);
        //Win Condition
        if (player.getXWinPosition() == player.getPosition().x || player.getYWinPosition() == player.getPosition().y) {
            this.model.setMatchState(GameModel.MatchState.WINNER);
            this.model.setWinningPlayer(player);
        }
        if (model.getBoard().getCellType(player.getPosition()) == CellType.DOUBLE_TURN) {
            new DobleTurnCell().action(this);
        }
        if (model.getBoard().getCellType(player.getPosition()) == CellType.RETURN) {
            new ReturnCell().action(this);
        }
        if (isAPlayer) {
            this.nextTurn();
        }
    }

    public void executeMove(Player player, Point moveTo) {
        this.movePlayer(player, moveTo, true);
    }

    public void setPlayerPosition(Player player, Point moveTo) {
        this.movePlayer(player, moveTo, false);
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
        player.subtractWall(wall.getWallType());
        wall.setCreationTurn(this.model.getTurnCount());
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

                if (boardWallX < 0 || boardWallY < 0 || boardWallX >= this.model.getBoard().getWidth() * 2 - 1 || boardWallY >= this.model.getBoard().getHeight() * 2 - 1) {
                    continue;
                }

                final WallData wallDataPosition = this.model.getBoard().getBoardWalls()[boardWallX][boardWallY];

                if (wallDataPosition == wall.getWallData()) {
                    model.getBoard().getBoardWalls()[boardWallX][boardWallY] = null;
                }
            }
        }

        wall.getOwner().removeWallPlaced(wall.getWallData());
        wall.setOwner(null);
        return this.walls.remove(wall.getWallId());
    }

    private int[][] getAbstractBoardFor(Player player) {
        final int height = this.model.getBoard().getHeight() * 2 - 1;
        final int width = this.model.getBoard().getWidth() * 2 - 1;

        final int[][] abstactBoard = new int[width][height];

        for (int x = 0; x < width; x++) { //Abstract the values for the algorithm
            for (int y = 0; y < height; y++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    abstactBoard[x][y] = 0;
                } else if (this.model.getBoard().getWallData(x, y) == null || this.model.getBoard().getWallData(x, y).getIsAlly() && this.model.getBoard().getWallData(x, y).getOwner().equals(player)) {
                    abstactBoard[x][y] = 1;
                } else {
                    abstactBoard[x][y] = 0;
                }
            }
        }
        return abstactBoard;
    }

    public boolean isABlockerWallFor(final ArrayList<Point> newWalls, final Player player) {

        final int[][] wantedBoard = getAbstractBoardFor(player);

        for (Point point : newWalls) { //Add the new wall
            wantedBoard[point.x][point.y] = 0;
        }

        return isGoalReachable(wantedBoard, new ArrayList<>(this.model.getPlayers().values()));
    }

    public boolean isGoalReachable(final int[][] wantedBoard, final ArrayList<Player> players) {

        final HashSet<Player> playersThatGoalIsReachable = new HashSet<>();
        ArrayList<Point> island;

        for (Player player : this.model.getPlayers().values()) {

            island = getIslandBFS(wantedBoard, player.getPosition());

            if (containsWinnerPosition(island, player)) {
                playersThatGoalIsReachable.add(player);
            }
        }

        return playersThatGoalIsReachable.size() != players.size(); // if all players can reach their goal -> true
    }

    private boolean containsWinnerPosition(final ArrayList<Point> points, final Player searchFrom) {
        for (Point point : points) {
            if ((searchFrom.getYWinPosition() != -1 && point.y == searchFrom.getYWinPosition() * 2) ||
                    (searchFrom.getXWinPosition() != -1 && point.x == searchFrom.getXWinPosition() * 2)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Point> getIslandBFS(final int[][] board, final Point initialPoint) {
        final int x = initialPoint.x * 2;
        final int y = initialPoint.y * 2;

        final int height = board[0].length;
        final int width = board.length;

        final ArrayList<Point> island = new ArrayList<>();

        final Point[] directions = {new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0)};

        final boolean[][] visited = new boolean[width][height];

        //bfs
        visited[x][y] = true;

        ArrayDeque<Point> deque = new ArrayDeque<>();

        deque.add(new Point(x, y));

        while (!deque.isEmpty()) {
            final Point currPoint = new Point(deque.pop());
            island.add(currPoint);

            for (Point direction : directions) {
                final int dirY = currPoint.y + direction.y;
                final int dirX = currPoint.x + direction.x;
                if (dirY < height && dirX < width && dirY >= 0 && dirX >= 0 && board[dirX][dirY] == 1 && !visited[dirX][dirY]) { //TODO : ally walls are blockers?
                    deque.add(new Point(dirX, dirY)); // Save the point for searching here later
                    visited[dirX][dirY] = true;
                }
            }
        }

        return island;
    }


    private void nextTurn() {

        if (!this.extraTurns.isEmpty()) {
            this.extraTurns.poll();
            this.model.setTurnCount(model.getTurnCount() + 1);
            return;
        }

        this.triggerActionsAfterTurn();

        if (this.model.getDifficulty().getDifficultyType() != DifficultyType.NORMAL) {
            this.newTime();
        }

        int indexNextTurn = this.model.getPlayerInTurnId() + 1;

        if (!this.model.getPlayers().containsKey(indexNextTurn)) {
            this.model.setPlayerInTurn(0);

        } else {
            this.model.setPlayerInTurn(indexNextTurn);
        }

        if (!this.aiPlayers.isEmpty() && this.aiPlayers.containsKey(this.getPlayerInTurn())) {
            this.aiPlayers.get(this.getPlayerInTurn()).executeMove(this.getAbstractBoardFor(this.getPlayerInTurn()));
        }

        this.model.setTurnCount(model.getTurnCount() + 1);

        this.triggerActionBeforeTurn();

    }

    public void addATurn(Player player) {
        extraTurns.add(player);
    }

    public void returnCell(Player player) {
        final ArrayDeque<Point> points = new ArrayDeque<>(player.getMoveBuffer());

        final Point point = points.getFirst();
        if (this.isValidPoint(point) && !isOccupiedPoint(point) && this.containsWinnerPosition(this.getIslandBFS(getAbstractBoardFor(player), point), player)) {
            this.setPlayerPosition(player, point);
        }
    }

    private void startTimer() {
        ConcurrentLoop clockCurrentTurn = new ConcurrentLoop(this::clockPerTurn, 10, "Time Limit per Turn");
        this.secondCount = Instant.now();
        clockCurrentTurn.start();
    }

    private void newTime() {
        Player playerInTurn = getPlayerInTurn();
        playerInTurn.setTimePlayed(0);
        this.secondCount = Instant.now();
    }

    private void triggerActionBeforeTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtStartTurn(this));
    }

    private void triggerActionsAfterTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtFinishTurn(this));
    }

    public Player getPlayerInTurn() {
        return this.model.getPlayers().get(this.model.getPlayerInTurnId());
    }

    public int getTurnCount() {
        return this.model.getTurnCount();
    }

    private void clockPerTurn() {
        final Player playerInTurn = this.getPlayerInTurn();
        playerInTurn.setTimePlayed((int) Duration.between(this.secondCount, Instant.now()).getSeconds());

        if (playerInTurn.getTimePlayed() >= this.model.getDifficulty().getTimePerTurn()) {
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
