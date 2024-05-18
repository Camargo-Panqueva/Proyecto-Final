package model.difficulty;

public enum DifficultyType {

    NORMAL("NORMAL"),
    AGAINST_THE_CLOCK("AGAINST_THE_CLOCK"),
    TIMED("TIMED");

    private final String typeString;

    DifficultyType(String type) {
        this.typeString = type;
    }

    @Override
    public String toString() {
        return typeString;
    }

}
