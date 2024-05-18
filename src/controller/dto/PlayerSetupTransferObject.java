package controller.dto;

import model.player.AIProfile;
import model.player.PlayerType;

public record PlayerSetupTransferObject(String name, PlayerType playerType,
                                        AIProfile aiProfile) {//TODO : Add Color for players

}
