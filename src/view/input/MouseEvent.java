package view.input;

import view.components.GameComponent;

import java.awt.*;

/**
 * Represents a mouse event that occurred on a game component.
 * <p>
 * This class represents a mouse event that occurred on a game component.
 * It provides a basic structure for handling mouse events on game components.
 * The event contains the mouse that triggered the event, the target component
 * that the event occurred on, and the absolute and relative mouse positions.
 * </p>
 */
public final class MouseEvent {

    public final Mouse mouse;
    public final GameComponent target;
    public final Point absoluteMousePosition;
    public final Point relativeMousePosition;

    /**
     * Creates a new MouseEvent with the given mouse and target component.
     *
     * @param mouse  the mouse that triggered the event.
     * @param target the target component that the event occurred on.
     */
    public MouseEvent(Mouse mouse, GameComponent target) {
        this.mouse = mouse;
        this.target = target;

        this.absoluteMousePosition = new Point(mouse.getMousePosition());
        this.relativeMousePosition = new Point(
                this.absoluteMousePosition.x - target.getStyle().x,
                this.absoluteMousePosition.y - target.getStyle().y
        );
    }
}
