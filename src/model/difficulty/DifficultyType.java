package model.difficulty;

public enum DifficultyType {

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
