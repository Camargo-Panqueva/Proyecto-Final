package view;

import controller.main.GameController;
import util.ConcurrentLoop;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

import static view.Constants.*;

public final class GameView {

    private final GameController controller;
    private final Window window;

    private BufferStrategy bufferStrategy;
    private ConcurrentLoop renderLoop;

    private int currentFPS;

    public GameView(GameController controller) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.out.println("Could not set system look and feel");
        }

        this.currentFPS = 0;

        this.controller = controller;
        this.window = new Window(INITIAL_WINDOW_SIZE, "Quoridor"); // TODO: Set window size from controller data
    }

    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 60, "View");
        this.renderLoop.start();
        this.renderLoop.setTickConsumer(fps -> this.currentFPS = fps);
    }

    public void stop() {
        this.renderLoop.stop();
    }

    private void render() {

        if (bufferStrategy == null) {
            this.window.getCanvas().createBufferStrategy(2);
            this.bufferStrategy = this.window.getCanvas().getBufferStrategy();
        }

        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        this.renderBackground(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private void renderBackground(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(WINDOW_BACKGROUND);
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());

        graphics.setColor(WHITE);
        graphics.drawString("FPS: " + this.currentFPS, 6, 16);
    }
}
