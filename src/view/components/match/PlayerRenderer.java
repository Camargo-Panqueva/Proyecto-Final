package view.components.match;

import controller.dto.PlayerTransferObject;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

/**
 * Represents a renderer for the players of the board.
 * <p>
 * This class represents a renderer for the players of the board.
 * It provides a structure for rendering the players of the board on the screen.
 * The player renderer is used to render the players of the board on the screen.
 * </p>
 */
public final class PlayerRenderer {

    private static final int PLAYER_PADDING = 3;

    private final Style boardStyle;
    private final GlobalContext globalContext;
    private final MatchContext matchContext;

    /**
     * Creates a new player renderer with the given style, global context, and match context.
     *
     * @param boardStyle    the style of the board.
     * @param globalContext the global context of the game.
     * @param matchContext  the match context of the game.
     */
    public PlayerRenderer(Style boardStyle, GlobalContext globalContext, MatchContext matchContext) {
        this.boardStyle = boardStyle;
        this.globalContext = globalContext;
        this.matchContext = matchContext;
    }

    /**
     * Renders the players of the board on the screen.
     *
     * @param graphics the graphics object to render the players with.
     */
    public void render(Graphics2D graphics) {

        ArrayList<PlayerTransferObject> players = this.matchContext.players();

        for (PlayerTransferObject player : players) {
            this.renderPlayer(graphics, player);
            this.renderAllowedMoves(graphics, player);
        }
    }

    /**
     * Renders the allowed moves of the player on the screen.
     * <p>
     * This method renders the allowed moves of the player on the screen.
     * It draws an oval at each position where the player is allowed to move.
     * The ovals are drawn in the player's color.
     * <p>
     *
     * @param graphics the graphics object to render the allowed moves with.
     * @param player   the player whose allowed moves are to be rendered.
     */
    private void renderAllowedMoves(Graphics2D graphics, PlayerTransferObject player) {
        ColorVariant variant = player.isInTurn() ? ColorVariant.NORMAL : ColorVariant.DIMMED;
        Color color = this.globalContext.currentTheme().getColor(this.matchContext.getPlayerColor(player, variant));

        graphics.setColor(color);
        for (Point move : player.allowedMoves()) {
            int x = move.x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.borderWidth;
            int y = move.y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.borderWidth;

            graphics.drawOval(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    /**
     * Renders the player on the screen.
     * <p>
     * This method renders the player on the screen.
     * It draws a circle at the player's position on the board.
     * The circle is filled with the player's color.
     * The player's name is displayed above the circle.
     * <p>
     *
     * @param graphics the graphics object to render the player with.
     * @param player   the player to render.
     */
    private void renderPlayer(Graphics2D graphics, PlayerTransferObject player) {
        graphics.setFont(this.globalContext.window().getCanvas().getFont().deriveFont(16.0f));

        ColorVariant variant = player.isInTurn() ? ColorVariant.NORMAL : ColorVariant.DIMMED;
        Color color = this.globalContext.currentTheme().getColor(this.matchContext.getPlayerColor(player, variant));

        int x = player.position().x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.borderWidth;
        int y = player.position().y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.borderWidth;

        graphics.setColor(color);
        graphics.fillOval(x, y, CELL_SIZE, CELL_SIZE);

        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
        graphics.fillOval(
                x + PLAYER_PADDING,
                y + PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING
        );

        graphics.setColor(color);
        graphics.fillOval(
                x + 2 * PLAYER_PADDING,
                y + 2 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING
        );

        if (player.name().isEmpty()) {
            return;
        }

        graphics.setColor(color);
        graphics.drawString(player.name(), x + CELL_SIZE, y - 3);

        graphics.setFont(this.globalContext.window().getCanvas().getFont().deriveFont(22.0f));
        FontMetrics metrics = this.globalContext.window().getCanvas().getFontMetrics(graphics.getFont());
        String initial = player.name().substring(0, 1).toUpperCase();

        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
        graphics.drawString(
                initial,
                x + (CELL_SIZE - metrics.stringWidth(initial)) / 2,
                y + CELL_SIZE / 2 + metrics.getHeight() / 4
        );
    }
}
