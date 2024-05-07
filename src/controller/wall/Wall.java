package controller.wall;

import model.GameModel;
import model.wall.WallData;
import model.wall.WallType;

import java.util.Arrays;

public abstract class Wall {

    protected final WallData wallData;

    public Wall() {
        this.wallData = new WallData();
    }

    public abstract void action(GameModel gameModel);

    public void rotate() {
        int width = this.wallData.getWidth();
        int height = this.wallData.getHeight();

        WallType[][] shapeRotated = new WallType[height][width];

        for (int i = 0; i < width; i++) {
            shapeRotated[i] = Arrays.copyOf(this.wallData.getWallShape()[i], height);
        }

        for (int x = 0; x < width - 1; x++) {
            for (int y = 0; y < height - 1; y++) {
                shapeRotated[y][x] = this.wallData.getWallShape()[x][y];
            }
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                shapeRotated[i][j] = this.wallData.getWallShape()[width - 1 - j][i];
            }
        }

        this.wallData.setWallShape(shapeRotated);
    }

    public WallData getWallData() {
        return wallData;
    }

    @Override
    public String toString() {
        if (this.wallData.getWallShape() == null) {
            return "Null";
        }
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (int x = 0; x < this.wallData.getWidth(); x++) {
            for (int y = 0; y < this.wallData.getHeight(); y++) {
                final WallType currWall = this.wallData.getWallShape()[x][y];

                switch (currWall) {
                    case NORMAL -> sb.append("Normal").append(" ");
                    case LARGE -> sb.append("Large").append(" ");
                    default -> sb.append("  0   ").append(" ");
                }

            }
            if (i % this.wallData.getWidth() == 0) {
                sb.append("\n");
            }
            i++;

        }

        return sb.toString();
    }

}