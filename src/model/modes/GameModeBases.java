package model.modes;

public abstract class GameModeBases {

    public final int boardHeight;
    public final int boardWidth;

    public final int playersCount;
    public final int wallsCount;

    public GameModeBases(int boardHeight, int boardWidth, int numOfPlayers, int numOfWalls) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.playersCount = numOfPlayers;
        this.wallsCount = numOfWalls;
    }

    public abstract GameModes getGameModeType();
}
