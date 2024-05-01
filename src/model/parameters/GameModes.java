package model.parameters;

import java.util.ArrayList;

public enum GameModes {
    NORMAL_TWO_PLAYERS("Player vs Player", new TwoNormalMode()),
    NORMAL_PLAYER_IA("Player vs IA", new TwoNormalMode()),
    NORMAL_FOUR_PLAYERS("4 Players", new FourNormalMode());

    private final String mode;
    private final GameMode gameModeClass;

    GameModes(String mode, GameMode gameModeClass) {
        this.mode = mode;
        this.gameModeClass = gameModeClass;
    }

    public String getMode() {
        return mode;
    }

    public GameMode getGameModeClass() {
        return gameModeClass;
    }

    public static ArrayList<String> getModes() {
        final ArrayList<String> modes = new ArrayList<>();
        for (GameModes gameMode : GameModes.values()) {
            modes.add(gameMode.getMode());
        }
        return modes;
    }
}
