package controller.states;

public abstract class GlobalState {

    public abstract GlobalStateType getStateType();

    //One for each scene
    public enum GlobalStateType {
        WELCOME,
        SELECTING_GAME_MODE,
        PLAYING,
        GAME_FINISHED;
    }
}
