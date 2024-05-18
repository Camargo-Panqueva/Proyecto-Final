package model.player;

import model.wall.WallData;

import java.awt.Point;
import java.util.ArrayList;

public class Player {
    private Point position;
    private int wallsPlaced;
    private final String name;
    private final int allowedWalls;
    private final int xWinPosition;
    private final int yWinPosition;
    private final Point winDirection;
    private final ArrayList<WallData> playerWalls;
    private int timePlayed;
    private boolean isAI;
    private AIProfile aiProfile;

    public Player(final Point initialPosition, final String name, final int allowedWalls, final int xWinner, final int yWinner) {
        this.name = name;
        this.position = initialPosition;
        this.allowedWalls = allowedWalls;
        this.playerWalls = new ArrayList<>();
        this.xWinPosition = xWinner;
        this.yWinPosition = yWinner;
        this.winDirection = this.generateWinDirection();
        this.wallsPlaced = 0;
        this.timePlayed = 0;
        this.isAI = false;
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
        return playerWalls.size();
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

    public boolean getIsAI(){
        return this.isAI;
    }

    public AIProfile getAiProfile() {
        return aiProfile;
    }

    public ArrayList<WallData> getPlayerWalls() {
        return new ArrayList<>(playerWalls);
    }

    public void setAsAI(){
        this.isAI = true;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setAiProfile(AIProfile aiProfile) {
        this.aiProfile = aiProfile;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addWallPlaced(WallData newAddedWall) {
        this.playerWalls.add(newAddedWall);
        this.wallsPlaced++;
    }

    public void removeWallPlaced(WallData wallData){
        this.playerWalls.remove(wallData);
    }
}