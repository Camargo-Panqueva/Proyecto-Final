package objects;

import graphics.Colors;
import interfaces.Drawable;

import java.awt.*;

import static game.Constants.*;

public final class Board implements Drawable {
    private final Cell[] cells;
    private final int realSize;
    private final int cellCount;

    private Point drawingOffset;

    public Board(final int cellCount) {
        this.cellCount = cellCount;
        this.realSize = BOARD_PADDING * 2 + CELL_SIZE * cellCount + WALL_SIZE * (cellCount - 1);

        this.drawingOffset = new Point(0, 0);
        this.cells = new Cell[cellCount * cellCount];

        this.createCells();
    }

    private void createCells() {
        for (int x = 0; x < this.cellCount; x++) {
            for (int y = 0; y < this.cellCount; y++) {
                this.cells[x + y * this.cellCount] = new Cell(x, y);
            }
        }
    }

    private void setCellsDrawingOffset() {
        for (final Cell cell : this.cells) {
            cell.setBoardDrawingOffset(this.drawingOffset);
        }
    }

    public void calculateDrawingOffset(final int canvasSize) {
        final int x = (canvasSize - realSize) / 2;
        final int y = (canvasSize - realSize) / 2;

        this.drawingOffset = new Point(x, y);
        this.setCellsDrawingOffset();
    }

    public void draw(final Graphics2D graphics) {

        int x = this.drawingOffset.x;
        int y = this.drawingOffset.y;

        graphics.setColor(Colors.BOARD_BACKGROUND);
        graphics.fillRoundRect(x, y, realSize, realSize, BOARD_BORDER_RADIUS, BOARD_BORDER_RADIUS);

        for (final Cell cell : this.cells) {
            cell.draw(graphics);
        }
    }

    public int getRealSize() {
        return this.realSize;
    }
}
