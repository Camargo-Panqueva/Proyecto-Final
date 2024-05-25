package view.scene;

import controller.dto.PlayerTransferObject;
import controller.dto.ServiceResponse;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.themes.ThemeColor;

public final class GameFinishedScene extends Scene {


    private Text congratulationsText;
    private Text playerText;
    private Text messageText;

    /**
     * Creates a new Game Finished Scene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public GameFinishedScene(GlobalContext globalContext) {
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
            throw new RuntimeException("Failed to get winner: " + response.message);
        }

        return response.payload;
    }
}
