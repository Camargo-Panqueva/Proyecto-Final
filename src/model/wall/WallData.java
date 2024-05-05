package model.wall;

import java.awt.*;
import java.util.ArrayList;

public class WallData {
    private int length;
    private char course;
    private Point position;
    private WallType wallType;
    private ArrayList<WallType> wallShape;


    public Point getPosition() {
        return position;
    }

    public char getCourse() {
        return course;
    }

    public ArrayList<WallType> getWallShape() {
        return wallShape;
    }

    public WallType getWallType() {
        return wallType;
    }

    public int getLength() {
        return length;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setCourse(char course) {
        this.course = course;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setWallShape(ArrayList<WallType> wallShape) {
        this.wallShape = wallShape;
    }

    public void setWallType(WallType wallType) {
        this.wallType = wallType;
    }
}

