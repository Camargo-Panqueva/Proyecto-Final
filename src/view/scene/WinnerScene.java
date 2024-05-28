package view.scene;

import controller.dto.PlayerTransferObject;
import controller.dto.ServiceResponse;
import util.Logger;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.themes.ThemeColor;

/**
 * Represents a scene that is displayed when the game is finished.
 * <p>
 * This class represents a scene that is displayed when the game is finished.
 * It provides a structure for displaying the game finished message.
 * The scene is displayed when the game is finished and a player has won.
 * </p>
 */
public final class WinnerScene extends Scene {


    private Text congratulationsText;
    private Text playerText;
    private Text messageText;

    /**
     * Creates a new Game Finished Scene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public WinnerScene(GlobalContext globalContext) {
        super(globalContext);
    }

    @Override
    protected void addAllComponents() {
        this.addComponent(this.congratulationsText);
        this.addComponent(this.playerText);
    }

    @Override
    protected void setupComponents() {
        this.congratulationsText = new Text("# Congratulations! #", this.globalContext);
        this.congratulationsText.getStyle().font = this.globalContext.gameFont().deriveFont(68f);
        this.congratulationsText.getStyle().x = 50;
        this.congratulationsText.getStyle().y = 100;
        this.congratulationsText.getStyle().foregroundColor = new ThemeColor(ThemeColor.ColorName.YELLOW, ThemeColor.ColorVariant.NORMAL);
        this.congratulationsText.fitSize();

        this.playerText = new Text(String.format("# %s wins! #", this.getWinner().name()), this.globalContext);
        this.playerText.getStyle().font = this.globalContext.gameFont().deriveFont(44f);
        this.playerText.getStyle().x = 50;
        this.playerText.getStyle().y = 250;
    }

    @Override
    protected void setupEvents() {

    }

    @Override
    protected void fixCanvasSize() {
        int expectedHeight = 700;
        int expectedWidth = 900;

        this.globalContext.window().setCanvasHeight(expectedHeight);
        this.globalContext.window().setCanvasWidth(expectedWidth);

        this.congratulationsText.getStyle().centerHorizontally(globalContext);
        this.playerText.getStyle().centerHorizontally(globalContext);
    }

    private PlayerTransferObject getWinner() {
        ServiceResponse<PlayerTransferObject> response = this.globalContext.controller().getWinner();

        if (!response.ok) {
            //TODO: Handle error
            Logger.error("Failed to get winner: " + response.message);
            throw new RuntimeException("Failed to get winner: " + response.message);
        }

        return response.payload;
    }
}
