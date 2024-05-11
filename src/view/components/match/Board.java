package view.components.match;

import controller.dto.BoardTransferObject;
import controller.dto.PlayerTransferObject;
import controller.dto.ServiceResponse;
import model.cell.CellType;
import model.wall.WallType;
import view.components.GameComponent;
import view.context.ContextProvider;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class Board extends GameComponent {

    private final static int CELL_SIZE = 54;
    private final static int WALL_SIZE = 14;
    private final int widthCells;
    private final int heightCells;
    private final Point parsedMousePosition;
    private CellType[][] cells;
    private WallType[][] walls;
    private ArrayList<PlayerTransferObject> players;
    private PlayerTransferObject playerInTurn;

    /**
     * Creates a new Board component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public Board(ContextProvider contextProvider) {

        //TODO : fit canvas size for the height
        super(contextProvider);

        this.updateBoardState();

        this.widthCells = cells.length;
        this.heightCells = cells[0].length;

        this.parsedMousePosition = new Point();
    }

    private void updateBoardState() {
        ServiceResponse<BoardTransferObject> boardStateResponse = this.contextProvider.controller().getBoardState();

        if (!boardStateResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to get board state: " + boardStateResponse.message);
        }

        BoardTransferObject boardState = boardStateResponse.payload;

        this.cells = boardState.cells();
        this.players = boardState.players();
        this.walls = boardState.walls();

        this.playerInTurn = boardState.playerInTurn();
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

            if (player.isInTurn()) {
                graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().primaryColor);
            } else {
                graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().secondaryColor);
            }

            int x = player.position().x * (CELL_SIZE + WALL_SIZE) + this.style.x + this.style.paddingX;
            int y = player.position().y * (CELL_SIZE + WALL_SIZE) + this.style.y + this.style.paddingY;

            graphics.fillOval(x, y, CELL_SIZE, CELL_SIZE);
            graphics.drawString(player.name(), x + CELL_SIZE, y - 3);
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

    private void updateParsedMousePosition() {
        this.parsedMousePosition.x = this.contextProvider.mouse().getMouseRelativePosition(this.getBounds()).x / (CELL_SIZE + WALL_SIZE);
        this.parsedMousePosition.y = this.contextProvider.mouse().getMouseRelativePosition(this.getBounds()).y / (CELL_SIZE + WALL_SIZE);
    }

    private void updateCursor() {
        if (this.playerInTurn.allowedMoves().contains(this.parsedMousePosition)) {
            this.contextProvider.window().getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            this.contextProvider.window().getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public void update() {
        this.pollMouseEvents();
        this.updateParsedMousePosition();
        this.updateCursor();
    }

    @Override
    public void render(Graphics2D graphics) {
        graphics.setColor(this.style.backgroundColor);
        graphics.fillRoundRect(this.style.x, this.style.y, this.style.width, this.style.height, this.style.borderRadius, this.style.borderRadius);

        this.renderCells(graphics);
        this.renderPlayers(graphics);
        this.renderWalls(graphics);
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
            if (!playerInTurn.allowedMoves().contains(this.parsedMousePosition)) {
                return;
            }

            ServiceResponse<Void> movementResponse =
                    this.contextProvider.controller().processPlayerMove(this.playerInTurn.id(), new Point(this.parsedMousePosition));

            if (!movementResponse.ok) {
                //TODO: Handle error
                throw new RuntimeException("Failed to process player move: " + movementResponse.message);
            }

            this.updateBoardState();
        });
    }
}
