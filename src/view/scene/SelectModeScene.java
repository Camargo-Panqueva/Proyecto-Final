package view.scene;

import controller.dto.ServiceResponse;
import view.components.ui.BackgroundSeparator;
import view.components.ui.Button;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.input.MouseEvent;

import java.util.ArrayList;

//TODO: Update docs when different modes are implemented

/**
 * Represents the scene for selecting the game mode.
 * <p>
 * This class represents the scene for selecting the game mode.
 * It provides a basic structure for rendering the game mode selection screen.
 * The scene contains buttons for selecting the single player and multiplayer modes.
 * </p>
 */
public final class SelectModeScene extends Scene {

    private BackgroundSeparator separator;
    private Text selectModeText;
    private Selector gameModeSelect;
    private Button startButton;

    /**
     * Creates a new SelectModeScene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public SelectModeScene(GlobalContext globalContext) {
        super(globalContext);
    }

    /**
     * Adds all components to the scene.
     * <p>
     * This method adds all components to the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void addAllComponents() {
        this.addComponent(this.separator);
        this.addComponent(this.selectModeText);
        this.addComponent(this.gameModeSelect);
        this.addComponent(this.startButton);
    }

    /**
     * Sets up the components for the scene.
     * <p>
     * This method sets up the components for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void setupComponents() {
        int canvasWidth = this.globalContext.window().getCanvasSize();
        int separatorMargin = 40;
        int separatorHeight = 200;

        this.separator = new BackgroundSeparator(globalContext);
        this.separator.getStyle().y = separatorMargin;
        this.separator.getStyle().width = canvasWidth - 2 * separatorMargin;
        this.separator.getStyle().height = separatorHeight;
        this.separator.getStyle().borderRadius = 20;
        this.separator.getStyle().centerHorizontally(globalContext);

        this.selectModeText = new Text("Please select the mode", globalContext);
        this.selectModeText.getStyle().y = separatorHeight + separatorMargin + 50;
        this.selectModeText.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(40.0f);
        this.selectModeText.fitSize();
        this.selectModeText.getStyle().centerHorizontally(globalContext);

        ServiceResponse<ArrayList<String>> gameModesResponse = this.globalContext.controller().getGameModes();

        if (!gameModesResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Error getting game modes " + gameModesResponse.message);
        }

        ArrayList<String> options = gameModesResponse.payload;

        this.gameModeSelect = new Selector(options, globalContext);
        this.gameModeSelect.getStyle().y = 400;
        this.gameModeSelect.getStyle().centerHorizontally(globalContext);

        this.startButton = new Button("Start", globalContext);
        this.startButton.getStyle().y = this.gameModeSelect.getStyle().y + 90;
        this.startButton.getStyle().centerHorizontally(globalContext);
    }

    /**
     * Sets up the events for the scene.
     * <p>
     * This method sets up the events for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void setupEvents() {
        //TODO: Implement event handling for the scene
        this.startButton.addMouseListener(MouseEvent.EventType.RELEASED, _event -> {
            ServiceResponse<Void> gameModeResponse = this.globalContext.controller().setGameMode(this.gameModeSelect.getSelectedOption());
            ServiceResponse<Void> startGameResponse = this.globalContext.controller().startGame();

            if (!gameModeResponse.ok) {
                //TODO: Handle error
                throw new RuntimeException("Error setting game mode " + gameModeResponse.message);
            }

            if (!startGameResponse.ok) {
                //TODO: Handle error
                throw new RuntimeException("Error starting game " + startGameResponse.message);
            }
        });
    }
}
