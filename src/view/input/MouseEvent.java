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

    /**
     * Represents the mouse event type.
     */
    public final Mouse mouse;

    /**
     * Represents the target component that the event occurred on.
     */
    public final GameComponent target;

    /**
     * Represents the absolute mouse position.
     */
    public final Point absoluteMousePosition;

    /**
     * Represents the mouse position relative to the target component.
     */
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
        this.relativeMousePosition = new Point(mouse.getMouseRelativePosition(target.getBounds()));
    }

    /**
     * Represents a mouse event that can be dispatched to the event handlers.
     */
    public enum EventType {

        /**
         * Dispatched when the mouse is hovering over a component.
         */
        HOVER,

        /**
         * Dispatched when the mouse enters the bounds of a component.
         */
        ENTER,

        /**
         * Dispatched when the mouse leaves the bounds of a component.
         */
        LEAVE,

        /**
         * Dispatched when the mouse is pressed on a component.
         */
        PRESSED,

        /**
         * Dispatched when the mouse is released on a component.
         */
        RELEASED,

        /**
         * Dispatched when the mouse is clicked on a component.
         */
        GET_FOCUS,

        /**
         * Dispatched when the mouse is clicked outside a component.
         */
        LOSE_FOCUS
    }
}
