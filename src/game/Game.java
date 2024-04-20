package game;

import graphics.Window;

public final class Game implements Runnable {

    private final Window window;

    private boolean isRunning;
    private int currentTPS;
    private int currentFPS;

    public Game() {
        this.window = new Window(600, "My Game");

        this.isRunning = false;
    }

    @Override
    public void run() {
        final int TPS = 15;
        final double NS_PER_SECOND = 1_000_000_000;
        final double NS_PER_TICK = NS_PER_SECOND / TPS;

        long tickRef = System.nanoTime();
        long countRef = System.nanoTime();

        double time;
        double delta = 0;

        while (isRunning) {
            final long start = System.nanoTime();

            time = start - tickRef;
            tickRef = start;

            delta += time / NS_PER_TICK;

            while (delta >= 1) {
                this.update();
                delta--;
            }

            this.draw();

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
        currentFPS++;
    }

    public void start() {
        this.isRunning = true;

        Thread thread = new Thread(this, "Graphics thread");
        thread.start();

        this.window.makeVisible();
    }
}
