package model.modes;

public final class FourNormalMode extends GameModeBases {
    public FourNormalMode() {
        super(11, 11, 4, 10, 60);
    }

    @Override
    public GameModes getGameModeType() {
        return GameModes.NORMAL_FOUR_PLAYERS;
    }
}
