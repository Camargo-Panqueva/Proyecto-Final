package model.player;

import java.io.Serializable;

public enum PlayerType implements Serializable {
    PLAYER("Player"),
    AI("AI");

    private final String playerString;

    PlayerType(final String playerString) {
        this.playerString = playerString;
    }

    @Override
    public String toString() {
        return this.playerString;
    }
}
