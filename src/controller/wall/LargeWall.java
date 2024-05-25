package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallData;
import model.wall.WallType;

/**
 * Represents a large wall in the game.
 * This class extends Wall and defines the properties and behavior specific to large walls.
 */
public class LargeWall extends Wall {
    /**
     * Constructs a LargeWall object and initializes its properties.
     * Large walls have a specific size, shape, and properties.
     */
    public LargeWall() {
        super.wallData.setWallType(WallType.LARGE);
        super.wallData.setHeight(5);
        super.wallData.setWidth(5);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.wallData.getWallShape()[0][0] = WallType.LARGE;
        super.wallData.getWallShape()[1][0] = WallType.LARGE;
        super.wallData.getWallShape()[2][0] = WallType.LARGE;
        super.wallData.getWallShape()[3][0] = WallType.LARGE;
        super.wallData.getWallShape()[4][0] = WallType.LARGE;

        super.wallData.setAlly(false);
    }

    public LargeWall(WallData wallData) {
        super(wallData);
    }

    @Override
    public void action(MatchManager matchManager) {

    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {

    }
}
