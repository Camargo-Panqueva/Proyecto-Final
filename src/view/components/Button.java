package view.components;

import view.context.ContextProvider;

import java.awt.*;

import static view.Constants.*;

public class Button extends GameComponent {

    private final String text;

    public Button(int x, int y, int width, int height, String text, ContextProvider context) {
        super(x, y, width, height, context);
        this.text = text;
    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(PRIMARY_COLOR);
        graphics.fillRoundRect(
                this.location.x,
                this.location.y,
                this.size.width,
                this.size.height,
                COMPONENT_BORDER_RADIUS,
                COMPONENT_BORDER_RADIUS
        );

        Point center = this.getCenter();
        Rectangle textBounds = graphics.getFontMetrics().getStringBounds(this.text, graphics).getBounds();

        graphics.setColor(TEXT_COLOR);
        graphics.drawString(this.text, center.x - textBounds.width / 2, center.y + textBounds.height / 4);
    }
}
