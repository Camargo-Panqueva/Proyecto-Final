package model.difficulty;

public enum DifficultyTypes {

    NORMAL("NORMAL"),
    AGAINST_THE_CLOCK("AGAINST_THE_CLOCK"),
    TIMED("TIMED");

    private final String typeString;

    DifficultyTypes(String type) {
        this.typeString = type;
    }

    @Override
    public String toString() {
        return typeString;
    }

}
