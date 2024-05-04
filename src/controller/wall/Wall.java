package controller.wall;

import model.GameModel;
import model.wall.WallData;
import model.wall.WallType;

import java.util.ArrayList;

public abstract class Wall {

    protected final WallData wallData;

    public Wall() {
        this.wallData = new WallData();
    }

    public abstract void action(GameModel gameModel);

    protected int convertToLinePosition(final int x, final int y) {
        //TODO : ERROR handler, x, y out of range
        return x + y * this.wallData.getLength();
    }

    public void rotate() {
        int length = this.wallData.getLength();
        int row = length - 1;
        int col = length - 1;

        ArrayList<WallType> shapeRotated = new ArrayList<>();
        for (int i = 0; i < length * length; i++) {
            shapeRotated.add(null);
        }

        for (int x = 0; x < row; x++) {
            for (int y = x; y < col; y++) {
                WallType temp = this.wallData.getWallShape().get(this.convertToLinePosition(x, y));
                shapeRotated.set(this.convertToLinePosition(x, y), this.wallData.getWallShape().get(this.convertToLinePosition(y, x)));
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

            this.wallData.setWallShape(shapeRotated);

        }
    }

    @Override
    public String toString() {
        if (this.wallData.getWallShape() == null) {
            return "Null";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (WallType wallType : this.wallData.getWallShape()) {
            if (wallType == WallType.NORMAL) {
                sb.append("Normal").append(" ");
            } else {
                sb.append("  0   ").append(" ");
            }
            if (i % this.wallData.getLength() == 0) {
                sb.append("\n");
            }
            i++;
        }

        return sb.toString();
    }

}