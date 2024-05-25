package model.player;

import java.io.Serializable;

/**
 * Represents the type of player in the game.
 */
public enum PlayerType implements Serializable {
    /**
     * Represents a human player.
     */
    PLAYER("Player"),

    /**
     * Represents an AI player.
     */
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
