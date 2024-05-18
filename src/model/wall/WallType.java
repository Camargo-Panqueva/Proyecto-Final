package model.wall;

public enum WallType {
    NORMAL("NORMAL"),
    LARGE("LARGE"),
    TEMPORAL_WALL("TEMPORAL"),
    ALLY("ALLY");

    private final String typeString;

    WallType(String type) {
        this.typeString = type;
    }

    @Override
    public String toString() {
        return typeString;
    }
}