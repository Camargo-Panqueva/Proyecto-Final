package controller.wall;

import controller.cells.Cell;
import controller.cells.NormalCell;
import model.wall.WallType;

public class WallManager {
    public Cell getCellInstance(WallType wallType) {
        switch (wallType) {
            case NORMAL:
                return new NormalCell();
            default:
                return null;
        }
    }


}
