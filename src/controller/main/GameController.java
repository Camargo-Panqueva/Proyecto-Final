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

        if (!this.model.getMatchState().equals(GameModel.MatchState.INITIALIZED)) {
            return new ErrorResponse<>("Cannot change the Game Mode after started");
        }

        Optional<GameModes> gameMode = Arrays.stream(GameModes.values()).filter(c -> c.getMode().equals(selection)).findAny();
        if (gameMode.isEmpty()) {
            return new ErrorResponse<>("Invalid Game Mode");
        }

        this.model.getGameModeManager().setCurrentGameMode(gameMode.get());
        return new SuccessResponse<>(null, "Ok");
    }

    public ServiceResponse<Void> setInitialCustomParameters(final int width, final int height, final int playerCount, final int wallCount) {
        //TODO : Error handler for invalid initial parameters
        this.model.getGameModeManager().setCurrentGameMode(GameModes.CUSTOM, width, height, playerCount, wallCount);
        return new SuccessResponse<>(null, "Custom initial parameters were established");
    }

    public ServiceResponse<Void> startGame() {
        this.model.getGameModeManager().setCurrentParameters();
        this.model.setMatchState(GameModel.MatchState.STARTED);
        this.builtGame();

        this.globalStateManager.setCurrentState(GlobalState.PLAYING);

        return new SuccessResponse<>(null, "Game Started");
    }

    public ServiceResponse<CellType[][]> getBoardCells() {
        if (this.model.getMatchState().equals(GameModel.MatchState.INITIALIZED)) {
            return new ErrorResponse<>("The model does not have parameters to build it; select them using setGameMode, and build them using startGame");
        }

        int width = this.model.getBoard().getWidth();
        int height = this.model.getBoard().getHeight();

        CellType[][] cellTypesCopy = new CellType[width][height];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cellTypesCopy[x][y] = this.model.getBoard().getCell(x, y);
            }
        }
        return new SuccessResponse<>(cellTypesCopy, "Ok");
    }

    public GlobalState getGlobalCurrentState() {
        return this.globalStateManager.getCurrentState();
    }

    public ServiceResponse<Void> setGlobalState(GlobalState globalState) {
        //TODO : logic upon change global state
        this.globalStateManager.setCurrentState(globalState);
        return new SuccessResponse<>(null, "Global State Changed");
    }
}
