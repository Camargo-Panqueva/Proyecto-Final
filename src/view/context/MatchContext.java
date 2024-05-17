package view.context;

import controller.dto.PlayerTransferObject;
import model.cell.CellType;
import model.wall.WallType;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class MatchContext {

    private final GlobalContext globalContext;
    private final Point mousePosition;

    private ArrayList<PlayerTransferObject> players;
    private CellType[][] cells;
    private WallType[][] walls;

    private WallType selectedWallType;
    private PlayerTransferObject playerInTurn;

    private int boardWidth;
    private int boardHeight;

    private boolean mouseOutOfBounds;
    private boolean mouseOverWall;
    private boolean mouseOverEmptyWall;
    private boolean mouseOverFilledWall;

    public MatchContext(GlobalContext globalContext) {

        this.mousePosition = new Point(0, 0);
        this.players = new ArrayList<>();
        this.cells = new CellType[0][0];
        this.walls = new WallType[0][0];

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

    public Color getPlayerColor(PlayerTransferObject player, Theme.ColorVariant variant) {
        int playerId = player.id();
        Theme theme = this.globalContext.currentTheme();

        return switch (playerId) {
            case 0 -> theme.getColor(Theme.ColorName.RED, variant);
            case 1 -> theme.getColor(Theme.ColorName.PURPLE, variant);
            case 2 -> theme.getColor(Theme.ColorName.BLUE, variant);
            case 3 -> theme.getColor(Theme.ColorName.GREEN, variant);
            default -> throw new IllegalArgumentException("Invalid player id: " + playerId);
        };
    }

    public int boardWidth() {
        return boardWidth;
    }

    public int boardHeight() {
        return boardHeight;
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
}
