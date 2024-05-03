package model.modes;

import java.util.ArrayList;

public enum GameModes {
    NORMAL_TWO_PLAYERS("2 Players"),
    NORMAL_FOUR_PLAYERS("4 Players"),
    NORMAL_PLAYER_IA("1 vs AI"),
    CUSTOM("Custom");

    private final String mode;

    GameModes(String mode) {
        this.mode = mode;
    }

    public String getMode() {
        return mode;
    }

    public static ArrayList<String> getModes() {
        final ArrayList<String> modes = new ArrayList<>();
        for (GameModes gameMode : GameModes.values()) {
            modes.add(gameMode.getMode());
        }
        return modes;
    }
}
