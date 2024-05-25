package controller.states;

/**
 * Manages the global state of the application.
 * This class handles transitions between different global states.
 */
public final class GlobalStateManager {

    /**
     * The current global state of the application.
     */
    private GlobalState currentState;

    /**
     * Constructs a GlobalStateManager object and initializes it with the default state.
     */
    public GlobalStateManager() {
        this.currentState = GlobalState.WELCOME;
    }

    /**
     * Retrieves the current global state of the application.
     *
     * @return The current global state.
     */
    public GlobalState getCurrentState() {
        return this.currentState;
    }

    /**
     * Sets the current global state of the application to the specified new state.
     * TODO: Implement state transition logic as needed.
     *
     * @param newState The new global state to set.
     */
    public void setCurrentState(GlobalState newState) {
        this.currentState = newState;
    }
}
