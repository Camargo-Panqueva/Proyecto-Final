package controller.wall;

import model.wall.WallData;
import model.wall.WallType;

public class WallManager {
    public Wall getWallInstance(WallType wallType) {
        switch (wallType) {
            case NORMAL:
                return new NormalWall();
            case LARGE:
                return new LargeWall();
            default:
                return null;
        }
    }

    public Wall getWallInstance(WallData wallData){
        switch (wallData.getWallType()) {
            case NORMAL:
                return new NormalWall(wallData);
            case LARGE:
                return new LargeWall(wallData);
            default:
                return null;
        }
    }


}
