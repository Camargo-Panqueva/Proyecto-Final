package game;

import graphics.Colors;
import graphics.Window;
import objects.Board;

import java.awt.*;
import java.awt.image.BufferStrategy;

import static game.Constants.*;

public final class Game implements Runnable {

    private final Window window;
    private final Board board;

    private boolean isRunning;
    private int currentTPS;
    private int currentFPS;

    private BufferStrategy bufferStrategy;

    public Game() {
        this.board = new Board(10);
        this.window = new Window(this.board.getRealSize() + CANVAS_PADDING, "My Game");

        this.board.calculateDrawingOffset(this.window.getCanvasSize());

        this.isRunning = false;
    }

    @Override
    public void run() {
        final int TPS = 15;
        final int FPS = 75;

        final double NS_PER_SECOND = 1_000_000_000;
        final double NS_PER_UPDATE = NS_PER_SECOND / TPS;
        final double NS_PER_FRAME = NS_PER_SECOND / FPS;

        long tickRef = System.nanoTime();
        long countRef = System.nanoTime();

        double time;
        double deltaTPS = 0;
        double deltaFPS = 0;

        while (isRunning) {
            final long start = System.nanoTime();

            time = start - tickRef;
            tickRef = start;

            deltaTPS += time / NS_PER_UPDATE;
            deltaFPS += time / NS_PER_FRAME;

            while (deltaTPS >= 1) {
                this.update();
                deltaTPS--;
            }

            while (deltaFPS >= 1) {
                this.draw();
                deltaFPS--;
            }

            if (System.nanoTime() - countRef > NS_PER_SECOND) {
                window.getFrame().setTitle(window.getTitle() + " || TPS: " + currentTPS + " || FPS: " + currentFPS);
                currentTPS = 0;
                currentFPS = 0;
                countRef = System.nanoTime();
            }
        }
    }

    private void update() {
        currentTPS++;
    }

    private void draw() {

        if (bufferStrategy == null) {
            this.window.getCanvas().createBufferStrategy(2);
            this.bufferStrategy = this.window.getCanvas().getBufferStrategy();
        }

        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(Colors.WINDOW_BACKGROUND);
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());

        // Draw area
        this.board.draw(graphics);
        // Draw area

        graphics.dispose();
        bufferStrategy.show();

        currentFPS++;
    }

    public void start() {
        this.isRunning = true;

        Thread thread = new Thread(this, "Graphics thread");
        thread.start();

        this.window.makeVisible();
    }
}
