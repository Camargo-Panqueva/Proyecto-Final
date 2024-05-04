package view.context;

import java.awt.*;

/**
 * Represents a style object that can be applied to components.
 * <p>
 * This class represents a style object that can be applied to components.
 * It provides a structure for storing style properties such as colors, fonts, and sizes.
 * The style object can be applied to components to change their appearance.
 * </p>
 */
public final class Style {

    /**
     * The color of the component's border.
     */
    public Color borderColor;

    /**
     * The color of the component's background.
     */
    public Color backgroundColor;

    /**
     * The color of the component's foreground.
     */
    public Color foregroundColor;

    /**
     * The font of the component's text.
     */
    public Font font;

    /**
     * The cursor of the component.
     */
    public Cursor cursor;

    /**
     * The x-coordinate in {@code pixels} of the component respective to the canvas.
     */
    public int x;

    /**
     * The y-coordinate in {@code pixels} of the component respective to the canvas.
     */
    public int y;

    /**
     * The width in {@code pixels} of the component.
     */
    public int width;

    /**
     * The height in {@code pixels} of the component.
     */
    public int height;

    /**
     * The padding in {@code pixels} on the x-axis of the component.
     */
    public int paddingX;

    /**
     * The padding in {@code pixels} on the y-axis of the component.
     */
    public int paddingY;

    /**
     * The border radius in {@code pixels} of the component.
     */
    public int borderRadius;

    /**
     * The border width in {@code pixels} of the component.
     */
    public int borderWidth;

    /**
     * Creates a new style object with default values.
     */
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

    /**
     * Centers the component horizontally on the canvas.
     * <p>
     * This method centers the component horizontally on the canvas.
     * It calculates the x-coordinate of the component to center it horizontally.
     * The component must have a width set for this method to work.
     * The method uses the canvas size from the context provider to calculate the center.
     * </p>
     *
     * @param context the context provider for the component.
     */
    public void centerHorizontally(ContextProvider context) {
        int canvasSize = context.window().getCanvasSize();
        this.x = (canvasSize - this.width) / 2;
    }

    /**
     * Centers the component vertically on the canvas.
     * <p>
     * This method centers the component vertically on the canvas.
     * It calculates the y-coordinate of the component to center it vertically.
     * The component must have a height set for this method to work.
     * The method uses the canvas size from the context provider to calculate the center.
     * </p>
     *
     * @param context the context provider for the component.
     */
    public void centerVertically(ContextProvider context) {
        int canvasSize = context.window().getCanvasSize();
        this.y = (canvasSize - this.height) / 2;
    }
}
