package controller;

import controller.dto.*;
import controller.logic.MatchManager;
import controller.states.GlobalState;
import controller.states.GlobalStateManager;
import controller.wall.*;
import model.GameModel;
import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.player.Player;
import model.player.PlayerType;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.io.*;
import java.util.*;

public final class GameController {

    private GameModel model;
    private final GlobalStateManager globalStateManager;
    private MatchManager matchManager;

    public GameController(GameModel model) {
        this.model = model;
        this.model.setMatchState(GameModel.MatchState.INITIALIZED);
        this.globalStateManager = new GlobalStateManager();
    }

    public ServiceResponse<Void> saveMatch(String path) {
        if (this.model == null) {
            return new ErrorResponse<>("There is no match to save");
        }
        if (this.model.getMatchState() == GameModel.MatchState.INITIALIZED) {
            return new ErrorResponse<>("The match has not started yet");
        }
        if (this.model.getMatchState() == GameModel.MatchState.WINNER) {
            return new ErrorResponse<>("The match has already ended");
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.model);
            out.close();
            fileOut.close();
            return new SuccessResponse<>(null, "Match saved in " + path);
        } catch (IOException e) {
            return new ErrorResponse<>("Error saving the match: " + e.getMessage());
        }
    }

    public ServiceResponse<Void> loadMatch(String path) {
        try{
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);

            this.model = (GameModel) in.readObject();
            this.matchManager = new MatchManager(this.model);

            fileIn.close();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            return new ErrorResponse<>("Error loading the match: " + e.getMessage());
        }

        return new SuccessResponse<>(null, "Match loaded");

    }

    public ServiceResponse<Void> createMatch(SetupTransferObject setupSettings) {
        final int playerCount = setupSettings.players().size();
        final int boardWidth = setupSettings.boardWidth();
        final int boardHeight = setupSettings.boardHeight();

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

        //TODO : conditionals for cells

        if (setupSettings.wallTypeCount().values().stream().mapToInt(Integer::intValue).sum() > playerWallQuota) {
            return new ErrorResponse<>("The sum of walls at most it should be: " + playerWallQuota);
        }

        this.model.getGameBaseParameters().setBaseParameters(boardWidth, boardHeight, playerCount, setupSettings.wallTypeCount());

        this.model.setBoard(this.model.getGameBaseParameters().getBoardWidth(), this.model.getGameBaseParameters().getBoardHeight());

        this.matchManager = new MatchManager(this.model);

        this.setupPlayers(setupSettings.players());

        this.buildCells(setupSettings.randomCells(), setupSettings.cellTypeCount());

        this.globalStateManager.setCurrentState(GlobalState.PLAYING);
        this.model.setMatchState(GameModel.MatchState.PLAYING);
        return new SuccessResponse<>(null, "Game Started");
    }

    private void buildCells(final boolean isRandom, HashMap<CellType, Integer> cellTypeCount) {

        if (isRandom) {
            this.model.getBoard().setAsRandomly();
        }

        if (this.model.getBoard().getIsRandomly()) {
            this.createRandomlyCells(cellTypeCount);
        } else {
            this.createCells();
        }
    }

    private void createRandomlyCells(HashMap<CellType, Integer> cellTypeCount) { // TODO : check if cell spawn in spawn player
        Random random = new Random();
        final double SPECIAL_CELL_PROBABILITY = 0.15;

        this.createCells();

        while (!this.allValuesZero(cellTypeCount)) {
            final CellType cellType = this.getRandomKey(cellTypeCount, random);
            final int width = this.model.getBoard().getWidth();
            final int height = this.model.getBoard().getHeight();
            final int x = random.nextInt(width);
            final int y = random.nextInt(height);

            if (cellTypeCount.get(cellType) <= 0) {
                continue;
            }

            if (this.model.getBoard().getCellType(x, y) != CellType.NORMAL) {
                continue;
            }

            if (random.nextDouble() < SPECIAL_CELL_PROBABILITY && !this.matchManager.isOccupiedPoint(new Point(x, y))) {
                this.model.getBoard().getBoardCells()[x][y] = cellType;
                cellTypeCount.put(cellType, cellTypeCount.get(cellType) - 1);
            }
        }
    }

    private CellType getRandomKey(HashMap<CellType, Integer> hashMap, Random random) {
        final Set<CellType> keys = hashMap.keySet();
        return (CellType) keys.toArray()[random.nextInt(keys.size())];
    }

    private boolean allValuesZero(HashMap<CellType, Integer> hashMap) {
        for (int value : hashMap.values()) {
            if (value > 0) {
                return false;
            }
        }
        return true;
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

    private void setupPlayers(ArrayList<PlayerSetupTransferObject> players) {
        final HashMap<WallType, Integer> allowedWallsPerPlayer = this.model.getGameBaseParameters().getWallsCountPerPlayer();

        final int width = this.model.getBoard().getWidth() - 1;
        final int height = this.model.getBoard().getHeight() - 1;

        final boolean widthIsEven = this.model.getBoard().getWidth() % 2 == 0;
        final boolean heightIsEven = this.model.getBoard().getHeight() % 2 == 0;

        //player position starts from 0
        final int widthCenterPosition = widthIsEven ? (width / 2) - 1 : (width / 2);
        final int heightCenterPosition = heightIsEven ? (height / 2) - 1 : (height / 2);

        if (widthIsEven) {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition + 1, 0), players.get(0).name(), new HashMap<>(allowedWallsPerPlayer), -1, height));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition + 2, height), players.get(1).name(), new HashMap<>(allowedWallsPerPlayer), -1, 0));
        } else {
            this.model.addPlayer(0, new Player(new Point(widthCenterPosition, 0), players.get(0).name(), new HashMap<>(allowedWallsPerPlayer), -1, height));
            this.model.addPlayer(1, new Player(new Point(widthCenterPosition, height), players.get(1).name(), new HashMap<>(allowedWallsPerPlayer), -1, 0));
        }

        if (heightIsEven) {
            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition + 2), players.get(2).name(), new HashMap<>(allowedWallsPerPlayer), width, -1));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition + 1), players.get(3).name(), new HashMap<>(allowedWallsPerPlayer), 0, -1));
            }
        } else {

            if (this.model.getPlayerCount() > 2) {
                this.model.addPlayer(2, new Player(new Point(0, heightCenterPosition), players.get(2).name(), new HashMap<>(allowedWallsPerPlayer), width, -1));
            }
            if (this.model.getPlayerCount() > 3) {
                this.model.addPlayer(3, new Player(new Point(width, heightCenterPosition), players.get(3).name(), new HashMap<>(allowedWallsPerPlayer), 0, -1));
            }
        }

        for (int i = 0; i < this.model.getPlayerCount(); i++) {
            if (players.get(i).playerType() == PlayerType.AI) {
                this.model.getPlayers().get(i).setAsAI();
                this.model.getPlayers().get(i).setAiProfile(players.get(i).aiProfile());
            }
            // TODO : set color
        }
    }

    public ServiceResponse<BoardTransferObject> getBoardState() {
        if (this.model.getMatchState().equals(GameModel.MatchState.INITIALIZED)) {
            return new ErrorResponse<>("The model does not have parameters to build it; select them using setGameMode, and build them using startGame");
        }

        int width = this.model.getBoard().getWidth();
        int height = this.model.getBoard().getHeight();

        CellType[][] cellTypesCopy = new CellType[width][height];

        for (int i = 0; i < width; i++) {
            cellTypesCopy[i] = Arrays.copyOf(this.model.getBoard().getBoardCells()[i], this.model.getBoard().getHeight());
        }

        final ArrayList<PlayerTransferObject> playerTransferObjectArrayList = new ArrayList<>(this.model.getPlayerCount());
        final ArrayList<WallTransferObject> wallTransferObjectArrayList = new ArrayList<>();

        this.model.getPlayers().forEach(((id, player) -> {
            final PlayerTransferObject playerTransferObject = this.getPlayerTransferObject(id);

            playerTransferObjectArrayList.add(playerTransferObject);

            for (WallData wallData : player.getPlayerWallsPlaced()) { // Wall transfer object
                WallType[][] wallShapeCopy = wallData.getWallShape();

                for (int i = 0; i < wallData.getWidth(); i++) { //copy wall shape
                    wallShapeCopy[i] = Arrays.copyOf(wallData.getWallShape()[i], wallData.getWallShape()[i].length);
                }

                final WallTransferObject wallTransferObject = new WallTransferObject(playerTransferObject, wallData.getWallType(),
                        model.getTurnCount() - wallData.getCreationTurn(), wallData.getPositionOnBoard(), wallShapeCopy);

                wallTransferObjectArrayList.add(wallTransferObject);
            }
        }));

        return new SuccessResponse<>(
                new BoardTransferObject(
                        cellTypesCopy,
                        wallTransferObjectArrayList, this.model.getTurnCount(),
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

        this.matchManager.movePlayerAdvancingTurn(this.model.getPlayers().get(playerId), point);
        return new SuccessResponse<>(null, "Updated position in the model");
    }

    public ServiceResponse<WallTransferObject> getWallPreview(final Point positionOnBoard, final WallType wallType) {
        final Wall newWall = this.createNewWall(positionOnBoard, wallType);

        final ServiceResponse<Void> assertion = canPlaceWall(this.model.getPlayerInTurnId(), positionOnBoard, wallType);

        if (assertion instanceof ErrorResponse) {
            return new ErrorResponse<>("Wall cannot be placed");
        }

        return new SuccessResponse<>(new WallTransferObject(this.getPlayerTransferObject(this.model.getPlayerInTurnId()), wallType, -1, positionOnBoard, newWall.getWallShape()), "Wall preview created");
    }

    private PlayerTransferObject getPlayerTransferObject(int playerId) {
        final Player player = this.model.getPlayers().get(playerId);
        return new PlayerTransferObject(playerId, player.getName(), new Point(player.getPosition()),
                this.model.getPlayerInTurnId() == playerId, this.matchManager.getPossibleMovements(player), player.getWallsInField(),
                this.model.getDifficulty().getTimePerTurn() - player.getTimePlayed(),
                player.getPlayerWalls());
    }

    private Wall createNewWall(final Point positionOnBoard, final WallType wallType) {
        // TODO : create a WallFactory
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

    private ServiceResponse<Void> wallAssertion(final int playerId, final Wall wall) {

        if (wall.getWallType() == null || wall.getPositionOnBoard() == null) {
            return new ErrorResponse<>("Walls passed as parameter must have defined its position, use wall.getDataWall().setPositionOnBoard()");
        }
        if (wall.getPositionOnBoard().x % 2 == 1 && wall.getPositionOnBoard().y % 2 == 1) {
            return new ErrorResponse<>("Walls cannot be placed in corners");
        }
        final Player player = this.model.getPlayers().get(playerId);
        if (player.getPlayerWalls().get(wall.getWallType()) <= 0) {
            return new ErrorResponse<>(player.getName() + " already placed all of his " + wall.getWallType().toString() + " Walls");
        }
        return null;
    }

    private ServiceResponse<Void> placeWallProcess(final int playerId, final Point positionOnBoard, final WallType wallType, boolean placeIt) {

        final Wall wall = this.createNewWall(positionOnBoard, wallType);

        final ServiceResponse<Void> assertion = this.validBasicParameters(playerId);
        if (this.validBasicParameters(playerId) != null) {
            return assertion;
        }

        final ServiceResponse<Void> wallAssertion = this.wallAssertion(playerId, wall);
        if (wallAssertion != null) {
            return wallAssertion;
        }

        final ArrayList<Point> newWalls = new ArrayList<>();

        if (this.addNewWallsIfIsPossible(wall, newWalls) != null) {
            return this.addNewWallsIfIsPossible(wall, newWalls);
        }

        if (placeIt) {
            this.matchManager.executePlaceWall(this.model.getPlayers().get(playerId), wall, newWalls);
            return new SuccessResponse<>(null, "Ok, The Wall was placed");
        }
        return new SuccessResponse<>(null, "Can place the Wall");
    }

    private ServiceResponse<Void> addNewWallsIfIsPossible(Wall wall, ArrayList<Point> newWalls) {

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
        if (this.matchManager.isABlockerWallFor(newWalls, this.model.getPlayers().get(this.model.getPlayerInTurnId()))) {
            return new ErrorResponse<>("You cannot block the path, chose another position");
        }
        return null;
    }

    public ServiceResponse<Void> canPlaceWall(final int playerId, final Point positionOnBoard, final WallType wallType) {  //TODO : return a copy of the board with the new wall?
        ServiceResponse<Void> response = this.placeWallProcess(playerId, positionOnBoard, wallType, false);
        if (response.ok) {
            return new SuccessResponse<>(null, response.message);
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
        if (this.model.getMatchState() == GameModel.MatchState.WINNER) { // TODO : Error for Winner?
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

    public ServiceResponse<Boolean> isThereAWall(Point point) {
        if (!this.isPointInsideBoard(point.x, point.y)) {
            return new ErrorResponse<>("Point off the board");
        }
        if (point.x % 2 == 0 && point.y % 2 == 0) {
            return new ErrorResponse<>("A cell was selected, select a Wall");
        }

        return new SuccessResponse<>(this.model.getBoard().getWallData(point.x, point.y) != null, "Ok");
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
