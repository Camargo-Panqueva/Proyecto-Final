package controller.main;

import controller.serviceResponse.ErrorResponse;
import controller.serviceResponse.ServiceResponse;
import controller.serviceResponse.SuccessResponse;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import model.GameModel;
import model.cell.CellType; //enum
import model.modes.GameModes; //enum

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class GameController {

    private final GameModel model;
    private final GlobalStateManager globalStateManager;

    public GameController(GameModel model) {
        this.model = model;
        this.globalStateManager = new GlobalStateManager();
    }

    public ServiceResponse<ArrayList<String>> getGameModes() {
        return new SuccessResponse<>(GameModes.getModes(), "Game Mode List Obtained Successfully");
    }

    private void builtGame() {
        this.model.playersCount = this.model.gameModeManager.getBaseParameters().playersCount;
        this.model.wallsCount = this.model.gameModeManager.getBaseParameters().wallsCount;

        this.model.setBoard(this.model.gameModeManager.getBaseParameters().boardWidth, this.model.gameModeManager.getBaseParameters().boardHeight);

        this.model.gameState = GameModel.GameState.READY;
    }

    public ServiceResponse<Void> setGameMode(String selection) {
        Optional<GameModes> gameMode = Arrays.stream(GameModes.values()).filter(c -> c.getMode().equals(selection)).findAny();
        if (gameMode.isEmpty()) {
            return new ErrorResponse<>("Invalid Game Mode");
        }

        this.model.gameModeManager.setCurrentGameMode(gameMode.get());
        this.builtGame();
        return new SuccessResponse<>(null, "Ok");
    }

    public ServiceResponse<Void> startGame() {

        return new SuccessResponse<>(null, "Game Started");
    }

    public ServiceResponse<ArrayList<CellType>> getBoardCells() {
        if (this.model.gameState == GameModel.GameState.STARTED) {
            return new ErrorResponse<>("The model have not parameters for built it, use setGameMode");
        }
        return new SuccessResponse<>(model.board.getBoardCells(), "Current board height");
    }

    public GlobalState getGlobalCurrentState() {
        return this.globalStateManager.getCurrentState();
    }
}
