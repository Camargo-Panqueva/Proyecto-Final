package view.scene;

import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;
import view.context.Style;
import view.themes.Theme;

import java.awt.*;

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

        Style titleStyle = new Style();
        titleStyle.y = 80;
        titleStyle.font = new Font("Times New Roman", Font.BOLD, 84);

        this.welcomeTitle = new Text("Quoridor!", titleStyle, contextProvider);
        this.welcomeTitle.fitSize();
        this.welcomeTitle.getStyle().centerHorizontally(contextProvider);

        Style authorsStyle = new Style();
        authorsStyle.y = 180;
        authorsStyle.font = new Font("Times New Roman", Font.BOLD, 18);

        this.authorsText = new Text("By Camargo & Panqueva", authorsStyle, contextProvider);
        this.authorsText.fitSize();
        this.authorsText.getStyle().centerHorizontally(contextProvider);

        Style startButtonStyle = new Style();
        startButtonStyle.y = 400;
        startButtonStyle.font = new Font("Arial", Font.BOLD, 24);
        startButtonStyle.height = 60;
        startButtonStyle.width = 300;
        startButtonStyle.borderRadius = 16;

        this.startButton = new Button("Start", startButtonStyle, contextProvider);
        this.startButton.getStyle().centerHorizontally(contextProvider);

        Style themeButtonStyle = new Style();
        themeButtonStyle.y = 490;
        themeButtonStyle.font = new Font("Arial", Font.BOLD, 24);
        themeButtonStyle.height = 60;
        themeButtonStyle.width = 300;
        themeButtonStyle.borderRadius = 16;

        this.themeButton = new Button("Toggle Theme", themeButtonStyle, contextProvider);
        this.themeButton.getStyle().centerHorizontally(contextProvider);
    }

    @Override
    protected void setupEvents() {
        this.startButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> System.out.println("Start button clicked"));
        this.themeButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> this.contextProvider.themeManager().toggleTheme());
    }
}
