package model.modes;

import model.wall.WallType;

import java.util.HashMap;

public final class GameBaseParameters {
    private int boardHeight;
    private int boardWidth;
    private int playerCount;
    private HashMap<WallType, Integer> wallsPerPlayer;
    private boolean hasBeenSet;

    public GameBaseParameters() {
        this.boardWidth = 9;
        this.boardHeight = 9;
        this.wallsPerPlayer = new HashMap<>();
        this.wallsPerPlayer.put(WallType.NORMAL, 10);
        this.hasBeenSet = false;
    }

    public void setBaseParameters(int boardWidth, int boardHeight, int playerCount, HashMap<WallType, Integer> wallsPerPlayer) {
        if (!hasBeenSet) {
            this.boardHeight = boardHeight;
            this.boardWidth = boardWidth;
            this.playerCount = playerCount;
            this.wallsPerPlayer = wallsPerPlayer;
        }
        this.hasBeenSet = true;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public HashMap<WallType, Integer> getWallsPerPlayer(){
        return this.wallsPerPlayer;
    }

    public HashMap<WallType, Integer> getWallsCountPerPlayer() {
        return wallsPerPlayer;
    }

    public boolean getHasBeenSet(){
        return this.hasBeenSet;
    }

    @Override
    public String toString() {
        return String.format("Base Parameter: boardHeight=%d, boardWidth=%d, playersCount=%d, wallsCount=%d, gameModeType=%s", boardHeight, boardWidth, playerCount, wallsPerPlayer);
    }

}
