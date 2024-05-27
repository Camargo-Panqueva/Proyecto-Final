package controller.main;

import controller.GameController;
import model.GameModel;
import util.Logger;
import view.GameView;

public final class Launcher {

    public static void main(String[] args) {
        Logger.info("Starting launcher...");

        GameModel gameModel = new GameModel();
        Logger.success("Game model created");

        GameController gameController = new GameController(gameModel);
        Logger.success("Game controller created");

        GameView gameView = new GameView(gameController);
        Logger.success("Game view created");

        gameView.start();

        Logger.success("Game started");
    }
}
