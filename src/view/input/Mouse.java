package view.input;

import view.window.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;

/**
 * Represents a mouse input device.
 * <p>
 * This class represents a mouse input device.
 * It provides a structure for handling mouse events and tracking the mouse's position.
 * </p>
 */
public final class Mouse extends MouseAdapter {

    public static final int LEFT_BUTTON = 1;
    public static final int MIDDLE_BUTTON = 2;
    public static final int RIGHT_BUTTON = 3;
    public static final int BACKWARD_BUTTON = 4;
    public static final int FORWARD_BUTTON = 5;

    private final Point mousePosition;
    private final boolean[] buttons;

    /**
     * Creates a new Mouse input device.
     */
    public Mouse() {
        this.mousePosition = new Point();
        this.buttons = new boolean[5];
    }

    /**
     * Updates the mouse input device.
     * <p>
     * This method updates the mouse input device by tracking the mouse's position.
     * It updates the mouse's position based on the current mouse location on the screen.
     * The mouse's position is clamped to the canvas size to prevent out-of-bounds errors.
     * </p>
     *
     * @param window the window to update the mouse input device for.
     */
    public void update(Window window) {
        Point canvasLocation = window.getCanvas().getLocationOnScreen();

        int canvasSize = window.getCanvasSize();
        int x = MouseInfo.getPointerInfo().getLocation().x - canvasLocation.x;
        int y = MouseInfo.getPointerInfo().getLocation().y - canvasLocation.y;

        this.mousePosition.setLocation(
                Math.min(Math.max(x, 0), canvasSize),
                Math.min(Math.max(y, 0), canvasSize)
        );
    }

    /**
     * Processes a mouse pressed event.
     * <p>
     * This method processes a mouse pressed event by updating the state of the mouse buttons.
     * It sets the button that was pressed to be in the pressed state.
     * The button is identified by the event's button code.
     * </p>
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        this.buttons[e.getButton() - 1] = true;
    }

    /**
     * Processes a mouse released event.
     * <p>
     * This method processes a mouse released event by updating the state of the mouse buttons.
     * It sets the button that was released to be in the released state.
     * The button is identified by the event's button code.
     * </p>
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        this.buttons[e.getButton() - 1] = false;
    }

    /**
     * Gets the current mouse position.
     *
     * @return the current mouse position.
     */
    public Point getMousePosition() {
        return this.mousePosition;
    }

    /**
     * Checks if a button is pressed.
     * <p>
     * This method checks if a button is currently pressed.
     * It returns true if the button is pressed and false otherwise.
     * The button is identified by its button code.
     * </p>
     *
     * @param button the button code to check.
     * @return true if the button is pressed, false otherwise.
     */
    public boolean isButtonPressed(int button) {
        return this.buttons[button - 1];
    }
}
