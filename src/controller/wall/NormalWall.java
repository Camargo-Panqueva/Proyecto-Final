package controller.wall;

import model.GameModel;
import model.wall.WallData;
import model.wall.WallType;

public class NormalWall extends Wall {

    public NormalWall() {
        super.wallData.setWidth(1);
        super.wallData.setHeight(1);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.setWallType(WallType.NORMAL);

        super.wallData.getWallShape()[0][0] = WallType.NORMAL;
    }

    public NormalWall(WallData wallData) {
        super(wallData);
    }

    @Override
    public void action(GameModel gameModel) {
    }


}
