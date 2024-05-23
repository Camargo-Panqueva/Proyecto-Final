package controller.dto;

import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.wall.WallType;

import java.util.ArrayList;
import java.util.HashMap;

public record SetupTransferObject(int boardWidth, int boardHeight, boolean randomCells,
                                  DifficultyType difficultyType, int time, HashMap<CellType, Integer> cellTypeCount,
                                  HashMap<WallType, Integer> wallTypeCount,
                                  ArrayList<PlayerSetupTransferObject> players) {
}
