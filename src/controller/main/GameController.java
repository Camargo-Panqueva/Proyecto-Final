package controller.main;

import controller.serviceResponse.ErrorResponse;
import controller.serviceResponse.ServiceResponse;
import controller.serviceResponse.SuccessResponse;
import model.GameModel;
import model.parameters.GameModes;
import model.states.BaseState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class GameController {

    private final GameModel model;

    public GameController(GameModel model) {
        this.model = model;
    }

    public ServiceResponse<ArrayList<String>> getGameModes(){
        return new SuccessResponse<>(GameModes.getModes(), "Game Mode List Obtained Successfully");
    }

    public ServiceResponse<Void> setGameMode(String selection) {
        Optional<GameModes> gameMode = Arrays.stream(GameModes.values()).filter(c -> c.getMode().equals(selection)).findAny();
        if(gameMode.isEmpty()){
            return new ErrorResponse<>("Invalid Game Mode");
        }

        this.model.startGame(gameMode.get());
        return new SuccessResponse<>(null, "Ok");
    }

    public BaseState getCurrentState() {
        return this.model.getStateManager().getCurrentState();
    }
}
