package view.context;

import java.awt.*;

public final class Style {

    public Color borderColor;
    public Color backgroundColor;
    public Color foregroundColor;
    public Font font;
    public Cursor cursor;

    public int x;
    public int y;
    public int width;
    public int height;

    public int paddingX;
    public int paddingY;
    public int borderRadius;
    public int borderWidth;

    public Style() {
        this.borderColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.foregroundColor = Color.BLACK;
        this.paddingX = 0;
        this.paddingY = 0;
        this.borderRadius = 0;
        this.borderWidth = 0;
        this.width = 0;
        this.height = 0;
        this.x = 0;
        this.y = 0;

        this.font = new Font("Arial", Font.PLAIN, 12);
        this.cursor = Cursor.getDefaultCursor();
    }

    public Point getLocation() {
        return new Point(x, y);
    }

    public void setLocation(Point location) {
        this.x = location.x;
        this.y = location.y;
    }

    public Dimension getSize() {
        return new Dimension(width, height);
    }

    public void setSize(Dimension size) {
        this.width = size.width;
        this.height = size.height;
    }

    public void centerHorizontally(ContextProvider context) {
        int canvasSize = context.window().getCanvasSize();
        this.x = (canvasSize - this.width) / 2;
    }

    public void centerVertically(ContextProvider context) {
        int canvasSize = context.window().getCanvasSize();
        this.y = (canvasSize - this.height) / 2;
    }
}
