package controller.wall;

import controller.logic.MatchManager;
import model.player.Player;
import model.wall.WallType;

/**
 * Represents a temporal wall in the game.
 * This class extends Wall and defines the behavior- and properties-specific to temporal walls.
 */
public class TemporalWall extends Wall {

    /**
     * Constructs a TemporalWall object and initializes its properties.
     * Temporal walls have a specific size, shape, and lifespan.
     */
    public TemporalWall() {
        super.wallData.setWidth(3);
        super.wallData.setHeight(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.getWallData().setWallType(WallType.TEMPORAL_WALL);

        super.wallData.getWallShape()[0][0] = WallType.TEMPORAL_WALL;
        super.wallData.getWallShape()[1][0] = WallType.TEMPORAL_WALL;
        super.wallData.getWallShape()[2][0] = WallType.TEMPORAL_WALL;

        super.wallData.setAlly(false);
    }
    public int getTurnsAlive() {
        return 4;
    }

    @Override
    public void action(MatchManager matchManager) {

    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {
        if (matchManager.getTurnCount() - this.getCreationTurn() >= this.getTurnsAlive()) {
            Player player = this.getOwner();
            matchManager.executeWallDeletion(this.getWallId());
            player.addWallToPlace(this.getWallType());
        }
    }
}
