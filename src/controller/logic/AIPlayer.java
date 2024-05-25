package controller.logic;

import controller.GameController;
import model.player.AIProfile;
import model.player.Player;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer {

    private final Player player;
    private final MatchManager matchManager;
    private final GameController gameController;
    private final AIProfile profile;
    private int[][] abstractBoard;

    public AIPlayer(Player player, MatchManager matchManager, GameController gameController) {
        this.player = player;
        this.profile = player.getAiProfile();
        this.matchManager = matchManager;
        this.gameController = gameController;
    }

    public void executeMove(final int[][] abstractBoard) {
        this.abstractBoard = abstractBoard;
        this.beginnerMove();
    }

    /**
     * This method move the player or place a wall, with a 50% chance of each, his movement is random, but principally he moves to his goal, walls are placed randomly
     */
    private void beginnerMove() {
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
