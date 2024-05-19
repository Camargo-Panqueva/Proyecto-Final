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

/**
 * Represents the game board component.
 * <p>
 * This class represents the game board component.
 * It is responsible for rendering the game board and handling player input.
 * The game board component is the main component of the game view.
 * It is responsible for rendering the game board and handling player input.
 * The game board component is responsible for rendering the game board and handling player input.
 * It is the main component of the game view.
 * </p>
 */
public final class Board extends GameComponent {

    /**
     * The size of a cell in pixels.
     */
    public final static int CELL_SIZE = 48;

    /**
     * The size of a wall in pixels.
     */
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
     * @param matchContext  the context provider for the component.
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

    /**
     * Fetches the current state of the game board.
     *
     * <p>
     * This method fetches the current state of the game board from the controller.
     * It updates the match context with the new state of the game board.
     * This method is called every frame to keep the game board up to date.
     * If the turn count has changed, the method dispatches a turn changed event.
     * If the remaining time has changed, the method dispatches a remaining time changed event.
     * </p>
     */
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

    /**
     * Renders the background of the game board.
     *
     * <p>
     * This method renders the background of the game board.
     * It fills the background with the background color from the current theme.
     * The background is rendered as a rounded rectangle with the border radius from the style.
     * </p>
     *
     * @param graphics the graphics object to render the background with.
     */
    private void renderBackground(Graphics2D graphics) {
        graphics.setColor(this.globalContext.currentTheme().getColor(this.style.backgroundColor));
        graphics.fillRoundRect(this.style.x, this.style.y, this.style.width, this.style.height, this.style.borderRadius, this.style.borderRadius);
    }

    /**
     * Updates the parsed mouse position.
     *
     * <p>
     * This method updates the parsed mouse position.
     * It calculates the parsed mouse position from the global mouse position.
     * The parsed mouse position is the position of the mouse in the game board grid.
     * The parsed mouse position is used to determine the player movement and wall placement.
     * </p>
     */
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

    /**
     * Polls the mouse events.
     *
     * <p>
     * This method polls the mouse events.
     * It checks if the mouse is pressed or released.
     * The method updates the mouse pressed and mouse entered flags.
     * The mouse pressed flag is used to determine if the mouse is currently pressed.
     * The mouse entered flag is used to determine if the mouse is currently over the game board.
     * </p>
     */
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

    /**
     * Updates the game board component.
     *
     * <p>
     * This method updates the game board component.
     * It polls the mouse events, updates the parsed mouse position, and updates the cursor.
     * It also fetches the current state of the game board.
     * This method is called every frame to keep the game board up to date.
     * </p>
     */
    @Override
    public void update() {
        this.pollMouseEvents();
        this.updateParsedMousePosition();
        this.updateCursor();
        this.fetchBoardState();
    }

    /**
     * Renders the game board component.
     *
     * <p>
     * This method renders the game board component.
     * It renders the background, walls, cells, and players of the game board.
     * The background is rendered as a rounded rectangle with the background color from the current theme.
     * The walls are rendered as rectangles with the wall color from the current theme.
     * The cells are rendered as rectangles with the cell color from the current theme.
     * The players are rendered as circles with the player color from the current theme.
     * </p>
     *
     * @param graphics the graphics object to render the component with.
     */
    @Override
    public void render(Graphics2D graphics) {

        this.renderBackground(graphics);

        this.wallRenderer.render(graphics);
        this.cellRenderer.render(graphics);
        this.playerRenderer.render(graphics);
    }

    /**
     * Fits the game board component to its content.
     *
     * <p>
     * This method fits the game board component to its content.
     * It calculates the width and height of the game board based on the number of cells and walls.
     * The width and height are calculated based on the cell size, wall size, and border width.
     * The game board is then centered on the canvas.
     * </p>
     */
    @Override
    public void fitSize() {

        int widthCells = this.matchContext.cells().length;
        int heightCells = this.matchContext.cells()[0].length;

        this.style.width = this.style.borderWidth * 2 + CELL_SIZE * widthCells + WALL_SIZE * (widthCells - 1);
        this.style.height = this.style.borderWidth * 2 + CELL_SIZE * heightCells + WALL_SIZE * (heightCells - 1);
    }

    /**
     * Sets up the default style for the game board component.
     */
    @Override
    protected void setupDefaultStyle() {
        this.style.width = this.globalContext.window().getCanvasSize();
        this.style.height = this.globalContext.window().getCanvasSize();
        this.style.backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
    }

    /**
     * Sets up the default event listeners for the game board component.
     *
     * <p>
     * This method sets up the default event listeners for the game board component.
     * It adds event listeners for mouse released events and key pressed events.
     * The mouse released event listener is used to handle wall placement.
     * The key pressed event listener is used to handle player movement.
     * </p>
     */
    @Override
    protected void setupDefaultEventListeners() {
        super.setupDefaultEventListeners();

        this.addMouseListener(MouseEvent.EventType.RELEASED, _event -> {
            this.handleWallPlacement();
            this.handlePlayerMouseMovement();
        });

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, this::handlePlayerKeyboardMovement);
    }

    /**
     * Handles the player mouse movement.
     *
     * <p>
     * This method handles the player mouse movement.
     * It checks if the mouse is out of bounds or over a wall.
     * If the mouse is out of bounds or over a wall, the method returns.
     * The method then calculates the movement point from the mouse position.
     * The method then tries to move the player to the movement point.
     * </p>
     */
    private void handlePlayerMouseMovement() {
        if (this.matchContext.mouseOutOfBounds() || this.matchContext.mouseOverWall()) {
            return;
        }

        Point movementPoint = new Point((this.matchContext.mousePosition().x + 1) / 2, (this.matchContext.mousePosition().y + 1) / 2);

        this.tryMovePlayer(movementPoint);
    }

    /**
     * Handles the player keyboard movement.
     *
     * <p>
     * This method handles the player keyboard movement.
     * It checks if the key code is a valid movement key.
     * If the key code is not a valid movement key, the method returns.
     * The method then calculates the movement point from the key code.
     * The method then tries to move the player to the movement point.
     * </p>
     *
     * @param event the keyboard event to handle.
     */
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

    /**
     * Handles the wall placement.
     *
     * <p>
     * This method handles the wall placement.
     * It checks if the mouse is out of bounds or over an empty wall.
     * If the mouse is out of bounds or over an empty wall, the method returns.
     * The method then calculates the player ID, wall position, and wall type.
     * The method then tries to place the wall at the wall position.
     * </p>
     */
    private void handleWallPlacement() {
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


    /**
     * Tries to move the player to the given position.
     *
     * <p>
     * This method tries to move the player to the given position.
     * It checks if the player is allowed to move to the given position.
     * If the player is not allowed to move to the given position, the method returns.
     * The method then tries to process the player move with the controller.
     * If the player move is successful, the method dispatches a player moved event.
     * </p>
     *
     * @param newPosition the new position to move the player to.
     */
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
