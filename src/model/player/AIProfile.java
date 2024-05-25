package model.player;

import java.io.Serializable;

/**
 * Represents the AI profile levels in the game.
 */
public enum AIProfile implements Serializable {

    /**
     * Beginner AI profile.
     */
    BEGINNER("Beginner &"),

    /**
     * Intermediate AI profile.
     */
    INTERMEDIATE("Intermediate #"),

    /**
     * Advanced AI profile.
     */
    ADVANCED("Advanced !");

    private final String stringAIProfile;

    /**
     * Constructor to initialize the AI profile with a specific string representation.
     *
     * @param stringAIProfile The string representation of the AI profile.
     */
    AIProfile(final String stringAIProfile) {
        this.stringAIProfile = stringAIProfile;
    }

    @Override
    public String toString() {
        return this.stringAIProfile;
    }
}
