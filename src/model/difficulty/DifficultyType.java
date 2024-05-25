package model.difficulty;

import java.io.Serializable;

/**
 * Represents different types of game difficulties.
 */
public enum DifficultyType implements Serializable {

    /**
     * Normal difficulty.
     */
    NORMAL("Normal"),

    /**
     * Against the Clock difficulty.
     */
    AGAINST_THE_CLOCK("Against the Clock"),

    /**
     * Timed difficulty, also known as Dead Zone.
     */
    TIMED("Dead Zone !");

    /**
     * The string representation of the difficulty type.
     */
    private final String typeString;

    /**
     * Constructor for DifficultyType.
     *
     * @param type The string representation of the difficulty type.
     */
    DifficultyType(String type) {
        this.typeString = type;
    }

    /**
     * Returns the string representation of the difficulty type.
     *
     * @return The string representation of the difficulty type.
     */
    @Override
    public String toString() {
        return typeString;
    }
}
