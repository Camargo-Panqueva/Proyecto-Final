package view.components;

import view.context.ContextProvider;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;

public final class Text extends GameComponent {

    private final String text;

    public Text(String text, Style style, ContextProvider contextProvider) {
        super(style, contextProvider);
        this.text = text;
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setFont(this.style.font);
        FontMetrics fontMetrics = this.contextProvider.window().getCanvas().getFontMetrics(this.style.font);

        graphics.setColor(this.style.foregroundColor);
        graphics.drawString(this.text, this.style.x, this.style.y + this.style.height - fontMetrics.getDescent());
    }

    @Override
    public void fitSize() {
        FontMetrics fontMetrics = this.contextProvider.window().getCanvas().getFontMetrics(this.style.font);

        this.style.width = fontMetrics.stringWidth(this.text) + this.style.paddingX;
        this.style.height = fontMetrics.getHeight() + this.style.paddingY;
    }

    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.foregroundColor = theme.foregroundColor;
    }
}
