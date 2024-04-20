package model.cell;

public final class NormalCell extends BaseCell {
    public NormalCell(int x, int y) {
        super(x, y);
    }

    @Override
    public CellType getType() {
        return CellType.NORMAL;
    }
}
