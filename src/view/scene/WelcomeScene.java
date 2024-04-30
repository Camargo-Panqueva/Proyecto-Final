package view.scene;

import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;

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
    private Button themeButton;

    /**
     * Creates a new WelcomeScene with the given context provider.
     *
     * @param contextProvider the context provider for the scene.
     */
    public WelcomeScene(ContextProvider contextProvider) {
        super(contextProvider);
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
        this.addComponent(this.welcomeTitle);
        this.addComponent(this.authorsText);
        this.addComponent(this.startButton);
        this.addComponent(this.themeButton);
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
        this.welcomeTitle = new Text("Quoridor!", contextProvider);
        this.welcomeTitle.getStyle().y = 80;
        this.welcomeTitle.getStyle().font = this.contextProvider.window().getCanvas().getFont().deriveFont(100.0f);
        this.welcomeTitle.fitSize();
        this.welcomeTitle.getStyle().centerHorizontally(contextProvider);

        this.authorsText = new Text("Camargo # Panqueva", contextProvider);
        this.authorsText.getStyle().y = 190;
        this.authorsText.getStyle().font = this.contextProvider.window().getCanvas().getFont().deriveFont(21.0f);
        this.authorsText.fitSize();
        this.authorsText.getStyle().centerHorizontally(contextProvider);

        this.startButton = new Button("Start", contextProvider);
        this.startButton.getStyle().y = 400;
        this.startButton.getStyle().centerHorizontally(contextProvider);

        this.themeButton = new Button("Toggle Theme", contextProvider);
        this.themeButton.getStyle().y = 490;
        this.themeButton.getStyle().centerHorizontally(contextProvider);
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
        this.startButton.addEventListener(GameComponent.MouseEventType.CLICK, _ -> System.out.println("Start button clicked"));
        this.themeButton.addEventListener(GameComponent.MouseEventType.CLICK, _ -> this.contextProvider.themeManager().toggleTheme());
    }
}
