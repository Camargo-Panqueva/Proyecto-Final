package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallData;
import model.wall.WallType;
/**
 * Represents a normal wall in the game.
 * This class extends Wall and defines the properties and behavior specific to normal walls.
 */
public class NormalWall extends Wall {

    /**
     * Constructs a NormalWall object and initializes its properties.
     * Normal walls have a specific size, shape, and properties.
     */
    public NormalWall() {
        super.wallData.setWidth(3);
        super.wallData.setHeight(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.setWallType(WallType.NORMAL);

        super.wallData.getWallShape()[0][0] = WallType.NORMAL;
        super.wallData.getWallShape()[1][0] = WallType.NORMAL;
        super.wallData.getWallShape()[2][0] = WallType.NORMAL;

        super.wallData.setAlly(false);
    }

    public NormalWall(WallData wallData) {
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
