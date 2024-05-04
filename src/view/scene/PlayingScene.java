package view.scene;

import controller.serviceResponse.ServiceResponse;
import model.cell.CellType;
import view.components.match.Board;
import view.context.ContextProvider;

import java.util.ArrayList;

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
        ServiceResponse<CellType[][]> cellsResponse = this.contextProvider.controller().getBoardCells();
        CellType[][] cells = cellsResponse.payload;

        if (!cellsResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to get cells from controller: " + cellsResponse.message);
        }

        this.board = new Board(cells, this.contextProvider);
        this.board.fitSize();
        this.board.getStyle().centerHorizontally(this.contextProvider);
        this.board.getStyle().centerVertically(this.contextProvider);
    }

    @Override
    protected void setupEvents() {

    }
}
