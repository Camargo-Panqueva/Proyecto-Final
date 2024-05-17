package model;

import model.board.Board;
import model.modes.GameModeManager;
import model.modes.GameModes;
import model.player.Player;
import model.wall.WallData;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public final class GameModel {

    private int wallCount;

    private Board board;

    private MatchState matchState;

    private final HashMap<Integer, Player> players;
    private final HashMap<UUID, WallData> walls;

    private int playerInTurn;

    private Player winningPlayer;

    private final GameModeManager gameModeManager;

    private int turnCount;

    public GameModel() {
        this.gameModeManager = new GameModeManager();
        this.players = new HashMap<>();
        this.walls = new HashMap<>();
        this.matchState = MatchState.INITIALIZED;
    }

    public void addPlayer(int playerId, Player newPlayer) {
        this.players.put(playerId, newPlayer);
    }

    public void addWall(UUID uuid, WallData wallData) {
        this.walls.put(uuid, wallData);
    }

    public void setBoard(final int wight, final int height) {
        this.board = new Board(wight, height);
    }

    public Board getBoard() {
        return board;
    }

    public void setMatchState(MatchState matchState) {
        this.matchState = matchState;
    }

    public MatchState getMatchState() {
        return matchState;
    }

    public void setPlayer(int playerIndex, Player newPlayer) {
        this.players.put(playerIndex, newPlayer);
    }

    public void setPlayerPosition(int indexPlayer, Point newPoint) {
        this.players.get(indexPlayer).setPosition(newPoint);
    }

    public int getPlayerCount() {
        return this.gameModeManager.getBaseParameters().playersCount;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public HashMap<Integer, Player> getPlayers() {
        return this.players;
    }

    public int getWallCount() {
        return wallCount;
    }


    public GameModeManager getGameModeManager() {
        return gameModeManager;
    }

    public GameModes getGameMode() {
        return gameModeManager.getCurrentGameMode();
    }

    public int getPlayerInTurnId() {
        return playerInTurn;
    }

    public Player getWinningPlayer() {
        return winningPlayer;
    }

    public WallData getWall(UUID uuid) {
        return this.walls.get(uuid);
    }

    public void setWallCount(int wallCount) {
        this.wallCount = wallCount;
    }

    public HashMap<UUID, WallData> getWalls() {
        return walls;
    }

    public void setPlayerInTurn(int playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public void setWinningPlayer(final Player winningPlayer) {
        this.winningPlayer = winningPlayer;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public enum MatchState {
        STARTED,
        INITIALIZED,
        PLAYING,
        WINNER;
    }
}
