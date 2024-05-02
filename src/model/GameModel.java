package model;

import model.board.Board;
import model.modes.GameModeBases;
import model.modes.GameModeManager;
import model.modes.GameModes;
import model.player.Player;

import java.awt.*;

public final class GameModel {

    private int playerCount;
    private int wallCount;

    private Board board;

    private MatchState matchState;

    private Player player;

    private final GameModeManager gameModeManager;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.matchState = MatchState.STARTED;
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

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setPlayerPosition(Point point){
        player.setPosition(point);
    }
    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
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
        READY,
        STARTED,
        PLAYING,
        WINNER;
    }
}
