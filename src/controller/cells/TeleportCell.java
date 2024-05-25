package controller.cells;

import controller.logic.MatchManager;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a teleport cell in the game board.
 * This class extends Cell and defines the behavior of a cell that teleports players to specific points.
 */
public class TeleportCell extends Cell {

    /**
     * The list of teleportation points associated with this teleport cell.
     */
    private final ArrayList<Point> teleportPoints = new ArrayList<>();

    /**
     * Constructs a TeleportCell object and initializes the teleportation points.
     * The teleportation points determine where players are teleported when they land on this cell.
     */
    public TeleportCell() {
        teleportPoints.add(new Point(0, 1));
        teleportPoints.add(new Point(1, 1));
        teleportPoints.add(new Point(1, 0));
        teleportPoints.add(new Point(1, -1));
        teleportPoints.add(new Point(0, -1));
        teleportPoints.add(new Point(-1, -1));
        teleportPoints.add(new Point(-1, 0));
        teleportPoints.add(new Point(-1, 1));
    }

    @Override
    public void action(MatchManager matchManager) {

    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {

    }

    public ArrayList<Point> getTeleportPoints() {
        return teleportPoints;
    }
}
