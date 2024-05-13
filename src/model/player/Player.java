package model.player;

import model.wall.WallData;

import java.awt.*;
import java.util.ArrayList;

public class Player {
    private Point position;
    private int wallsPlaced;
    private int wallsInField;
    private final String name;
    private final int allowedWalls;
    private final int xWinPosition;
    private final int yWinPosition;
    private final ArrayList<WallData> playerWalls;

    public Player(final Point initialPosition, final String name, final int allowedWalls, final int xWinner, final int yWinner) {
        this.name = name;
        this.position = initialPosition;
        this.allowedWalls = allowedWalls;
        this.playerWalls = new ArrayList<>();
        this.xWinPosition = xWinner;
        this.yWinPosition = yWinner;
    }

    public Point getPosition() {
        return position;
    }

    public int getAllowedWalls() {
        return allowedWalls;
    }

    public int getWallsInField() {
        return wallsInField;
    }

    public int getWallsPlaced() {
        return wallsPlaced;
    }

    public int getXWinPosition() {
        return xWinPosition;
    }

    public int getYWinPosition() {
        return yWinPosition;
    }

    public String getName() {
        return name;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setWallsInField(int wallsInField) {
        this.wallsInField = wallsInField;
    }

    public void setWallsPlaced(int wallsPlaced) {
        this.wallsPlaced = wallsPlaced;
    }

    public ArrayList<WallData> getPlayerWalls() {
        return new ArrayList<>(playerWalls);
    }

    public void addWallPlaced(WallData newAddedWall) {
        this.playerWalls.add(newAddedWall);
    }
}
