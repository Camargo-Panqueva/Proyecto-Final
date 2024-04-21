package model.states;

public final class StateManager {

    private BaseState currentState;

    public StateManager() {
        this.currentState = new WelcomeState();
    }

    public BaseState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(BaseState.StateType newState) {
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
