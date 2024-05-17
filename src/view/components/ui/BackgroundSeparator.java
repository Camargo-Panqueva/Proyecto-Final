package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.themes.Theme;

import java.awt.*;

/**
 * Represents a background separator component that can be rendered on the screen.
 * <p>
 * This class represents a background separator component that can be rendered on the screen.
 * It provides a basic structure for rendering background separators on the screen.
 * Background separators are used to separate sections of the screen with a background color.
 * </p>
 */
public final class BackgroundSeparator extends GameComponent {

    /**
     * Creates a new BackgroundSeparator component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public BackgroundSeparator(GlobalContext globalContext) {
        super(globalContext);
    }

    /**
     * Updates the component's logic.
     * <p>
     * This method does nothing for background separator components.
     * Background separator components are static and do not require any logic updates.
     * This method is implemented to satisfy the GameComponent interface.
     * </p>
     */
    @Override
    public void update() {
    }

    /**
     * Renders the BackgroundSeparator component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(this.style.backgroundColor);
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );
    }

    /**
     * Fits the component's size to its content.
     * <p>
     * This method does nothing for background separator components.
     * Background separator components are static and do not require any size fitting.
     * This method is implemented to satisfy the GameComponent interface.
     * </p>
     */
    @Override
    public void fitSize() {
    }

    /**
     * Handles a change in the theme.
     * <p>
     * This method updates the background color of the background separator to match the new theme.
     * It sets the background color of the background separator to the new theme's background contrast color.
     * </p>
     *
     * @param theme the new theme.
     */
    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.backgroundColor = theme.getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED);
    }

    /**
     * Sets up the default style for the component.
     * <p>
     * This method sets up the default style for the background separator component.
     * It sets the background color to the background contrast color of the current theme.
     * </p>
     */
    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = this.globalContext.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED);
    }
}
