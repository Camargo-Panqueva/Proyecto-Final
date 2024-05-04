package controller.wall;

import model.GameModel;
import model.wall.WallType;

import java.util.ArrayList;

public abstract class Wall {

    protected ArrayList<WallType> wallShape;
    protected int length;
    protected WallType wallType;

    public Wall() {
    }

    public abstract void action(GameModel gameModel);

    protected int convertToLinePosition(final int x, final int y) {
        //TODO : ERROR handler, x, y out of range
        return x + y * this.length;
    }

    public void rotate() {
        int row = this.length - 1;
        int col = this.length - 1;

        ArrayList<WallType> shapeRotated = new ArrayList<>();
        for (int i = 0; i < this.length * this.length; i++) {
            shapeRotated.add(null);
        }

        for (int x = 0; x < row; x++) {
            for (int y = x; y < col; y++) {
                WallType temp = this.wallShape.get(this.convertToLinePosition(x, y));
                shapeRotated.set(this.convertToLinePosition(x, y), this.wallShape.get(this.convertToLinePosition(y, x)));
                shapeRotated.set(this.convertToLinePosition(y, x), temp);
            }

        }

        for (int i = 0; i < col; i++) {
            int low = 0;
            int high = col - 1;
            while (low < high) {
                WallType temp = shapeRotated.get(this.convertToLinePosition(i, low));
                shapeRotated.set(this.convertToLinePosition(i, low), shapeRotated.get(this.convertToLinePosition(i, high)));
                shapeRotated.set(this.convertToLinePosition(i, high), temp);
                low++;
                high--;
            }

            this.wallShape = shapeRotated;

        }
    }

    @Override
    public String toString() {
        if (wallShape == null) {
            return "Null";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (WallType wallType : wallShape) {
            if (wallType == WallType.NORMAL) {
                sb.append("Normal").append(" ");
            } else {
                sb.append("  0   ").append(" ");
            }
            if (i % length == 0) {
                sb.append("\n");
            }
            i++;
        }

        return sb.toString();
    }

}