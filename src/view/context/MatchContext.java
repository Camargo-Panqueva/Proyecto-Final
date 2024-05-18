package view.context;

import controller.dto.PlayerTransferObject;
import model.cell.CellType;
import model.wall.WallType;
import util.ConsumerFunction;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public final class MatchContext {

    private final HashMap<MatchEvent, ArrayList<ConsumerFunction<PlayerTransferObject>>> eventListeners;

    private final GlobalContext globalContext;
    private final Point mousePosition;

    private ArrayList<PlayerTransferObject> players;
    private CellType[][] cells;
    private WallType[][] walls;

    private WallType selectedWallType;
    private PlayerTransferObject playerInTurn;

    private int boardWidth;
    private int boardHeight;
    private int turnCount;

    private boolean mouseOutOfBounds;
    private boolean mouseOverWall;
    private boolean mouseOverEmptyWall;
    private boolean mouseOverFilledWall;

    public MatchContext(GlobalContext globalContext) {
        this.eventListeners = new HashMap<>();

        this.mousePosition = new Point(0, 0);
        this.players = new ArrayList<>();
        this.cells = new CellType[0][0];
        this.walls = new WallType[0][0];
        this.turnCount = 0;

        this.selectedWallType = WallType.NORMAL;
        this.playerInTurn = null;

        this.globalContext = globalContext;
    }

    public ArrayList<PlayerTransferObject> players() {
        return players;
    }

    public PlayerTransferObject playerInTurn() {
        return playerInTurn;
    }

    public CellType[][] cells() {
        return cells;
    }

    public WallType[][] walls() {
        return walls;
    }

    public WallType selectedWallType() {
        return selectedWallType;
    }

    public Point mousePosition() {
        return mousePosition;
    }

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

    public int boardWidth() {
        return boardWidth;
    }

    public int boardHeight() {
        return boardHeight;
    }

    public int turnCount() {
        return turnCount;
    }

    public boolean mouseOutOfBounds() {
        return mouseOutOfBounds;
    }

    public boolean mouseOverWall() {
        return this.mouseOverWall;
    }

    public boolean mouseOverEmptyWall() {
        return this.mouseOverEmptyWall;
    }

    public boolean mouseOverFilledWall() {
        return this.mouseOverFilledWall;
    }

    public void setPlayers(ArrayList<PlayerTransferObject> players) {
        this.players = players;
    }

    public void setCells(CellType[][] cells) {
        this.cells = cells;

        this.boardWidth = cells.length * 2 - 1;
        this.boardHeight = cells[0].length * 2 - 1;
    }

    public void setWalls(WallType[][] walls) {
        this.walls = walls;
    }

    public void setPlayerInTurn(PlayerTransferObject playerInTurn) {
        this.playerInTurn = playerInTurn;
    }

    public void setTurnCount(int turnCount) {
        this.turnCount = turnCount;
    }

    public void toggleWallType() {
        if (this.selectedWallType == WallType.NORMAL) {
            this.selectedWallType = WallType.LARGE;
        } else {
            this.selectedWallType = WallType.NORMAL;
        }
    }

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
        this.mouseOverEmptyWall = this.mouseOverWall && this.walls[this.mousePosition.x][this.mousePosition.y] == null;
        this.mouseOverFilledWall = this.mouseOverWall && this.walls[this.mousePosition.x][this.mousePosition.y] != null;
    }

    public void addEventListener(MatchEvent eventType, ConsumerFunction<PlayerTransferObject> handler) {
        if (!this.eventListeners.containsKey(eventType)) {
            this.eventListeners.put(eventType, new ArrayList<>());
        }

        this.eventListeners.get(eventType).add(handler);
    }

    public void dispatchEvent(MatchEvent eventType, PlayerTransferObject payload) {
        if (this.eventListeners.containsKey(eventType)) {
            for (ConsumerFunction<PlayerTransferObject> function : this.eventListeners.get(eventType)) {
                function.run(payload);
            }
        }
    }

    public enum MatchEvent {
        PLAYER_MOVED,
        WALL_PLACED,
        PLAYER_WON,
        TURN_CHANGED,
        REMAINING_TIME_CHANGED,
    }
}
