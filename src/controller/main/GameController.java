package controller.main;

import controller.serviceResponse.ErrorResponse;
import controller.serviceResponse.ServiceResponse;
import controller.serviceResponse.SuccessResponse;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import model.GameModel;
import model.cell.CellType;
import model.modes.GameModes;

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
        this.model.setPlayerCount(this.model.getGameModeManager().getBaseParameters().playersCount);
        this.model.setWallCount(this.model.getGameModeManager().getBaseParameters().wallsCount);

        this.model.setBoard(this.model.getGameModeManager().getBaseParameters().boardWidth, this.model.getGameModeManager().getBaseParameters().boardHeight);

        this.model.setMatchState(GameModel.MatchState.STARTED);
    }

    public ServiceResponse<Void> setGameMode(String selection) {

        if (!model.getMatchState().equals(GameModel.MatchState.INITIALIZED)){
            return new ErrorResponse<>("Cannot change the Game Mode after started");
        }

        Optional<GameModes> gameMode = Arrays.stream(GameModes.values()).filter(c -> c.getMode().equals(selection)).findAny();
        if (gameMode.isEmpty()) {
            return new ErrorResponse<>("Invalid Game Mode");
        }

        this.model.getGameModeManager().setCurrentGameMode(gameMode.get());
        return new SuccessResponse<>(null, "Ok");
    }

    public ServiceResponse<Void> setInitialCustomParameters(final int width, final int height, final int playerCount, final int wallCount){
        //TODO : Error handler for invalid initial parameters
        model.getGameModeManager().setCurrentGameMode(GameModes.CUSTOM, width, height, playerCount, wallCount);
        return new SuccessResponse<>(null, "Custom initial parameters were established");
    }

    public ServiceResponse<Void> startGame() {
        model.getGameModeManager().setCurrentParameters();
        model.setMatchState(GameModel.MatchState.STARTED);
        this.builtGame();

        return new SuccessResponse<>(null, "Game Started");
    }

    public ServiceResponse<ArrayList<CellType>> getBoardCells() {
        if (this.model.getMatchState().equals(GameModel.MatchState.INITIALIZED)) {
            return new ErrorResponse<>("The model have not parameters for built it, use setGameMode");
        }
        return new SuccessResponse<>(model.getBoard().getBoardCells(), "Current board height");
    }

    public GlobalState getGlobalCurrentState() {
        return this.globalStateManager.getCurrentState();
    }
}
