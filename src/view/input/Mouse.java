package view.input;

import view.window.Window;

import java.awt.*;
import java.awt.event.MouseAdapter;

public final class Mouse extends MouseAdapter {

    public static final int LEFT_BUTTON = 1;
    public static final int MIDDLE_BUTTON = 2;
    public static final int RIGHT_BUTTON = 3;

    private final Point mousePosition;
    private final boolean[] buttons;

    public Mouse() {
        this.mousePosition = new Point();
        this.buttons = new boolean[3];
    }

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

    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        this.buttons[e.getButton() - 1] = true;
    }

    @Override
    public void mouseReleased(java.awt.event.MouseEvent e) {
        this.buttons[e.getButton() - 1] = false;
    }

    public Point getMousePosition() {
        return this.mousePosition;
    }

    public boolean isButtonPressed(int button) {
        return this.buttons[button - 1];
    }
}
