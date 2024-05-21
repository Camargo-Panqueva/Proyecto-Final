package controller.cells;

import controller.logic.MatchManager;

public class DobleTurnCell extends Cell {
    @Override
    public void action(MatchManager matchManager) {
        matchManager.addATurn(matchManager.getPlayerInTurn());
    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {

    }
}
