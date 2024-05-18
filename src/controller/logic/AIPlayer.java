package controller.logic;

import model.player.AIProfile;
import model.player.Player;

import java.awt.*;
import java.util.ArrayList;

public class AIPlayer {

    private final Player aiPlayer;
    private final MatchManager matchManager;
    private final AIProfile profile;
    private int[][] abstractBoard;
    public AIPlayer(Player player, MatchManager matchManager){
        this.aiPlayer = player;
        this.profile = player.getAiProfile();
        this.matchManager = matchManager;
    }

    public void executeMove(final int[][] abstractBoard){
        this.abstractBoard = abstractBoard;
        switch (profile){
            case BEGINNER ->beginnerMove();
        }
    }

    private void beginnerMove(){
        ArrayList<Point> possibleMoves = this.matchManager.getPossibleMovements(this.aiPlayer);
        this.matchManager.executeMove(this.aiPlayer, possibleMoves.get(0));
    }
}
