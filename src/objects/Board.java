package objects;

import graphics.Colors;

import java.awt.*;

public final class Board {

    public static final int CELL_SIZE = 40;
    public static final int WALL_SIZE = 16;
    public static final int BOARD_PADDING = 60;

    public static final int BORDER_RADIUS = 60;

    private final int realSize;

    private Point drawingOffset;

    public Board(final int cellCount) {
        this.realSize = BOARD_PADDING * 2 + CELL_SIZE * cellCount + WALL_SIZE * (cellCount - 1);

        this.drawingOffset = new Point(0, 0);
    }

    public void calculateDrawingOffset(final int canvasSize) {
        final int x = (canvasSize - realSize) / 2;
        final int y = (canvasSize - realSize) / 2;

        this.drawingOffset = new Point(x, y);
    }

    public void draw(final Graphics2D graphics) {

        graphics.setColor(Colors.BOARD_BACKGROUND);
        graphics.fillRoundRect(
                this.drawingOffset.x,
                this.drawingOffset.y,
                realSize,
                realSize,
                BORDER_RADIUS,
                BORDER_RADIUS
        );
    }

    public int getRealSize() {
        return this.realSize;
    }
}
