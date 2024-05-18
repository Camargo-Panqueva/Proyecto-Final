package model.player;

public enum PlayerTypes {
    PLAYER("Player"),
    AI("AI");

    private final String playerString;

    PlayerTypes(final String playerString) {
        this.playerString = playerString;
    }

    @Override
    public String toString() {
        return this.playerString;
    }
}
