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
import util.Logger;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class MatchManager {
    private final GameModel model;
    private final HashMap<UUID, Wall> walls;
    private final HashMap<Player, AIPlayer> aiPlayers;
    private final Deque<Player> extraTurns = new ArrayDeque<>();
    private final Chronometer chronometer;

    /**
     * Manages the state and logic of the game match.
     *
     * @param gameModel The game model that match manager will manage.
     */
    public MatchManager(final GameModel gameModel) {
        this.model = gameModel;
        this.walls = new HashMap<>();
        this.chronometer = new Chronometer(this, this.model.getDifficulty());

        this.aiPlayers = new HashMap<>();

        if (this.model.getDifficulty().getDifficultyType() == DifficultyType.AGAINST_THE_CLOCK) {
            this.chronometer.setFunctionToExecute(this::nextTurn);
            this.chronometer.startTimer();
        } else if (this.model.getDifficulty().getDifficultyType() == DifficultyType.TIMED) {
            this.chronometer.setFunctionToExecute(this::killPlayerInTurn);
            this.chronometer.startTimer();
        }

        this.triggerActionBeforeTurn();
    }

    private void killPlayerInTurn() {
        this.getPlayerInTurn().kill();
        this.nextTurn();
    }

    /**
     * Determines the possible movements from a player considering directions and occupation, and saves them in possibleMovements.
     *
     * @param fromPlayer        the player to move from
     * @param directions        the directions to consider
     * @param possibleMovements the list to store possible movements
     * @param playerLooking     the player looking for possible moves
     */
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

    /**
     * Checks if two directions are opposite to each other, is for avoided the possible infinite loop in lookForwardMoves.
     *
     * @param dir             the first direction
     * @param playerDirection the second direction
     * @return true if the directions are opposite, false otherwise
     */
    private boolean isOppositeDirection(Point dir, Point playerDirection) {
        return (dir.x == 1 && playerDirection.x == -1) || (dir.x == -1 && playerDirection.x == 1) ||
                (dir.y == 1 && playerDirection.y == -1) || (dir.y == -1 && playerDirection.y == 1);
    }

    /**
     * Gets the possible movements for a player.
     *
     * @param player the player to get movements for
     * @return a list of possible movements
     */
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

    /**
     * Gets the player in a given point.
     *
     * @param point the point to get the player from
     * @return the player in the point, or null if there is no player
     */
    private Player getPlayer(Point point) {
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return player;
            }
        }
        return null;
    }

    /**
     * Checks if a point is valid within the board boundaries.
     *
     * @param point the point to check
     * @return true if the point is valid, false otherwise
     */
    private boolean isValidPoint(Point point) {
        final int width = model.getBoard().getWidth();
        final int height = model.getBoard().getHeight();
        return point.x >= 0 && point.x < width && point.y >= 0 && point.y < height;
    }

    /**
     * Checks if a wall blocks a path between two points.
     *
     * @param playerPosition the current player position
     * @param objectivePoint the point to move to
     * @param player         the player to check for
     * @return true if the point is blocked, false otherwise
     */
    private boolean isBlocker(Point playerPosition, Point objectivePoint, Player player) {
        final Wall wall = this.getWallBetween(playerPosition, objectivePoint);
        if (wall == null) {
            return false;
        } else return !(wall.getIsAlly() && wall.getOwner().equals(player));
    }

    /**
     * Checks if a point is occupied by another player.
     *
     * @param point the point to check
     * @return true if the point is occupied, false otherwise
     */
    public boolean isOccupiedPoint(Point point) {
        for (Player player : this.model.getPlayers().values()) {
            if (player.getPosition().equals(point)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the wall that blocks a movement.
     *
     * @param initialPoint the initial point of the movement
     * @param finalPoint   the final point of the movement
     * @return the wall that blocks the movement or null if no wall is present
     */
    private Wall getWallBetween(final Point initialPoint, final Point finalPoint) {

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

    /**
     * Moves a player to a new position and checks for special cells.
     *
     * @param player    the player to move
     * @param moveTo    the new position
     * @param isAPlayer flag indicating if the mover is a player
     */
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

    /**
     * Moves a player to a new position and advances the turn.
     *
     * @param player the player to move
     * @param moveTo the new position
     */
    public void movePlayerAdvancingTurn(Player player, Point moveTo) {
        this.movePlayer(player, moveTo, true);
    }

    /**
     * Moves a player to a new position without advancing the turn.
     *
     * @param player the player to move
     * @param moveTo the new position
     */
    public void movePlayerNotAdvancingTurn(Player player, Point moveTo) {
        this.movePlayer(player, moveTo, false);
    }

    /**
     * Places a wall on the board.
     *
     * @param player   the player placing the wall
     * @param wall     the wall to place
     * @param newWalls the point to place the wall
     */
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

    /**
     * Deletes a wall from the board.
     *
     * @param wallId the id of the wall to delete
     * @return the deleted wall
     */
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

    /**
     * Gets the abstract board full of 1s and 0s for the algorithm.
     * 1's for valid position, 0's for invalid position.
     *
     * @param player the player to get the board for
     * @return the abstract board
     */
    private int[][] getAbstractBoardFor(Player player) {
        final int height = this.model.getBoard().getHeight() * 2 - 1;
        final int width = this.model.getBoard().getWidth() * 2 - 1;

        final int[][] abstractBoard = new int[width][height];

        for (int x = 0; x < width; x++) { //Abstract the values for the algorithm
            for (int y = 0; y < height; y++) {
                if (x % 2 != 0 && y % 2 != 0) {
                    abstractBoard[x][y] = 0;
                } else if (!this.blockCondition(x, y, player)) {
                    abstractBoard[x][y] = 1;
                } else {
                    abstractBoard[x][y] = 0;
                }
            }
        }
        return abstractBoard;
    }

    private boolean blockCondition(final int x, final int y, final Player player) {
        return ((this.model.getBoard().getWallData(x, y) != null && (!this.model.getBoard().getWallData(x, y).getIsAlly() || !this.model.getBoard().getWallData(x, y).getOwner().equals(player))));
    }

    /**
     * Checks if a goal is reachable for all players in the ArrayList.
     *
     * @param newWalls the new walls to check for
     * @param players  the players to check for
     * @return true if all goals are reachable, false otherwise
     */
    public boolean isGoalReachable(final ArrayList<Player> players, final ArrayList<Point> newWalls) {

        final HashSet<Player> playersThatGoalIsReachable = new HashSet<>();
        ArrayList<Point> island;

        for (Player player : this.model.getPlayers().values()) {

            int[][] wantedBoard = this.getAbstractBoardFor(player);

            for (Point point : newWalls) { //Add the new wall
                wantedBoard[point.x][point.y] = 0;
            }

            island = getIslandBFS(wantedBoard, player.getPosition());

            if (containsWinnerPosition(island, player)) {
                playersThatGoalIsReachable.add(player);
            }
        }
        return playersThatGoalIsReachable.size() != players.size(); // if all players can reach their goal -> true
    }

    /**
     * Checks if an ArrayList of points contains a winner position for a player.
     *
     * @param points     the points to check
     * @param searchFrom the player to check for
     * @return true if the points contain a winner position, false otherwise
     */
    public boolean containsWinnerPosition(final ArrayList<Point> points, final Player searchFrom) {
        for (Point point : points) {
            if ((searchFrom.getYWinPosition() != -1 && point.y == searchFrom.getYWinPosition() * 2) ||
                    (searchFrom.getXWinPosition() != -1 && point.x == searchFrom.getXWinPosition() * 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the island from a point using a breadth-first search, as a list of points.
     *
     * @param board        the board to search
     * @param initialPoint the initial point to search from
     * @return the island as a list of points
     */
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

    /**
     * Executes the next turn in the game.
     * If there are extra turns, it will execute them first.
     * If the difficulty is not normal, it will start the timer.
     * It will trigger the actions before and after the turn.
     */
    private void nextTurn() {
        Player playerInTurn = this.getPlayerInTurn();

        if (!this.extraTurns.isEmpty()) {
            this.extraTurns.poll();
            this.model.setTurnCount(model.getTurnCount() + 1);

            this.checkForAITurn();
            return;
        }

        this.triggerActionsAfterTurn();

        if (this.model.getDifficulty().getDifficultyType() == DifficultyType.AGAINST_THE_CLOCK) {
            playerInTurn.setTimePlayed(0);
        }

        int indexNextTurn = this.calcNextTurn();
        if (indexNextTurn == -1) {
            return;
        }
        this.model.setPlayerInTurn(indexNextTurn);

        this.checkForAITurn();

        this.model.setTurnCount(model.getTurnCount() + 1);

        this.triggerActionBeforeTurn();

    }

    private int calcNextTurn() {
        //find the next player alive
        int nextTurn = this.model.getPlayerInTurnId();
        for (int i = 0; i < this.model.getPlayers().size(); i++) {
            nextTurn++;
            if (!this.model.getPlayers().containsKey(nextTurn)) {
                nextTurn = 0;
            }
            if (this.model.getPlayers().get(nextTurn).isAlive()) {
                return nextTurn;
            }
        }
        return -1;
    }

    /**
     * Checks if it is the turn for an AI player and executes it.
     */
    private void checkForAITurn() {
        if (!this.aiPlayers.isEmpty() && this.aiPlayers.containsKey(this.getPlayerInTurn())) {
            this.aiPlayers.get(this.getPlayerInTurn()).executeMove(this.getAbstractBoardFor(this.getPlayerInTurn()));
        }
    }

    /**
     * Adds an extra turn for a player.
     *
     * @param player the player to add the turn for
     */
    public void addATurn(Player player) {
        extraTurns.add(player);
    }

    /**
     * Return player two turns if is possible.
     *
     * @param player the player to return
     */
    public void returnCell(Player player) {
        final ArrayDeque<Point> points = new ArrayDeque<>(player.getMoveBuffer());

        final Point point = points.getFirst();
        if (this.isValidPoint(point) && !isOccupiedPoint(point) && this.containsWinnerPosition(this.getIslandBFS(getAbstractBoardFor(player), point), player)) {
            this.movePlayerNotAdvancingTurn(player, point);
        }
    }

    /**
     * Triggers the actions before and after the turn.
     */
    private void triggerActionBeforeTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtStartTurn(this));
    }

    /**
     * Triggers the actions after the turn.
     */
    private void triggerActionsAfterTurn() {
        final HashMap<UUID, Wall> wallHashMap = new HashMap<>(this.walls);
        wallHashMap.values().forEach(wall -> wall.actionAtFinishTurn(this));
    }

    /**
     * Gets the player in turn.
     *
     * @return the player in turn
     */
    public Player getPlayerInTurn() {
        return this.model.getPlayers().get(this.model.getPlayerInTurnId());
    }

    public int getTurnCount() {
        return this.model.getTurnCount();
    }

    public int getPlayerInTurnId() {
        return this.model.getPlayerInTurnId();
    }

    public void addAIPlayer(AIPlayer aiPlayer) {
        if (this.aiPlayers.containsKey(aiPlayer.getPlayer()) || this.aiPlayers.size() >= this.model.getPlayerCount()) {
            return;
        }
        this.aiPlayers.put(aiPlayer.getPlayer(), aiPlayer);
        this.checkForAITurn();
    }

    public void printMatrix(int[][] matrix) {
        Logger.info(" ________matrix________ ");
        if (matrix == null) {
            Logger.warning("Null");
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
        Logger.info(sb.toString());
    }

    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(this.model.getPlayers().values());
    }
}
