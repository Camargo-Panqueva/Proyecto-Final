package controller;

import controller.dto.*;
import controller.logic.MatchManager;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import controller.wall.Wall;
import model.GameModel;
import model.cell.CellType;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

public final class GameController {

    private final GameModel model;
    private final GlobalStateManager globalStateManager;
    private MatchManager matchManager;

    public GameController(GameModel model) {
        this.model = model;
        this.model.setMatchState(GameModel.MatchState.INITIALIZED);
        this.globalStateManager = new GlobalStateManager();
    }

    public ServiceResponse<ArrayList<String>> getGameModes() {
        return new SuccessResponse<>(GameModes.getModes(), "Game Mode List Obtained Successfully");
    }

    private void buildGame() {
        this.model.setWallCount(this.model.getGameModeManager().getBaseParameters().wallsPerPlayer);
        this.model.setBoard(this.model.getGameModeManager().getBaseParameters().boardWidth, this.model.getGameModeManager().getBaseParameters().boardHeight);

        this.setupPlayers();

        this.model.setMatchState(GameModel.MatchState.STARTED);
    }

    private void setupPlayers() {
        final int allowedWallsPerPlayer = this.model.getGameModeManager().getBaseParameters().wallsPerPlayer;

        final int width = this.model.getBoard().getWidth() - 1;
        final int height = this.model.getBoard().getHeight() - 1;

        final boolean widthIsEven = this.model.getBoard().getWidth() % 2 == 0;
        final boolean heightIsEven = this.model.getBoard().getHeight() % 2 == 0;

        //player position starts from 0
        final int widthCenterPosition = widthIsEven ? (width / 2) - 1 : (width / 2);
        final int heightCenterPosition = heightIsEven ? (height / 2) - 1 : (height / 2);

        if (widthIsEven) {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition + 1, 0), "Player 1", allowedWallsPerPlayer));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition + 2, height), "Player 2", allowedWallsPerPlayer));
        } else {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition, 0), "Player 1", allowedWallsPerPlayer));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition, height), "Player 2", allowedWallsPerPlayer));
        }

        if (heightIsEven) {
            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition + 2), "Player 3", allowedWallsPerPlayer));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition + 1), "Player 4", allowedWallsPerPlayer));
            }
        } else {

            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition), "Player 3", allowedWallsPerPlayer));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition), "Player 4", allowedWallsPerPlayer));

            }
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
        if (playerCount < 2 || playerCount > 4) {
            return new ErrorResponse<>("Our Quoridor accept just 2, 3 and 4 players");
        }
        if (width < 0 || height < 0 || width > 21 || height > 21) {
            return new ErrorResponse<>("Out of limits, the size must be between 0 and 21");
        }
        this.model.getGameModeManager().setCurrentGameMode(GameModes.CUSTOM, width, height, playerCount, wallsPerPlayer);
        return new SuccessResponse<>(null, "Custom initial parameters were established");
    }

    public ServiceResponse<Void> startGame() {
        if (this.model.getMatchState() == GameModel.MatchState.PLAYING) {
            return new ErrorResponse<>("The game already started");
        }

        this.model.getGameModeManager().setCurrentParameters();
        this.buildGame();
        this.matchManager = new MatchManager(this.model);

        this.globalStateManager.setCurrentState(GlobalState.PLAYING);
        this.model.setMatchState(GameModel.MatchState.PLAYING);
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

        for (int i = 0; i < width * 2 - 1; i++) {
            for (int j = 0; j < height * 2 - 1; j++) {
                wallTypesCopy[i][j] = this.model.getBoard().getBoardWalls()[i][j] == null ? null : this.model.getBoard().getBoardWalls()[i][j].getWallType();
            }
        }

        final ArrayList<PlayerTransferObject> playerTransferObjectArrayList = new ArrayList<>(this.model.getPlayerCount());

        this.model.getPlayers().forEach(((id, player) -> {
            playerTransferObjectArrayList.add(new PlayerTransferObject(id, player.getName(), new Point(player.getPosition()),
                    this.model.getPlayerInTurnId() == id, this.matchManager.getPossibleMovements(player)));
        }));

        return new SuccessResponse<>(
                new BoardTransferObject(
                        cellTypesCopy,
                        wallTypesCopy,
                        playerTransferObjectArrayList,
                        playerTransferObjectArrayList.stream().filter(PlayerTransferObject::isInTurn).findFirst().orElse(null) //TODO : check if this is correct
                ), "Ok");
    }

    public ServiceResponse<Void> processPlayerMove(int playerId, Point point) {

        final ServiceResponse<Void> assertion = this.validBasicParameters(playerId);
        if (this.validBasicParameters(playerId) != null) {
            return assertion;
        }

        if (!this.matchManager.getPossibleMovements(this.model.getPlayers().get(playerId)).contains(point)) {
            return new ErrorResponse<>("Illegal Movement for " + this.model.getPlayers().get(playerId).getName());
        }

        this.matchManager.executeMove(this.model.getPlayers().get(playerId), point);
        return new SuccessResponse<>(null, "Updated position in the model");
    }

    private ServiceResponse<Void> placeWallProcess(final int playerId, final Wall wall, final boolean placeIt) {
        final ServiceResponse<Void> assertion = this.validBasicParameters(playerId);
        if (this.validBasicParameters(playerId) != null) {
            return assertion;
        }
        if (wall.getWallType() == null || wall.getPositionOnBoard() == null) {
            return new ErrorResponse<>("Walls passed as parameter must have defined its position, use wall.getDataWall().setPositionOnBoard()");
        }
        if (wall.getPositionOnBoard().x % 2 == 1 && wall.getPositionOnBoard().y % 2 == 1){
            return new ErrorResponse<>("Walls cannot be placed in corners");
        }

        final ArrayList<Point> newWalls = new ArrayList<>();

        int boardWallX;
        int boardWallY;
        for (int x = 0; x < wall.getWidth(); x++) {
            for (int y = 0; y < wall.getHeight(); y++) {

                boardWallX = wall.getPositionOnBoard().x + x;
                boardWallY = wall.getPositionOnBoard().y + y;

                final boolean isPositionInBoard = this.isPointInsideBoard(boardWallX, boardWallY);

                if (!isPositionInBoard) {
                    if (wall.getWallShape()[x][y] != null) {
                        return new ErrorResponse<>("Wall placed in a invalid position. The Wall is off the board");
                    }
                    continue;
                }

                final WallData wallDataPosition = this.model.getBoard().getBoardWalls()[boardWallX][boardWallY];

                final boolean isNullPosition = wallDataPosition == null;

                if (!isNullPosition && null != wall.getWallShape()[x][y]) {
                    return new ErrorResponse<>("Wall placed in a invalid position. Already there is a wall");
                }

                if (boardWallX % 2 == 0 && boardWallY % 2 == 0 && null != wall.getWallShape()[x][y]) {
                    return new ErrorResponse<>("Wall placed in a invalid position. That position is Illegal! there should be a cell");
                }

                if (wall.getWallShape()[x][y] != null) {
                    newWalls.add(new Point(boardWallX, boardWallY));
                }
            }
        }

        if (placeIt) {
            this.matchManager.executePlaceWall(this.model.getPlayers().get(playerId), wall, newWalls);
            return new SuccessResponse<>(null, "Ok, The Wall was placed");
        }
        return new SuccessResponse<>(null, "Can place the Wall");
    }

    public ServiceResponse<Void> canPlaceWall(final int playerId, final Wall wall) {
        return this.placeWallProcess(playerId, wall, false);
    }

    public ServiceResponse<Void> placeWall(final int playerId, final Wall wall) {
        return this.placeWallProcess(playerId, wall, true);
    }

    public boolean isPointInsideBoard(final int x, final int y) {
        return x <= this.model.getBoard().getWidth() * 2 - 2 && y <= this.model.getBoard().getHeight() * 2 - 2 && x >= 0 && y >= 0;
    }

    private ServiceResponse<Void> validBasicParameters(int playerId) {
        if (!this.model.getMatchState().equals(GameModel.MatchState.PLAYING)) {
            return new ErrorResponse<>("There is not a match, call startGame");
        }
        if (!this.model.getPlayers().containsKey(playerId)) {
            return new ErrorResponse<>("Player doesn't exist");
        }
        if (!this.model.getPlayers().get(playerId).equals(this.model.getPlayers().get(this.model.getPlayerInTurnId()))) {
            return new ErrorResponse<>("Its not " + this.model.getPlayers().get(playerId).getName() + " turn");
        }
        return null;
    }

    public ServiceResponse<UUID> getWallId(Point point) {
        if (!this.isPointInsideBoard(point.x, point.y)) {
            return new ErrorResponse<>("Point off the board");
        }
        if (point.x % 2 == 0 && point.y % 2 == 0) {
            return new ErrorResponse<>("A cell was selected, select a Wall");
        }
        WallData positionRequested = this.model.getBoard().getWallData(point.x, point.y);
        if (positionRequested == null) {
            return new ErrorResponse<>("There is no a Wall in: " + point);
        }

        return new SuccessResponse<>(this.model.getBoard().getWallId(point), "Ok");
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
