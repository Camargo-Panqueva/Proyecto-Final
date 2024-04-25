package view.components;

import view.context.ContextProvider;

import java.awt.*;

import static view.Constants.TEXT_COLOR;

public final class Text extends GameComponent {

    private final String text;

    public Text(int x, int y, String text, ContextProvider contextProvider) {
        super(x, y, contextProvider);
        this.text = text;

        int canvasSize = this.contextProvider.window().getCanvasSize();

        this.location = new Point(canvasSize / 2, canvasSize / 2);
        this.size = new Dimension(canvasSize, 60);
    }

    @Override
    public void update() {
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(TEXT_COLOR);
        graphics.drawString(this.text, this.location.x, this.location.y);
    }

    @Override
    protected void pack() {
        // TODO: Implement this method
        this.size = new Dimension(this.contextProvider.window().getCanvasSize(), 60);
    }
}
