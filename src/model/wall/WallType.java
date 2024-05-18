package model.wall;

public enum WallType {
    NORMAL("Normal"),
    LARGE("Large"),
    TEMPORAL_WALL("Temporal"),
    ALLY("Ally");

    private final String typeString;

    WallType(String type) {
        this.typeString = type;
    }

    @Override
    public String toString() {
        return typeString;
    }
}