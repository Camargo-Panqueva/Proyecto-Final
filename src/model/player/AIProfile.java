package model.player;

public enum AIProfile {
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
