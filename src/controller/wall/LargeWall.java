package controller.wall;

import model.GameModel;
import model.wall.WallData;
import model.wall.WallType;

public class LargeWall extends  Wall{

    public LargeWall(){
        super.wallData.setWallType(WallType.LARGE);
        super.wallData.setHeight(3);
        super.wallData.setWidth(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);

        super.wallData.getWallShape()[1][0] = WallType.LARGE;
        super.wallData.getWallShape()[1][1] = WallType.LARGE;
        super.wallData.getWallShape()[1][2] = WallType.LARGE;
    }

    public LargeWall(WallData wallData){
        super(wallData);
    }

    @Override
    public void action(GameModel gameModel) {

    }
}
