package controller.states;

public abstract class GlobalState {

    public abstract StateType getStateType();

    //One for each scene
    public enum StateType {
        WELCOME,
        SELECTING_GAME_MODE,
        PLAYING,
    }
}
