package controller.wall;

import model.GameModel;
import model.wall.WallType;

import java.util.ArrayList;

public class NormalWall extends Wall {

    public NormalWall() {

        super.wallData.setWidth(1);
        super.wallData.setHeight(1);
        super.wallData.setWallShape(new WallType[super.wallData.getWidth()][super.wallData.getHeight()]);


        super.wallData.getWallShape()[0][0] =  WallType.NORMAL;

    }

    @Override
    public void action(GameModel gameModel) {
    }


}
