package model.wall;

import model.player.Player;

import java.awt.*;

public class WallData {
    private int width;
    private int height;
    private char course;
    private Player owner;
    private WallType wallType;
    private Point positionOnBoard;
    private WallType[][] wallShape;


    public Point getPositionOnBoard() {
        return positionOnBoard;
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

    public Player getOwner() {
        return owner;
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

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setPositionOnBoard(Point positionOnBoard) {
        this.positionOnBoard = positionOnBoard;
    }
}

