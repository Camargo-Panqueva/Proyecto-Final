package controller.cells;

import controller.logic.MatchManager;

import java.awt.*;
import java.util.ArrayList;

public class TeleportCell extends Cell{

    private final ArrayList<Point> teleportPoints = new ArrayList<>();

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
