package controller.states;

public final class GlobalStateManager {

    private GlobalState currentState;

    public GlobalStateManager() {
        this.currentState = new WelcomeState();
    }

    public GlobalState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(GlobalState.StateType newState) {
        switch (newState) {
            case WELCOME:
                this.currentState = new WelcomeState();
                break;
            case SELECTING_GAME_MODE:
                this.currentState = new SelectingGameModeState();
                break;
        }
    }
}
