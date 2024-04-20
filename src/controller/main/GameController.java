package controller.main;

import util.ConcurrentLoop;

public final class GameController {

    private ConcurrentLoop updateLoop;

    public void start() {
        this.updateLoop = new ConcurrentLoop(this::update, 20, "Controller");
        this.updateLoop.start();
        this.updateLoop.setTickConsumer(ups -> System.out.println("UPS: " + ups));
    }

    public void stop() {
        this.updateLoop.stop();
    }

    private void update() {
//        System.out.println("Updating game controller...");
    }
}
