package view.components.match;

import model.wall.WallType;
import view.components.GameComponent;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.context.GlobalContext;
import view.context.MatchContext;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;

public final class ControlPanel extends GameComponent {

    private final MatchContext matchContext;
    private ArrayList<GameComponent> components;
    private Text title;
    private Text playerName;
    private Text timer;
    private Selector wallSelector;

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
        graphics.setColor(this.globalContext.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.DIMMED));
        graphics.fillRoundRect(
                this.style.x,
                this.style.y,
                this.style.width,
                this.style.height,
                this.style.borderRadius,
                this.style.borderRadius
        );

        graphics.setColor(this.globalContext.currentTheme().getColor(Theme.ColorName.BACKGROUND, Theme.ColorVariant.NORMAL));
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

        int expectedWidth = this.style.x + this.style.width + this.style.paddingX;

        if (expectedWidth > this.globalContext.window().getCanvas().getWidth()) {
            this.globalContext.window().setCanvasWidth(expectedWidth);
        }

        for (GameComponent component : this.components) {
            component.getStyle().centerHorizontally(this.getBounds());
        }

        this.wallSelector.getStyle().y = this.style.y + this.style.height - 60;
    }

    @Override
    protected void handleThemeChange(Theme theme) {
    }

    @Override
    protected void setupDefaultStyle() {
    }

    private void updatePlayerText() {
        String text = String.format("Is the %s's turn!", this.matchContext.playerInTurn().name());

        this.playerName.setText(text);
        this.playerName.getStyle().centerHorizontally(this.getBounds());

        this.playerName.getStyle().foregroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), Theme.ColorVariant.NORMAL);
        this.wallSelector.getStyle().backgroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), Theme.ColorVariant.NORMAL);
    }

    private void updateTimer() {
        int timeRemaining = this.matchContext.playerInTurn().secondRemaining();
        String text = String.format("%d Sec.", this.matchContext.playerInTurn().secondRemaining());

        if (timeRemaining > 15) {
            this.timer.getStyle().foregroundColor = this.globalContext.currentTheme().getColor(
                Theme.ColorName.GREEN,
                Theme.ColorVariant.NORMAL
            );
        } else {
            this.timer.getStyle().foregroundColor = this.globalContext.currentTheme().getColor(
                Theme.ColorName.RED,
                Theme.ColorVariant.NORMAL
            );

            text += " Hurry up!";
        }

        this.timer.setText(text);
        this.timer.getStyle().centerHorizontally(this.getBounds());
    }

    private String mapWallType(WallType wallType) {
        return switch (wallType) {
            case NORMAL -> "Normal";
            case LARGE -> "Large";
            case TEMPORAL_WALL -> "Temporal";
            default -> throw new IllegalStateException("Unexpected value: " + wallType);
        };
    }

    private void setupComponentEvents() {
        this.matchContext.addEventListener(MatchContext.MatchEvent.TURN_CHANGED, _event -> {
            this.updatePlayerText();
        });

        this.matchContext.addEventListener(MatchContext.MatchEvent.REMAINING_TIME_CHANGED, _event -> {
            this.updateTimer();
        });
    }

    private void setupComponents() {
        this.components = new ArrayList<>();

        this.title = new Text("Control  Panel", this.globalContext);
        this.title.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(32.0f);
        this.title.getStyle().y = this.style.y + this.style.paddingY + 60;
        this.title.fitSize();
        this.components.add(this.title);

        this.playerName = new Text("undefined", this.globalContext);
        this.playerName.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.playerName.getStyle().y = this.title.getStyle().y + 90;
        this.playerName.fitSize();
        this.components.add(this.playerName);

        this.timer = new Text("undefined", this.globalContext);
        this.timer.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.timer.getStyle().y = this.playerName.getStyle().y + 30;
        this.timer.fitSize();
        this.components.add(this.timer);

        ArrayList<String> wallTypes = new ArrayList<>();

        wallTypes.add(this.mapWallType(WallType.NORMAL));
        wallTypes.add(this.mapWallType(WallType.LARGE));
        wallTypes.add(this.mapWallType(WallType.TEMPORAL_WALL));

        this.wallSelector = new Selector(wallTypes, this.globalContext);
        this.wallSelector.getStyle().height = 44;
        this.wallSelector.getStyle().font = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.components.add(this.wallSelector);

        this.updatePlayerText();
        this.updateTimer();
    }
}
