package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.input.MouseEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;

/**
 * A component that allows the user to select an option from a list of options.
 * <p>
 * This class represents a selector component that allows the user to select an option from a list of options.
 * It provides a basic structure for rendering a selector component on the screen.
 * </p>
 *
 * @param <T> the type of the options in the selector.
 */
public final class Selector<T> extends GameComponent {

    private final ArrayList<T> options;
    private final SelectorType type;
    private final int min;
    private final int max;
    private int selectedOption;

    /**
     * Creates a new Selector with the given options and context provider.
     *
     * @param options       the list of options for the selector.
     * @param globalContext the context provider for the selector.
     */
    public Selector(ArrayList<T> options, GlobalContext globalContext) {
        super(globalContext);

        this.options = options;
        this.selectedOption = 0;
        this.min = 0;
        this.max = options.size() - 1;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.type = SelectorType.OBJECT;
    }

    public Selector(int min, int max, GlobalContext globalContext) {
        super(globalContext);

        this.options = null;
        this.selectedOption = min;
        this.min = min;
        this.max = max;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.type = SelectorType.NUMBER;
    }

    public Selector(boolean defaultValue, GlobalContext globalContext) {
        super(globalContext);

        this.options = null;
        this.selectedOption = defaultValue ? 1 : 0;
        this.min = 0;
        this.max = 1;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.type = SelectorType.BOOLEAN;
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

        FontMetrics fontMetrics = this.globalContext.window().getCanvas().getFontMetrics(this.style.font);
        Color backgroundColor = this.globalContext.currentTheme().getColor(this.getStyle().backgroundColor);
        Color foregroundColor = this.globalContext.currentTheme().getColor(this.getStyle().foregroundColor);

        graphics.setColor(backgroundColor);
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        int textWidth = fontMetrics.stringWidth(this.getStringValue());
        int textHeight = fontMetrics.getHeight();
        int adjust = 8;

        graphics.setColor(foregroundColor);
        graphics.drawString(
                this.getStringValue(),
                this.style.x + (this.style.width - textWidth) / 2,
                this.style.y + (this.style.height + textHeight - adjust) / 2
        );

        graphics.drawString(
                ">",
                this.style.x + this.style.width - 32,
                this.style.y + (this.style.height + textHeight - adjust) / 2
        );
        graphics.drawString(
                "<",
                this.style.x + 16,
                this.style.y + (this.style.height + textHeight - adjust) / 2
        );
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
        this.style.backgroundColor = new ThemeColor(ColorName.PRIMARY, ColorVariant.NORMAL);
        this.style.foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.style.font = this.globalContext.window().getCanvas().getFont().deriveFont(26.0f);
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
                this.decrementSelectedOption();
            } else {
                this.incrementSelectedOption();
            }
        });
    }

    private void incrementSelectedOption() {
        if (this.type == SelectorType.OBJECT)
            this.selectedOption = (this.selectedOption + 1) % this.options.size();
        else if (this.type == SelectorType.NUMBER)
            this.selectedOption += this.selectedOption < this.max ? 1 : 0;
        else {
            this.selectedOption = this.selectedOption == 0 ? 1 : 0;
        }
    }

    private void decrementSelectedOption() {
        if (this.type == SelectorType.OBJECT)
            this.selectedOption = (this.selectedOption - 1 + this.options.size()) % this.options.size();
        else if (this.type == SelectorType.NUMBER)
            this.selectedOption -= this.selectedOption > this.min ? 1 : 0;
        else {
            this.selectedOption = this.selectedOption == 1 ? 0 : 1;
        }
    }

    /**
     * Gets the selected option from the selector.
     *
     * @return the selected option from the selector.
     */
    public T getSelectedOption() {

        return switch (this.type) {
            case OBJECT -> this.options.get(this.selectedOption);
            case NUMBER -> (T) Integer.valueOf(this.selectedOption);
            case BOOLEAN -> (T) Boolean.valueOf(this.selectedOption == 1);
        };
    }

    private String getStringValue() {
        return switch (this.type) {
            case OBJECT -> this.options.get(this.selectedOption).toString();
            case NUMBER -> Integer.toString(this.selectedOption);
            case BOOLEAN -> this.selectedOption == 1 ? "Yes" : "No";
        };
    }

    public enum SelectorType {
        OBJECT,
        NUMBER,
        BOOLEAN
    }
}
