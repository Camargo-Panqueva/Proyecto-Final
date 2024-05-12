package model.board;

import model.cell.CellType;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.UUID;

public final class Board {

    private final CellType[][] boardCells;
    private final WallData[][] boardWalls;

    private final int height;
    private final int width;

    public Board(final int width, final int height) {
        this.height = height;
        this.width = width;

        this.boardWalls = new WallData[2 * width - 1][2 * height - 1];
        WallData wallData = new WallData();
        wallData.setWallType(WallType.NORMAL);
        this.boardWalls[5][2] = wallData;
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

        for (int y = 0; y < this.width * 2 - 1; y++) {
            for (int x = 0; x < this.height * 2 - 1; x++) {

                final WallData currPosition = this.boardWalls[x][y];

                if (currPosition == null) {
                    sb.append("   0   ").append(" ");
                    continue;
                }
                if (currPosition.getWallType() == WallType.NORMAL) {
                    sb.append("  Nor  ").append(" ");
                } else if (currPosition.getWallType() == WallType.LARGE) {
                    sb.append("  Lar  ").append(" ");
                }

            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
