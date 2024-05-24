package model.difficulty;

import java.io.Serializable;

public enum DifficultyType implements Serializable {

    NORMAL("Normal"),
    AGAINST_THE_CLOCK("Against the Clock"),
    TIMED("Dead Zone !");

    private final String typeString;

    DifficultyType(String type) {
        this.typeString = type;
    }

    @Override
    public String toString() {
        return typeString;
    }

}
