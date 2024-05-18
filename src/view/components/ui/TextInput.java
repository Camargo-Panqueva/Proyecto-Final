package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.context.Style;
import view.input.KeyboardEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

public final class TextInput extends GameComponent {

    private static final int BLINK_INTERVAL = 1600;
    private final String placeholder;
    private String value;
    private int maxLength;

    /**
     * Creates a new TextInput component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public TextInput(GlobalContext globalContext, String placeholder) {
        super(globalContext);

        this.value = "";
        this.placeholder = placeholder;
        this.style.cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
        this.maxLength = 20;
    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(this.style.font);

        FontMetrics metrics = graphics.getFontMetrics(this.style.font);
        Color borderColor = this.globalContext.currentTheme().getColor(this.getStyle().borderColor);
        Color backgroundColor = this.globalContext.currentTheme().getColor(this.getStyle().backgroundColor);
        Color foregroundColor = this.globalContext.currentTheme().getColor(this.getStyle().foregroundColor);
        Color placeholderColor = this.globalContext.currentTheme().getColor(this.getStyle().backgroundColor.name(), ColorVariant.DIMMED);

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

    @Override
    public void fitSize() {

    }

    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.style.foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
        this.style.borderColor = new ThemeColor(ColorName.PRIMARY, ColorVariant.NORMAL);
        this.style.font = this.globalContext.window().getCanvas().getFont().deriveFont(26.0f);
        this.style.height = 60;
        this.style.width = 300;
        this.style.borderRadius = 16;
        this.style.paddingX = 16;
    }

    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, this::handleKeyTyped);
    }

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

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;

        if (this.value.length() > maxLength) {
            this.value = this.value.substring(0, maxLength);
            this.dispatchComponentEvent(ComponentEvent.VALUE_CHANGED, this.value, this.value);
        }
    }

    public String getValue() {
        return this.value;
    }
}
