package model.difficulty;

import java.io.Serializable;

/**
 * Represents the difficulty settings of the game.
 */
public class Difficulty implements Serializable {

    /**
     * The type of difficulty.
     */
    private DifficultyType difficultyType;

    /**
     * The time allowed per turn in seconds.
     */
    private int timePerTurn;

    /**
     * The total time allowed for the game in seconds.
     */
    private int timeTotal;

    /**
     * Indicates whether the difficulty has been set.
     */
    private boolean hasBeenSet;

    /**
     * Constructs a new Difficulty instance with default settings.
     */
    public Difficulty() {
        this.difficultyType = DifficultyType.NORMAL;
        this.timePerTurn = 0;
        this.timeTotal = 0;
        this.hasBeenSet = false;
    }

    /**
     * Sets the difficulty settings.
     *
     * @param difficultyType The type of difficulty.
     * @param timePerTurn    The time allowed per turn in seconds.
     * @param timeTotal      The total time allowed for the game in seconds.
     */
    public void setDifficulty(final DifficultyType difficultyType, final int timePerTurn, final int timeTotal) {
        if (!this.hasBeenSet) {
            this.difficultyType = difficultyType;
            this.timePerTurn = timePerTurn;
            this.timeTotal = timeTotal;
        }
        this.hasBeenSet = true;
    }

    public DifficultyType getDifficultyType() {
        return difficultyType;
    }

    public int getTimePerTurn() {
        return timePerTurn;
    }

    public boolean getHasBeenSet() {
        return this.hasBeenSet;
    }

    public int getTimeTotal() {
        return timeTotal;
    }
}

