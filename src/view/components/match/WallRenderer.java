package view.components.match;

import model.wall.WallType;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

public final class WallRenderer {

    private final GlobalContext globalContext;
    private final MatchContext matchContext;
    private final Style boardStyle;

    public WallRenderer(Style boardStyle, GlobalContext globalContext, MatchContext matchContext) {
        this.globalContext = globalContext;
        this.matchContext = matchContext;
        this.boardStyle = boardStyle;
    }

    public void render(Graphics2D graphics) {
        this.renderWalls(graphics);
        this.renderWallPreview(graphics);
    }

    private void renderWalls(Graphics2D graphics) {

        for (int x = 0; x < this.matchContext.walls().length; x++) {
            for (int y = 0; y < this.matchContext.walls()[0].length; y++) {
                WallType wallType = this.matchContext.walls()[x][y];

                if (wallType == null) {
                    continue;
                }

                this.renderWall(graphics, x, y, null, this.globalContext.currentTheme().getColor(Theme.ColorName.PRIMARY, Theme.ColorVariant.NORMAL));
            }
        }
    }

    private void renderWallPreview(Graphics2D graphics) {
        if (!this.matchContext.mouseOverEmptyWall()) {
            return;
        }

        int x = this.matchContext.mousePosition().x;
        int y = this.matchContext.mousePosition().y;
        int[] scaleParams = {0, 0};

        //TODO: Get scale from model
        int scale = this.matchContext.selectedWallType() == WallType.NORMAL ? 2 : 3;

        if (this.matchContext.mousePosition().x % 2 == 0) {
            scaleParams[0] = scale;
        }
        if (this.matchContext.mousePosition().y % 2 == 0) {
            scaleParams[1] = scale;
        }

        this.renderWall(graphics, x, y, scaleParams, this.globalContext.currentTheme().getColor(Theme.ColorName.PRIMARY, Theme.ColorVariant.DIMMED));
    }

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
                this.boardStyle.x + this.boardStyle.paddingX + CELL_SIZE * cellsCountX + WALL_SIZE * wallsCountX,
                this.boardStyle.y + this.boardStyle.paddingY + CELL_SIZE * cellsCountY + WALL_SIZE * wallsCountY,
                width,
                height
        );
    }

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
