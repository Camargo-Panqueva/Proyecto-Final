package view.scene;

import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;

public final class WelcomeScene extends Scene {

    private Text welcomeTitle;
    private Text authorsText;
    private Button startButton;
    private Button themeButton;

    public WelcomeScene(ContextProvider contextProvider) {
        super(contextProvider);
    }

    @Override
    protected void addAllComponents() {
        this.addComponent(this.welcomeTitle);
        this.addComponent(this.authorsText);
        this.addComponent(this.startButton);
        this.addComponent(this.themeButton);
    }

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

    @Override
    protected void setupEvents() {
        this.startButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> System.out.println("Start button clicked"));
        this.themeButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> this.contextProvider.themeManager().toggleTheme());
    }
}
