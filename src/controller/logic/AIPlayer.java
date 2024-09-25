package controller.logic;

import controller.GameController;
import controller.wall.Wall;
import model.player.AIProfile;
import model.player.Player;
import model.wall.WallType;
import util.Logger;
import util.Timeout;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
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

        final int TIME_FOR_MOVE = 150;

        if (this.profile == AIProfile.BEGINNER) {
            Timeout.startTimeout(this::beginnerTurn, TIME_FOR_MOVE);
        } else if (this.profile == AIProfile.INTERMEDIATE) {
            Timeout.startTimeout(this::intermediateTurn, TIME_FOR_MOVE);
        } else if (this.profile == AIProfile.ADVANCED) {
            Timeout.startTimeout(this::advanceTurn, TIME_FOR_MOVE);
        } else {
            Logger.error("Invalid Profile for AIPlayer: " + this.profile.toString());

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

    private Player getOpponentWithBestPath() {
        int minPathSize = Integer.MAX_VALUE;
        Player bestPlayer = null;

        for (Player player1 : this.matchManager.getPlayers()) {
            ArrayDeque<Point> path = this.getShortestPath(player1, this.abstractBoard);
            if (path != null && path.size() < minPathSize) {
                minPathSize = path.size();
                bestPlayer = player1;
            }
        }
        return bestPlayer;
    }


    private void advanceTurn() {

        Player bestPlayer = getOpponentWithBestPath();

        int selfDistance = this.getShortestPath(this.player, this.abstractBoard).size();
        int opponentDistance = this.getShortestPath(bestPlayer, this.abstractBoard).size();
        double PROBABILITY_FOR_MOVE = Math.min(((double) opponentDistance) / (selfDistance + opponentDistance), 1);
        System.out.println(PROBABILITY_FOR_MOVE);
        if (Math.random() < 0.5) {
            this.advancedPlayerMovement();
            return;
        }
        if (((bestPlayer == null || !bestPlayer.equals(this.player)) && player.getRemainingWallsCount() != 0)) {

            Wall bestWall = null;

            int bestScore = Integer.MIN_VALUE;

            ArrayDeque<Point> bestPath = this.getShortestPath(bestPlayer, this.abstractBoard);
            for (Point pathPoint : bestPath) {

                WallType wallTypeRandom = getRandomWallType();

                if (pathPoint.x % 2 == 0 && pathPoint.y % 2 == 0) {
                    continue;
                }
                if (!this.gameController.canPlaceWall(this.matchManager.getPlayerInTurnId(), pathPoint, wallTypeRandom).ok) {
                    continue; //TODO : check all wall types
                }

                Wall testWall = this.gameController.createNewWall(pathPoint, wallTypeRandom);
                ArrayList<Point> newPoints = new ArrayList<>();

                for (int x = 0; x < testWall.getWallShape().length; x++) {
                    for (int y = 0; y < testWall.getWallShape()[0].length; y++) {
                        if (testWall.getWallShape()[x][y] != null) {
                            newPoints.add(new Point(pathPoint.x + x, pathPoint.y + y));
                        }
                    }
                }

                int[][] abstractBoardCopy = new int[this.abstractBoard.length][this.abstractBoard[0].length];
                for (int x = 0; x < this.abstractBoard.length; x++) {
                    System.arraycopy(this.abstractBoard[x], 0, abstractBoardCopy[x], 0, this.abstractBoard[0].length);
                }

                this.matchManager.placeWallsOnABoard(abstractBoardCopy, newPoints);

                ArrayDeque<Point> longestPathTest = this.getShortestPath(bestPlayer, abstractBoardCopy);

                ArrayDeque<Point> aiPath = this.getShortestPath(this.player, abstractBoardCopy);

                int score = longestPathTest.size() - aiPath.size();

                if (score >= bestScore) {
                    bestScore = score;
                    bestWall = testWall;
                }
            }

            if (bestWall == null) { // move player when
                this.advancedPlayerMovement();
                return;
            }

            System.out.println(this.gameController.placeWall(matchManager.getPlayerInTurnId(), bestWall.getPositionOnBoard(), bestWall.getWallType()).message);

        }

        this.advancedPlayerMovement();
    }

    private int boardScore(final int[][] board, final Player playerPOV) {

        final int selfDistance = this.getShortestPath(playerPOV, board).size();

        int boardScore = 0;

        final HashMap<Player, Integer> opponentsScore = this.calculateOpponentsScore(board, selfDistance, playerPOV);

        for (Player opponent : this.matchManager.getPlayers()) {
            if (opponent.equals(playerPOV)){continue;}

            final int opponentScore = opponentsScore.get(opponent);
            if (opponentScore > 0) {
                boardScore += opponentScore;
            }
            else if (opponentScore < 0) {
                boardScore += opponentScore;
            }
        }
        return boardScore;
    }

    private HashMap<Player, Integer> calculateOpponentsScore(final int[][] board, final int selfDistance, final Player playerPOV) {
        final HashMap<Player, Integer> opponentsScore = new HashMap<Player, Integer>();
        for (Player opponent : this.matchManager.getPlayers()) {
            if (!opponent.equals(playerPOV)) {
                opponentsScore.put(opponent, this.getShortestPath(opponent, board).size() - selfDistance);
            }
        }
        return opponentsScore;
    }


    private void advancedPlayerMovement() {
        Point bestMove = new Point(0, 0);
        int bestPathSize = Integer.MAX_VALUE;

        Player playerCopy = new Player(player);

        for (Point possiblePoint : this.matchManager.getPossibleMovements(this.player)) {
            playerCopy.setPosition(possiblePoint);
            ArrayDeque<Point> path = this.getShortestPath(playerCopy, this.abstractBoard);

            if (path.size() < bestPathSize) {
                bestPathSize = path.size();
                bestMove = possiblePoint;
            }
        }
        System.out.println(this.gameController.processPlayerMove(matchManager.getPlayerInTurnId(), bestMove).message);
    }

    private void intermediateTurn() {
        Player bestPlayer = this.getOpponentWithBestPath();

        System.out.println(this.boardScore(this.abstractBoard, this.player));

        final boolean shouldMove = (bestPlayer == null || !bestPlayer.equals(this.player)) && player.getRemainingWallsCount() > 0;
        final double MOVE_PROBABILITY = 0.30;

        if (shouldMove) {
            if (Math.random() < MOVE_PROBABILITY) {
                this.intermediatePlayerMovement();
                return;
            }
            ArrayDeque<Point> bestPath = this.getShortestPath(bestPlayer, this.abstractBoard);
            while (!bestPath.isEmpty()) {
                Point point = bestPath.pollLast();
                if (this.gameController.placeWall(matchManager.getPlayerInTurnId(), point, this.getRandomWallType()).ok) {
                    return;
                }
            }
        }

        this.intermediatePlayerMovement();
    }


    private void intermediatePlayerMovement() {
        //TODO: Find path based in possibles movements not the other way around.
        final ArrayList<Point> possibleInitialMoves = this.matchManager.getPossibleMovements(this.player);
        ArrayDeque<Point> path = new ArrayDeque<>();

        for (Point point : this.getShortestPath(this.player, this.abstractBoard)) {
            if (point.x % 2 == 0 && point.y % 2 == 0) {
                path.add(new Point(point.x / 2, point.y / 2));
            }
        }

        for (Point pathPoint : path) {
            for (Point initialPoint : possibleInitialMoves) {
                if (pathPoint.equals(new Point(initialPoint.x, initialPoint.y))) { //Exists any possible move point in the shortest path?
                    this.gameController.processPlayerMove(matchManager.getPlayerInTurnId(), pathPoint);
                    return;
                }
            }
        }

        Point bestDirection = this.player.getWinDirection();
        Point bestMove = new Point(0, 0);
        for (Point point : possibleInitialMoves) {
            if (Math.abs(point.x * bestDirection.x) >= Math.abs(bestMove.x * bestDirection.x) ||
                    Math.abs(point.y * bestDirection.y) >= Math.abs(bestMove.y * bestDirection.y)) { //Else move in the direction of the win
                bestMove = point;
            }
        }
        this.gameController.processPlayerMove(matchManager.getPlayerInTurnId(), bestMove);
    }

    private ArrayDeque<Point> findPathBFS(final int[][] abstractBoard, final Player player) {

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

            if (this.matchManager.containsWinPosition(possibleWinPoint, player)) {
                tree.getAllParents(new Point(currPoint)).forEach(tree1 -> path.add(tree1.data));
                return path;
            }
        }
        return new ArrayDeque<>();
    }

    private ArrayList<Point> favorableDirections(Player player) {
        ArrayList<Point> directions = new ArrayList<>(4);
        Point winDirection = player.getWinDirection();
        directions.add(winDirection);
        directions.add(new Point(winDirection.y, winDirection.x));
        directions.add(new Point(-winDirection.y, -winDirection.x));
        directions.add(new Point(-winDirection.x, -winDirection.y));
        return directions;

    }

    private ArrayDeque<Point> getShortestPath(Player player, int[][] abstractBoard) {
        return findPathBFS(abstractBoard, player);
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
