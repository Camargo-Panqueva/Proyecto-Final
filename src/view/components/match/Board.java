package view.components.match;

import model.cell.CellType;
import view.components.GameComponent;
import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class Board extends GameComponent {

    private final static int CELL_SIZE = 64;
    private final static int WALL_SIZE = 16;
    private final ArrayList<CellType> cells;

    /**
     * Creates a new Board component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public Board(ArrayList<CellType> cells, ContextProvider contextProvider) {
        super(contextProvider);
        this.cells = cells;
    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(this.style.backgroundColor);
        graphics.fillRoundRect(this.style.x, this.style.y, this.style.width, this.style.height, this.style.borderRadius, this.style.borderRadius);

        /*
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int x = this.style.x + this.style.paddingX + i * (CELL_SIZE + WALL_SIZE);
                int y = this.style.y + this.style.paddingY + j * (CELL_SIZE + WALL_SIZE);

                graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().backgroundColor);
                graphics.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 12, 12);

                CellType cell = this.cells.get(i * 9 + j);
            }
        } */
    }

    @Override
    public void fitSize() {
        int margin = this.style.paddingX * 4;

        this.style.width = this.style.paddingX * 2 + CELL_SIZE * 9 + WALL_SIZE * 8;
        this.style.height = this.style.paddingY * 2 + CELL_SIZE * 9 + WALL_SIZE * 8;

        if (this.style.width + margin > this.contextProvider.window().getCanvasSize()) {
            this.contextProvider.window().setCanvasSize(this.style.width + margin);
        }
    }

    @Override
    protected void handleThemeChange(Theme theme) {

    }

    @Override
    protected void setupDefaultStyle() {
        this.style.paddingX = 16;
        this.style.paddingY = 16;
        this.style.borderRadius = 26;
        this.style.width = this.contextProvider.window().getCanvasSize();
        this.style.height = this.contextProvider.window().getCanvasSize();
        this.style.backgroundColor = this.contextProvider.themeManager().getCurrentTheme().backgroundContrastColor;
    }
}
