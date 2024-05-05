package view.components.match;

import controller.dto.BoardTransferObject;
import controller.dto.PlayerTransferObject;
import model.cell.CellType;
import model.wall.WallType;
import view.components.GameComponent;
import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class Board extends GameComponent {

    private final static int CELL_SIZE = 42;
    private final static int WALL_SIZE = 14;

    private final CellType[][] cells;
    private final WallType[][] walls;
    private final ArrayList<PlayerTransferObject> players;
    private final int widthCells;
    private final int heightCells;

    /**
     * Creates a new Board component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public Board(BoardTransferObject boardState, ContextProvider contextProvider) {
        super(contextProvider);

        this.cells = boardState.cells();
        this.players = boardState.players();
        this.walls = boardState.walls();

        this.widthCells = cells.length;
        this.heightCells = cells[0].length;
    }

    private void renderCells(Graphics2D graphics) {
        graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().backgroundColor);

        for (int i = 0; i < this.widthCells; i++) {
            for (int j = 0; j < this.heightCells; j++) {
                int x = this.style.x + this.style.paddingX + i * (CELL_SIZE + WALL_SIZE);
                int y = this.style.y + this.style.paddingY + j * (CELL_SIZE + WALL_SIZE);

                graphics.fillRoundRect(x, y, CELL_SIZE, CELL_SIZE, 8, 8);
            }
        }
    }

    private void renderPlayers(Graphics2D graphics) {

        for (PlayerTransferObject player : this.players) {
            graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().primaryColor);

            int x = player.position().x * (CELL_SIZE + WALL_SIZE) + this.style.x + this.style.paddingX;
            int y = player.position().y * (CELL_SIZE + WALL_SIZE) + this.style.y + this.style.paddingY;

            graphics.fillOval(x, y, CELL_SIZE, CELL_SIZE);

            graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().primaryColor);
            graphics.drawString(player.name(), x + CELL_SIZE, y - 5);

            this.renderAllowedMoves(graphics, player);
        }
    }

    private void renderAllowedMoves(Graphics2D graphics, PlayerTransferObject player) {
        for (Point move : player.allowedMoves()) {
            int x = move.x * (CELL_SIZE + WALL_SIZE) + this.style.x + this.style.paddingX;
            int y = move.y * (CELL_SIZE + WALL_SIZE) + this.style.y + this.style.paddingY;

            graphics.drawOval(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private void renderWalls(Graphics2D graphics) {

    }

    @Override
    public void update() {
        this.pollMouseEvents();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(this.style.backgroundColor);
        graphics.fillRoundRect(this.style.x, this.style.y, this.style.width, this.style.height, this.style.borderRadius, this.style.borderRadius);

        this.renderCells(graphics);
        this.renderPlayers(graphics);
    }

    @Override
    public void fitSize() {
        int margin = this.style.paddingX * 4;

        this.style.width = this.style.paddingX * 2 + CELL_SIZE * this.widthCells + WALL_SIZE * (this.widthCells - 1);
        this.style.height = this.style.paddingY * 2 + CELL_SIZE * this.heightCells + WALL_SIZE * (this.heightCells - 1);

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

    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addEventListener(MouseEventType.RELEASED, event -> {
            int x = event.relativeMousePosition.x;
            int y = event.relativeMousePosition.y;

            int cellX = x / (CELL_SIZE + WALL_SIZE);
            int cellY = y / (CELL_SIZE + WALL_SIZE);

            System.out.println("Clicked on cell: " + cellX + ", " + cellY);
        });
    }
}
