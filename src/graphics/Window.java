package graphics;

import javax.swing.*;

public final class Window {

    private final JFrame frame;

    public Window(final int size, final String title) {
        this.frame = new JFrame();

        this.setupJFrame(size, title);
    }

    private void setupJFrame(final int size, final String title) {
        this.frame.setSize(size, size);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setTitle(title);

        this.frame.setJMenuBar(new MenuBar());
    }

    public void makeVisible() {
        this.frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
