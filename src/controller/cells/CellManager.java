package controller.cells;

import model.cell.CellType;

public final class CellManager {
    public Cell getCellInstance(CellType cellType) {
        switch (cellType) {
            case NORMAL:
                return new NormalCell();
            default:
                return null;
        }
    }

}
