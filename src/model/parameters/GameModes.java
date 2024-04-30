package model.parameters;

import java.util.ArrayList;

public enum GameModes {
    NORMAL("Normal", new TwoNormalMode()),
    NORMAL_4_PLAYERS("Normal 4 Players", new FourNormalMode());

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
