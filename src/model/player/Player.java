package model.player;

import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

public class Player {
    private Point position;
    private final String name;
    private final HashMap<WallType, Integer> playerWalls;
    private final int xWinPosition;
    private final int yWinPosition;
    private final Point winDirection;
    private final ArrayList<WallData> PlayerWallsPlaced;
    private int timePlayed;
    private boolean isAI;
    private AIProfile aiProfile;
    private final ArrayDeque<Point> moveBuffer;

    public Player(final Point initialPosition, final String name, final HashMap<WallType, Integer> allowedWalls, final int xWinner, final int yWinner) {
        this.name = name;
        this.position = initialPosition;
        this.playerWalls = allowedWalls;
        this.PlayerWallsPlaced = new ArrayList<>();
        this.moveBuffer = new ArrayDeque<>();
        this.xWinPosition = xWinner;
        this.yWinPosition = yWinner;
        this.winDirection = this.generateWinDirection();
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

    public int getRemainingWallsCount() {
        return playerWalls.values().stream().mapToInt(Integer::intValue).sum();
    }

    public HashMap<WallType, Integer> getPlayerWalls() {
        return playerWalls;
    }

    public int getWallsInField() {
        return PlayerWallsPlaced.size();
    }

    public int getWallsPlaced() {
        return getPlayerWallsPlaced().size();
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

    public boolean getIsAI() {
        return this.isAI;
    }

    public ArrayDeque<Point> getMoveBuffer() {
        return moveBuffer;
    }

    public AIProfile getAiProfile() {
        return aiProfile;
    }

    public ArrayList<WallData> getPlayerWallsPlaced() {
        return new ArrayList<>(PlayerWallsPlaced);
    }

    public void setAsAI() {
        this.isAI = true;
    }

    public void setPosition(Point position) {
        this.moveBuffer.add(position);
        if (this.moveBuffer.size() >= 2){
            this.moveBuffer.pop();
        }
        this.position = position;
    }

    public void setAiProfile(AIProfile aiProfile) {
        this.aiProfile = aiProfile;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addWallPlaced(WallData newAddedWall) {
        this.PlayerWallsPlaced.add(newAddedWall);
    }

    public void addWallToPlace(WallType wallType){
        final Integer newValue = this.playerWalls.get(wallType) + 1;
        this.getPlayerWalls().put(wallType, newValue);
    }

    public void subtractWall(WallType wallType){
        final Integer newValue = this.playerWalls.get(wallType) - 1;
        this.getPlayerWalls().put(wallType, newValue);
    }

    public void removeWallPlaced(WallData wallData) {
        this.PlayerWallsPlaced.remove(wallData);
    }
}