package model.cell;

public enum CellType {
    NORMAL("Normal"),
    TELEPORT("Teleport"),
    RETURN("Return"),
    DOUBLE_TURN("Double Turn");

    private final String name;

    CellType(String type) {
        this.name = type;
    }

    @Override
    public String toString() {
        return name;
    }
}
