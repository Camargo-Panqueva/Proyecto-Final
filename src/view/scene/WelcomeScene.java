package view.scene;

import controller.states.GlobalState;
import view.components.GameComponent;
import view.components.ui.Button;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.input.MouseEvent;
import view.themes.Theme;

import java.util.ArrayList;

//TODO: Update docs when different modes are implemented

/**
 * Represents the scene for welcoming the player to the game.
 * <p>
 * This class represents the scene for welcoming the player to the game.
 * It provides a basic structure for rendering the welcome screen.
 * The scene contains a welcome title, authors text, start button, and theme button.
 * </p>
 */
public final class WelcomeScene extends Scene {

    private Text welcomeTitle;
    private Text authorsText;
    private Button startButton;
    private Selector<Theme> themeSelector;

    /**
     * Creates a new WelcomeScene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public WelcomeScene(GlobalContext globalContext) {
        super(globalContext);
    }

    /**
     * Adds all components to the scene.
     * <p>
     * This method adds all components to the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     *
     * @see Scene#addAllComponents()
     */
    @Override
    protected void addAllComponents() {
        this.addComponent(this.welcomeTitle);
        this.addComponent(this.authorsText);
        this.addComponent(this.startButton);
        this.addComponent(this.themeSelector);
    }

    /**
     * Sets up the components for the scene.
     * <p>
     * This method sets up the components for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     *
     * @see Scene#setupComponents()
     */
    @Override
    protected void setupComponents() {
        this.welcomeTitle = new Text("Quoridor!", globalContext);
        this.welcomeTitle.getStyle().y = 80;
        this.welcomeTitle.getStyle().font = this.globalContext.gameFont().deriveFont(100.0f);
        this.welcomeTitle.fitSize();
        this.welcomeTitle.getStyle().centerHorizontally(globalContext);

        this.authorsText = new Text("Camargo # Panqueva", globalContext);
        this.authorsText.getStyle().y = 200;
        this.authorsText.getStyle().font = this.globalContext.gameFont().deriveFont(21.0f);
        this.authorsText.fitSize();
        this.authorsText.getStyle().centerHorizontally(globalContext);

        this.startButton = new Button("Start", globalContext);
        this.startButton.getStyle().y = 400;
        this.startButton.getStyle().width = 370;
        this.startButton.getStyle().centerHorizontally(globalContext);

        ArrayList<Theme> themes = this.globalContext.themeManager().getThemes();
        this.themeSelector = new Selector<>(themes, globalContext);
        this.themeSelector.getStyle().y = this.startButton.getStyle().y + 90;
        this.themeSelector.getStyle().width = this.startButton.getStyle().width;
        this.themeSelector.getStyle().centerHorizontally(globalContext);
    }

    /**
     * Sets up the events for the scene.
     * <p>
     * This method sets up the events for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     *
     * @see Scene#setupEvents()
     */
    @Override
    protected void setupEvents() {
        this.startButton.addMouseListener(
                MouseEvent.EventType.RELEASED, _event -> this.globalContext.controller().setGlobalState(GlobalState.SETUP_MATCH_SETTINGS)
        );

        this.themeSelector.addComponentListener(
                GameComponent.ComponentEvent.VALUE_CHANGED,
                (_old, _new) -> this.globalContext.themeManager().setTheme(this.themeSelector.getSelectedOption())
        );
    }

    /**
     * Fixes the canvas size for the scene.
     * <p>
     * This method fixes the canvas size for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     *
     * @see Scene#fixCanvasSize()
     */
    @Override
    public void fixCanvasSize() {
        this.globalContext.window().setCanvasSize(600);
    }
}
