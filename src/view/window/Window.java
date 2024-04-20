package view.window;

import javax.swing.*;
import java.awt.*;

public final class Window {

    private final JFrame frame;
    private final Canvas canvas;
    private final String title;

    public Window(final int size, final String title) {
        this.title = title;

        this.frame = new JFrame();
        this.canvas = new Canvas();

        this.setupCanvas(size);
        this.setupJFrame(title);
    }

    private void setupJFrame(final String title) {
        this.frame.add(this.canvas);

        this.frame.setResizable(false);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setTitle(title);
        this.frame.setJMenuBar(new MenuBar());

        this.frame.pack();
        this.frame.setLocationRelativeTo(null);
    }

    private void setupCanvas(int size) {
        this.canvas.setPreferredSize(new Dimension(size, size));
    }

    public void makeVisible() {
        this.frame.setVisible(true);
    }

    public String getTitle() {
        return title;
    }

    public int getCanvasSize() {
        return this.canvas.getWidth();
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
