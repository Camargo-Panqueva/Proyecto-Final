package controller.main;

import model.GameModel;
import view.GameView;

public final class Launcher {

    public static void main(String[] args) {

        GameModel gameModel = new GameModel();
        GameController gameController = new GameController(gameModel);
        GameView gameView = new GameView(gameController);

        gameView.start();
    }
}
