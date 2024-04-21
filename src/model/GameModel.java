package model;

import model.states.StateManager;

public final class GameModel {

    private final StateManager stateManager;

    public GameModel() {
        this.stateManager = new StateManager();
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }
}
