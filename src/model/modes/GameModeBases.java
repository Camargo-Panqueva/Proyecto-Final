package model.modes;

public abstract class GameModeBases {

    public final int boardHeight;
    public final int boardWidth;

    public final int playersCount;
    public final int wallsPerPlayer;

    public final int timeLimitPerPlayer;


    public GameModeBases(int boardWidth, int boardHeight, int playerCount, int wallsPerPlayer, int timeLimitPerPlayer) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.playersCount = playerCount;
        this.wallsPerPlayer = wallsPerPlayer;
        this.timeLimitPerPlayer = timeLimitPerPlayer;
    }

    public abstract GameModes getGameModeType();

    @Override
    public String toString() {
        return String.format("Base Parameter: boardHeight=%d, boardWidth=%d, playersCount=%d, wallsCount=%d, gameModeType=%s", boardHeight, boardWidth, playersCount, wallsPerPlayer, getGameModeType());
    }
}
