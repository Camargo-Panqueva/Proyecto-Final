package model.modes;

public final class GameModeManager {
    private GameModeBases currentGameMode;

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
            default:
        }
    }

    public GameModes getCurrentGameMode() {
        return currentGameMode.getGameModeType();
    }

    public GameModeBases getBaseParameters() {
        return currentGameMode;
    }
}
