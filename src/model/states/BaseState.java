package model.states;

public abstract class BaseState {

    public abstract StateType getStateType();

    public enum StateType {
        WELCOME,
        SELECTING_GAME_MODE,
        PLAYING,
    }
}
