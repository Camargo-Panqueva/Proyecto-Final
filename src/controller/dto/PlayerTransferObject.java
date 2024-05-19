package controller.dto;

import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public record PlayerTransferObject(int id, String name, Point position, boolean isInTurn,
                                   ArrayList<Point> allowedMoves, int wallsPlaced, int secondRemaining,
                                   HashMap<WallType, Integer> wallsRemaining) {

}

