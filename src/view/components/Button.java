package view.components;

import java.awt.*;

public class Button extends GameComponent {

    private final String text;

    public Button(int x, int y, int width, int height, String text) {
        super(x, y, width, height);
        this.text = text;
    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(Color.black);
        graphics.fillRect(this.location.x, this.location.y, this.size.width, this.size.height);

        // TODO: Fix render method on Button
        graphics.setColor(Color.WHITE);
        Point center = this.getCenter();
        graphics.drawString(this.text, center.x, center.y);
    }
}
