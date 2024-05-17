package model.player;

import model.wall.WallData;

import java.awt.Point;
import java.util.ArrayList;

public class Player {
    private Point position;
    private int wallsPlaced;
    private int wallsInField;
    private final String name;
    private final int allowedWalls;
    private final int xWinPosition;
    private final int yWinPosition;
    private final Point winDirection;
    private final ArrayList<WallData> playerWalls;
    private int timePlayed;

    public Player(final Point initialPosition, final String name, final int allowedWalls, final int xWinner, final int yWinner, final long timeLimitSeg) {
        this.name = name;
        this.position = initialPosition;
        this.allowedWalls = allowedWalls;
        this.playerWalls = new ArrayList<>();
        this.xWinPosition = xWinner;
        this.yWinPosition = yWinner;
        this.winDirection = this.generateWinDirection();
        this.timePlayed = 0;
    }

    private Point generateWinDirection() {
        final Point vector = new Point();
        if (this.yWinPosition == 0) {
            vector.setLocation(0, -1);
        } else if (this.yWinPosition != -1) {
            vector.setLocation(0, 1);
        } else if (this.xWinPosition == 0) {
            vector.setLocation(-1, 0);
        } else {
            vector.setLocation(1, 0);
        }

        return vector;
    }

    public Point getPosition() {
        return position;
    }

    public Point getWinDirection() {
        return this.winDirection;
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

    public int getTimePlayed() {
        return timePlayed;
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

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
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