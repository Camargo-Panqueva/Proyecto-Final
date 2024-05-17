package view.components.ui;

import view.components.GameComponent;
import view.context.ContextProvider;
import view.input.KeyboardEvent;
import view.themes.Theme;

import java.awt.*;

public final class TextInput extends GameComponent {

    private static final int BLINK_INTERVAL = 1600;

    private String value;
    private int maxLength;

    /**
     * Creates a new TextInput component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public TextInput(ContextProvider contextProvider) {
        super(contextProvider);

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

        graphics.setColor(
                this.hasFocus
                        ? this.contextProvider.currentTheme().getColor(Theme.ColorName.PRIMARY, Theme.ColorVariant.NORMAL)
                        : this.style.backgroundColor
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

            graphics.setColor(this.style.backgroundColor);
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
        graphics.setColor(this.style.foregroundColor);
        graphics.drawString(this.value + appendedValue, this.style.x + this.style.paddingX, this.style.y + this.style.height / 2 + metrics.getHeight() / 4);
    }

    @Override
    public void fitSize() {

    }

    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.backgroundColor = theme.getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED);
        this.style.foregroundColor = theme.getColor(Theme.ColorName.FOREGROUND, Theme.ColorVariant.NORMAL);
    }

    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = this.contextProvider.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED);
        this.style.foregroundColor = this.contextProvider.currentTheme().getColor(Theme.ColorName.FOREGROUND, Theme.ColorVariant.NORMAL);
        this.style.font = this.contextProvider.window().getCanvas().getFont().deriveFont(26.0f);
        this.style.height = 60;
        this.style.width = 300;
        this.style.borderRadius = 16;
    }

    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, this::handleKeyTyped);
    }

    private void handleKeyTyped(KeyboardEvent event) {
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
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}
