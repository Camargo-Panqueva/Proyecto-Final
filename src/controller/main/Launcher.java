package controller.main;

import controller.GameController;
import model.GameModel;
import util.Timeout;
import view.GameView;

public final class Launcher {

    public static void main(String[] args) {

        GameModel gameModel = new GameModel();
        GameController gameController = new GameController(gameModel);
        GameView gameView = new GameView(gameController);

        Timeout timeOut = new Timeout(()->{
            System.out.println("TimeOut");
        }, 3000);

        timeOut.start();

        gameView.start();
    }
}
