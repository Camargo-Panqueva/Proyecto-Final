package view.components;

import view.context.ContextProvider;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;

public final class Button extends GameComponent {

    private final String text;

    public Button(String text, Style style, ContextProvider context) {
        super(style, context);
        this.text = text;

        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
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
        Rectangle textBounds = graphics.getFontMetrics().getStringBounds(this.text, graphics).getBounds();

        graphics.setColor(this.style.foregroundColor);
        graphics.drawString(this.text, center.x - textBounds.width / 2, center.y + textBounds.height / 4);
    }

    @Override
    public void fitSize() {
        // TODO: Implement this method
    }

    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.backgroundColor = theme.primaryColor;
        this.style.foregroundColor = theme.backgroundColor;
    }
}
