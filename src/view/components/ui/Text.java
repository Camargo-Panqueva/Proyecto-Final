package view.components.ui;

import view.components.GameComponent;
import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;

/**
 * Represents a text component that can be rendered on the screen.
 * <p>
 * This class represents a text component that can be rendered on the screen.
 * It provides a basic structure for rendering text on the screen.
 * <p>
 */
public final class Text extends GameComponent {

    private final String text;

    /**
     * Creates a new Text component with the given text and context provider.
     *
     * @param text            the text to render.
     * @param contextProvider the context provider for the component.
     */
    public Text(String text, ContextProvider contextProvider) {
        super(contextProvider);
        this.text = text;
    }

    /**
     * Updates the component's logic.
     * <p>
     * This method does nothing for text components.
     * Text components are static and do not require any logic updates.
     * This method is implemented to satisfy the GameComponent interface.
     * </p>
     */
    @Override
    public void update() {
    }

    /**
     * Renders the Text component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(this.style.font);
        FontMetrics fontMetrics = this.contextProvider.window().getCanvas().getFontMetrics(this.style.font);

        graphics.setColor(this.style.foregroundColor);
        graphics.drawString(this.text, this.style.x, this.style.y + this.style.height - fontMetrics.getDescent());
    }

    /**
     * Fits the component's size to its content.
     * <p>
     * This method fits the component's size to the size of the text content.
     * It uses the current font to calculate the size of the text.
     * The padding values are added to the width and height of the component.
     * </p>
     */
    @Override
    public void fitSize() {
        FontMetrics fontMetrics = this.contextProvider.window().getCanvas().getFontMetrics(this.style.font);

        this.style.width = fontMetrics.stringWidth(this.text) + this.style.paddingX;
        this.style.height = fontMetrics.getHeight() + this.style.paddingY;
    }

    /**
     * Handles a theme change event.
     * <p>
     * This method updates the foreground color of the text component to match the new theme.
     * </p>
     *
     * @param theme the new theme.
     */
    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.foregroundColor = theme.foreground;
    }

    /**
     * Sets up the default style for the component.
     * <p>
     * This method sets up the default style for the text component.
     * It sets the font to the current canvas font and the foreground color to the current theme's foreground color.
     * </p>
     */
    @Override
    protected void setupDefaultStyle() {
        this.style.font = this.contextProvider.window().getCanvas().getFont().deriveFont(16.0f);
        this.style.foregroundColor = this.contextProvider.themeManager().getCurrentTheme().foreground;
    }
}
