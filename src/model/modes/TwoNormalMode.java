package model.modes;

public final class TwoNormalMode extends GameModeBases {
    public TwoNormalMode() {
        super(9, 9, 2, 10);
    }

    @Override
    public GameModes getGameModeType() {
        return GameModes.NORMAL_TWO_PLAYERS;
    }
}
