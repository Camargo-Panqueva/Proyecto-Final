package view.scene;

import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;
import view.context.Style;

import java.awt.*;

public final class WelcomeScene extends Scene {

    private Text welcomeTitle;
    private Text authorsText;
    private Button startButton;

    public WelcomeScene(ContextProvider contextProvider) {
        super(contextProvider);

        this.setupComponents();
        this.addAllComponents();
    }

    @Override
    protected void addAllComponents() {
        this.addComponent(this.welcomeTitle);
        this.addComponent(this.authorsText);
        this.addComponent(this.startButton);
    }

    @Override
    protected void setupComponents() {
        Style titleStyle = new Style();
        titleStyle.y = 80;
        titleStyle.font = new Font("Times New Roman", Font.BOLD, 84);
        titleStyle.foregroundColor = new Color(0x161616);

        this.welcomeTitle = new Text("Quoridor!", titleStyle, contextProvider);
        this.welcomeTitle.fitSize();
        this.welcomeTitle.getStyle().centerHorizontally(contextProvider);

        Style authorsStyle = new Style();
        authorsStyle.y = 180;
        authorsStyle.font = new Font("Times New Roman", Font.BOLD, 24);
        authorsStyle.foregroundColor = new Color(0x161616);

        this.authorsText = new Text("By Camargo & Panqueva", authorsStyle, contextProvider);
        this.authorsText.fitSize();
        this.authorsText.getStyle().centerHorizontally(contextProvider);

        Style buttonStyle = new Style();
        buttonStyle.y = 400;
        buttonStyle.font = new Font("Arial", Font.BOLD, 24);
        buttonStyle.foregroundColor = new Color(0xFAFAFA);
        buttonStyle.backgroundColor = new Color(0x161616);
        buttonStyle.height = 60;
        buttonStyle.width = 300;
        buttonStyle.borderRadius = 16;

        this.startButton = new Button("Start", buttonStyle, contextProvider);
        this.startButton.getStyle().centerHorizontally(contextProvider);

        this.startButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> System.out.println("Start button clicked!"));
    }
}
