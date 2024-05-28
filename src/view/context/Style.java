package view.context;

import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

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
    public ThemeColor borderColor;

    /**
     * The color of the component's background.
     */
    public ThemeColor backgroundColor;

    /**
     * The color of the component's foreground.
     */
    public ThemeColor foregroundColor;

    /**
     * The font of the component's text.
     */
    public Font font;

    /**
     * The cursor of the component.
     */
    public Cursor cursor;

    /**
     * The text alignment of the component.
     */
    public TextAlignment textAlignment;

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
        this.borderColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.paddingX = 0;
        this.paddingY = 0;
        this.borderRadius = 0;
        this.borderWidth = 0;
        this.width = 0;
        this.height = 0;
        this.x = 0;
        this.y = 0;
        this.textAlignment = TextAlignment.LEFT;

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
    public void centerHorizontally(GlobalContext context) {
        int canvasSize = context.window().getCanvas().getWidth();
        this.x = (canvasSize - this.width) / 2;
    }

    /**
     * Centers the component horizontally within the given bounds.
     * <p>
     * This method centers the component horizontally within the given bounds.
     * It calculates the x-coordinate of the component to center it horizontally.
     * The component must have a width set for this method to work.
     * The method uses the bounds to calculate the center.
     * </p>
     *
     * @param bounds the bounds to center the component within.
     */
    public void centerHorizontally(Rectangle bounds) {
        this.x = (bounds.width - this.width) / 2 + bounds.x;
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
    public void centerVertically(GlobalContext context) {
        int canvasSize = context.window().getCanvas().getHeight();
        this.y = (canvasSize - this.height) / 2;
    }

    /**
     * Centers the component vertically within the given bounds.
     * <p>
     * This method centers the component vertically within the given bounds.
     * It calculates the y-coordinate of the component to center it vertically.
     * The component must have a height set for this method to work.
     * The method uses the bounds to calculate the center.
     * </p>
     *
     * @param bounds the bounds to center the component within.
     */
    public void centerVertically(Rectangle bounds) {
        this.y = (bounds.height - this.height) / 2 + bounds.y;
    }

    /**
     * Enum representing the text alignment options.
     * <p>
     * This enum provides the possible values for text alignment within a component.
     * The values can be:
     * </p>
     */
    public enum TextAlignment {
        /**
         * Aligns the text to the left.
         */
        LEFT,
        /**
         * Aligns the text to the center.
         */
        CENTER,
        /**
         * Aligns the text to the right.
         */
        RIGHT
    }
}
