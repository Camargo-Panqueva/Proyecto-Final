package model.cell;

import java.io.Serializable;

public enum CellType implements Serializable {
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
