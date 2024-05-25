package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.context.Style;
import view.input.KeyboardEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

/**
 * Represents a text input component that can be rendered on the screen.
 * <p>
 * This class represents a text input component that can be rendered on the screen.
 * It provides a basic structure for rendering text input fields on the screen.
 * Text input fields are used to capture user input from the keyboard.
 * </p>
 */
public final class TextInput extends GameComponent {

    private static final int BLINK_INTERVAL = 1600;
    private final String placeholder;
    private String value;
    private int maxLength;

    /**
     * Creates a new TextInput component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     * @param placeholder   the placeholder text to display in the text input field.
     */
    public TextInput(GlobalContext globalContext, String placeholder) {
        super(globalContext);

        this.value = "";
        this.placeholder = placeholder;
        this.style.cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
        this.maxLength = 20;
    }

    /**
     * Updates the component's logic.
     * <p>
     * This method only handles keyboard events for the text input field.
     * It runs the {@link #pollMouseEvents} method to check for keyboard events.
     * </p>
     */
    @Override
    public void update() {
        this.pollMouseEvents();
    }

    /**
     * Renders the TextInput component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(this.style.font);

        FontMetrics metrics = graphics.getFontMetrics(this.style.font);
        Color borderColor = this.globalContext.currentTheme().getColor(this.getStyle().borderColor);
        Color backgroundColor = this.globalContext.currentTheme().getColor(this.getStyle().backgroundColor);
        Color foregroundColor = this.globalContext.currentTheme().getColor(this.getStyle().foregroundColor);
        Color placeholderColor = this.globalContext.currentTheme().getColor(this.getStyle().foregroundColor.name(), ColorVariant.DIMMED);

        graphics.setColor(
                this.hasFocus
                        ? borderColor
                        : backgroundColor
        );

        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        if (this.hasFocus) {
            int borderWidth = 4;

            graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
            graphics.fillRoundRect(
                    this.style.x + borderWidth,
                    this.style.y + borderWidth,
                    this.style.width - 2 * borderWidth,
                    this.style.height - 2 * borderWidth,
                    this.style.borderRadius,
                    this.style.borderRadius
            );
        }


        int textX;
        int placeHolderX;

        int textY = this.style.y + this.style.height / 2 + metrics.getHeight() / 4;

        if (this.style.textAlignment == Style.TextAlignment.LEFT) {
            textX = this.style.x + this.style.paddingX;
            placeHolderX = this.style.x + this.style.paddingX;
        } else if (this.style.textAlignment == Style.TextAlignment.CENTER) {
            textX = this.style.x + (this.style.width - metrics.stringWidth(this.value)) / 2;
            placeHolderX = this.style.x + (this.style.width - metrics.stringWidth(this.placeholder)) / 2;
        } else {
            textX = this.style.x + this.style.width - this.style.paddingX - metrics.stringWidth(this.value);
            placeHolderX = this.style.x + this.style.width - this.style.paddingX - metrics.stringWidth(this.placeholder);
        }

        if (this.value.isEmpty() && !this.hasFocus) {
            graphics.setColor(placeholderColor);
            graphics.drawString(this.placeholder, placeHolderX, textY);
        }

        String appendedValue = this.hasFocus && (System.currentTimeMillis() % BLINK_INTERVAL < BLINK_INTERVAL / 2) ? "&" : "";
        graphics.setColor(foregroundColor);
        graphics.drawString(this.value + appendedValue, textX, textY);
    }

    /**
     * Fits the component's size to its content.
     * <p>
     * This method is not implemented for the TextInput component.
     * TextInput components have a fixed size and do not need to fit to their content.
     * </p>
     */
    @Override
    public void fitSize() {

    }

    /**
     * Sets up the default style for the component.
     * <p>
     * This method sets up the default style for the text input component.
     * It sets the background color to the background contrast color of the current theme,
     * the foreground color to the foreground color of the current theme,
     * the border color to the primary color of the current theme,
     * the font to a {@code 26pt} font, the height to {@code 60px}, the width to {@code 300px},
     * the border radius to {@code 16px}, and the padding to {@code 16px}.
     * <br>
     * This method is called in the constructor of the component.
     * </p>
     */
    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.style.foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
        this.style.borderColor = new ThemeColor(ColorName.PRIMARY, ColorVariant.NORMAL);
        this.style.font = this.globalContext.gameFont().deriveFont(26.0f);
        this.style.height = 60;
        this.style.width = 300;
        this.style.borderRadius = 16;
        this.style.paddingX = 16;
    }

    /**
     * Sets up the default event listeners for the component.
     * <p>
     * This method sets up the default event listeners for the text input component.
     * It adds a key listener to the component to handle keyboard events.
     * </p>
     */
    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, this::handleKeyTyped);
    }

    /**
     * Handles the key typed event for the text input field.
     * <p>
     * This method handles the key typed event for the text input field.
     * It appends the typed character to the text input field's value.
     * If the backspace key is pressed, it removes the last character from the text input field's value.
     * </p>
     *
     * @param event the key typed event to handle.
     */
    private void handleKeyTyped(KeyboardEvent event) {
        String lastValue = this.value;

        if (!this.hasFocus) {
            return;
        }

        if (event.isAlphaNumeric() && this.value.length() < this.maxLength) {
            this.value += event.keyChar;
        }

        if (event.keyCode == KeyboardEvent.VK_SPACE) {
            this.value += " ";
        }

        if (event.keyCode == KeyboardEvent.VK_BACKSPACE && !this.value.isEmpty()) {
            this.value = this.value.substring(0, this.value.length() - 1);
        }

        if (!this.value.equals(lastValue)) {
            this.dispatchComponentEvent(ComponentEvent.VALUE_CHANGED, lastValue, this.value);
        }
    }

    /**
     * Sets the maximum length of the text input field.
     * <p>
     * This method sets the maximum length of the text input field.
     * If the current value of the text input field is longer than the maximum length,
     * it truncates the value to the maximum length.
     * </p>
     *
     * @param maxLength the maximum length of the text input field.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;

        if (this.value.length() > maxLength) {
            this.value = this.value.substring(0, maxLength);
            this.dispatchComponentEvent(ComponentEvent.VALUE_CHANGED, this.value, this.value);
        }
    }

    /**
     * Gets the current value of the text input field.
     *
     * @return the current value of the text input field.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the text input field.
     * <p>
     * This method sets the value of the text input field.
     * It dispatches a value changed event if the value has changed.
     * </p>
     *
     * @param value the new value of the text input field.
     */
    public void setValue(String value) {
        this.dispatchComponentEvent(ComponentEvent.VALUE_CHANGED, this.value, value);
        this.value = value;
    }
}
