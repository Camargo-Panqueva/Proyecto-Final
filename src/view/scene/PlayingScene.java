package view.scene;

import controller.dto.BoardTransferObject;
import controller.dto.ServiceResponse;
import view.components.match.Board;
import view.context.ContextProvider;

public final class PlayingScene extends Scene {

    private Board board;

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
        this.addComponent(this.board);
    }

    @Override
    protected void setupComponents() {
        ServiceResponse<BoardTransferObject> boardStateResponse = this.contextProvider.controller().getBoardState();

        if (!boardStateResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to get cells from controller: " + boardStateResponse.message);
        }

        this.board = new Board(boardStateResponse.payload, this.contextProvider);
        this.board.fitSize();
        this.board.getStyle().centerHorizontally(this.contextProvider);
        this.board.getStyle().centerVertically(this.contextProvider);
    }

    @Override
    protected void setupEvents() {

    }
}
