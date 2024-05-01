package model.board;

import model.cell.BaseCell;
import model.cell.NormalCell;

import java.util.ArrayList;

public final class Board {

    private final ArrayList<BaseCell> boardCells;

    private int height;
    private int width;

    public Board(final int length, final int width) {
        this.height = length;
        this.width = width;
        this.boardCells = new ArrayList<>();
        this.createCells();
    }

    private void createCells() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.boardCells.add(new NormalCell(x, y));
            }
        }
    }

    private int convertToLinePosition(final int x, final int y) {
        //TODO : Send this to controller. ERROR handler, x, y out of range
        return x + y * this.width;
    }

    public BaseCell getCell(final int x, final int y) {
        return this.boardCells.get(this.convertToLinePosition(x, y));
    }

    public ArrayList<BaseCell> getBoardCells() {
        return boardCells;
    }

    public int getCellCount() {
        return this.boardCells.size();
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
