package model;

import model.gameType.GameMode;
import model.states.StateManager;

public final class GameModel {

     public GameState gameState;

     public GameMode gameMode;

    private final StateManager stateManager;

    public GameModel() {
        this.stateManager = new StateManager();
        this.gameState = GameState.STARTED;
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public enum GameState{
        STARTED,
        PLAYING,
        PAYER1WON,
        PLAYER2WON;
    }
}
