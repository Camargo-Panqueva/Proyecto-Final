package view.components.match;

import controller.dto.ServiceResponse;
import controller.dto.WallTransferObject;
import model.wall.WallType;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.ThemeColor;
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

        for (WallTransferObject wall : this.matchContext.walls()) {
            Color color = this.globalContext.currentTheme().getColor(this.getWallColor(wall, ColorVariant.NORMAL));
            this.renderWall(graphics, wall, color);
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

        ServiceResponse<WallTransferObject> wallResponse = this.globalContext.controller().getWallPreview(new Point(x, y), this.matchContext.selectedWallType());

        if (!wallResponse.ok) {

            return;
        }

        WallTransferObject wall = wallResponse.payload;

        Color color = this.globalContext.currentTheme().getColor(this.getWallColor(wall, ColorVariant.DIMMED));

        this.renderWall(graphics, wall, color);
    }

    /**
     * Renders a wall on the screen.
     *
     * @param graphics the graphics object to render the wall with.
     * @param wall     the wall to render.
     * @param color    the color of the wall.
     */
    private void renderWall(Graphics2D graphics, WallTransferObject wall, Color color) {
        int width;
        int height;

        int wallsCountX;
        int wallsCountY;

        int cellsCountX;
        int cellsCountY;

        graphics.setColor(color);

        for (int x = 0; x < wall.wallShape().length; x++) {
            for (int y = 0; y < wall.wallShape()[0].length; y++) {

                WallType wallType = wall.wallShape()[x][y];

                if (wallType == null) {
                    continue;
                }

                int currentX = wall.position().x + x;
                int currentY = wall.position().y + y;

                wallsCountX = currentX / 2;
                wallsCountY = currentY / 2;

                cellsCountX = (int) Math.ceil(currentX / 2.0);
                cellsCountY = (int) Math.ceil(currentY / 2.0);

                if (currentX % 2 == 0 && currentY % 2 == 0) {
                    width = CELL_SIZE;
                    height = CELL_SIZE;
                } else if (currentX % 2 == 0) {
                    width = CELL_SIZE;
                    height = WALL_SIZE;
                } else if (currentY % 2 == 0) {
                    width = WALL_SIZE;
                    height = CELL_SIZE;
                } else {
                    width = WALL_SIZE;
                    height = WALL_SIZE;
                }

                graphics.fillRect(
                        this.boardStyle.x + this.boardStyle.borderWidth + CELL_SIZE * cellsCountX + WALL_SIZE * wallsCountX,
                        this.boardStyle.y + this.boardStyle.borderWidth + CELL_SIZE * cellsCountY + WALL_SIZE * wallsCountY,
                        width,
                        height
                );
            }
        }


    }

    /**
     * Gets the color of the wall.
     *
     * @param wall    the wall to get the color of.
     * @param variant the variant of the color.
     * @return the color of the wall.
     */
    private ThemeColor getWallColor(WallTransferObject wall, ColorVariant variant) {

        if (wall.wallType() == WallType.ALLY) {
            return this.matchContext.getPlayerColor(wall.owner(), variant);
        }

        return new ThemeColor(ColorName.PRIMARY, variant);
    }
}
