package view.components.match;

import model.cell.CellType;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

public final class CellRenderer {

    private final Style boardStyle;
    private final GlobalContext globalContext;
    private final MatchContext matchContext;

    public CellRenderer(Style boardStyle, GlobalContext globalContext, MatchContext matchContext) {
        this.boardStyle = boardStyle;
        this.globalContext = globalContext;
        this.matchContext = matchContext;
    }

    public void render(Graphics2D graphics) {

        int widthCells = this.matchContext.cells().length;
        int heightCells = this.matchContext.cells()[0].length;

        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));

        for (int i = 0; i < widthCells; i++) {
            for (int j = 0; j < heightCells; j++) {
                CellType cellType = this.matchContext.cells()[i][j];

                int x = this.boardStyle.x + this.boardStyle.borderWidth + i * (CELL_SIZE + WALL_SIZE);
                int y = this.boardStyle.y + this.boardStyle.borderWidth + j * (CELL_SIZE + WALL_SIZE);

//                graphics.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 8, 8);
                graphics.drawImage(this.getCellImage(cellType), x, y, null);
            }
        }
    }

    private Image getCellImage(CellType cellType) {
        BufferedImage image = new BufferedImage(CELL_SIZE, CELL_SIZE, BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = image.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        switch (cellType) {
            case NORMAL -> drawNormalCell(graphics);
            default ->
                //TODO: Handle this case or throw
                    throw new IllegalArgumentException("Invalid cell type: " + cellType);
        }

        graphics.dispose();

        return image;
    }

    private void drawNormalCell(Graphics2D graphics) {
        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
        graphics.fillRoundRect(0, 0, CELL_SIZE, CELL_SIZE, 8, 8);
    }
}
