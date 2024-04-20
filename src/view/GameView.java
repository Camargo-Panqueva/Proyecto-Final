package view;

import controller.main.GameController;
import util.ConcurrentLoop;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

import static view.Constants.WINDOW_BACKGROUND;

public final class GameView {

    private final GameController controller;
    private final Window window;

    private BufferStrategy bufferStrategy;
    private ConcurrentLoop renderLoop;

    public GameView(GameController controller) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.out.println("Could not set system look and feel");
        }

        this.controller = controller;
        this.window = new Window(800, "Game of Life"); // TODO: Set window size from controller data
    }

    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 60, "View");
        this.renderLoop.start();
        this.renderLoop.setTickConsumer(fps -> System.out.println("FPS: " + fps));
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
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(WINDOW_BACKGROUND);

//        System.out.println("Rendering game view...");

        graphics.dispose();
        bufferStrategy.show();
    }
}
