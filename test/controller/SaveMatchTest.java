package controller;

import controller.dto.ServiceResponse;
import model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;

public class SaveMatchTest {

    private GameController gameController;
    private GameModel gameModel;

    @BeforeEach
    public void setUp() {
        gameModel = new GameModel();
        gameController = new GameController(gameModel);
    }

    @Test
    public void testSaveMatch_MatchNotStarted() {
        gameModel.setMatchState(GameModel.MatchState.INITIALIZED);
        ServiceResponse<Void> response = gameController.saveMatch("testMatch.qpg");
        assertFalse(response.ok);
        assertEquals("The match has not started yet", response.message);
    }

    @Test
    public void testSaveMatch_MatchAlreadyEnded() {
        gameModel.setMatchState(GameModel.MatchState.WINNER);
        ServiceResponse<Void> response = gameController.saveMatch("testMatch.qpg");
        assertFalse(response.ok);
        assertEquals("The match has already ended", response.message);
    }

    @Test
    public void testSaveMatch_Success() throws Exception {
        gameModel.setMatchState(GameModel.MatchState.PLAYING);
        ServiceResponse<Void> response = gameController.saveMatch("testMatch.qpg");
        assertTrue(response.ok);
        assertEquals("Match saved in testMatch.qpg", response.message);

        // Verify the file was created and contains the correct data
        File file = new File("testMatch.qpg");
        assertTrue(file.exists());

        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        GameModel savedModel = (GameModel) in.readObject();
        in.close();
        fileIn.close();
        file.delete();
    }

    @Test
    public void testSaveMatch_IOException() {
        gameModel.setMatchState(GameModel.MatchState.PLAYING);
        ServiceResponse<Void> response = gameController.saveMatch("/invalid/path/testMatch.qpg");
        assertFalse(response.ok);
        assertTrue(response.message.startsWith("Error saving the match:"));
    }
}
