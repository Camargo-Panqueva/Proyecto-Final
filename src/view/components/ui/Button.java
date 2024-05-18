package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

/**
 * Represents a button component that can be rendered on the screen.
 * <p>
 * This class represents a button component that can be rendered on the screen.
 * It provides a basic structure for rendering buttons on the screen and
 * handling mouse events.
 * <p>
 */
public final class Button extends GameComponent {

    private final String text;

    /**
     * Creates a new Button component with the given text and context provider.
     *
     * @param text    the text to render in the button.
     * @param context the context provider for the component.
     */
    public Button(String text, GlobalContext context) {
        super(context);
        this.text = text;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Updates the component's logic.
     * <p>
     * This method only handles mouse events for the button.
     * It runs the {@link #pollMouseEvents} method to check for mouse events.
     * </p>
     */
    @Override
    public void update() {
        this.pollMouseEvents();
    }

    /**
     * Renders the Button component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(this.style.font);
        Color backgroundColor = this.globalContext.currentTheme().getColor(this.style.backgroundColor);
        Color foregroundColor = this.globalContext.currentTheme().getColor(this.style.foregroundColor);

        graphics.setColor(backgroundColor);
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        Point center = this.getCenter();
        Rectangle textBounds = graphics.getFontMetrics().getStringBounds(this.text, graphics).getBounds();

        graphics.setColor(foregroundColor);
        graphics.drawString(this.text, center.x - textBounds.width / 2, center.y + textBounds.height / 4);
    }

    /**
     * Fits the component's size to its content.
     * <p>
     * This method is not implemented for the Button component.
     * Button components have a fixed size and do not need to fit to their content.
     * </p>
     */
    @Override
    public void fitSize() {
    }

    /**
     * Sets up the default style for the component.
     * <p>
     * This method sets up the default style for the button component.
     * It sets the background color to the primary color of the current theme,
     * the foreground color to the background color of the current theme,
     * the font to a {@code 26pt} font, the height to {@code 60px}, the width to {@code 300px},
     * and the border radius to {@code 16px}.
     * <br>
     * This method is called in the constructor of the component.
     * </p>
     */
    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = new ThemeColor(ColorName.PRIMARY, ColorVariant.NORMAL);
        this.style.foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.style.font = this.globalContext.window().getCanvas().getFont().deriveFont(26.0f);
        this.style.height = 60;
        this.style.width = 300;
        this.style.borderRadius = 16;
    }
}
