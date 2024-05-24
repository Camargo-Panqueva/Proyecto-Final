package model.player;

import java.io.Serializable;

public enum AIProfile implements Serializable {
    BEGINNER("Beginner &"),
    INTERMEDIATE("Intermediate #"),
    ADVANCED("Advanced !");

    private final String stringAIProfile;

    AIProfile(final String stringAIProfile) {
        this.stringAIProfile = stringAIProfile;


    }

    @Override
    public String toString() {
        return this.stringAIProfile;
    }
}
