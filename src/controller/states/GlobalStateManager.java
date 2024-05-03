package controller.states;

public final class GlobalStateManager {

    private GlobalState currentState;

    public GlobalStateManager() {
        this.currentState = GlobalState.WELCOME;
    }

    public GlobalState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(GlobalState newState) {
        //TODO: Implement state transition logic
        this.currentState = newState;
    }
}
