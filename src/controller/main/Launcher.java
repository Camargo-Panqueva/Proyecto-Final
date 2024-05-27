package controller.main;

import controller.GameController;
import model.GameModel;
import util.Logger;
import view.GameView;

public final class Launcher {

    public static void main(String[] args) {
        Logger.logInfo("Starting launcher...");

        GameModel gameModel = new GameModel();
        Logger.logSuccess("Game model created");

        GameController gameController = new GameController(gameModel);
        Logger.logSuccess("Game controller created");

        GameView gameView = new GameView(gameController);
        Logger.logSuccess("Game view created");

        gameView.start();

        Logger.logSuccess("Game started");
    }
}
