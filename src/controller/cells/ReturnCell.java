package controller.cells;

import controller.logic.MatchManager;

public class ReturnCell extends Cell{
    @Override
    public void action(MatchManager matchManager) {
        matchManager.returnCell(matchManager.getPlayerInTurn());
    }

    @Override
    public void actionAtStartTurn(MatchManager matchManager) {

    }

    @Override
    public void actionAtFinishTurn(MatchManager matchManager) {

    }
}
