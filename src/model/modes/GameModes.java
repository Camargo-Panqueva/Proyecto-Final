package model.modes;

import java.util.ArrayList;

public enum GameModes {
    NORMAL_TWO_PLAYERS("Player vs Player"),
    NORMAL_PLAYER_IA("Player vs IA"),
    NORMAL_FOUR_PLAYERS("4 Players"),
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
