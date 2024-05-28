package controller.dto;

import model.wall.WallType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a transfer object for player-related data.
 * This record encapsulates information about a player, including their ID, name, position, turn status, allowed moves,
 * walls placed, remaining time in seconds, and the number of walls remaining for each wall type.
 *
 * @param id               The unique identifier of the player.
 * @param name             The name of the player.
 * @param position         The current position of the player on the board, represented as a Point object.
 * @param isInTurn         A boolean indicating whether the player is in turn or not.
 * @param allowedMoves     A list of points representing the allowed moves for the player.
 * @param wallsPlaced      The number of walls placed by the player.
 * @param secondsRemaining The remaining time in seconds for the player.
 * @param wallsRemaining   A map containing the number of walls remaining for each wall type (WallType).
 * @param isAI             A boolean indicating whether the player is controlled by AI or not.
 */
public record PlayerTransferObject(int id, String name, Point position, boolean isInTurn,
                                   ArrayList<Point> allowedMoves, int wallsPlaced, int secondsRemaining,
                                   HashMap<WallType, Integer> wallsRemaining, boolean isAI, boolean isAlive) {

}

