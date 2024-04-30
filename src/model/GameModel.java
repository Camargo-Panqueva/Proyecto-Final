package model;

import model.parameters.GameMode;
import model.parameters.GameModes;
import model.states.StateManager;

public final class GameModel {

    private int height;
    private int width;

    private int nPlayers;
    private int nWall;

     public GameState gameState;

     private GameMode gameMode;

    private final StateManager stateManager;

    public GameModel() {
        this.stateManager = new StateManager();
        this.gameState = GameState.STARTED;
    }

    public void startGame(GameModes gameMode){
        this.gameMode = gameMode.getGameModeClass();
        this.builtParams();
    }

    private void builtParams(){
        this.height = this.gameMode.getBoardHeight();
        this.width = this.gameMode.getBoardWidth();
        this.nPlayers = this.gameMode.getNumOfPlayers();
        this.nWall = this.gameMode.getNumOfWalls();
        this.gameState = GameState.READY;
    }

    public StateManager getStateManager() {
        return this.stateManager;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getNPlayers() {
        return nPlayers;
    }

    public int getNWall() {
        return nWall;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public enum GameState{
        READY,
        STARTED,
        PLAYING,
        PAYER1WON,
        PLAYER2WON;
    }
}
