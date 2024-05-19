import controller.GameController;
import controller.dto.PlayerSetupTransferObject;
import controller.dto.SetupTransferObject;
import model.GameModel;
import model.difficulty.DifficultyType;
import model.player.AIProfile;
import model.player.Player;
import model.player.PlayerType;
import model.wall.WallType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The test class QuoriPoobV1.
 *
 * @author POOB
 * @version 2024-1
 */
public class TestQuoridor {

    final Random random = new Random();
    final HashMap<WallType, Integer> wallsPerPlayer = new HashMap<>();
    final ArrayList<PlayerSetupTransferObject> players = new ArrayList<>();

    final SetupTransferObject setupTransferObject;

    private GameModel gameModel;
    private GameController gameController;

    /**
     * Default constructor for test class
     *
     * <p>
     * Creates a new Quoridor test class with a default setup transfer object.
     * </p>
     */
    public TestQuoridor() {
        wallsPerPlayer.put(WallType.NORMAL, 2);
        wallsPerPlayer.put(WallType.TEMPORAL_WALL, 2);
        wallsPerPlayer.put(WallType.LARGE, 2);
        wallsPerPlayer.put(WallType.ALLY, 2);

        players.add(new PlayerSetupTransferObject("Player1", PlayerType.PLAYER, AIProfile.BEGINNER));
        players.add(new PlayerSetupTransferObject("Player2", PlayerType.PLAYER, AIProfile.BEGINNER));

        this.setupTransferObject = new SetupTransferObject(9, 9, false, DifficultyType.NORMAL, 60, this.wallsPerPlayer, this.players);
    }

    /**
     * Sets up the test fixture.
     * <p>
     * Called before every test case method.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        this.gameModel = new GameModel();
        this.gameController = new GameController(this.gameModel);
    }

    /**
     * Verify that the game can create board {@code N x N} size
     */
    @Test
    public void shouldCreateBoardsOfDifferentSizes() {
        int randomHeight = random.nextInt(16) + 5;
        int randomWidth = random.nextInt(16) + 5;

        SetupTransferObject setupTransferObject = new SetupTransferObject(randomWidth, randomHeight, false, DifficultyType.NORMAL, 60, this.wallsPerPlayer, this.players);

        this.gameController.createMatch(setupTransferObject);

        if (this.gameModel.getBoard().getHeight() != randomHeight || this.gameModel.getBoard().getWidth() != randomWidth) {
            fail();
        }
    }

    /**
     * Verify that the game can create a player an assign them Walls
     */
    @Test
    public void shouldAssignBarriersToPlayers() {
        this.gameController.createMatch(this.setupTransferObject);

        for (Player player : this.gameModel.getPlayers().values()) {
            if (!player.getPlayerWalls().equals(this.wallsPerPlayer)) {
                fail();
            }
        }
    }


    /**
     * Verify that the players can move orthogonally
     */
    @Test
    public void shouldMoveOrthogonallyAPawn() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        int possibleMovesRandomIndex = random.nextInt(possibleMoves.size());

        this.gameController.processPlayerMove(0, possibleMoves.get(possibleMovesRandomIndex));

        if (!this.gameModel.getPlayers().get(0).getPosition().equals(possibleMoves.get(possibleMovesRandomIndex))) {
            fail();
        }
    }


    /**
     * Verify that the players can move diagonally when is necessary
     */
    @Test
    public void shouldMoveDiagonallyAPawn() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameModel.getPlayers().get(1).setPosition(new Point(this.gameModel.getPlayers().get(0).getPosition().x, this.gameModel.getPlayers().get(0).getPosition().y + 1));

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        Point diagonalPoint = new Point(this.gameModel.getPlayers().get(0).getPosition().x + 1, this.gameModel.getPlayers().get(0).getPosition().y);
        Point diagonalPoint1 = new Point(this.gameModel.getPlayers().get(0).getPosition().x - 1, this.gameModel.getPlayers().get(0).getPosition().y);

        if (!possibleMoves.contains(diagonalPoint) || !possibleMoves.contains(diagonalPoint1)) {
            fail();
        }
    }

    /**
     * Verify that the players can place normal barriers
     */
    @Test
    public void shouldPlaceANormalBarrier() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        // Wall just can be placed in position x odd and y even or x even and y odd

        this.gameController.placeWall(0, new Point(0, 1), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(2, 5), WallType.NORMAL);
        this.gameController.placeWall(0, new Point(4, 3), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(6, 7), WallType.NORMAL);

        if (this.gameModel.getWalls().size() != 4) {
            fail();
        }

        assertFalse(this.gameController.placeWall(1, new Point(0, 0), WallType.NORMAL).ok); //is not player 1 turn
        assertFalse(this.gameController.placeWall(0, new Point(-1, -1), WallType.NORMAL).ok); //is not a valid position
        assertFalse(this.gameController.placeWall(0, new Point(22, 22), WallType.NORMAL).ok); //is not a valid position
    }


    /**
     * Verify that the players can jump over a pawn
     */
    @Test
    public void shouldMoveAPawnOverAPawn() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameModel.getPlayers().get(0).setPosition(new Point(4, 4));
        this.gameModel.getPlayers().get(1).setPosition(new Point(4, 5));

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        if (!possibleMoves.contains(new Point(4, 6))) {
            fail();
        }
    }

    /**
     * Verify that the players cannot jump over a wall no allied
     */
    @Test
    public void shouldNotMoveAPawnOverANonAlliedBarrier() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(8, 1), WallType.NORMAL);

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        if (possibleMoves.contains(new Point(4, 1))) {
            fail();
        }
    }

    /**
     * Verify that the players can jump over a wall allied
     */
    @Test
    public void shouldMoveAPawnOverAnAlliedBarrier() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(8, 1), WallType.ALLY);

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        if (!possibleMoves.contains(new Point(4, 1))) {
            fail();
        }
    }

    /**
     * Verify when a player wins the game
     */
    @Test
    public void shouldKnowWhenSomeoneWonTheGame() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.processPlayerMove(0, new Point(4, 1));
        this.gameController.processPlayerMove(1, new Point(4, 7));

        this.gameController.processPlayerMove(0, new Point(4, 2));
        this.gameController.processPlayerMove(1, new Point(4, 6));

        this.gameController.processPlayerMove(0, new Point(4, 3));
        this.gameController.processPlayerMove(1, new Point(4, 5));

        this.gameController.processPlayerMove(0, new Point(4, 4));
        this.gameController.processPlayerMove(1, new Point(4, 3));

        this.gameController.processPlayerMove(0, new Point(4, 5));
        this.gameController.processPlayerMove(1, new Point(4, 2));

        this.gameController.processPlayerMove(0, new Point(4, 6));
        this.gameController.processPlayerMove(1, new Point(4, 1));

        this.gameController.processPlayerMove(0, new Point(4, 7));
        this.gameController.processPlayerMove(1, new Point(4, 0));

        if (this.gameModel.getMatchState() != GameModel.MatchState.WINNER) {
            fail();
        }

    }

    /**
     * Verify the walls placed by the players
     */
    @Test
    public void shouldKnowTheBarriersLeftForEachPlayer() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(8, 1), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(8, 3), WallType.NORMAL);
        this.gameController.placeWall(0, new Point(8, 5), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(8, 7), WallType.NORMAL);
        this.gameController.placeWall(0, new Point(0, 1), WallType.LARGE);
        this.gameController.placeWall(1, new Point(2, 3), WallType.LARGE);
        this.gameController.placeWall(0, new Point(0, 5), WallType.LARGE);
        this.gameController.placeWall(1, new Point(0, 7), WallType.LARGE);

        if (this.gameModel.getPlayers().get(0).getWallsInField() != 4 || this.gameModel.getPlayers().get(1).getWallsInField() != 4) {
            fail();
        }
    }

    /**
     * Verify must not blockers walls
     */
    @Test
    public void shouldNotBlockThePassageOfAPlayer() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(4, 7), WallType.NORMAL);
        this.gameController.placeWall(0, new Point(8, 7), WallType.NORMAL);
        this.gameController.placeWall(1, new Point(12, 7), WallType.NORMAL);
        this.gameController.placeWall(0, new Point(15, 8), WallType.NORMAL);

        assertFalse(this.gameController.placeWall(1, new Point(14, 11), WallType.NORMAL).ok);

    }

    /**
     * Verify Normal Walls Conditions
     */
    @Test
    public void shouldMeetNormalBarrierConditions() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.NORMAL);

        // already are a wall in that position
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.NORMAL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.LARGE).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.TEMPORAL_WALL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.ALLY).ok);

        this.gameController.placeWall(1, new Point(0, 1), WallType.NORMAL);

        this.gameController.placeWall(0, new Point(0, 3), WallType.NORMAL);

        this.gameController.placeWall(1, new Point(0, 5), WallType.NORMAL);

        // player place all walls
        assertFalse(this.gameController.placeWall(0, new Point(0, 9), WallType.NORMAL).ok);
    }

    /**
     * Verify Temporal Walls Conditions
     */
    @Test
    public void shouldMeetTemporalBarrierConditions() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.TEMPORAL_WALL);

        assertEquals(1, this.gameModel.getPlayers().get(0).getWallsInField());

        //temporal walls are deleted after four turns

        this.gameController.processPlayerMove(1, new Point(4, 7));
        this.gameController.processPlayerMove(0, new Point(4, 1));
        this.gameController.processPlayerMove(1, new Point(4, 6));
        this.gameController.processPlayerMove(0, new Point(4, 2));
        this.gameController.processPlayerMove(1, new Point(4, 5));

        assertEquals(0, this.gameModel.getPlayers().get(0).getWallsInField());
    }

    /**
     * Verify Long Walls Conditions
     */
    @Test
    public void shouldMeetLongBarrierConditions() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.LARGE);

        // already are a wall in that position
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.LARGE).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.NORMAL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.TEMPORAL_WALL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.ALLY).ok);

        this.gameController.placeWall(1, new Point(0, 1), WallType.LARGE);

        this.gameController.placeWall(0, new Point(0, 3), WallType.LARGE);

        this.gameController.placeWall(1, new Point(0, 5), WallType.LARGE);

        // player place all walls
        assertFalse(this.gameController.placeWall(0, new Point(0, 9), WallType.LARGE).ok);
    }

    /**
     * Verify Allied Walls Conditions
     */
    @Test
    public void shouldMeetAlliedBarrierConditions() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.ALLY);

        // already are a wall in that position
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.ALLY).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.NORMAL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.TEMPORAL_WALL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.LARGE).ok);

        this.gameController.placeWall(1, new Point(0, 1), WallType.ALLY);

        this.gameController.placeWall(0, new Point(0, 3), WallType.ALLY);

        this.gameController.placeWall(1, new Point(0, 5), WallType.ALLY);

        // player place all walls
        assertFalse(this.gameController.placeWall(0, new Point(0, 9), WallType.ALLY).ok);
    }

    /**
     * Verify when is not possible create a board
     */
    @Test
    public void shouldNotCreateABoardIfItsNotPossible() {
        if (this.gameController.createMatch(new SetupTransferObject(0, 0, false, DifficultyType.NORMAL, 60, this.wallsPerPlayer, this.players)).ok) {
            fail();
        }

        assertFalse(this.gameController.createMatch(new SetupTransferObject(0, 9, false, DifficultyType.NORMAL, 60, this.wallsPerPlayer, this.players)).ok);
    }

    /**
     * Verify when is not possible move orthogonally a player
     */
    @Test
    public void shouldNotMoveOrthogonallyAPawnIfItsNotPossible() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.NORMAL);

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        if (possibleMoves.contains(new Point(0, 6))) {
            fail();
        }
    }

    /**
     * Verify when is not possible move diagonally a player
     */
    @Test
    public void shouldNotMoveDiagonallyAPawnIfItsNotPossible() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameModel.getPlayers().get(0).setPosition(new Point(this.gameModel.getPlayers().get(1).getPosition().x, this.gameModel.getPlayers().get(0).getPosition().y - 1));

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        Point diagonalPoint = new Point(this.gameModel.getPlayers().get(0).getPosition().x + 1, this.gameModel.getPlayers().get(0).getPosition().y);

        if (possibleMoves.contains(diagonalPoint)) {
            fail();
        }
    }

    /**
     * Test that the game does not allow to place a barrier in a position that is not possible
     */
    @Test
    public void shouldNotPlaceANormalBarrierIfItsNotPossible() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameController.placeWall(0, new Point(0, 7), WallType.NORMAL);

        // already are a wall in that position
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.NORMAL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.LARGE).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.TEMPORAL_WALL).ok);
        assertFalse(this.gameController.placeWall(1, new Point(0, 7), WallType.ALLY).ok);
    }

    /**
     * Test that the game does not allow to place a barrier in a position that is not possible
     */
    @Test
    public void shouldNotMoveAPawnOverAPawnIfItsNotPossible() {
        if (!this.gameController.createMatch(this.setupTransferObject).ok) {
            fail();
        }

        this.gameModel.getPlayers().get(1).setPosition(new Point(4, 4));

        final ArrayList<Point> possibleMoves = this.gameController.getBoardState().payload.players().get(0).allowedMoves();

        if (possibleMoves.contains(new Point(4, 5))) {
            fail();
        }
    }
}
