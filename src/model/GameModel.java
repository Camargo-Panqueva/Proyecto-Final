package model;

import model.board.Board;
import model.parameters.GameMode;
import model.parameters.GameModes;
import controller.states.StateManager;

public final class GameModel {

    private int numOfPlayers;
    private int numOfWalls;

    public Board board;

    public GameState gameState;

    private GameMode gameMode;

    private final StateManager stateManager;

    public GameModel() {
        this.stateManager = new StateManager();
        this.gameState = GameState.STARTED;
    }

    public void startGame(GameModes gameMode) {
        this.gameMode = gameMode.getGameModeClass();
        this.builtParameters();
    }

    private void builtParameters() {
        this.numOfPlayers = this.gameMode.getNumOfPlayers();
        this.numOfWalls = this.gameMode.getNumOfWalls();

        this.board = new Board(this.gameMode.getBoardHeight(), this.gameMode.getBoardWidth());

        this.gameState = GameState.READY;
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public int getNPlayers() {
        return numOfPlayers;
    }

    public int get() {
        return numOfWalls;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public enum GameState {
        READY,
        STARTED,
        PLAYING,
        WINNER;
    }
}
