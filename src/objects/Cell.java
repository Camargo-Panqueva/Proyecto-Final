package objects;

import graphics.Colors;
import interfaces.Drawable;

import java.awt.*;

import static game.Constants.*;

public final class Cell implements Drawable {

    private final Point location;
    private Point boardDrawingOffset;

    public Cell(final int x, final int y) {
        this.location = new Point(x, y);

        this.boardDrawingOffset = new Point(0, 0);
    }

    @Override
    public void draw(Graphics2D graphics) {

        int x = this.boardDrawingOffset.x + BOARD_PADDING + this.location.x * CELL_SIZE + this.location.x * WALL_SIZE;
        int y = this.boardDrawingOffset.y + BOARD_PADDING + this.location.y * CELL_SIZE + this.location.y * WALL_SIZE;

        graphics.setColor(Colors.CELL_BACKGROUND);
        graphics.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, CELL_BORDER_RADIUS, CELL_BORDER_RADIUS);
    }

    public void setBoardDrawingOffset(final Point offset) {
        this.boardDrawingOffset = new Point(offset);
    }

    public Point getLocation() {
        return this.location;
    }
}
