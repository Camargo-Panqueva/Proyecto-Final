package model.cell;

import java.io.Serializable;

/**
 * Represents different types of cells on the board.
 */
public enum CellType implements Serializable {
    /**
     * Normal cell.
     */
    NORMAL("Normal"),

    /**
     * Teleport cell.
     */
    TELEPORT("Teleport"),

    /**
     * Return cell.
     */
    RETURN("Return"),

    /**
     * Double Turn cell.
     */
    DOUBLE_TURN("Double Turn");

    /**
     * The name of the cell type.
     */
    private final String name;

    /**
     * Constructor for CellType.
     *
     * @param type The name of the cell type.
     */
    CellType(String type) {
        this.name = type;
    }

    /**
     * Returns the name of the cell type.
     *
     * @return The name of the cell type.
     */
    @Override
    public String toString() {
        return name;
    }
}
