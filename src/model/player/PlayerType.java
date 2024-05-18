package model.player;

public enum PlayerType {
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
