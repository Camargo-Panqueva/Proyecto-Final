package controller.dto;

import model.cell.CellType;
import java.util.ArrayList;

public record BoardTransferObject(CellType[][] cells, ArrayList<WallTransferObject> walls, int turnCount,
                                  ArrayList<PlayerTransferObject> players, PlayerTransferObject playerInTurn) {

}
