package controller.wall;

import model.GameModel;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class NormalWall extends Wall{
    private LinkedList<Character> courses;
    private char course;

    public NormalWall() {
        super.wallType = WallType.NORMAL;
        super.wallShape = new ArrayList<>(super.length * super.length);
        super.length = 3;

        for (int i = 0; i < super.length * super.length; i++) {
            super.wallShape.add(null);
        }

        super.wallShape.set(super.convertToLinePosition(0, 1), WallType.NORMAL);
        super.wallShape.set(super.convertToLinePosition(1, 1), WallType.NORMAL);

    }

    @Override
    public void action(GameModel gameModel) {
    }


}
