package view.components.match;

import controller.dto.BoardTransferObject;
import controller.dto.PlayerTransferObject;
import controller.dto.ServiceResponse;
import controller.wall.LargeWall;
import controller.wall.Wall;
import model.cell.CellType;
import model.wall.WallType;
import view.components.GameComponent;
import view.context.ContextProvider;
import view.input.KeyboardEvent;
import view.input.MouseEvent;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class Board extends GameComponent {

    private final static int CELL_SIZE = 48;
    private final static int WALL_SIZE = CELL_SIZE / 4;
    private final int widthCells;
    private final int heightCells;
    private final Point parsedMousePosition;

    private CellType[][] cells;
    private WallType[][] walls;
    private ArrayList<PlayerTransferObject> players;
    private PlayerTransferObject playerInTurn;

    private boolean isMouseInvalid;

    /**
     * Creates a new Board component with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public Board(ContextProvider contextProvider) {

        //TODO : fit canvas size for the height
        super(contextProvider);

        this.fetchBoardState();

        this.widthCells = cells.length;
        this.heightCells = cells[0].length;

        this.parsedMousePosition = new Point();
    }

    private void fetchBoardState() {
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

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().foregroundColor);
        graphics.drawString(String.format("Relative Mouse Position: [%d, %d]", this.parsedMousePosition.x, this.parsedMousePosition.y), 6, 86);
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

        for (int x = 0; x < this.walls.length; x++) {
            for (int y = 0; y < this.walls[0].length; y++) {
                WallType wallType = this.walls[x][y];

                if (wallType == null) {
                    continue;
                }

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

                graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().secondaryColor);

                graphics.fillRect(
                        this.style.x + this.style.paddingX + CELL_SIZE * cellsCountX + WALL_SIZE * wallsCountX,
                        this.style.y + this.style.paddingY + CELL_SIZE * cellsCountY + WALL_SIZE * wallsCountY,
                        width,
                        height
                );
            }
        }
    }

    private void updateParsedMousePosition() {

        Point relativePosition = this.contextProvider.mouse().getMouseRelativePosition(this.getBounds());

        relativePosition.x -= this.style.paddingX;
        relativePosition.y -= this.style.paddingY;

        int parsedX = ((relativePosition.x) / (CELL_SIZE + WALL_SIZE)) * 2;
        int parsedY = ((relativePosition.y) / (CELL_SIZE + WALL_SIZE)) * 2;

        if ((relativePosition.x) % (CELL_SIZE + WALL_SIZE) > CELL_SIZE) {
            parsedX++;
        }
        if ((relativePosition.y) % (CELL_SIZE + WALL_SIZE) > CELL_SIZE) {
            parsedY++;
        }

        boolean isOutOfBounds =
                relativePosition.y < 0 || relativePosition.x < 0 || parsedY >= 2 * this.heightCells - 1 || parsedX >= 2 * this.widthCells - 1;

        if (isOutOfBounds) {
            this.parsedMousePosition.setLocation(-1, -1);
            this.isMouseInvalid = true;
            return;
        }

        this.isMouseInvalid = false;
        this.parsedMousePosition.setLocation(parsedX, parsedY);
    }

    private void updateCursor() {
        Point movementPoint = new Point((this.parsedMousePosition.x + 1) / 2, (this.parsedMousePosition.y + 1) / 2);

        if (this.playerInTurn.allowedMoves().contains(movementPoint)) {
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
        this.fetchBoardState();
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

        this.addMouseListener(MouseEvent.EventType.RELEASED, event -> {
            this.handleWallPlacement(event);
            this.handlePlayerMouseMovement();
        });

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, event -> {
            this.handlePlayerKeyboardMovement(event);
        });
    }

    private void handlePlayerMouseMovement() {
        if (this.isMouseInvalid) {
            return;
        }

        Point movementPoint = new Point((this.parsedMousePosition.x + 1) / 2, (this.parsedMousePosition.y + 1) / 2);

        this.tryMovePlayer(movementPoint);
    }

    private void handlePlayerKeyboardMovement(KeyboardEvent event) {
        Point movementPoint = new Point(this.playerInTurn.position());

        switch (event.keyCode) {
            case KeyboardEvent.VK_UP:
            case KeyboardEvent.VK_W:
                movementPoint.y--;
                break;
            case KeyboardEvent.VK_DOWN:
            case KeyboardEvent.VK_S:
                movementPoint.y++;
                break;
            case KeyboardEvent.VK_LEFT:
            case KeyboardEvent.VK_A:
                movementPoint.x--;
                break;
            case KeyboardEvent.VK_RIGHT:
            case KeyboardEvent.VK_D:
                movementPoint.x++;
                break;
            default:
                return;
        }

        this.tryMovePlayer(movementPoint);
    }

    private void handleWallPlacement(MouseEvent event) {
        if (this.isMouseInvalid) {
            return;
        }

        Wall wall = new LargeWall();
        wall.getWallData().setPositionOnBoard(new Point(parsedMousePosition.x, parsedMousePosition.y));

        ServiceResponse<Void> response = this.contextProvider.controller().placeWall(this.playerInTurn.id(), wall);

        if (!response.ok) {
            //TODO: Handle error
            System.out.println("Failed to place wall: " + response.message);
        }
    }

    private void tryMovePlayer(Point newPosition) {
        if (!this.playerInTurn.allowedMoves().contains(newPosition)) {
            return;
        }

        ServiceResponse<Void> movementResponse =
                this.contextProvider.controller().processPlayerMove(this.playerInTurn.id(), newPosition);

        if (!movementResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to process player move: " + movementResponse.message);
        }
    }
}
