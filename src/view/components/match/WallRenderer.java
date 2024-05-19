package view.components.match;

import model.wall.WallType;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

/**
 * Represents a renderer for the walls of the board.
 * <p>
 * This class represents a renderer for the walls of the board.
 * It provides a structure for rendering the walls of the board on the screen.
 * The wall renderer is used to render the walls of the board on the screen.
 * </p>
 */
public final class WallRenderer {

    private final GlobalContext globalContext;
    private final MatchContext matchContext;
    private final Style boardStyle;

    /**
     * Creates a new wall renderer with the given style, global context, and match context.
     *
     * @param boardStyle    the style of the board.
     * @param globalContext the global context of the game.
     * @param matchContext  the match context of the game.
     */
    public WallRenderer(Style boardStyle, GlobalContext globalContext, MatchContext matchContext) {
        this.globalContext = globalContext;
        this.matchContext = matchContext;
        this.boardStyle = boardStyle;
    }

    /**
     * Renders the walls of the board on the screen.
     *
     * @param graphics the graphics object to render the walls with.
     */
    public void render(Graphics2D graphics) {
        this.renderWalls(graphics);
        this.renderWallPreview(graphics);
    }

    /**
     * Renders the walls of the board on the screen.
     *
     * @param graphics the graphics object to render the walls with.
     */
    private void renderWalls(Graphics2D graphics) {

        for (int x = 0; x < this.matchContext.walls().length; x++) {
            for (int y = 0; y < this.matchContext.walls()[0].length; y++) {
                WallType wallType = this.matchContext.walls()[x][y];

                if (wallType == null) {
                    continue;
                }

                this.renderWall(graphics, x, y, null, this.globalContext.currentTheme().getColor(ColorName.PRIMARY, ColorVariant.NORMAL));
            }
        }
    }

    /**
     * Renders the wall preview on the screen.
     *
     * @param graphics the graphics object to render the wall preview with.
     */
    private void renderWallPreview(Graphics2D graphics) {
        if (!this.matchContext.mouseOverEmptyWall()) {
            return;
        }

        int x = this.matchContext.mousePosition().x;
        int y = this.matchContext.mousePosition().y;
        int[] scaleParams = {0, 0};

        //TODO: Get scale from model
        int scale = this.matchContext.selectedWallType() == WallType.LARGE ? 3 : 2;

        if (this.matchContext.mousePosition().x % 2 == 0) {
            scaleParams[0] = scale;
        }
        if (this.matchContext.mousePosition().y % 2 == 0) {
            scaleParams[1] = scale;
        }

        this.renderWall(graphics, x, y, scaleParams, this.globalContext.currentTheme().getColor(ColorName.PRIMARY, ColorVariant.DIMMED));
    }

    /**
     * Renders a wall on the screen.
     *
     * @param graphics    the graphics object to render the wall with.
     * @param x           the x-coordinate of the wall.
     * @param y           the y-coordinate of the wall.
     * @param scaleParams the scale parameters of the wall.
     * @param color       the color of the wall.
     */
    private void renderWall(Graphics2D graphics, int x, int y, int[] scaleParams, Color color) {
        int[] renderParams = this.calculateWallRenderParams(x, y);

        int cellsCountX = renderParams[0];
        int cellsCountY = renderParams[1];
        int wallsCountX = renderParams[2];
        int wallsCountY = renderParams[3];
        int width = renderParams[4];
        int height = renderParams[5];

        graphics.setColor(color);

        if (scaleParams != null) {

            boolean adjustWidth = scaleParams[0] != 0;
            boolean adjustHeight = scaleParams[1] != 0;

            if (adjustWidth) {
                width = scaleParams[0] * (WALL_SIZE + CELL_SIZE) - WALL_SIZE;
            }

            if (adjustHeight) {
                height = scaleParams[1] * (WALL_SIZE + CELL_SIZE) - WALL_SIZE;
            }
        }

        graphics.fillRect(
                this.boardStyle.x + this.boardStyle.borderWidth + CELL_SIZE * cellsCountX + WALL_SIZE * wallsCountX,
                this.boardStyle.y + this.boardStyle.borderWidth + CELL_SIZE * cellsCountY + WALL_SIZE * wallsCountY,
                width,
                height
        );
    }

    /**
     * Calculates the render parameters of a wall.
     * <p>
     * This method calculates the render parameters of a wall.
     * It calculates the number of cells and walls in the x and y direction.
     * It also calculates the width and height of the wall.
     * </p>
     *
     * @param x the x-coordinate of the wall.
     * @param y the y-coordinate of the wall.
     * @return the render parameters of the wall.
     */
    private int[] calculateWallRenderParams(int x, int y) {

        int cellsCountX = (int) Math.ceil(x / 2.0);
        int cellsCountY = (int) Math.ceil(y / 2.0);

        int wallsCountX = x / 2;
        int wallsCountY = y / 2;

        int width;
        int height;

        if (x % 2 == 0 && y % 2 == 0) {
            width = CELL_SIZE;
            height = CELL_SIZE;
        } else if (x % 2 == 0) {
            width = CELL_SIZE;
            height = WALL_SIZE;
        } else if (y % 2 == 0) {
            width = WALL_SIZE;
            height = CELL_SIZE;
        } else {
            width = WALL_SIZE;
            height = WALL_SIZE;
        }

        return new int[]{cellsCountX, cellsCountY, wallsCountX, wallsCountY, width, height};
    }
}
