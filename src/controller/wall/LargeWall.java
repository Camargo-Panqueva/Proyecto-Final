package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallData;
import model.wall.WallType;

public class LargeWall extends Wall {

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
