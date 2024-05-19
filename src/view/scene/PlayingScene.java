package view.scene;

import view.components.match.Board;
import view.components.match.ControlPanel;
import view.context.GlobalContext;
import view.context.MatchContext;

/**
 * Represents a scene that is displayed when the game is being played.
 * <p>
 * This class represents a scene that is displayed when the game is being played.
 * It provides a structure for displaying the game board and control panel.
 * The scene is displayed when the game is being played and the player is making moves.
 * </p>
 */
public final class PlayingScene extends Scene {

    private Board board;
    private ControlPanel controlPanel;
    private MatchContext matchContext;

    private int margin;

    /**
     * Creates a new PlayingScene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public PlayingScene(GlobalContext globalContext) {
        super(globalContext);
    }

    /**
     * Adds all components to the scene.
     * <p>
     * This method adds all components to the scene.
     * It adds the control panel and board to the scene.
     * </p>
     */
    @Override
    protected void addAllComponents() {
        this.addComponent(this.controlPanel);
        this.addComponent(this.board);
    }

    /**
     * Sets up the components for the scene.
     * <p>
     * This method sets up the components for the scene.
     * It sets up the board and control panel for the scene.
     * </p>
     */
    @Override
    protected void setupComponents() {
        this.margin = 46;

        int controlPanelWidth = 440;
        int borderRadius = 26;
        int borderWidth = 16;

        this.matchContext = new MatchContext(this.globalContext);

        this.board = new Board(this.globalContext, this.matchContext);
        this.board.getStyle().borderRadius = borderRadius;
        this.board.getStyle().borderWidth = borderWidth;
        this.board.getStyle().x = this.margin;
        this.board.getStyle().y = this.margin;
        this.board.fitSize();

        this.controlPanel = new ControlPanel(this.globalContext, this.matchContext);
        this.controlPanel.getStyle().x = this.board.getStyle().x + this.board.getStyle().width + this.margin;
        this.controlPanel.getStyle().y = this.board.getStyle().y;
        this.controlPanel.getStyle().width = controlPanelWidth;
        this.controlPanel.getStyle().height = this.board.getStyle().height;
        this.controlPanel.getStyle().borderWidth = borderWidth;
        this.controlPanel.getStyle().borderRadius = borderRadius;
        this.controlPanel.fitSize();
    }

    /**
     * Sets up the events for the scene.
     * <p>
     * This method sets up the events for the scene.
     * It sets up the events for the board and control panel.
     * </p>
     */
    @Override
    protected void setupEvents() {

    }

    /**
     * Fixes the canvas size for the scene.
     * </p>
     * This method fixes the canvas size for the scene.
     * It sets the canvas width and height based on the board and control panel sizes.
     * </p>
     */
    @Override
    protected void fixCanvasSize() {
        int expectedWidth = this.controlPanel.getStyle().x + this.controlPanel.getStyle().width + this.margin;
        int expectedHeight = this.board.getStyle().y + this.board.getStyle().height + this.margin;

        this.globalContext.window().setCanvasWidth(expectedWidth);
        this.globalContext.window().setCanvasHeight(expectedHeight);
    }
}
