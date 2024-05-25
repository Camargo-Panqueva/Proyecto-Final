package controller.dto;

import model.player.AIProfile;
import model.player.PlayerType;

/**
 * Represents a transfer object for setting up player configurations.
 * This record encapsulates the necessary information to configure a player, including their name, player type, and AI profile.
 * <p>
 * TODO: Add Color for players
 */
public record PlayerSetupTransferObject(String name, PlayerType playerType,
                                        AIProfile aiProfile) {
}
