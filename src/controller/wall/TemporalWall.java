package controller.wall;

import controller.logic.MatchManager;
import model.wall.WallType;

public class TemporalWall extends Wall {
    private final short turnRemaining;

    public TemporalWall() {
        super.wallData.setWidth(3);
        super.wallData.setHeight(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.getWallData().setWallType(WallType.TEMPORAL_WALL);

        super.wallData.getWallShape()[0][0] = WallType.TEMPORAL_WALL;
        super.wallData.getWallShape()[1][0] = WallType.TEMPORAL_WALL;
        super.wallData.getWallShape()[2][0] = WallType.TEMPORAL_WALL;

        turnRemaining = 4;
    }

    @Override
    public void action(MatchManager matchManager) {

    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {
        if (matchManager.getTurnCount() - this.getCreationTurn() >= this.turnRemaining) {
            matchManager.executeDeleteWall(this.getWallId());
        }
    }
}
