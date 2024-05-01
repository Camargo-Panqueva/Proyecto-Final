package model;

import model.board.Board;
import model.modes.GameModeBases;
import model.modes.GameModeManager;
import model.modes.GameModes;

public final class GameModel {

    private int playersCount;
    private int wallsCount;

    public Board board;

    public GameState gameState;

    private final GameModeManager gameModeManager;
    private GameModeBases baseParameters;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.gameState = GameState.STARTED;
    }

    public void builtGame(GameModes gameMode) {
        this.gameModeManager.setCurrentGameMode(gameMode);
        this.baseParameters = this.gameModeManager.getBaseParameters();
        this.builtParameters();
    }

    private void builtParameters() {
        this.playersCount = this.baseParameters.playersCount;
        this.wallsCount = this.baseParameters.wallsCount;

        this.board = new Board(this.baseParameters.boardWidth, this.baseParameters.boardHeight);

        this.gameState = GameState.READY;
    }

    public GameModeManager getGameModeManager() {
        return this.gameModeManager;
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
