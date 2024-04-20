package model.board;

import model.cell.BaseCell;
import model.cell.NormalCell;

public final class Board {
    private final BaseCell[] baseCells;

    public Board(final int cellCount) {
        this.baseCells = new BaseCell[cellCount * cellCount];
        this.createCells();
    }

    private void createCells() {
        for (int x = 0; x < this.getCellCount(); x++) {
            for (int y = 0; y < this.getCellCount(); y++) {
                this.baseCells[x + y * this.getCellCount()] = new NormalCell(x, y);
            }
        }
    }

    public int getCellCount() {
        return this.baseCells.length;
    }

    public BaseCell[] getCells() {
        return this.baseCells;
    }
}
