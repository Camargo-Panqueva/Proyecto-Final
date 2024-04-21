package controller.main;

import model.GameModel;
import model.states.BaseState;

public final class GameController {

    private final GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public BaseState getCurrentState() {
        return this.model.getStateManager().getCurrentState();
    }
}
