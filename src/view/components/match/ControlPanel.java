package view.components.match;

import view.components.GameComponent;
import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;

public final class ControlPanel extends GameComponent {


    /**
     * Creates a new ControlPanel component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public ControlPanel(ContextProvider contextProvider) {
        super(contextProvider);
    }

    private void renderBackground(Graphics2D graphics) {
        graphics.setColor(this.contextProvider.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED));
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        graphics.setColor(this.contextProvider.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.fillRoundRect(
                this.style.x + this.style.borderWidth,
                this.style.y + this.style.borderWidth,
                this.style.width - this.style.borderWidth * 2,
                this.style.height - this.style.borderWidth * 2,
                this.style.borderRadius,
                this.style.borderRadius
        );
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D graphics) {
        this.renderBackground(graphics);
    }

    @Override
    public void fitSize() {

        int expectedWidth = this.style.x + this.style.width + this.style.paddingX;

        if (expectedWidth > this.contextProvider.window().getCanvas().getWidth()) {
            this.contextProvider.window().setCanvasWidth(expectedWidth);
        }
    }

    @Override
    protected void handleThemeChange(Theme theme) {

    }

    @Override
    protected void setupDefaultStyle() {
        this.style.backgroundColor = Color.RED;

    }
}
