package view.scene;

import view.components.match.Board;
import view.components.match.ControlPanel;
import view.context.GlobalContext;
import view.context.MatchContext;

public final class PlayingScene extends Scene {

    private Board board;
    private ControlPanel controlPanel;
    private MatchContext matchContext;

    /**
     * Creates a new PlayingScene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public PlayingScene(GlobalContext globalContext) {
        super(globalContext);
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

        this.matchContext = new MatchContext();

        this.board = new Board(this.globalContext, this.matchContext);
        this.board.getStyle().paddingX = padding;
        this.board.getStyle().paddingY = padding;
        this.board.getStyle().borderRadius = 26;
        this.board.fitSize();
        this.board.getStyle().centerHorizontally(this.globalContext);
        this.board.getStyle().centerVertically(this.globalContext);

        this.controlPanel = new ControlPanel(this.globalContext);
        this.controlPanel.getStyle().x = this.board.getStyle().x + this.board.getStyle().width + padding;
        this.controlPanel.getStyle().width = controlPanelWidth;
        this.controlPanel.getStyle().height = this.board.getStyle().height;
        this.controlPanel.getStyle().paddingX = padding;
        this.controlPanel.getStyle().borderWidth = padding;
        this.controlPanel.getStyle().borderRadius = borderRadius;
        this.controlPanel.fitSize();
        this.controlPanel.getStyle().centerVertically(this.globalContext);
    }

    @Override
    protected void setupEvents() {

    }
}
