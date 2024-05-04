package model;

import model.board.Board;
import model.modes.GameModeManager;
import model.modes.GameModes;
import model.player.Player;

import java.awt.*;
import java.util.ArrayList;

public final class GameModel {

    private int wallCount;

    private Board board;

    private MatchState matchState;

    private final ArrayList<Player> players;

    private final GameModeManager gameModeManager;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.players = new ArrayList<>();
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
        this.players.set(playerIndex, newPlayer);
    }

    public void setPlayerPosition(int indexPlayer, Point newPoint) {
        this.players.get(indexPlayer).setPosition(newPoint);
    }

    public void addPlayer(Player newPlayer) {
        this.players.add(newPlayer);
    }

    public int getPlayerCount() {
        return this.gameModeManager.getBaseParameters().playersCount;
    }

    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    public int getWallCount() {
        return wallCount;
    }

    public void setWallCount(int wallCount) {
        this.wallCount = wallCount;
    }

    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    public GameModes getGameMode() {
        return gameModeManager.getCurrentGameMode();
    }

    public enum MatchState {
        STARTED,
        INITIALIZED,
        PLAYING,
        WINNER;
    }
}
