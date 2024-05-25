package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallType;

/**
 * Represents an allayed wall in the game.
 * This class extends Wall and defines the properties and behavior specific to allay walls.
 */
public class AllyWall extends Wall {

/**
 * Constructs an AllyWall object and initializes its properties.
 * Ally walls have a specific size, shape, and properties.
 * Ally walls are used to protect the player's base.
 */
    public AllyWall() {
        super.wallData.setWidth(3);
        super.wallData.setHeight(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.setWallType(WallType.ALLY);

        super.wallData.getWallShape()[0][0] = WallType.ALLY;
        super.wallData.getWallShape()[1][0] = WallType.ALLY;
        super.wallData.getWallShape()[2][0] = WallType.ALLY;

        super.wallData.setAlly(true);
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
