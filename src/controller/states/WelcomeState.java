package controller.states;

public final class WelcomeState extends GlobalState {

    @Override
    public GlobalStateType getStateType() {
        return GlobalStateType.WELCOME;
    }
}
