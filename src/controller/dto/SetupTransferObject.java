package controller.dto;

import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.wall.WallType;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a transfer object for setting up game configurations.
 * This record encapsulates information about the game setup, including board dimensions, cell and wall types,
 * player configurations, and game difficulty.
 *
 * @param boardWidth     The width of the game board.
 * @param boardHeight    The height of the game board.
 * @param randomCells    A boolean indicating whether cells should be randomized on the board.
 * @param difficultyType The difficulty type of the game.
 * @param time           The time limit for the game.
 * @param cellTypeCount  A map containing the count of each cell type on the board.
 * @param wallTypeCount  A map containing the count of each wall type available in the game.
 * @param players        An ArrayList of PlayerSetupTransferObject representing player configurations.
 */
public record SetupTransferObject(int boardWidth, int boardHeight, boolean randomCells,
                                  DifficultyType difficultyType, int time, HashMap<CellType, Integer> cellTypeCount,
                                  HashMap<WallType, Integer> wallTypeCount,
                                  ArrayList<PlayerSetupTransferObject> players) {
}
