package model;

import model.board.Board;
import model.modes.GameModeManager;
import model.modes.GameModes;
import model.player.Player;

import java.awt.*;
import java.util.HashMap;

public final class GameModel {

    private int wallCount;

    private Board board;

    private MatchState matchState;

    private final HashMap<Integer, Player> players;

    private int playerInTurn;

    private final GameModeManager gameModeManager;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.players = new HashMap<>();
        this.matchState = MatchState.INITIALIZED;
    }

    public void setBoard(final int wight, final int height) {
        this.board = new Board(wight, height);
    }

    public Board getBoard() {
        return board;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setPlayer(int playerIndex, Player newPlayer) {
        this.players.put(playerIndex, newPlayer);
    }

    public void setPlayerPosition(int indexPlayer, Point newPoint) {
        this.players.get(indexPlayer).setPosition(newPoint);
    }

    public void addPlayer(int playerId, Player newPlayer) {
        this.players.put(playerId, newPlayer);
    }

    public int getPlayerCount() {
        return this.gameModeManager.getBaseParameters().playersCount;
    }

    public HashMap<Integer, Player> getPlayers() {
        return this.players;
    }

    public int getWallCount() {
        return wallCount;
    }


    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    public GameModes getGameMode() {
        return gameModeManager.getCurrentGameMode();
    }

    public int getPlayerInTurn() {
        return playerInTurn;
    }

    public void setWallCount(int wallCount) {
        this.wallCount = wallCount;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public enum MatchState {
        STARTED,
        INITIALIZED,
        PLAYING,
        WINNER;
    }
}
