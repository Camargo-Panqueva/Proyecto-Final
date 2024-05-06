package model.wall;

import java.awt.*;

public class WallData {
    private int width;
    private int height;
    private char course;
    private Point position;
    private WallType wallType;
    private WallType[][] wallShape;


    public Point getPosition() {
        return position;
    }

    public char getCourse() {
        return course;
    }

    public WallType[][] getWallShape() {
        return wallShape;
    }

    public WallType getWallType() {
        return wallType;
    }

    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setCourse(char course) {
        this.course = course;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWallShape(WallType[][] wallShape) {
        this.wallShape = wallShape;
    }

    public void setWallType(WallType wallType) {
        this.wallType = wallType;
    }
}

