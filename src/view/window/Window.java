package view.window;

import javax.swing.*;
import java.awt.*;

/**
 * Represents a window object that can be displayed on the screen.
 * <p>
 * This class represents a window object that can be displayed on the screen.
 * It provides a structure for creating a window with a canvas.
 * The window can be displayed on the screen and resized.
 * The window can also have a title that is displayed in the title bar.
 * </p>
 */
public final class Window {

    private final JFrame frame;
    private final Canvas canvas;
    private final String title;

    /**
     * Creates a new Window with the given size and title.
     * <p>
     * This constructor creates a new Window with the given size and title.
     * The window is created with a canvas of the given size.
     * The window is created with a title that is displayed in the title bar.
     * </p>
     *
     * @param size  the size of the window.
     * @param title the title of the window.
     */
    public Window(final int size, final String title) {
        this.title = title;

        this.frame = new JFrame();
        this.canvas = new Canvas();

        this.setupCanvas(size);
        this.setupJFrame(title);
    }

    /**
     * Sets up the JFrame with the given title.
     * <p>
     * This method sets up the JFrame with the given title.
     * It adds the canvas to the JFrame and sets the JFrame properties.
     * </p>
     *
     * @param title the title of the JFrame.
     */
    private void setupJFrame(final String title) {
        this.frame.add(this.canvas);

        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle(title);
        this.frame.setJMenuBar(new MenuBar());

        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    /**
     * Sets up the canvas with the given size.
     * <p>
     * This method sets up the canvas with the given size.
     * </p>
     *
     * @param size the size of the canvas.
     */
    private void setupCanvas(int size) {
        this.canvas.setPreferredSize(new Dimension(size, size));
    }

    /**
     * Makes the window visible on the screen.
     */
    public void makeVisible() {
        this.frame.setVisible(true);
    }

    /**
     * Gets the title of the window.
     *
     * @return the title of the window.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the size of the canvas.
     * @return the size of the canvas.
     */
    public int getCanvasSize() {
        return this.canvas.getWidth();
    }

    /**
     * Sets the size of the canvas.
     * @param size the size of the canvas.
     */
    public void setCanvasSize(int size) {
        this.canvas.setPreferredSize(new Dimension(size, size));
        this.canvas.setSize(new Dimension(size, size));
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    public void setCanvasWidth(int width) {
        this.canvas.setPreferredSize(new Dimension(width, this.canvas.getHeight()));
        this.canvas.setSize(new Dimension(width, this.canvas.getHeight()));
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    public void setCanvasHeight(int height) {
        this.canvas.setPreferredSize(new Dimension(this.canvas.getWidth(), height));
        this.canvas.setSize(new Dimension(this.canvas.getWidth(), height));
        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    /**
     * Gets the JFrame of the window.
     * @return the JFrame of the window.
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     * Gets the canvas of the window.
     * @return the canvas of the window.
     */
    public Canvas getCanvas() {
        return canvas;
    }
}
