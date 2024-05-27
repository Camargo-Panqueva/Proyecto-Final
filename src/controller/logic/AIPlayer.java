package controller.logic;

import controller.GameController;
import model.player.AIProfile;
import model.player.Player;
import model.wall.WallType;
import util.Timeout;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer {

    private final Player player;
    private final MatchManager matchManager;
    private final GameController gameController;
    private final AIProfile profile;
    private int[][] abstractBoard;

    /**
     * Constructor of the AIPlayer
     *
     * @param player         Player to be controlled by the AI
     * @param matchManager   MatchManager to control the match
     * @param gameController GameController to control the game
     */
    public AIPlayer(Player player, MatchManager matchManager, GameController gameController) {
        this.player = player;
        this.profile = player.getAiProfile();
        this.matchManager = matchManager;
        this.gameController = gameController;
    }

    /**
     * This method is called by the GameController to execute the turn of the AI.
     *
     * @param abstractBoard The abstract board of the game
     */
    public void executeMove(final int[][] abstractBoard) {
        this.abstractBoard = abstractBoard;

        if (this.profile == AIProfile.BEGINNER) {
            Timeout.startTimeout(this::beginnerTurn, 500);
        } else if (this.profile == AIProfile.INTERMEDIATE) {
            Timeout.startTimeout(this::intermediateTurn, 500);
        }
    }

    /**
     * This method moves the player or places a wall, with a 50% chance of each, his movement is random, but principally he moves to his goal, walls are placed randomly
     */
    private void beginnerTurn() {
        ArrayList<Point> possibleMoves = this.matchManager.getPossibleMovements(this.player);
        Random random = new Random();
        Point winDirection = this.player.getWinDirection();

        if (random.nextBoolean()) {
            for (int att = 0; att < 100; att++) { // Try to place a wall 100 times

                int xRandom = random.nextInt(this.abstractBoard.length);
                int yRandom = random.nextInt(this.abstractBoard[0].length);
                WallType randomWallType = getRandomWallType();
                if (randomWallType == null) {
                    break;
                }
                if (this.gameController.placeWall(matchManager.getPlayerInTurnId(), new Point(xRandom, yRandom), getRandomWallType()).ok) {
                    return;
                }
            }
        }

        final Point preferredMove = new Point(player.getPosition().x + winDirection.x, player.getPosition().y + winDirection.y);
        if (possibleMoves.contains(preferredMove)) {
            this.matchManager.movePlayerAdvancingTurn(this.player, preferredMove);
        } else {
            this.matchManager.movePlayerAdvancingTurn(this.player, possibleMoves.get(random.nextInt(possibleMoves.size())));
        }
    }

    private void intermediateTurn() {
        int minPathSize = Integer.MAX_VALUE;
        Player bestPlayer = null;

        for (Player player1 : this.matchManager.getPlayers()) {
            ArrayDeque<Point> path = this.getShortestPath(player1);
            if (path != null && path.size() <= minPathSize) {
                minPathSize = path.size();
                bestPlayer = player1;
            }
        }

        if (((bestPlayer == null || !bestPlayer.equals(this.player)) && player.getRemainingWallsCount() != 0)) {
            double PROBABILITY_FOR_MOVE = 0.30;
            if (Math.random() < PROBABILITY_FOR_MOVE) {
                this.intermediateMovement();
                return;
            }
            ArrayDeque<Point> bestPath = this.getShortestPath(bestPlayer);
            for (int i = 0; i < bestPath.size(); i++) {
                Point point = bestPath.pollLast();
                if (this.gameController.placeWall(matchManager.getPlayerInTurnId(), point, this.getRandomWallType()).ok) {
                    return;
                }
            }
        }

        this.intermediateMovement();
    }

    private void intermediateMovement() {
        final ArrayList<Point> possibleMoves = this.matchManager.getPossibleMovements(this.player);
        ArrayDeque<Point> path = new ArrayDeque<>();

        for (Point point : this.getShortestPath(this.player)) {
            if (point.x % 2 == 0 && point.y % 2 == 0) {
                path.add(new Point(point.x / 2, point.y / 2));
            }
        }

        for (Point point : path) {
            for (Point point1 : possibleMoves) {
                if (point.equals(new Point(point1.x, point1.y))) { //Exists any possible move point in the shortest path?
                    this.gameController.processPlayerMove(matchManager.getPlayerInTurnId(), point);
                    return;
                }
            }
        }

        Point bestDirection = this.player.getWinDirection();
        Point bestMove = new Point(0, 0);
        for (Point point : possibleMoves) {
            if (Math.abs(point.x * bestDirection.x) >= Math.abs(bestMove.x * bestDirection.x) ||
                    Math.abs(point.y * bestDirection.y) >= Math.abs(bestMove.y * bestDirection.y)) { //Else move in the direction of the win
                bestMove = point;
            }
        }
        this.gameController.processPlayerMove(matchManager.getPlayerInTurnId(), bestMove);
    }

    private ArrayDeque<Point> findPathBFS(final int[][] abstractBoard, final Player player, final ArrayList<Point> island) {

        final int width = abstractBoard.length;
        final int height = abstractBoard[0].length;
        final boolean[][] visited = new boolean[width][height];

        final ArrayList<Point> directions = this.favorableDirections(player);

        final ArrayDeque<Point> deque = new ArrayDeque<>();

        final Point playerPosition = new Point(player.getPosition().x * 2, player.getPosition().y * 2);

        deque.add(playerPosition);

        final Tree tree = new Tree(new Point(playerPosition));
        ArrayDeque<Point> path = new ArrayDeque<>();

        while (!deque.isEmpty()) {
            final Point currPoint = new Point(deque.pop());

            island.add(currPoint);

            ArrayList<Point> possibleWinPoint = new ArrayList<>();
            possibleWinPoint.add(new Point(currPoint));


            for (Point direction : directions) {
                final int dirY = currPoint.y + direction.y;
                final int dirX = currPoint.x + direction.x;
                if (dirY < height && dirX < width && dirY >= 0 && dirX >= 0 && abstractBoard[dirX][dirY] == 1 && !visited[dirX][dirY]) { //TODO : ally walls are blockers?
                    deque.add(new Point(dirX, dirY)); // Save the point for searching here later
                    visited[dirX][dirY] = true;

                    tree.addChild(new Point(currPoint), new Point(dirX, dirY));
                }
            }

            if (this.matchManager.containsWinnerPosition(possibleWinPoint, player)) {
                tree.getAllParents(new Point(currPoint)).forEach(tree1 -> path.add(tree1.data));
                return path;
            }
        }
        return new ArrayDeque<>();
    }

    private ArrayList<Point> favorableDirections(Player player) {
        ArrayList<Point> directions = new ArrayList<>();
        Point winDirection = player.getWinDirection();
        directions.add(winDirection);
        directions.add(new Point(winDirection.y, winDirection.x));
        directions.add(new Point(-winDirection.y, -winDirection.x));
        directions.add(new Point(-winDirection.x, -winDirection.y));
        return directions;

    }

    private ArrayDeque<Point> getShortestPath(Player player) {
        final ArrayList<Point> island = new ArrayList<>();
        return findPathBFS(this.abstractBoard, player, island);
    }


    /**
     * This method returns a random wall type from the wall that the player has
     *
     * @return WallType
     */
    private WallType getRandomWallType() {
        Random random = new Random();
        ArrayList<WallType> wallTypes = new ArrayList<>();
        this.player.getPlayerWalls().forEach((wallType, integer) -> {
            if (integer > 0) {
                wallTypes.add(wallType);
            }
        });

        if (wallTypes.isEmpty()) {
            return null;
        }

        return wallTypes.get(random.nextInt(wallTypes.size()));
    }

    public Player getPlayer() {
        return this.player;
    }
}
