package model.player;

import model.wall.WallData;
import model.wall.WallType;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a player in the game.
 */
public class Player implements Serializable {

    /**
     * The name of the player.
     */
    private final String name;
    /**
     * The walls available to the player, categorized by wall type.
     */
    private final HashMap<WallType, Integer> playerWalls;
    /**
     * The X coordinate of the winning position for the player.
     */
    private final int xWinPosition;
    /**
     * The Y coordinate of the winning position for the player.
     */
    private final int yWinPosition;
    /**
     * The direction towards the winning position.
     */
    private final Point winDirection;
    /**
     * The walls placed by the player during the game.
     */
    private final ArrayList<WallData> playerWallsPlaced;
    /**
     * The buffer for storing the player's moves.
     */
    private final ArrayDeque<Point> moveBuffer;
    /**
     * The current position of the player on the board.
     */
    private Point position;
    /**
     * The time the player has played in the game.
     */
    private int timePlayed;
    /**
     * Indicates if the player is controlled by AI.
     */
    private boolean isAI;
    /**
     * The AI profile associated with the player.
     */
    private AIProfile aiProfile;
    /**
     * The player's status.
     */
    private boolean isAlive;

    public Player(final Point initialPosition, final String name, final HashMap<WallType, Integer> allowedWalls, final int xWinner, final int yWinner) {
        this.name = name;
        this.position = initialPosition;
        this.playerWalls = allowedWalls;
        this.playerWallsPlaced = new ArrayList<>();
        this.moveBuffer = new ArrayDeque<>();
        this.moveBuffer.add(initialPosition);
        this.xWinPosition = xWinner;
        this.yWinPosition = yWinner;
        this.winDirection = this.generateWinDirection();
        this.timePlayed = 0;
        this.isAI = false;
        this.isAlive = true;
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

    public void setPosition(Point position) {
        this.moveBuffer.add(position);
        if (this.moveBuffer.size() > 3) {
            this.moveBuffer.poll();
        }
        this.position = position;
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
        return playerWallsPlaced.size();
    }

    public int getWallsPlaced() {
        return getPlayerWallsPlaced().size();
    }

    public int getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
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

    public boolean isAI() {
        return this.isAI;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public ArrayDeque<Point> getMoveBuffer() {
        return moveBuffer;
    }

    public AIProfile getAiProfile() {
        return aiProfile;
    }

    public void setAiProfile(AIProfile aiProfile) {
        this.aiProfile = aiProfile;
    }

    public ArrayList<WallData> getPlayerWallsPlaced() {
        return new ArrayList<>(playerWallsPlaced);
    }

    public void setAsAI() {
        this.isAI = true;
    }

    public void addWallPlaced(WallData newAddedWall) {
        this.playerWallsPlaced.add(newAddedWall);
    }

    public void addWallToPlace(WallType wallType) {
        final Integer newValue = this.playerWalls.get(wallType) + 1;
        this.getPlayerWalls().put(wallType, newValue);
    }

    public void subtractWall(WallType wallType) {
        final Integer newValue = this.playerWalls.get(wallType) - 1;
        this.getPlayerWalls().put(wallType, newValue);
    }

    public void kill() {
        this.isAlive = false;
    }

    public void removeWallPlaced(WallData wallData) {
        this.playerWallsPlaced.remove(wallData);
    }
}