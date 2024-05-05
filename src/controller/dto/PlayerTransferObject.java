package controller.dto;

import java.awt.*;
import java.util.ArrayList;

public record PlayerTransferObject(int id, String name, Point position, boolean isInTurn,
                                   ArrayList<Point> possibleMovements) {

}

