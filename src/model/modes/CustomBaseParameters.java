package model.modes;

public class CustomBaseParameters extends GameModeBases{

    public CustomBaseParameters(final int boardWidth,final int boardHeight,final int numOfPlayers,final int numOfWalls) {
        super(boardWidth, boardHeight, numOfPlayers, numOfWalls);
    }

    public CustomBaseParameters(){
        super(9,9,2,10);

    }

    @Override
    public GameModes getGameModeType() {
        return GameModes.CUSTOM;
    }
}
