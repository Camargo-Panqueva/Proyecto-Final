package controller.main;

import view.GameView;

public final class Launcher {

    public static void main(String[] args) {

        GameController gameController = new GameController();
        GameView gameView = new GameView(gameController);

        gameController.start();
        gameView.start();
    }
}
