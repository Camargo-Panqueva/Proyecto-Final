package controller.dto;

import model.cell.CellType;
import model.wall.WallType;

import java.util.ArrayList;

public record BoardTransferObject(CellType[][] cells, WallType[][] walls, int turnCount,
                                  ArrayList<PlayerTransferObject> players, PlayerTransferObject playerInTurn) {

}
