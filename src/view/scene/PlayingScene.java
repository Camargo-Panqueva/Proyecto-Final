package view.scene;

import view.components.match.Board;
import view.components.match.ControlPanel;
import view.context.ContextProvider;

public final class PlayingScene extends Scene {

    private Board board;
    private ControlPanel controlPanel;

    /**
     * Creates a new PlayingScene with the given context provider.
     *
     * @param contextProvider the context provider for the scene.
     */
    public PlayingScene(ContextProvider contextProvider) {
        super(contextProvider);
    }

    @Override
    protected void addAllComponents() {
        this.addComponent(this.controlPanel);
        this.addComponent(this.board);
    }

    @Override
    protected void setupComponents() {
        int padding = 16;
        int controlPanelWidth = 440;
        int borderRadius = 16;

        this.board = new Board(this.contextProvider);
        this.board.getStyle().paddingX = padding;
        this.board.getStyle().paddingY = padding;
        this.board.getStyle().borderRadius = 26;
        this.board.fitSize();
        this.board.getStyle().centerHorizontally(this.contextProvider);
        this.board.getStyle().centerVertically(this.contextProvider);

        this.controlPanel = new ControlPanel(this.contextProvider);
        this.controlPanel.getStyle().x = this.board.getStyle().x + this.board.getStyle().width + padding;
        this.controlPanel.getStyle().width = controlPanelWidth;
        this.controlPanel.getStyle().height = this.contextProvider.window().getCanvasSize() - padding * 2;
        this.controlPanel.getStyle().paddingX = padding;
        this.controlPanel.getStyle().borderWidth = padding;
        this.controlPanel.getStyle().borderRadius = borderRadius;
        this.controlPanel.fitSize();
        this.controlPanel.getStyle().centerVertically(this.contextProvider);
    }

    @Override
    protected void setupEvents() {

    }
}
