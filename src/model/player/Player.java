package model.player;

import java.awt.*;

public class Player{
    private Point position;
    private final String name;

    public Player(final Point initialPosition, String name){
        this.position = initialPosition;
        this.name = name;
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
}
