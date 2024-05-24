package view.context;

import controller.dto.PlayerTransferObject;
import controller.dto.ServiceResponse;
import controller.dto.WallTransferObject;
import model.cell.CellType;
import model.wall.WallType;
import util.ConsumerFunction;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Provides a context for the match view.
 * <p>
 * This class provides a context for the match view.
 * It contains references to the global context, mouse position, players, cells, walls, and other match-related data.
 * This context is used to provide the necessary dependencies to the components of the match view.
 * It is used to provide a single point of access to the match view's dependencies.
 * This class is used to reduce the number of dependencies that need to be passed to the components of the match view.
 * </p>
 */
public final class MatchContext {

    private final HashMap<MatchEvent, ArrayList<ConsumerFunction<PlayerTransferObject>>> eventListeners;

    private final GlobalContext globalContext;
    private final Point mousePosition;

    private ArrayList<PlayerTransferObject> players;
    private CellType[][] cells;
    private ArrayList<WallTransferObject> walls;

    private WallType selectedWallType;
    private PlayerTransferObject playerInTurn;

    private int boardWidth;
    private int boardHeight;
    private int turnCount;

    private boolean mouseOutOfBounds;
    private boolean mouseOverWall;
    private boolean mouseOverEmptyWall;
    private boolean mouseOverFilledWall;

    /**
     * Initializes a new instance of the MatchContext class.
     *
     * @param globalContext the global context
     */
    public MatchContext(GlobalContext globalContext) {
        this.eventListeners = new HashMap<>();

        this.mousePosition = new Point(0, 0);
        this.players = new ArrayList<>();
        this.walls = new ArrayList<>();
        this.cells = new CellType[0][0];
        this.turnCount = 0;

        this.selectedWallType = WallType.NORMAL;
        this.playerInTurn = null;

        this.globalContext = globalContext;
    }

    /**
     * Gets an {@code ArrayList} of {@code PlayerTransferObject} objects representing the players.
     * <p>
     * This method returns an {@code ArrayList} of {@code PlayerTransferObject} objects representing the players.
     * The players are stored in the context and can be accessed using this method.
     * <p>
     *
     * @return an {@code ArrayList} of {@code PlayerTransferObject} objects representing the players
     */
    public ArrayList<PlayerTransferObject> players() {
        return players;
    }

    /**
     * Gets the player in turn.
     * <p>
     * This method returns the player in turn.
     * The player in turn is stored in the context and can be accessed using this method.
     * <p>
     *
     * @return the player in turn
     */
    public PlayerTransferObject playerInTurn() {
        return playerInTurn;
    }

    /**
     * Gets the cells of the board.
     * <p>
     * This method returns the cells of the board.
     * The cells are stored in the context and can be accessed using this method.
     * </p>
     *
     * @return the cells of the board
     */
    public CellType[][] cells() {
        return cells;
    }

    /**
     * Gets the walls of the board.
     * <p>
     * This method returns the walls of the board.
     * The walls are stored in the context and can be accessed using this method.
     * </p>
     *
     * @return the walls of the board
     */
    public ArrayList<WallTransferObject> walls() {
        return walls;
    }

    /**
     * Gets the selected wall type.
     * <p>
     * This method returns the selected wall type.
     * The selected wall type is stored in the context and can be accessed using this method.
     * </p>
     *
     * @return the selected wall type
     */
    public WallType selectedWallType() {
        return selectedWallType;
    }

    /**
     * Gets the parsed mouse position.
     *
     * <p>
     * This method returns the parsed mouse position.
     * The parsed mouse position is relative to the board.
     * and is in range {@code [0, 2n-1]}
     * where {@code n} is the number of cells in the board.
     * </p>
     *
     * @return the parsed mouse position
     */
    public Point mousePosition() {
        return mousePosition;
    }

    /**
     * Gets the player color given the player and the color variant.
     * <p>
     * This method returns the player color given the player and the color variant.
     * The player color is determined by the player id.
     * The color variant is used to determine the shade of the color.
     * </p>
     *
     * @param player  the player to get the color for
     * @param variant the color variant to use
     * @return the player color
     */
    public ThemeColor getPlayerColor(PlayerTransferObject player, ColorVariant variant) {
        int playerId = player.id();

        return switch (playerId) {
            case 0 -> new ThemeColor(ColorName.RED, variant);
            case 1 -> new ThemeColor(ColorName.PURPLE, variant);
            case 2 -> new ThemeColor(ColorName.BLUE, variant);
            case 3 -> new ThemeColor(ColorName.GREEN, variant);
            default -> throw new IllegalArgumentException("Invalid player id: " + playerId);
        };
    }

    /**
     * Gets the width of the board.
     * <p>
     * This method returns the width of the board.
     * The width of the board is stored in the context and can be accessed using this method.
     * The width is calculated as {@code cells.length * 2 - 1} when the cells are set.
     * </p>
     *
     * @return the width of the board
     */
    public int boardWidth() {
        return boardWidth;
    }

    /**
     * Gets the height of the board.
     * <p>
     * This method returns the height of the board.
     * The height of the board is stored in the context and can be accessed using this method.
     * The height is calculated as {@code cells[0].length * 2 - 1} when the cells are set.
     * </p>
     *
     * @return the height of the board
     */
    public int boardHeight() {
        return boardHeight;
    }

    /**
     * Gets the current turn count.
     * <p>
     * This method returns the current turn count.
     * The turn count is stored in the context and can be accessed using this method.
     * </p>
     *
     * @return the current turn count
     */
    public int turnCount() {
        return turnCount;
    }

    /**
     * Checks if the mouse is out of bounds.
     * <p>
     * This method checks if the mouse is out of bounds.
     * The mouse out of bounds status is stored in the context and can be accessed using this method.
     * </p>
     *
     * @return true if the mouse is out of bounds, false otherwise
     */
    public boolean mouseOutOfBounds() {
        return mouseOutOfBounds;
    }

    /**
     * Checks if the mouse is over a wall.
     * <p>
     * This method checks if the mouse is over a wall.
     * The mouse over wall status is stored in the context and can be accessed using this method.
     * </p>
     *
     * @return true if the mouse is over a wall, false otherwise
     */
    public boolean mouseOverWall() {
        return this.mouseOverWall;
    }

    /**
     * Checks if the mouse is over an empty wall.
     * <p>
     * This method checks if the mouse is over an empty wall.
     * The mouse over empty wall status is stored in the context and can be accessed using this method.
     * </p>
     *
     * @return true if the mouse is over an empty wall, false otherwise
     */
    public boolean mouseOverEmptyWall() {
        return this.mouseOverEmptyWall;
    }

    /**
     * Sets the players for the match.
     * <p>
     * This method sets the players for the match.
     * The players are stored in the context and can be updated using this method.
     * </p>
     *
     * @param players an ArrayList of PlayerTransferObject representing the players
     */
    public void setPlayers(ArrayList<PlayerTransferObject> players) {
        this.players = players;
    }

    /**
     * Sets the cells for the board.
     * <p>
     * This method sets the cells for the board.
     * The cells are stored in the context and can be updated using this method.
     * It also updates the board width and height based on the cells.
     * </p>
     *
     * @param cells a 2D array of CellType representing the cells of the board
     */
    public void setCells(CellType[][] cells) {
        this.cells = cells;

        this.boardWidth = cells.length * 2 - 1;
        this.boardHeight = cells[0].length * 2 - 1;
    }

    /**
     * Sets the walls for the board.
     * <p>
     * This method sets the walls for the board.
     * The walls are stored in the context and can be updated using this method.
     * </p>
     *
     * @param walls a 2D array of WallType representing the walls of the board
     */
    public void setWalls(ArrayList<WallTransferObject> walls) {
        this.walls = walls;
    }

    /**
     * Sets the player in turn.
     * <p>
     * This method sets the player in turn.
     * The player in turn is stored in the context and can be updated using this method.
     * </p>
     *
     * @param playerInTurn a PlayerTransferObject representing the player in turn
     */
    public void setPlayerInTurn(PlayerTransferObject playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    /**
     * Sets the turn count.
     * <p>
     * This method sets the turn count.
     * The turn count is stored in the context and can be updated using this method.
     * </p>
     *
     * @param turnCount an integer representing the turn count
     */
    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    /**
     * Sets the selected wall type.
     * <p>
     * This method sets the selected wall type.
     * The selected wall type is stored in the context and can be updated using this method.
     * </p>
     *
     * @param selectedWallType a WallType representing the selected wall type
     */
    public void setSelectedWallType(WallType selectedWallType) {
        this.selectedWallType = selectedWallType;
        this.dispatchEvent(MatchEvent.WALL_TYPE_CHANGED, this.playerInTurn);
    }

    /**
     * Sets the mouse position.
     * <p>
     * This method sets the mouse position.
     * The mouse position is stored in the context and can be updated using this method.
     * It also updates the mouse out of bounds, over wall, empty wall, and filled wall status based on the mouse position.
     * </p>
     *
     * @param mousePosition a Point representing the mouse position
     */
    public void setMousePosition(Point mousePosition) {
        this.mousePosition.setLocation(mousePosition);

        boolean outOfBoundsX = this.mousePosition.x < 0 || this.mousePosition.x >= this.boardWidth;
        boolean outOfBoundsY = this.mousePosition.y < 0 || this.mousePosition.y >= this.boardHeight;

        this.mouseOutOfBounds = outOfBoundsX || outOfBoundsY;

        if (this.mouseOutOfBounds) {
            this.mouseOverWall = false;
            this.mouseOverEmptyWall = false;
            this.mouseOverFilledWall = false;
            this.mousePosition.setLocation(-1, -1);

            return;
        }

        boolean evenX = this.mousePosition.x % 2 == 0;
        boolean evenY = this.mousePosition.y % 2 == 0;

        this.mouseOverWall = (evenX && !evenY) || (!evenX && evenY);
        this.mouseOverEmptyWall = this.mouseOverWall && !this.isMouseOverWall();
        this.mouseOverFilledWall = this.mouseOverWall && this.isMouseOverWall();
    }

    /**
     * Adds an event listener for the given event type.
     * <p>
     * This method adds an event listener for the given event type.
     * The event listener is stored in the context and can be triggered using the dispatchEvent method.
     * </p>
     *
     * @param eventType the event type to listen for
     * @param handler   the event handler to run when the event is triggered
     */
    public void addEventListener(MatchEvent eventType, ConsumerFunction<PlayerTransferObject> handler) {
        if (!this.eventListeners.containsKey(eventType)) {
            this.eventListeners.put(eventType, new ArrayList<>());
        }

        this.eventListeners.get(eventType).add(handler);
    }

    /**
     * Dispatches an event with the given event type and payload.
     * <p>
     * This method dispatches an event with the given event type and payload.
     * The event is triggered for all event listeners of the given event type.
     * </p>
     *
     * @param eventType the event type to dispatch
     * @param payload   the payload to pass to the event listeners
     */
    public void dispatchEvent(MatchEvent eventType, PlayerTransferObject payload) {
        if (this.eventListeners.containsKey(eventType)) {
            for (ConsumerFunction<PlayerTransferObject> function : this.eventListeners.get(eventType)) {
                function.run(payload);
            }
        }
    }

    /**
     * Checks if the mouse is over a wall.
     * <p>
     * This method checks if the mouse is over a wall.
     * The method checks if the mouse position is over a wall on the board.
     * </p>
     *
     * @return true if the mouse is over a wall, false otherwise
     */
    private boolean isMouseOverWall() {
        int x = this.mousePosition.x;
        int y = this.mousePosition.y;

        ServiceResponse<Boolean> response = this.globalContext.controller().isThereAWall(new Point(x, y));

        if (!response.ok) {
            return false;
        }

        return response.payload;
    }

    /**
     * Enum representing the different types of events that can occur during a match.
     * <p>
     * This enum is used to identify the type of event that has occurred during a match.
     * The event type is used to trigger the appropriate event listeners.
     * </p>
     */
    public enum MatchEvent {
        /**
         * Dispatched when a player has moved.
         */
        PLAYER_MOVED,

        /**
         * Dispatched when a wall has been placed.
         */
        WALL_PLACED,

        /**
         * Dispatched when the selected wall type has changed.
         */
        WALL_TYPE_CHANGED,

        /**
         * Dispatched when a player has won the match.
         */
        PLAYER_WON,

        /**
         * Dispatched when the turn has changed.
         */
        TURN_CHANGED,

        /**
         * Dispatched when the remaining time has changed.
         */
        REMAINING_TIME_CHANGED,
    }
}
