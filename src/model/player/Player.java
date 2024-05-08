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
    private final ArrayList<WallData> playerWalls;

    public Player(final Point initialPosition, String name, int allowedWalls) {
        this.name = name;
        this.position = initialPosition;
        this.allowedWalls = allowedWalls;
        this.playerWalls = new ArrayList<>();
    }

    public Point getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public ArrayList<WallData> getPlayerWalls() {
        return new ArrayList<>(playerWalls);
    }

    public void addWallPlaced(WallData newAddedWall) {
        this.playerWalls.add(newAddedWall);
    }
}
