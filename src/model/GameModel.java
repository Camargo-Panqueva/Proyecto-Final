package model;

import model.board.Board;
import model.modes.GameModeBases;
import model.modes.GameModeManager;
import model.modes.GameModes;

public final class GameModel {

    public int playersCount;
    public int wallsCount;

    public Board board;

    public GameState gameState;

    public final GameModeManager gameModeManager;
    private GameModeBases baseParameters;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.gameState = GameState.STARTED;
    }

    public void setBoard(final int wight, final int height) {
        this.board = new Board(wight, height);
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public int getWallsCount() {
        return wallsCount;
    }

    public GameModes getGameMode() {
        return gameModeManager.getCurrentGameMode();
    }

    public enum GameState {
        READY,
        STARTED,
        PLAYING,
        WINNER;
    }
}
