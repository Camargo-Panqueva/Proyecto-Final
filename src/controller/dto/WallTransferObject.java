package controller.dto;

import model.wall.WallType;

import java.awt.*;

/**
 * Represents a transfer object for walls in the game.
 * This record encapsulates information about a wall, including its owner, type, creation turn, position, and shape.
 *
 * @param owner        The PlayerTransferObject representing the owner of the wall.
 * @param wallType     The type of the wall (e.g., horizontal, vertical).
 * @param creationTurn The turn in which the wall was created.
 * @param position     The position of the wall on the board, represented as a Point.
 * @param wallShape    A 2D array representing the shape of the wall on the board.
 */
public record WallTransferObject(PlayerTransferObject owner, WallType wallType, int creationTurn, Point position,
                                 WallType[][] wallShape) {
}
