package model.modes;

public abstract class GameModeBases {

    public final int boardHeight;
    public final int boardWidth;

    public final int playersCount;
    public final int wallsCount;

    public GameModeBases(int boardWidth, int boardHeight, int numOfPlayers, int numOfWalls) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.playersCount = numOfPlayers;
        this.wallsCount = numOfWalls;
    }

    public abstract GameModes getGameModeType();

    @Override
    public String toString() {
        return String.format("Base Parameter: boardHeight=%d, boardWidth=%d, playersCount=%d, wallsCount=%d, gameModeType=%s", boardHeight, boardWidth, playersCount, wallsCount, getGameModeType());
    }
}
