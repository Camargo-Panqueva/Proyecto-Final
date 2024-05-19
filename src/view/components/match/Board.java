package view.components.match;

import controller.dto.BoardTransferObject;
import controller.dto.ServiceResponse;
import model.wall.WallType;
import view.components.GameComponent;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.input.KeyboardEvent;
import view.input.MouseEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;

public final class Board extends GameComponent {

    public final static int CELL_SIZE = 48;
    public final static int WALL_SIZE = CELL_SIZE / 4;

    private final PlayerRenderer playerRenderer;
    private final CellRenderer cellRenderer;
    private final WallRenderer wallRenderer;
    private final MatchContext matchContext;

    private int lastTurnCount;
    private int lastSecondsRemaining;

    /**
     * Creates a new Board component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public Board(GlobalContext globalContext, MatchContext matchContext) {

        super(globalContext);
        this.matchContext = matchContext;
        this.lastTurnCount = -1;
        this.lastSecondsRemaining = -1;

        this.fetchBoardState();

        this.playerRenderer = new PlayerRenderer(this.style, this.globalContext, this.matchContext);
        this.cellRenderer = new CellRenderer(this.style, this.globalContext, this.matchContext);
        this.wallRenderer = new WallRenderer(this.style, this.globalContext, this.matchContext);
    }

    private void fetchBoardState() {
        ServiceResponse<BoardTransferObject> boardStateResponse = this.globalContext.controller().getBoardState();

        if (!boardStateResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to get board state: " + boardStateResponse.message);
        }

        BoardTransferObject boardState = boardStateResponse.payload;

        this.matchContext.setCells(boardState.cells());
        this.matchContext.setPlayers(boardState.players());
        this.matchContext.setWalls(boardState.walls());
        this.matchContext.setPlayerInTurn(boardState.playerInTurn());
        this.matchContext.setTurnCount(boardState.turnCount());

        if (this.lastTurnCount != boardState.turnCount()) {
            this.lastTurnCount = boardState.turnCount();
            this.matchContext.dispatchEvent(MatchContext.MatchEvent.TURN_CHANGED, this.matchContext.playerInTurn());
        }

        if (this.lastSecondsRemaining != this.matchContext.playerInTurn().secondRemaining()) {
            this.lastSecondsRemaining = this.matchContext.playerInTurn().secondRemaining();
            this.matchContext.dispatchEvent(MatchContext.MatchEvent.REMAINING_TIME_CHANGED, this.matchContext.playerInTurn());
        }
    }

    private void renderBackground(Graphics2D graphics) {
        graphics.setColor(this.globalContext.currentTheme().getColor(this.style.backgroundColor));
        graphics.fillRoundRect(this.style.x, this.style.y, this.style.width, this.style.height, this.style.borderRadius, this.style.borderRadius);
    }

    private void updateParsedMousePosition() {

        Point relativePosition = this.globalContext.mouse().getMouseRelativePosition(this.getBounds());

        relativePosition.x -= this.style.borderWidth;
        relativePosition.y -= this.style.borderWidth;

        int parsedX = ((relativePosition.x) / (CELL_SIZE + WALL_SIZE)) * 2;
        int parsedY = ((relativePosition.y) / (CELL_SIZE + WALL_SIZE)) * 2;

        if ((relativePosition.x) % (CELL_SIZE + WALL_SIZE) > CELL_SIZE) {
            parsedX++;
        }
        if ((relativePosition.y) % (CELL_SIZE + WALL_SIZE) > CELL_SIZE) {
            parsedY++;
        }

        parsedX = relativePosition.x < 0 ? -1 : parsedX;
        parsedY = relativePosition.y < 0 ? -1 : parsedY;

        this.matchContext.setMousePosition(new Point(parsedX, parsedY));
    }

    private void updateCursor() {
        Point movementPoint = new Point((this.matchContext.mousePosition().x + 1) / 2, (this.matchContext.mousePosition().y + 1) / 2);

        boolean inAllowedAction = this.matchContext.mouseOverWall() || this.matchContext.playerInTurn().allowedMoves().contains(movementPoint);

        if (this.matchContext.mouseOutOfBounds()) return;

        if (inAllowedAction) {
            this.globalContext.window().getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        } else {
            this.globalContext.window().getCanvas().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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

        this.renderBackground(graphics);

        this.wallRenderer.render(graphics);
        this.cellRenderer.render(graphics);
        this.playerRenderer.render(graphics);
    }

    @Override
    public void fitSize() {

        int widthCells = this.matchContext.cells().length;
        int heightCells = this.matchContext.cells()[0].length;

        this.style.width = this.style.borderWidth * 2 + CELL_SIZE * widthCells + WALL_SIZE * (widthCells - 1);
        this.style.height = this.style.borderWidth * 2 + CELL_SIZE * heightCells + WALL_SIZE * (heightCells - 1);
    }

    @Override
    protected void setupDefaultStyle() {
        this.style.width = this.globalContext.window().getCanvasSize();
        this.style.height = this.globalContext.window().getCanvasSize();
        this.style.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
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
        if (this.matchContext.mouseOutOfBounds() || this.matchContext.mouseOverWall()) {
            return;
        }

        Point movementPoint = new Point((this.matchContext.mousePosition().x + 1) / 2, (this.matchContext.mousePosition().y + 1) / 2);

        this.tryMovePlayer(movementPoint);
    }

    private void handlePlayerKeyboardMovement(KeyboardEvent event) {
        Point movementPoint = new Point(this.matchContext.playerInTurn().position());

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
        if (this.matchContext.mouseOutOfBounds() || !this.matchContext.mouseOverEmptyWall()) {
            return;
        }

        int playerId = this.matchContext.playerInTurn().id();
        Point wallPosition = new Point(this.matchContext.mousePosition().x, this.matchContext.mousePosition().y);
        WallType wallType = this.matchContext.selectedWallType();

        ServiceResponse<Void> response = this.globalContext.controller().placeWall(playerId, wallPosition, wallType);

        if (!response.ok) {
            //TODO: Handle error
            System.out.println("Failed to place wall: " + response.message);
        }

        this.matchContext.dispatchEvent(MatchContext.MatchEvent.WALL_PLACED, this.matchContext.playerInTurn());
    }

    private void tryMovePlayer(Point newPosition) {
        if (!this.matchContext.playerInTurn().allowedMoves().contains(newPosition)) {
            return;
        }

        ServiceResponse<Void> movementResponse = this.globalContext.controller().processPlayerMove(this.matchContext.playerInTurn().id(), newPosition);

        if (!movementResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to process player move: " + movementResponse.message);
        }

        this.matchContext.dispatchEvent(MatchContext.MatchEvent.PLAYER_MOVED, this.matchContext.playerInTurn());
    }
}
