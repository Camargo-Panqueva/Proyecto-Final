package controller;

import controller.dto.PlayerSetupTransferObject;
import controller.dto.ServiceResponse;
import controller.dto.SetupTransferObject;
import model.GameModel;
import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.player.PlayerType;
import model.wall.WallType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CreateMatchTest {

    private GameController gameController;
    private GameModel model;

    @BeforeEach
    public void setUp() {
        model = new GameModel();
        gameController = new GameController(model);
    }

    @Test
    public void testCreateMatchSuccess() {
        HashMap<WallType, Integer> wallTypeCount = new HashMap<>();
        wallTypeCount.put(WallType.NORMAL, 10);

        HashMap<CellType, Integer> cellTypeCount = new HashMap<>();
        cellTypeCount.put(CellType.NORMAL, 5);

        ArrayList<PlayerSetupTransferObject> players = new ArrayList<>();
        players.add(new PlayerSetupTransferObject("Player1", PlayerType.PLAYER, null));
        players.add(new PlayerSetupTransferObject("Player2", PlayerType.PLAYER, null));

        SetupTransferObject setupSettings = new SetupTransferObject(10, 10, false, DifficultyType.NORMAL, 300, cellTypeCount, wallTypeCount, players);

        ServiceResponse<Void> response = gameController.createMatch(setupSettings);

        assertTrue(response.ok);
        assertEquals("Game Started", response.message);
    }

    @Test
    public void testCreateMatchNoPlayers() {
        HashMap<WallType, Integer> wallTypeCount = new HashMap<>();
        wallTypeCount.put(WallType.NORMAL, 10);

        HashMap<CellType, Integer> cellTypeCount = new HashMap<>();
        cellTypeCount.put(CellType.DOUBLE_TURN, 5);

        ArrayList<PlayerSetupTransferObject> players = new ArrayList<>();

        SetupTransferObject setupSettings = new SetupTransferObject(10, 10, false, DifficultyType.NORMAL, 300, cellTypeCount, wallTypeCount, players);

        ServiceResponse<Void> response = gameController.createMatch(setupSettings);

        assertFalse(response.ok);
        assertEquals("Our Quoridor accept just 2, 3 and 4 players", response.message);
    }

    @Test
    public void testCreateMatchInvalidPlayerCount() {
        HashMap<WallType, Integer> wallTypeCount = new HashMap<>();
        wallTypeCount.put(WallType.NORMAL, 10);

        HashMap<CellType, Integer> cellTypeCount = new HashMap<>();
        cellTypeCount.put(CellType.TELEPORT, 5);

        ArrayList<PlayerSetupTransferObject> players = new ArrayList<>();
        players.add(new PlayerSetupTransferObject("Player1", PlayerType.PLAYER, null)); // Only one player

        SetupTransferObject setupSettings = new SetupTransferObject(10, 10, false, DifficultyType.NORMAL, 300, cellTypeCount, wallTypeCount, players);

        ServiceResponse<Void> response = gameController.createMatch(setupSettings);

        assertFalse(response.ok);
        assertEquals("Our Quoridor accept just 2, 3 and 4 players", response.message);
    }
}
