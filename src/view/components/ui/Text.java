package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

/**
 * Represents a text component that can be rendered on the screen.
 * <p>
 * This class represents a text component that can be rendered on the screen.
 * It provides a basic structure for rendering text on the screen.
 * <p>
 */
public final class Text extends GameComponent {

    private String text;

    /**
     * Creates a new Text component with the given text and context provider.
     *
     * @param text          the text to render.
     * @param globalContext the context provider for the component.
     */
    public Text(String text, GlobalContext globalContext) {
        super(globalContext);
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
        FontMetrics fontMetrics = this.globalContext.window().getCanvas().getFontMetrics(this.style.font);
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

        int textWidth = fontMetrics.stringWidth(this.text);
        int textHeight = fontMetrics.getHeight();
        int adjust = 8;

        graphics.setColor(foregroundColor);
        graphics.drawString(
                this.text,
                this.style.x + (this.style.width - textWidth) / 2,
                this.style.y + (this.style.height + textHeight - adjust) / 2
        );
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
        FontMetrics fontMetrics = this.globalContext.window().getCanvas().getFontMetrics(this.style.font);

        this.style.width = fontMetrics.stringWidth(this.text) + this.style.paddingX;
        this.style.height = fontMetrics.getHeight() + this.style.paddingY;
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
        this.style.font = this.globalContext.window().getCanvas().getFont().deriveFont(16.0f);
        this.style.foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
        this.style.backgroundColor = new ThemeColor(ColorName.TRANSPARENT, ColorVariant.NORMAL);
        this.style.borderRadius = 16;
    }

    /**
     * Sets the text of the component.
     *
     * @param text the new text to render.
     */
    public void setText(String text) {
        if (!this.text.equals(text)) {
            this.fitSize();
            this.dispatchComponentEvent(ComponentEvent.VALUE_CHANGED, this.text, text);
            this.text = text;
        }
    }
}
