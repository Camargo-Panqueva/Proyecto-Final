package view.components;

import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;

public final class BackgroundSeparator extends GameComponent {

    public BackgroundSeparator(ContextProvider contextProvider) {
        super(contextProvider);
    }

    @Override
    public void update() {
    }

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

    @Override
    public void fitSize() {
    }

    @Override
    protected void handleThemeChange(Theme theme) {
        this.style.backgroundColor = theme.backgroundContrastColor;
    }

    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = this.contextProvider.themeManager().getCurrentTheme().backgroundContrastColor;
    }
}
