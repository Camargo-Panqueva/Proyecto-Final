package view.scene;

import view.components.BackgroundSeparator;
import view.components.Button;
import view.components.GameComponent;
import view.components.Text;
import view.context.ContextProvider;

public final class SelectModeScene extends Scene {

    private BackgroundSeparator separator;
    private Text selectModeText;
    private Button singlePlayerButton;
    private Button multiPlayerButton;

    public SelectModeScene(ContextProvider contextProvider) {
        super(contextProvider);
    }

    @Override
    protected void addAllComponents() {
        this.addComponent(this.separator);
        this.addComponent(this.selectModeText);
        this.addComponent(this.singlePlayerButton);
        this.addComponent(this.multiPlayerButton);
    }

    @Override
    protected void setupComponents() {
        int canvasWidth = this.contextProvider.window().getCanvasSize();
        int separatorMargin = 40;
        int separatorHeight = 200;

        this.separator = new BackgroundSeparator(contextProvider);
        this.separator.getStyle().y = separatorMargin;
        this.separator.getStyle().width = canvasWidth - 2 * separatorMargin;
        this.separator.getStyle().height = separatorHeight;
        this.separator.getStyle().borderRadius = 20;
        this.separator.getStyle().centerHorizontally(contextProvider);

        this.selectModeText = new Text("Please select the mode", contextProvider);
        this.selectModeText.getStyle().y = separatorHeight + separatorMargin + 50;
        this.selectModeText.getStyle().font = this.contextProvider.window().getCanvas().getFont().deriveFont(40.0f);
        this.selectModeText.fitSize();
        this.selectModeText.getStyle().centerHorizontally(contextProvider);

        this.singlePlayerButton = new Button("Single Player", contextProvider);
        this.singlePlayerButton.getStyle().y = separatorHeight + separatorMargin + 150;
        this.singlePlayerButton.getStyle().centerHorizontally(contextProvider);

        this.multiPlayerButton = new Button("1 VS 1", contextProvider);
        this.multiPlayerButton.getStyle().y = separatorHeight + separatorMargin + 250;
        this.multiPlayerButton.getStyle().centerHorizontally(contextProvider);
    }

    @Override
    protected void setupEvents() {
        this.singlePlayerButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> System.out.println("Single player button clicked"));
        this.multiPlayerButton.addEventListener(GameComponent.MouseEvent.CLICK, _ -> System.out.println("Multi player button clicked"));
    }
}
