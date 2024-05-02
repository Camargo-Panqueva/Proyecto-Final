package model.modes;

public final class GameModeManager {
    private GameModeBases currentGameMode;
    private boolean hasBeenSet = false;

    public GameModeManager() {
        this.currentGameMode = new TwoNormalMode();
    }

    public void setCurrentGameMode(GameModes selectedGameMode) {

        switch (selectedGameMode) {
            case NORMAL_TWO_PLAYERS, NORMAL_PLAYER_IA:
                this.currentGameMode = new TwoNormalMode();
                break;
            case NORMAL_FOUR_PLAYERS:
                this.currentGameMode = new FourNormalMode();
                break;
            case CUSTOM:
                this.currentGameMode = new CustomBaseParameters();
            default:
                this.currentGameMode = new TwoNormalMode();
        }
    }

    public void setCurrentGameMode(GameModes selectedGameMode, final int width,final int height, final int playerCount, final int wallCount) {
        switch (selectedGameMode) {
            case CUSTOM:
                this.currentGameMode = new CustomBaseParameters(width, height, playerCount, wallCount);
                break;
            default:
                this.currentGameMode = new TwoNormalMode();
        }
    }

    public void setCurrentParameters(){
        this.hasBeenSet = true;
    }

    public GameModes getCurrentGameMode() {
        return currentGameMode.getGameModeType();
    }

    public GameModeBases getBaseParameters() {
        return currentGameMode;
    }
}
