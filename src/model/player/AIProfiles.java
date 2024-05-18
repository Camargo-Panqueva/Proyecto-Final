package model.player;

public enum AIProfiles {
    BEGINNER("Beginner &"),
    INTERMEDIATE("Intermediate #"),
    ADVANCED("Advanced !");

    private final String stringAIProfile;

    AIProfiles(final String stringAIProfile) {
        this.stringAIProfile = stringAIProfile;


    }

    @Override
    public String toString() {
        return this.stringAIProfile;
    }
}
