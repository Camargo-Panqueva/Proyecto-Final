package controller.wall;

import model.GameModel;
import model.wall.WallType;

import java.util.ArrayList;

public class NormalWall extends Wall {

    public NormalWall() {

        super.wallData.setWidth(3);

        int length = super.wallData.getWidth();

        super.wallData.setWallType(WallType.NORMAL);

        super.wallData.setWidth(3);
        super.wallData.setHeight(3);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);


        super.wallData.getWallShape()[1][0] =  WallType.NORMAL;
        super.wallData.getWallShape()[1][1] = WallType.NORMAL;
        super.wallData.getWallShape()[1][2] = WallType.NORMAL;

    }

    @Override
    public void action(GameModel gameModel) {
    }


}
