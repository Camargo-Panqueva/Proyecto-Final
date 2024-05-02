package controller.wall;

import model.GameModel;

public abstract class Wall {
    public Wall(){
    }

    public abstract void action(GameModel gameModel);
}
