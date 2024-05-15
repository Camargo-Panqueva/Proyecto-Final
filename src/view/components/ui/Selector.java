package view.components.ui;

import view.components.GameComponent;
import view.context.ContextProvider;
import view.input.MouseEvent;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

/**
 * A component that allows the user to select an option from a list of options.
 * <p>
 * This class represents a selector component that allows the user to select an option from a list of options.
 * It provides a basic structure for rendering a selector component on the screen.
 * </p>
 */
public final class Selector extends GameComponent {

    private final ArrayList<String> options;
    private int selectedOption;

    /**
     * Creates a new Selector with the given options and context provider.
     *
     * @param options         the list of options for the selector.
     * @param contextProvider the context provider for the selector.
     */
    public Selector(ArrayList<String> options, ContextProvider contextProvider) {
        super(contextProvider);

        this.options = options;
        this.selectedOption = 0;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /**
     * Updates the component's logic.
     * <p>
     * This method only handles mouse events for the selector.
     * It runs the {@link #pollMouseEvents} method to check for mouse events.
     * The selected option is updated when the user clicks on the selector.
     * </p>
     */
    @Override
    public void update() {
        this.pollMouseEvents();
    }

    /**
     * Renders the selector component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {
        //TODO: Remove code duplication with Button render method
        graphics.setFont(this.style.font);

        graphics.setColor(this.style.backgroundColor);
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        Point center = this.getCenter();
        Rectangle textBounds = graphics.getFontMetrics().getStringBounds(this.getSelectedOption(), graphics).getBounds();

        graphics.setColor(this.style.foregroundColor);
        graphics.drawString(this.getSelectedOption(), center.x - textBounds.width / 2, center.y + textBounds.height / 4);

        graphics.drawString(">", this.style.x + this.style.width - 32, center.y + textBounds.height / 4);
        graphics.drawString("<", this.style.x + 16, center.y + textBounds.height / 4);
    }

    /**
     * Fits the component's size to its content.
     * <p>
     * This method is not implemented for the Selector component.
     * Selector components have a fixed size and do not need to fit to their content.
     * </p>
     */
    @Override
    public void fitSize() {
    }

    /**
     * Handles a theme change event.
     * <p>
     * This method updates the background color of the selector to match the new theme.
     * It sets the background color of the selector to the new theme's primary color.
     * It also sets the foreground color of the selector to the new theme's background color.
     * </p>
     */
    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.backgroundColor = theme.primary;
        this.style.foregroundColor = theme.background;
    }

    /**
     * Sets up the default style for the component.
     * <p>
     * This method sets up the default style for the selector component.
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
        //TODO: Remove code duplication with Button default style
        this.style.backgroundColor = this.contextProvider.themeManager().getCurrentTheme().primary;
        this.style.foregroundColor = this.contextProvider.themeManager().getCurrentTheme().background;
        this.style.font = this.contextProvider.window().getCanvas().getFont().deriveFont(26.0f);
        this.style.height = 60;
        this.style.width = 300;
        this.style.borderRadius = 16;
    }

    /**
     * Sets up the default event listeners for the component.
     * <p>
     * This method sets up the default event listeners for the selector component.
     * It adds a click event listener that changes the selected option when the user clicks on the selector.
     * The selected option is changed to the previous option when the user clicks on the left side of the selector.
     * The selected option is changed to the next option when the user clicks on the right side of the selector.
     * </p>
     */
    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addMouseListener(MouseEvent.EventType.RELEASED, event -> {
            Point relativePoint = event.relativeMousePosition;
            if (relativePoint.x < this.style.width / 2) {
                this.selectedOption = (this.selectedOption - 1 + this.options.size()) % this.options.size();
            } else {
                this.selectedOption = (this.selectedOption + 1) % this.options.size();
            }
        });
    }

    /**
     * Gets the selected option from the selector.
     *
     * @return the selected option from the selector.
     */
    public String getSelectedOption() {
        return this.options.get(this.selectedOption);
    }
}
