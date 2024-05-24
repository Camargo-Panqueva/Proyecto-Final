package model.difficulty;

import java.io.Serializable;

public class Difficulty implements Serializable {
    private DifficultyType difficultyType;
    private int timePerTurn;
    private int timeTotal;
    private boolean hasBeenSet;

    public Difficulty() {
        this.difficultyType = DifficultyType.NORMAL;
        this.timePerTurn = 0;
        this.timeTotal = 0;
        this.hasBeenSet = false;
    }

    public void setDifficulty(final DifficultyType difficultyType, final int timePerTurn, final int timeTotal){
        if (!this.hasBeenSet){
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

    public boolean getHasBeenSet(){
        return this.hasBeenSet;
    }

    public int getTimeTotal() {
        return timeTotal;
    }
}

