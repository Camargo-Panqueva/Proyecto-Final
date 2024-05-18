package model.modes;

public final class GameBaseParameters {
    private GameModes gameMode;
    private int boardHeight;
    private int boardWidth;
    private int playerCount;
    private int wallsPerPlayer;
    private boolean hasBeenSet;

    public GameBaseParameters() {
        this.gameMode = GameModes.NORMAL_TWO_PLAYERS;
        this.boardWidth = 9;
        this.boardHeight = 9;
        this.wallsPerPlayer = 10;
        this.hasBeenSet = false;
    }

    public void setBaseParameters(GameModes gameMode, int boardWidth, int boardHeight, int playerCount, int wallsPerPlayer) {
        if (!hasBeenSet) {
            this.gameMode = gameMode;
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

    public GameModes getGameMode() {
        return gameMode;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public int getWallsPerPlayer() {
        return wallsPerPlayer;
    }

    public boolean getHasBeenSet(){
        return this.hasBeenSet;
    }

    @Override
    public String toString() {
        return String.format("Base Parameter: boardHeight=%d, boardWidth=%d, playersCount=%d, wallsCount=%d, gameModeType=%s", boardHeight, boardWidth, playerCount, wallsPerPlayer, gameMode);
    }

}
