package controller.logic;

import model.difficulty.Difficulty;
import model.player.Player;
import util.ConcurrentLoop;
import util.VoidFunction;

import java.time.Duration;
import java.time.Instant;

public class Chronometer {
    final int time;
    Instant secondCount;
    MatchManager matchManager;
    Difficulty difficulty;
    VoidFunction functionToExecute;

    public Chronometer(MatchManager matchManager, Difficulty difficulty) {
        this.matchManager = matchManager;
        this.secondCount = Instant.now();
        this.difficulty = difficulty;
        switch (difficulty.getDifficultyType()) {
            case AGAINST_THE_CLOCK -> this.time = difficulty.getTimePerTurn();
            case TIMED -> this.time = difficulty.getTimeTotal();
            default -> this.time = 60;
        }
    }


    public void startTimer() {
        ConcurrentLoop clockCurrentTurn = new ConcurrentLoop(this::clockPerTurn, 10, "Time Limit per Turn");
        this.secondCount = Instant.now();
        clockCurrentTurn.start();
    }

    /**
     * Starts a new turn.
     */
    public void resetTime() {
        this.secondCount = Instant.now();
    }

    private void clockPerTurn() {
        final Player playerInTurn = matchManager.getPlayerInTurn();
        playerInTurn.setTimePlayed((int) Duration.between(this.secondCount, Instant.now()).getSeconds());

        if (playerInTurn.getTimePlayed() >= this.time) {
            if (functionToExecute != null) {
                functionToExecute.run();
                this.resetTime();
            }
        }
    }

    public void setFunctionToExecute(VoidFunction functionToExecute) {
        this.functionToExecute = functionToExecute;
    }
}
