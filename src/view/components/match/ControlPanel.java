package view.components.match;

import model.wall.WallType;
import view.components.GameComponent;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.input.KeyboardEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class ControlPanel extends GameComponent {

    private final MatchContext matchContext;
    private ArrayList<GameComponent> components;
    private Text title;
    private Text playerName;
    private Text timer;
    private Text remainingWalls;
    private Selector<WallType> wallSelector;

    /**
     * Creates a new ControlPanel component with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public ControlPanel(GlobalContext globalContext, MatchContext matchContext) {
        super(globalContext);
        this.matchContext = matchContext;

        this.setupComponents();
        this.setupComponentEvents();
    }

    private void renderBackground(Graphics2D graphics) {
        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.DIMMED));
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
        graphics.fillRoundRect(
                this.style.x + this.style.borderWidth,
                this.style.y + this.style.borderWidth,
                this.style.width - this.style.borderWidth * 2,
                this.style.height - this.style.borderWidth * 2,
                this.style.borderRadius,
                this.style.borderRadius
        );
    }

    @Override
    public void update() {
        for (GameComponent component : this.components) {
            component.update();
        }
    }

    @Override
    public void render(Graphics2D graphics) {
        this.renderBackground(graphics);

        for (GameComponent component : this.components) {
            component.render(graphics);
        }
    }

    @Override
    public void fitSize() {
        for (GameComponent component : this.components) {
            component.getStyle().centerHorizontally(this.getBounds());
        }

        int margin = 20;
        int paddingX = 20;
        int paddingY = 20;

        this.title.getStyle().y = this.style.y + this.style.borderWidth + margin;
        this.playerName.getStyle().y = this.title.getStyle().y + this.title.getStyle().height + paddingY;
        this.timer.getStyle().y = this.playerName.getStyle().y + this.title.getStyle().height + paddingY;
        this.wallSelector.getStyle().y = this.style.y + this.style.height - this.style.borderWidth - this.wallSelector.getStyle().height - margin;

        this.remainingWalls.getStyle().centerVertically(this.wallSelector.getBounds());
        this.remainingWalls.getStyle().x = this.wallSelector.getStyle().x + this.wallSelector.getStyle().width - 70;

        System.out.println(this.remainingWalls.getStyle().x + " " + this.remainingWalls.getStyle().y);
    }

    @Override
    protected void setupDefaultStyle() {
    }

    private void updatePlayerText() {
        String text = String.format("Is the %s's turn!", this.matchContext.playerInTurn().name());

        this.playerName.setText(text);
        this.playerName.getStyle().centerHorizontally(this.getBounds());

        this.playerName.getStyle().foregroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), ColorVariant.NORMAL);
        this.wallSelector.getStyle().backgroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), ColorVariant.NORMAL);
    }

    private void updateTimer() {
        int timeRemaining = this.matchContext.playerInTurn().secondRemaining();
        String text = String.format("%d Sec.", this.matchContext.playerInTurn().secondRemaining());

        if (timeRemaining > 15) {
            this.timer.getStyle().foregroundColor = new ThemeColor(ColorName.GREEN, ColorVariant.NORMAL);
        } else {
            this.timer.getStyle().foregroundColor = new ThemeColor(ColorName.RED, ColorVariant.NORMAL);
            text += " Hurry up!";
        }

        this.timer.setText(text);
        this.timer.getStyle().centerHorizontally(this.getBounds());
    }

    private void updateRemainingWalls() {
        this.remainingWalls.setText("x" + this.matchContext.playerInTurn().wallsRemaining().get(this.matchContext.selectedWallType()));
    }

    private void setupComponentEvents() {
        this.matchContext.addEventListener(MatchContext.MatchEvent.TURN_CHANGED, _event -> {
            this.updatePlayerText();
            this.updateRemainingWalls();
        });

        this.matchContext.addEventListener(MatchContext.MatchEvent.REMAINING_TIME_CHANGED, _event -> {
            this.updateTimer();
        });

        this.matchContext.addEventListener(MatchContext.MatchEvent.WALL_TYPE_CHANGED, _event -> {
            this.updateRemainingWalls();
        });

        this.wallSelector.addComponentListener(ComponentEvent.VALUE_CHANGED, (_previous, _current) -> {
            this.matchContext.setSelectedWallType(this.wallSelector.getSelectedOption());
        });

        this.addKeyListener(KeyboardEvent.EventType.PRESSED, event -> {
            if (event.keyCode == KeyboardEvent.VK_SPACE) {
                this.wallSelector.incrementSelectedOption();
            }
        });
    }

    private void setupComponents() {

        this.components = new ArrayList<>();

        this.title = new Text("Control  Panel", this.globalContext);
        this.title.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(32.0f);
        this.title.fitSize();
        this.components.add(this.title);

        this.playerName = new Text("undefined", this.globalContext);
        this.playerName.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.playerName.fitSize();
        this.components.add(this.playerName);

        this.timer = new Text("undefined", this.globalContext);
        this.timer.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.timer.fitSize();
        this.components.add(this.timer);

        ArrayList<WallType> wallTypes = new ArrayList<>(Arrays.asList(WallType.values()));

        this.wallSelector = new Selector<>(wallTypes, this.globalContext);
        this.wallSelector.getStyle().height = 44;
        this.wallSelector.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.components.add(this.wallSelector);

        this.remainingWalls = new Text("0", this.globalContext);
        this.remainingWalls.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(16.0f);
        this.remainingWalls.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.remainingWalls.fitSize();
        this.components.add(this.remainingWalls);

        this.updatePlayerText();
        this.updateTimer();
        this.updateRemainingWalls();
    }
}
