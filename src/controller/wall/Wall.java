package controller.wall;

import model.GameModel;
import model.player.Player;
import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.Arrays;
import java.util.UUID;

public abstract class Wall {

    protected final WallData wallData;

    public Wall() {
        this.wallData = new WallData();
    }

    public Wall(WallData wallData) {
        this.wallData = wallData;
    }

    public abstract void action(GameModel gameModel);

    public void rotate() {
        int width = this.wallData.getWidth();
        int height = this.wallData.getHeight();

        WallType[][] shapeRotated = new WallType[height][width];

        for (int i = 0; i < width; i++) {
            shapeRotated[i] = Arrays.copyOf(this.wallData.getWallShape()[i], height);
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                shapeRotated[y][x] = this.wallData.getWallShape()[x][y];
            }
        }

        this.wallData.setWallShape(shapeRotated);
    }

    //TODO : getters & setters for WallData

    public WallData getWallData() {
        return wallData;
    }

    @Override
    public String toString() {
        if (this.getWallShape() == null) {
            return "Null";
        }
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < this.getWidth(); y++) {
            for (int x = 0; x < this.getHeight(); x++) {

                final WallType currPosition = this.getWallShape()[x][y];

                if (currPosition == null) {
                    sb.append(" 0 ").append(" ");
                    continue;
                }
                if (currPosition == WallType.NORMAL) {
                    sb.append("Nor").append(" ");
                } else if (currPosition == WallType.LARGE) {
                    sb.append("Lar").append(" ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public Point getPositionOnBoard() {
        return this.wallData.getPositionOnBoard();
    }

    public char getCourse() {
        return this.wallData.getCourse();
    }

    public WallType[][] getWallShape() {
        return this.wallData.getWallShape();
    }

    public WallType getWallType() {
        return this.wallData.getWallType();
    }

    public int getWidth() {
        return this.wallData.getWidth();
    }

    public int getHeight() {
        return this.wallData.getHeight();
    }

    public Player getOwner() {
        return this.wallData.getOwner();
    }

    public UUID getWallId() {
        return this.wallData.getWallId();
    }

    public void setCourse(char course) {
        this.wallData.setCourse(course);
    }

    public void setWidth(int width) {
        this.wallData.setWidth(width);
    }

    public void setHeight(int height) {
        this.wallData.setHeight(height);
    }

    public void setWallShape(WallType[][] wallShape) {
        this.wallData.setWallShape(wallShape);
    }

    public void setWallType(WallType wallType) {
        this.wallData.setWallType(wallType);
    }

    public void setOwner(Player owner) {
        this.wallData.setOwner(owner);
    }

    public void setPositionOnBoard(Point positionOnBoard) {
        this.wallData.setPositionOnBoard(positionOnBoard);
    }

    public void setWallId(UUID wallId) {
        this.wallData.setWallId(wallId);
    }


}