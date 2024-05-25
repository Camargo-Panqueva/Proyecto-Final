package controller.dto;

import model.cell.CellType;

import java.util.ArrayList;

/**
 * Represents a transfer object for transferring board-related data.
 * This record encapsulates the state of the game board, including cells, walls, turn count, players, and the player currently in turn.
 *
 * @param cells        A 2D array of CellType representing the cells on the board.
 * @param walls        An ArrayList of WallTransferObject representing the walls on the board.
 * @param turnCount    The current turn count in the game.
 * @param players      An ArrayList of PlayerTransferObject representing the players in the game.
 * @param playerInTurn The player currently in turn.
 */
public record BoardTransferObject(CellType[][] cells, ArrayList<WallTransferObject> walls, int turnCount,
                                  ArrayList<PlayerTransferObject> players, PlayerTransferObject playerInTurn) {

}
