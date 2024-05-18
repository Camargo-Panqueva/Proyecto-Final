package view.components.ui;

import view.components.GameComponent;
import view.context.GlobalContext;
import view.input.KeyboardEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

public final class TextInput extends GameComponent {

    private static final int BLINK_INTERVAL = 1600;

    private String value;
    private int maxLength;

    /**
     * Creates a new TextInput component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public TextInput(GlobalContext globalContext) {
        super(globalContext);

        this.value = "";
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
        Color backgroundColor = this.globalContext.currentTheme().getColor(this.getStyle().backgroundColor);
        Color foregroundColor = this.globalContext.currentTheme().getColor(this.getStyle().foregroundColor);

        graphics.setColor(
                this.hasFocus
                        ? this.globalContext.currentTheme().getColor(ColorName.PRIMARY, ColorVariant.NORMAL)
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

        String appendedValue = this.hasFocus && (System.currentTimeMillis() % BLINK_INTERVAL < BLINK_INTERVAL / 2) ? "&" : "";
        graphics.setColor(foregroundColor);
        graphics.drawString(this.value + appendedValue, this.style.x + this.style.paddingX, this.style.y + this.style.height / 2 + metrics.getHeight() / 4);
    }

    @Override
    public void fitSize() {

    }

    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.style.foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
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
}
