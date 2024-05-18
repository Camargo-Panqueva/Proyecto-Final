package controller;

import controller.dto.*;
import controller.logic.MatchManager;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import controller.wall.*;
import model.GameModel;
import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public final class GameController {

    private final GameModel model;
    private final GlobalStateManager globalStateManager;
    private MatchManager matchManager;

    public GameController(GameModel model) {
        this.model = model;
        this.model.setMatchState(GameModel.MatchState.INITIALIZED);
        this.globalStateManager = new GlobalStateManager();
    }


    public ServiceResponse<Void> createMatch(SetupTransferObject setupSettings) {
        final int playerCount = setupSettings.players().size();
        final int boardWidth = setupSettings.boardWidth();
        final int boardHeight = setupSettings.boardHeight();
        final int wallsPerPlayer = setupSettings.wallTypeCount().values().stream().mapToInt(Integer::intValue).sum()

        final int time = setupSettings.time();
        final DifficultyType difficultyType = setupSettings.difficultyType();

        final int playerWallQuota = Math.min(boardHeight, boardWidth) + 1;

        if (this.model.getMatchState() == GameModel.MatchState.PLAYING) {
            return new ErrorResponse<>("The game already started");
        }

        if (this.model.getGameBaseParameters().getHasBeenSet()) {
            return new ErrorResponse<>("There already a game with parameters set");
        }

        if (playerCount < 2 || playerCount > 4) {
            return new ErrorResponse<>("Our Quoridor accept just 2, 3 and 4 players");
        }

        if (boardWidth < 0 || boardHeight < 0 || boardWidth > 21 || boardHeight > 21) {
            return new ErrorResponse<>("Out of limits, the size must be between 0 and 21");
        }

        if (time <= 0) {
            return new ErrorResponse<>("Negative times? ;:l");
        }

        if (difficultyType != DifficultyType.NORMAL) {
            if (difficultyType == DifficultyType.AGAINST_THE_CLOCK && time > 600) {
                return new ErrorResponse<>("Turns must be between 1 second & 10 minutes");
            } else if (difficultyType == DifficultyType.AGAINST_THE_CLOCK) {
                this.model.getDifficulty().setDifficulty(setupSettings.difficultyType(), time, 0);
            }
            if (difficultyType == DifficultyType.TIMED && time > 1800) {
                return new ErrorResponse<>("Total time must be between 1 second & 30 minutes");
            } else if (difficultyType == DifficultyType.TIMED) {
                this.model.getDifficulty().setDifficulty(difficultyType, 0, time);
            }
        } else {
            this.model.getDifficulty().setDifficulty(difficultyType, 0, 0);
        }

        if (setupSettings.wallTypeCount().values().stream().mapToInt(Integer::intValue).sum() != playerWallQuota) {
            return new ErrorResponse<>("The sum of walls must be: " + playerWallQuota);
        }

        this.model.getGameBaseParameters().setBaseParameters(GameModes.CUSTOM, boardWidth, boardHeight, playerCount, wallsPerPlayer);

        this.buildBoard(setupSettings.randomCells());

        this.setupPlayers();

        this.matchManager = new MatchManager(this.model);

        this.globalStateManager.setCurrentState(GlobalState.PLAYING);
        this.model.setMatchState(GameModel.MatchState.PLAYING);
        return new SuccessResponse<>(null, "Game Started");
    }

    private void buildBoard(final boolean isRandom) {
        this.model.setWallCount(this.model.getGameBaseParameters().getWallsPerPlayer());

        if (isRandom) {
            this.model.getBoard().setAsRandomly();
        }
        this.model.setBoard(this.model.getGameBaseParameters().getBoardWidth(), this.model.getGameBaseParameters().getBoardHeight());

        if (this.model.getBoard().getIsRandomly()) {
            this.createRandomlyCells();
        } else {
            this.createCells();
        }

        this.model.setMatchState(GameModel.MatchState.STARTED);
    }

    private void createRandomlyCells() {
        Random random = new Random();
        final double SPECIAL_CELL_PROBABILITY = 0.15;

        for (int x = 0; x < this.model.getBoard().getWidth(); x++) {
            for (int y = 0; y < this.model.getBoard().getHeight(); y++) {
                if (random.nextDouble() < SPECIAL_CELL_PROBABILITY) {
                    this.model.getBoard().getBoardCells()[x][y] = getRandomSpecialCell(random);
                } else {
                    this.model.getBoard().getBoardCells()[x][y] = CellType.NORMAL;
                }
            }
        }
    }

    private CellType getRandomSpecialCell(Random random) {
        CellType[] specialCells = {CellType.TELEPORT, CellType.RETURN, CellType.DOUBLE_TURN};
        return specialCells[random.nextInt(specialCells.length)];
    }


    private void createCells() {
        final int width = this.model.getBoard().getWidth();
        final int height = this.model.getBoard().getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.model.getBoard().getBoardCells()[x][y] = CellType.NORMAL;
            }
        }
    }

    private void setupPlayers() {
        final int allowedWallsPerPlayer = this.model.getGameBaseParameters().getWallsPerPlayer();

        final int width = this.model.getBoard().getWidth() - 1;
        final int height = this.model.getBoard().getHeight() - 1;

        final boolean widthIsEven = this.model.getBoard().getWidth() % 2 == 0;
        final boolean heightIsEven = this.model.getBoard().getHeight() % 2 == 0;

        //player position starts from 0
        final int widthCenterPosition = widthIsEven ? (width / 2) - 1 : (width / 2);
        final int heightCenterPosition = heightIsEven ? (height / 2) - 1 : (height / 2);

        if (widthIsEven) {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition + 1, 0), "Player 1", allowedWallsPerPlayer, -1, height));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition + 2, height), "Player 2", allowedWallsPerPlayer, -1, 0));
        } else {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition, 0), "Player 1", allowedWallsPerPlayer, -1, height));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition, height), "Player 2", allowedWallsPerPlayer, -1, 0));
        }

        if (heightIsEven) {
            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition + 2), "Player 3", allowedWallsPerPlayer, width, -1));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition + 1), "Player 4", allowedWallsPerPlayer, 0, -1));
            }
        } else {

            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition), "Player 3", allowedWallsPerPlayer, width, -1));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition), "Player 4", allowedWallsPerPlayer, 0, -1));

            }
        }

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
                    this.model.getPlayerInTurnId() == id, this.matchManager.getPossibleMovements(player), player.getWallsInField(),
                    this.model.getDifficulty().getTimePerTurn() - player.getTimePlayed(),
                    this.model.getGameBaseParameters().getWallsPerPlayer() - player.getWallsInField()));
        }));

        return new SuccessResponse<>(
                new BoardTransferObject(
                        cellTypesCopy,
                        wallTypesCopy, this.model.getTurnCount(),
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

    private Wall createNewWall(final Point positionOnBoard, final WallType wallType) {
        Wall newWall;
        switch (wallType) {
            case LARGE -> newWall = new LargeWall();
            case TEMPORAL_WALL -> newWall = new TemporalWall();
            case ALLY -> newWall = new AllyWall();
            default -> newWall = new NormalWall();// NORMAL
        }
        newWall.setPositionOnBoard(positionOnBoard);

        if (positionOnBoard.y % 2 == 0 && positionOnBoard.x % 2 == 1) {
            newWall.rotate();
        }

        return newWall;
    }

    private ServiceResponse<Void> placeWallProcess(final int playerId, final Point positionOnBoard, final WallType wallType, boolean placeIt) {

        final Wall wall = this.createNewWall(positionOnBoard, wallType);

        final ServiceResponse<Void> assertion = this.validBasicParameters(playerId);
        if (this.validBasicParameters(playerId) != null) {
            return assertion;
        }
        if (wall.getWallType() == null || wall.getPositionOnBoard() == null) {
            return new ErrorResponse<>("Walls passed as parameter must have defined its position, use wall.getDataWall().setPositionOnBoard()");
        }
        if (wall.getPositionOnBoard().x % 2 == 1 && wall.getPositionOnBoard().y % 2 == 1) {
            return new ErrorResponse<>("Walls cannot be placed in corners");
        }
        final Player player = this.model.getPlayers().get(playerId);
        if (player.getAllowedWalls() - player.getWallsInField() <= 0) {
            return new ErrorResponse<>(player.getName() + " already placed all of his Walls: " + player.getAllowedWalls() + "/" + player.getWallsInField());
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
            if (this.matchManager.isABlockerWall(newWalls)) {
                return new ErrorResponse<>("You cannot block the path, chose another position");
            }
            this.matchManager.executePlaceWall(this.model.getPlayers().get(playerId), wall, newWalls);
            return new SuccessResponse<>(null, "Ok, The Wall was placed");
        }
        return new SuccessResponse<>(null, "Can place the Wall");
    }

    public ServiceResponse<Boolean> canPlaceWall(final int playerId, final Point positionOnBoard, final WallType wallType) {  //TODO : return a copy of the board with the new wall?
        ServiceResponse<Void> response = this.placeWallProcess(playerId, positionOnBoard, wallType, false);
        if (response.ok) {
            return new SuccessResponse<>(true, response.message);
        }

        return new ErrorResponse<>(response.message);

    }

    public ServiceResponse<Void> placeWall(final int playerId, final Point positionOnBoard, final WallType wallType) {
        return this.placeWallProcess(playerId, positionOnBoard, wallType, true);
    }

    public boolean isPointInsideBoard(final int x, final int y) {
        return x <= this.model.getBoard().getWidth() * 2 - 2 && y <= this.model.getBoard().getHeight() * 2 - 2 && x >= 0 && y >= 0;
    }

    private ServiceResponse<Void> validBasicParameters(final int playerId) {
        if (this.model.getMatchState() == GameModel.MatchState.WINNER) {
            return new ErrorResponse<>("There is a WINNER!! Congratulations " + this.model.getWinningPlayer().getName());
        }
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

    public ServiceResponse<Void> getWallId(Point point) {
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

        if (this.matchManager.executeDeleteWall(this.model.getBoard().getWallId(point)) == null) {
            return new ErrorResponse<>("There wasn't a wall for delete, check walls attribute in the Match Manager");
        }
        return new SuccessResponse<>(null, "The Wall was deleted");
    }

    public ServiceResponse<GlobalState> getGlobalCurrentState() {
        return new SuccessResponse<>(this.globalStateManager.getCurrentState(), "ok");
    }

    public ServiceResponse<Void> setGlobalState(GlobalState globalState) {
        //TODO : logic upon change global state
        this.globalStateManager.setCurrentState(globalState);
        return new SuccessResponse<>(null, "Global State Changed");
    }
}
