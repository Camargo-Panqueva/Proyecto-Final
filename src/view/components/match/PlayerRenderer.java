package view.components.match;

import controller.dto.PlayerTransferObject;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

public final class PlayerRenderer {

    private static final int PLAYER_PADDING = 3;

    private final Style boardStyle;
    private final GlobalContext globalContext;
    private final MatchContext matchContext;

    public PlayerRenderer(Style boardStyle, GlobalContext globalContext, MatchContext matchContext){
        this.boardStyle = boardStyle;
        this.globalContext = globalContext;
        this.matchContext = matchContext;
    }

    public void render(Graphics2D graphics) {

        ArrayList<PlayerTransferObject> players = this.matchContext.players();

        for (PlayerTransferObject player : players) {
            this.renderPlayer(graphics, player);
            this.renderAllowedMoves(graphics, player);
        }
    }


    private void renderAllowedMoves(Graphics2D graphics, PlayerTransferObject player) {
        Theme.ColorVariant variant = player.isInTurn() ? Theme.ColorVariant.NORMAL : Theme.ColorVariant.DIMMED;

        graphics.setColor(this.matchContext.getPlayerColor(player, variant));

        for (Point move : player.allowedMoves()) {
            int x = move.x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.paddingX;
            int y = move.y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.paddingY;

            graphics.drawOval(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private void renderPlayer(Graphics2D graphics, PlayerTransferObject player) {
        graphics.setFont(this.globalContext.window().getCanvas().getFont().deriveFont(16.0f));

        Theme.ColorVariant variant = player.isInTurn() ? Theme.ColorVariant.NORMAL : Theme.ColorVariant.DIMMED;

        int x = player.position().x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.paddingX;
        int y = player.position().y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.paddingY;

        graphics.setColor(this.matchContext.getPlayerColor(player, variant));
        graphics.fillOval(x, y, CELL_SIZE, CELL_SIZE);

        graphics.setColor(this.globalContext.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.fillOval(
                x + PLAYER_PADDING,
                y + PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING
        );

        graphics.setColor(this.matchContext.getPlayerColor(player, variant));
        graphics.fillOval(
                x + 2 * PLAYER_PADDING,
                y + 2 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING
        );

        graphics.setColor(this.matchContext.getPlayerColor(player, variant));
        graphics.drawString(player.name(), x + CELL_SIZE, y - 3);

        graphics.setColor(this.globalContext.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.drawString(
                player.name().substring(0, 1),
                x + CELL_SIZE / 2 - 5,
                y + CELL_SIZE / 2 + 5
        );
    }
}
