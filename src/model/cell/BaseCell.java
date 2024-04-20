package model.cell;

import java.awt.*;

public abstract class BaseCell {

    private final Point location;

    public BaseCell(final int x, final int y) {
        this.location = new Point(x, y);
    }

    public Point getLocation() {
        return this.location;
    }

    public abstract CellType getType();

    public enum CellType {
        NORMAL
    }
}
