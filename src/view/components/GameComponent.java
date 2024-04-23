package view.components;

import util.ConsumerFunction;
import view.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameComponent {

    private final HashMap<MouseEvent, ArrayList<ConsumerFunction<Mouse>>> mouseEventHandlers;

    protected Point location;
    protected Dimension size;
    protected Mouse mouse;

    private boolean isMouseEntered;
    private boolean isMousePressed;

    public GameComponent(final int x, final int y, final int width, final int height) {
        this.location = new Point(x, y);
        this.size = new Dimension(width, height);
        this.mouseEventHandlers = new HashMap<>();
    }

    public abstract void update();

    public void pollMouseEvents() {
        if (this.mouse == null) return;

        if (this.getBounds().contains(this.mouse.getMousePosition())) {
            this.dispatchMouseEvent(MouseEvent.HOVER, mouse);

            if (this.mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {

                if (!this.isMousePressed) {
                    this.isMousePressed = true;
                    this.dispatchMouseEvent(MouseEvent.CLICK, mouse);
                }

                this.dispatchMouseEvent(MouseEvent.PRESSED, mouse);
            } else {

                if (this.isMousePressed) {
                    this.isMousePressed = false;
                    this.dispatchMouseEvent(MouseEvent.RELEASED, mouse);
                }
            }

            if (!this.isMouseEntered) {
                this.isMouseEntered = true;
                this.dispatchMouseEvent(MouseEvent.ENTER, mouse);
            }
        } else {
            if (this.isMouseEntered) {
                this.isMouseEntered = false;
                this.dispatchMouseEvent(MouseEvent.LEAVE, mouse);
            }
        }
    }

    public abstract void render(Graphics2D graphics);

    public Point getCenter() {
        int centerX = this.location.x + this.size.width / 2;
        int centerY = this.location.y + this.size.height / 2;

        return new Point(centerX, centerY);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.location.x, this.location.y, this.size.width, this.size.height);
    }

    public void setMouse(Mouse mouse) {
        this.mouse = mouse;
    }

    public void addEventListener(MouseEvent event, ConsumerFunction<Mouse> handler) {
        if (!this.mouseEventHandlers.containsKey(event)) {
            this.mouseEventHandlers.put(event, new ArrayList<>());
        }

        this.mouseEventHandlers.get(event).add(handler);
    }

    public void dispatchMouseEvent(MouseEvent event, Mouse mouse) {
        if (this.mouseEventHandlers.containsKey(event)) {
            for (ConsumerFunction<Mouse> function : this.mouseEventHandlers.get(event)) {
                function.run(mouse);
            }
        }
    }

    public enum MouseEvent {
        HOVER,
        CLICK,
        ENTER,
        LEAVE,
        PRESSED,
        RELEASED
    }
}
