package model.wall;

public enum WallType {
    NORMAL("Normal"),
    LARGE("Large"),
    TEMPORAL_WALL("Temporal"),
    ALLY("Ally");

    private final String name;

    WallType(String type) {
        this.name = type;
    }

    @Override
    public String toString() {
        return name;
    }
}