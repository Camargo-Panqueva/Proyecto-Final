package model.wall;

import java.io.Serializable;

public enum WallType implements Serializable {
    /**
     * Normal wall type.
     */
    NORMAL("Normal"),

    /**
     * Large wall type.
     */
    LARGE("Large"),

    /**
     * Temporal wall type.
     */
    TEMPORAL_WALL("Temporal"),

    /**
     * Ally wall type.
     */
    ALLY("Ally");

    private final String name;

    /**
     * Constructor to initialize the wall type with a name.
     *
     * @param type The name of the wall type.
     */
    WallType(String type) {
        this.name = type;
    }

    @Override
    public String toString() {
        return name;
    }
}