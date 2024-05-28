package controller;

import controller.dto.ServiceResponse;
import controller.states.GlobalState;
import model.GameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class LoadMatchTest {

    private GameController gameController;
    private GameModel model;

    @BeforeEach
    public void setUp() {
        model = new GameModel();
        gameController = new GameController(model);
    }

    @Test
    public void testLoadMatchSuccess() throws IOException, ClassNotFoundException {
        GameModel mockModel = new GameModel();

        String path = "testMatch.ser";
        FileOutputStream fileOut = new FileOutputStream(path);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(mockModel);
        out.close();
        fileOut.close();

        ServiceResponse<Void> response = gameController.loadMatch(path);

        assertTrue(response.ok);
        assertEquals("Match loaded", response.message);
        assertEquals(GlobalState.PLAYING, gameController.getGlobalCurrentState().payload);

        new File(path).delete();
    }

    @Test
    public void testLoadMatchFileNotFound() {
        // Attempt to load a match from a non-existent file
        String path = "nonExistentFile.ser";
        ServiceResponse<Void> response = gameController.loadMatch(path);

        // Check that the response indicates an error
        assertFalse(response.ok);
        assertTrue(response.message.contains("Error loading the match:"));
    }
}