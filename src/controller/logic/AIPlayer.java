package controller.logic;

import model.player.AIProfile;
import model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class AIPlayer {

    private final Player aiPlayer;
    private final MatchManager matchManager;
    private final AIProfile profile;
    private int[][] abstractBoard;

    public AIPlayer(Player player, MatchManager matchManager) {
        this.aiPlayer = player;
        this.profile = player.getAiProfile();
        this.matchManager = matchManager;
    }

    public void executeMove(final int[][] abstractBoard) {
        this.abstractBoard = abstractBoard;
        this.beginnerMove();
    }

    /** This method move the player or place a wall, with a 50% chance of each, his movement is random, but principally he moves to his goal, walls are placed randomly */
    private void beginnerMove() {
        ArrayList<Point> possibleMoves = this.matchManager.getPossibleMovements(this.aiPlayer);
        Random random = new Random();
        Point winDirection = this.aiPlayer.getWinDirection();
        if (random.nextBoolean()){
            final Point preferredMove = new Point(aiPlayer.getPosition().x + winDirection.x, aiPlayer.getPosition().y + winDirection.y);
            if(possibleMoves.contains(preferredMove)){
                this.matchManager.movePlayerAdvancingTurn(this.aiPlayer, preferredMove);
            } else {
                this.matchManager.movePlayerAdvancingTurn(this.aiPlayer, possibleMoves.get(random.nextInt(possibleMoves.size())));
            }
        }
        else {

        }

    }
}
