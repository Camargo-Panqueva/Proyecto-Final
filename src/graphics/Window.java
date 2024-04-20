package graphics;

import graphics.menubar.MenuBar;

import javax.swing.*;
import java.awt.*;

public final class Window {

    private final JFrame frame;
    private final Canvas canvas;
    private final String title;

    private final int size;

    public Window(final int size, final String title) {
        this.size = size;
        this.title = title;

        this.frame = new JFrame();
        this.canvas = new Canvas();

        this.setupJFrame(size, title);
        this.setupCanvas(size);
    }

    private void setupJFrame(final int size, final String title) {
        this.frame.setSize(size, size);
        this.frame.setBackground(Colors.BACKGROUND_2);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setTitle(title);

        this.frame.setJMenuBar(new MenuBar());
    }

    private void setupCanvas(int size) {
        this.canvas.setMaximumSize(new Dimension(size, size));
        this.canvas.setMinimumSize(new Dimension(size, size));
        this.canvas.setPreferredSize(new Dimension(size, size));

        this.frame.add(this.canvas);
        this.frame.pack();
    }

    public void makeVisible() {
        this.frame.setVisible(true);
    }

    public void makeInvisible() {
        this.frame.setVisible(false);
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public JFrame getFrame() {
        return frame;
    }

    public Canvas getCanvas() {
        return canvas;
    }
}
