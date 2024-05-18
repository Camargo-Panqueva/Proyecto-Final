package controller.dto;

import model.difficulty.DifficultyType;
import model.wall.WallType;

import java.util.ArrayList;
import java.util.HashMap;

public record SetupTransferObject(int playerCount, int boardWidth, int boardHeight, int wallsPerPlayer,
                                  boolean randomCells, DifficultyType difficultyType, int time,
                                  HashMap<WallType, Integer> wallTypeCount,
                                  ArrayList<PlayerSetupTransferObject> players) {
}
