package view.components.match;

import controller.dto.PlayerTransferObject;
import view.context.ContextProvider;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

import static view.components.match.Board.CELL_SIZE;
import static view.components.match.Board.WALL_SIZE;

public final class PlayerRenderer {

    private static final int PLAYER_PADDING = 3;

    private final Style boardStyle;
    private final ContextProvider contextProvider;

    public PlayerRenderer(Style boardStyle, ContextProvider contextProvider) {
        this.boardStyle = boardStyle;
        this.contextProvider = contextProvider;
    }

    public void render(Graphics2D graphics, ArrayList<PlayerTransferObject> players) {

        for (PlayerTransferObject player : players) {
            this.renderPlayer(graphics, player);
            this.renderAllowedMoves(graphics, player);
        }
    }


    private void renderAllowedMoves(Graphics2D graphics, PlayerTransferObject player) {
        Theme.ColorVariant variant = player.isInTurn() ? Theme.ColorVariant.NORMAL : Theme.ColorVariant.DIMMED;

        graphics.setColor(this.getPlayerColor(player, variant));

        for (Point move : player.allowedMoves()) {
            int x = move.x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.paddingX;
            int y = move.y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.paddingY;

            graphics.drawOval(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    private void renderPlayer(Graphics2D graphics, PlayerTransferObject player) {

        Theme.ColorVariant variant = player.isInTurn() ? Theme.ColorVariant.NORMAL : Theme.ColorVariant.DIMMED;

        int x = player.position().x * (CELL_SIZE + WALL_SIZE) + this.boardStyle.x + this.boardStyle.paddingX;
        int y = player.position().y * (CELL_SIZE + WALL_SIZE) + this.boardStyle.y + this.boardStyle.paddingY;

        graphics.setColor(this.getPlayerColor(player, variant));
        graphics.fillOval(x, y, CELL_SIZE, CELL_SIZE);

        graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.fillOval(
                x + PLAYER_PADDING,
                y + PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING,
                CELL_SIZE - 2 * PLAYER_PADDING
        );

        graphics.setColor(this.getPlayerColor(player, variant));
        graphics.fillOval(
                x + 2 * PLAYER_PADDING,
                y + 2 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING,
                CELL_SIZE - 4 * PLAYER_PADDING
        );

        graphics.setColor(this.getPlayerColor(player, variant));
        graphics.drawString(player.name(), x + CELL_SIZE, y - 3);

        graphics.setColor(this.contextProvider.themeManager().getCurrentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
        graphics.drawString(
                player.name().substring(0, 1),
                x + CELL_SIZE / 2 - 5,
                y + CELL_SIZE / 2 + 5
        );
    }

    private Color getPlayerColor(PlayerTransferObject player, Theme.ColorVariant variant) {
        int playerId = player.id();
        Theme theme = this.contextProvider.themeManager().getCurrentTheme();

        return switch (playerId) {
            case 0 -> theme.getColor(Theme.ColorName.RED, variant);
            case 1 -> theme.getColor(Theme.ColorName.BLUE, variant);
            case 2 -> theme.getColor(Theme.ColorName.GREEN, variant);
            case 3 -> theme.getColor(Theme.ColorName.PURPLE, variant);
            default -> throw new IllegalArgumentException("Invalid player id: " + playerId);
        };
    }
}
