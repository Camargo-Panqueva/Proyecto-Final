package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallType;

public class AllyWall extends Wall {

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
