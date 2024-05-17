package view.components.match;

import model.cell.CellType;
import view.context.ContextProvider;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

public final class CellRenderer {

    private final Style boardStyle;
    private final ContextProvider contextProvider;

    public CellRenderer(Style boardStyle, ContextProvider contextProvider) {
        this.boardStyle = boardStyle;
        this.contextProvider = contextProvider;
    }

    public void render(Graphics2D graphics, CellType[][] cells) {

        int widthCells = cells.length;
        int heightCells = cells[0].length;

        graphics.setColor(this.contextProvider.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));

        for (int i = 0; i < widthCells; i++) {
            for (int j = 0; j < heightCells; j++) {
                CellType cellType = cells[i][j];

                int x = this.boardStyle.x + this.boardStyle.paddingX + i * (CELL_SIZE + WALL_SIZE);
                int y = this.boardStyle.y + this.boardStyle.paddingY + j * (CELL_SIZE + WALL_SIZE);

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
        graphics.setColor(this.contextProvider.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.fillRoundRect(0, 0, CELL_SIZE, CELL_SIZE, 8, 8);
    }
}
