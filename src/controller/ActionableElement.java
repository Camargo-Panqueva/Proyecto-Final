package controller;

import controller.logic.MatchManager;

public interface ActionableElement {
    public void action(MatchManager matchManager);
    public void actionAtStartTurn(MatchManager matchManager);
    public void actionAtFinishTurn(MatchManager matchManager);
}
