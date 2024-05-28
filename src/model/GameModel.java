package model;

import model.board.Board;
import model.difficulty.Difficulty;
import model.modes.GameBaseParameters;
import model.player.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;


public final class GameModel implements Serializable {

    /**
     * The players in the game.
     */
    private final HashMap<Integer, Player> players;
    /**
     * The base parameters of the game.
     */
    private final GameBaseParameters gameBaseParameters;
    /**
     * The difficulty of the game.
     */
    private final Difficulty difficulty;
    /**
     * The board of the game.
     */
    private Board board;
    /**
     * The current state of the match.
     */
    private MatchState matchState;
    /**
     * The ID of the player in turn.
     */
    private int playerInTurn;
    /**
     * The player that won the game.
     */
    private Player winningPlayer;
    /**
     * The turn count of the game.
     */
    private int turnCount;

    public GameModel() {
        this.gameBaseParameters = new GameBaseParameters();
        this.difficulty = new Difficulty();
        this.players = new HashMap<>();
        this.matchState = MatchState.INITIALIZED;
        this.playerInTurn = 0;
        this.turnCount = 0;
    }

    public void addPlayer(int playerId, Player newPlayer) {
        this.players.put(playerId, newPlayer);
    }


    public void setBoard(final int wight, final int height) {
        this.board = new Board(wight, height);
    }

    public Board getBoard() {
        return board;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public void setPlayer(int playerIndex, Player newPlayer) {
        this.players.put(playerIndex, newPlayer);
    }

    public void setPlayerPosition(int indexPlayer, Point newPoint) {
        this.players.get(indexPlayer).setPosition(newPoint);
    }

    public int getPlayerCount() {
        return this.gameBaseParameters.getPlayerCount();
    }

    public int getTurnCount() {
        return turnCount;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public HashMap<Integer, Player> getPlayers() {
        return this.players;
    }

    public int getWallCount() {
        int count = 0;
        for (Player player : this.players.values()) {
            count += player.getWallsInField();
        }
        return count;
    }

    public Difficulty getDifficulty() {
        return this.difficulty;
    }

    public GameBaseParameters getGameBaseParameters() {
        return gameBaseParameters;
    }

    public int getPlayerInTurnId() {
        return playerInTurn;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public void setWinningPlayer(final Player winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public enum MatchState {
        INITIALIZED,
        PLAYING,
        DEAD_ZONE,
        WINNER
    }
}
