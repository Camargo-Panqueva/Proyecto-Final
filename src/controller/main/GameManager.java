package controller.main;

import model.GameModel;
import model.player.Player;

import java.util.ArrayList;

public class GameManager {
    private final GameModel gameModel;
    private final ArrayList<Player> players;
    private Player playerInTurn;

    public GameManager(GameModel gameModel){
        this.gameModel = gameModel;

        this.players = new ArrayList<>(this.gameModel.getPlayers());
        this.playerInTurn = this.players.get(0);
    }
}
