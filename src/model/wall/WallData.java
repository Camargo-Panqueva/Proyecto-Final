package model.wall;

import model.player.Player;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public class WallData implements Serializable {
    /**
     * Width of the wall.
     */
    private int width;

    /**
     * Height of the wall.
     */
    private int height;

    /**
     * Course information of the wall.
     */
    private char course;

    /**
     * Unique identifier for the wall.
     */
    private UUID wallId;

    /**
     * Player who owns the wall.
     */
    private Player owner;

    /**
     * Indicates if the wall is an ally wall.
     */
    private boolean isAlly;

    /**
     * Type of the wall.
     */
    private WallType wallType;

    /**
     * Turn in which the wall was created.
     */
    private int creationTurn;

    /**
     * Position of the wall on the game board.
     */
    private Point positionOnBoard;

    /**
     * Shape representation of the wall on the board.
     */
    private WallType[][] wallShape;


    public Point getPositionOnBoard() {
        return positionOnBoard;
    }

    public void setPositionOnBoard(Point positionOnBoard) {
        this.positionOnBoard = positionOnBoard;
    }

    public char getCourse() {
        return course;
    }

    public void setCourse(char course) {
        this.course = course;
    }

    public WallType[][] getWallShape() {
        return wallShape;
    }

    public void setWallShape(WallType[][] wallShape) {
        this.wallShape = wallShape;
    }

    public WallType getWallType() {
        return wallType;
    }

    public void setWallType(WallType wallType) {
        this.wallType = wallType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public UUID getWallId() {
        return wallId;
    }

    public void setWallId(UUID wallId) {
        this.wallId = wallId;
    }

    public int getCreationTurn() {
        return this.creationTurn;
    }

    public void setCreationTurn(int creationTurn) {
        this.creationTurn = creationTurn;
    }

    public boolean getIsAlly() {
        return this.isAlly;
    }

    public void setAlly(boolean ally) {
        this.isAlly = ally;
    }
}

