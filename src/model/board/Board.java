package model.board;

import model.cell.CellType;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

/**
 * Represents the game board.
 */
public final class Board implements Serializable {

    /**
     * The cells of the board.
     */
    private final CellType[][] boardCells;

    /**
     * The walls on the board.
     */
    private final WallData[][] boardWalls;

    /**
     * The height of the board.
     */
    private final int height;

    /**
     * The width of the board.
     */
    private final int width;

    /**
     * Indicates if the board is set up randomly.
     */
    private boolean isRandomly;

    public Board(final int width, final int height) {
        this.height = height;
        this.width = width;

        this.boardWalls = new WallData[2 * width - 1][2 * height - 1];
        this.boardCells = new CellType[width][height];
        this.isRandomly = false;
    }

    public CellType getCellType(final int x, final int y) {
        return this.boardCells[x][y];
    }

    public CellType getCellType(final Point point) {
        return this.boardCells[point.x][point.y];
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

    public void setAsRandomly() {
        this.isRandomly = true;
    }

    public boolean getIsRandomly() {
        return this.isRandomly;
    }

    @Override
    public String toString() {
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
