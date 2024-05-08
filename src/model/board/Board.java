package model.board;

import model.cell.CellType;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.UUID;

public final class Board {

    private final CellType[][] boardCells;
    private final WallData[][] boardWalls;

    private int height;
    private int width;

    public Board(final int width, final int height) {
        this.height = height;
        this.width = width;

        this.boardWalls = new WallData[2 * width - 1][2 * height - 1];
        this.boardCells = new CellType[width][height];
        this.createCells();
    }

    private void createCells() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.boardCells[x][y] = CellType.NORMAL;
            }
        }
    }

    public CellType getCell(final int x, final int y) {
        return this.boardCells[x][y];
    }

    public WallData getWallData(final int x, final int y) {
        return this.boardWalls[x][y];
    }

    public UUID getWallId(Point point) {
        return this.boardWalls[point.y][point.x].getWallId();
    }

    public WallData[][] getBoardWalls() {
        return boardWalls;
    }

    public CellType[][] getBoardCells() {
        return boardCells;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String toString() {
        if (this.boardWalls == null) {
            return "Null";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (int x = 0; x < this.width * 2 - 1; x++) {
            for (int y = 0; y < this.height * 2 - 1; y++) {

                final WallData currPosition = this.boardWalls[x][y];

                if (currPosition == null) {
                    sb.append("   0   ").append(" ");
                    if (i % (this.width * 2 - 1) == 0) {
                        sb.append("\n");
                    }
                    i++;
                    continue;
                }
                if (currPosition.getWallType() == WallType.NORMAL) {
                    sb.append("  Nor  ").append(" ");
                } else if (currPosition.getWallType() == WallType.LARGE) {
                    sb.append("  Lar  ").append(" ");
                }
                if (i % (this.width * 2 - 1) == 0) {
                    sb.append("\n");
                }
                i++;
            }
        }

        return sb.toString();
    }
}
