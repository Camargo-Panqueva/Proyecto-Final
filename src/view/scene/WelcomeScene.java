package view.scene;

import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;

public final class WelcomeScene extends Scene {

    private final Text welcomeTitle;
    private final Text autorsText;
    private final Button startButton;

    public WelcomeScene(ContextProvider contextProvider) {
        super(contextProvider);

        int canvasSize = this.contextProvider.window().getCanvasSize();

        this.welcomeTitle = new Text(0, 0, "Welcome to the game!", this.contextProvider);
        this.welcomeTitle.centerHorizontally();

        this.autorsText = new Text(0, 0, "By Camargo & Panqueva", this.contextProvider);
        this.autorsText.centerHorizontally();

        this.startButton = new Button(0, 200, "Start", this.contextProvider);
        this.startButton.centerHorizontally();

        this.init();
    }

    @Override
    protected void init() {
        this.addComponent(this.welcomeTitle);
        this.addComponent(this.autorsText);
        this.addComponent(this.startButton);
    }
}
