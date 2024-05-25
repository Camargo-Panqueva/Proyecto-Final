package controller.wall;

import model.wall.WallType;

/**
 * Manages the creation of different types of walls based on the specified wall type.
 */
public class WallManager {

    /**
     * Creates an instance of a specific wall type based on the provided wallType parameter.
     *
     * @param wallType The type of wall to create.
     * @return An instance of the corresponding wall type.
     */
    public Wall getWallInstance(WallType wallType) {
        switch (wallType) {
            case LARGE -> {
                return new LargeWall();
            }
            case TEMPORAL_WALL -> {
                return new TemporalWall();
            }
            case ALLY -> {
                return new AllyWall();
            }
            default -> {
                return new NormalWall();// NORMAL
            }
        }
    }
}
