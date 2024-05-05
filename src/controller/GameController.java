package controller;

import controller.dto.*;
import controller.logic.MatchManager;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import model.GameModel;
import model.cell.CellType;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public final class GameController {

    private final GameModel model;
    private MatchManager matchManager;
    private final GlobalStateManager globalStateManager;

    public GameController(GameModel model) {
        this.model = model;
        this.globalStateManager = new GlobalStateManager();
    }

    public ServiceResponse<ArrayList<String>> getGameModes() {
        return new SuccessResponse<>(GameModes.getModes(), "Game Mode List Obtained Successfully");
    }

    private void builtGame() {
        this.model.setWallCount(this.model.getGameModeManager().getBaseParameters().wallsPerPlayer);

        this.model.setBoard(this.model.getGameModeManager().getBaseParameters().boardWidth, this.model.getGameModeManager().getBaseParameters().boardHeight);

        this.setPlayers();

        this.model.setMatchState(GameModel.MatchState.STARTED);
    }

    private void setPlayers() {
        final int allowedWallsPerPlayer = this.model.getGameModeManager().getBaseParameters().wallsPerPlayer;

        final int width = this.model.getBoard().getWidth();
        final int height = this.model.getBoard().getHeight();

        final boolean widthIsEven = this.model.getBoard().getWidth() % 2 == 0;
        final boolean heightIsEven = this.model.getBoard().getHeight() % 2 == 0;

        //player position starts from 0
        final int widthCenterPosition = widthIsEven ? (width / 2) - 1 : (width / 2);
        final int heightCenterPosition = heightIsEven ? (height / 2) - 1 : (height / 2);


        this.model.addPlayer(0, new Player(new Point(widthCenterPosition, 0), "Player 1", allowedWallsPerPlayer));
        this.model.addPlayer(1, new Player(new Point(widthCenterPosition, height), "Player 2", allowedWallsPerPlayer));

        if (this.model.getPlayerCount() == 4) {
            this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition), "Player 3", allowedWallsPerPlayer));
            this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition), "Player 4", allowedWallsPerPlayer));
        }
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

    public ServiceResponse<Void> setInitialCustomParameters(final int width, final int height, final int playerCount, final int wallsPerPlayer) {
        //TODO : Error handler for invalid initial parameters
        this.model.getGameModeManager().setCurrentGameMode(GameModes.CUSTOM, width, height, playerCount, wallsPerPlayer);
        return new SuccessResponse<>(null, "Custom initial parameters were established");
    }

    public ServiceResponse<Void> startGame() {
        if (this.model.getMatchState() == GameModel.MatchState.PLAYING) {
            return new ErrorResponse<>("The game already started");
        }

        this.model.getGameModeManager().setCurrentParameters();
        this.model.setMatchState(GameModel.MatchState.STARTED);
        this.builtGame();

        this.globalStateManager.setCurrentState(GlobalState.PLAYING);

        this.matchManager = new MatchManager(this.model);

        return new SuccessResponse<>(null, "Game Started");
    }

    public ServiceResponse<BoardTransferObject> getBoardState() {
        if (this.model.getMatchState().equals(GameModel.MatchState.INITIALIZED)) {
            return new ErrorResponse<>("The model does not have parameters to build it; select them using setGameMode, and build them using startGame");
        }

        int width = this.model.getBoard().getWidth();
        int height = this.model.getBoard().getHeight();

        WallType[][] wallTypesCopy = new WallType[width * 2 - 1][height * 2 - 1];
        CellType[][] cellTypesCopy = new CellType[width][height];

        for (int i = 0; i < width; i++) {
            cellTypesCopy[i] = Arrays.copyOf(this.model.getBoard().getBoardCells()[i], this.model.getBoard().getHeight());
        }

        for (int i = 0; i < width * 2 + 1; i++) {
            wallTypesCopy[i] = Arrays.copyOf(this.model.getBoard().getBoardWalls()[i], height * 2 - 1);
        }

        final ArrayList<PlayerTransferObject> playerTransferObjectArrayList = new ArrayList<>(this.model.getPlayerCount());

        this.model.getPlayers().forEach(((id, player) -> {
            playerTransferObjectArrayList.add(new PlayerTransferObject(id, player.getName(), player.getPosition(),
                    this.model.getPlayerInTurn() == id, this.matchManager.getPossibleMovements(player)));
        }));

        return new SuccessResponse<>(new BoardTransferObject(cellTypesCopy, wallTypesCopy, playerTransferObjectArrayList), "Ok");
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
