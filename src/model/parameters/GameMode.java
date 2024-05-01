package model.parameters;

public abstract class GameMode {

    protected final int boardHeight;
    protected final int boardWidth;

    protected final int numOfPlayers;
    protected final int numOfWalls;

    public GameMode(int boardHeight, int boardWidth, int numOfPlayers, int numOfWalls) {
        this.boardHeight = boardHeight;
        this.boardWidth = boardWidth;
        this.numOfPlayers = numOfPlayers;
        this.numOfWalls = numOfWalls;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    public int getNumOfWalls() {
        return numOfWalls;
    }
}
