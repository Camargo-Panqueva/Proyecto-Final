package controller.wall;

import model.GameModel;
import model.wall.WallType;

import java.util.ArrayList;

public class NormalWall extends Wall {

    public NormalWall() {

        super.wallData.setLength(3);

        int length = super.wallData.getLength();

        super.wallData.setWallType(WallType.NORMAL);
        super.wallData.setWallShape(new ArrayList<>(length * length));

        for (int i = 0; i < length * length; i++) {
            super.wallData.getWallShape().add(null);
        }

        super.wallData.getWallShape().set(super.convertToLinePosition(0, 1), WallType.NORMAL);
        super.wallData.getWallShape().set(super.convertToLinePosition(1, 1), WallType.NORMAL);

    }

    @Override
    public void action(GameModel gameModel) {
    }


}
