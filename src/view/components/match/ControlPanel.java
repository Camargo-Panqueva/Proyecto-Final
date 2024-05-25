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

/**
 * Represents a control panel for the game.
 * <p>
 * This class represents a control panel for the game.
 * It provides a structure for displaying information about the game state and the players.
 * The control panel is used to display information about the game state and the players.
 * </p>
 */
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
     * @param matchContext  the match context of the game.
     */
    public ControlPanel(GlobalContext globalContext, MatchContext matchContext) {
        super(globalContext);
        this.matchContext = matchContext;

        this.setupComponents();
        this.setupComponentEvents();
    }

    /**
     * Renders the background of the control panel.
     *
     * @param graphics the graphics object to render the background with.
     */
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

    /**
     * Updates the control panel's logic.
     *
     * <p>
     * This method updates the control panel's logic.
     * It updates the player text, timer, and remaining walls.
     * </p>
     */
    @Override
    public void update() {
        for (GameComponent component : this.components) {
            component.update();
        }
    }

    /**
     * Renders the control panel on the screen.
     * <p>
     * This method renders the control panel on the screen.
     * It renders the background and all the components of the control panel.
     * </p>
     *
     * @param graphics the graphics object to render the control panel with.
     */
    @Override
    public void render(Graphics2D graphics) {
        this.renderBackground(graphics);

        for (GameComponent component : this.components) {
            component.render(graphics);
        }
    }

    /**
     * Fits the control panel's size to its content.
     * <p>
     * This method fits the control panel's size to its content.
     * It fits the size of the title, player name, timer, wall selector, and remaining walls.
     * </p>
     */
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
    }

    /**
     * Sets up the default style for the control panel.
     */
    @Override
    protected void setupDefaultStyle() {
    }

    /**
     * Updates the player text.
     *
     * <p>
     * This method updates the player text.
     * It sets the text of the player name to the current player's name.
     * It sets the color of the player name to the color of the current player.
     * </p>
     */
    private void updatePlayerText() {
        String text = String.format("Is the %s's turn!", this.matchContext.playerInTurn().name());

        this.playerName.setText(text);
        this.playerName.getStyle().centerHorizontally(this.getBounds());

        this.playerName.getStyle().foregroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), ColorVariant.NORMAL);
        this.wallSelector.getStyle().backgroundColor = this.matchContext.getPlayerColor(this.matchContext.playerInTurn(), ColorVariant.NORMAL);
    }

    /**
     * Updates the timer.
     *
     * <p>
     * This method updates the timer.
     * It sets the text of the timer to the remaining time of the current player.
     * It sets the color of the timer to green if the remaining time is greater than 15 seconds, and red otherwise.
     * </p>
     */
    private void updateTimer() {
        int timeRemaining = this.matchContext.playerInTurn().secondsRemaining();
        String text = String.format("%d Sec.", this.matchContext.playerInTurn().secondsRemaining());

        if (timeRemaining > 15) {
            this.timer.getStyle().foregroundColor = new ThemeColor(ColorName.GREEN, ColorVariant.NORMAL);
        } else {
            this.timer.getStyle().foregroundColor = new ThemeColor(ColorName.RED, ColorVariant.NORMAL);
            text += " Hurry up!";
        }

        this.timer.setText(text);
        this.timer.getStyle().centerHorizontally(this.getBounds());
    }

    /**
     * Updates the remaining walls.
     *
     * <p>
     * This method updates the remaining walls.
     * It sets the text of the remaining walls to the number of walls of the current player.
     * It sets the color of the remaining walls to the color of the current player.
     * </p>
     */
    private void updateRemainingWalls() {
        this.remainingWalls.setText("x" + this.matchContext.playerInTurn().wallsRemaining().get(this.matchContext.selectedWallType()));
    }

    /**
     * Sets up the events for the control panel.
     *
     * <p>
     * This method sets up the events for the control panel.
     * It sets up the events for the turn changed, remaining time changed, and wall type changed events.
     * It sets up the events for the wall selector value changed event.
     * It sets up the event for the space key pressed event.
     * </p>
     */
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

    /**
     * Sets up the components of the control panel.
     *
     * <p>
     * This method sets up the components of the control panel.
     * It sets up the title, player name, timer, wall selector, and remaining walls.
     * </p>
     */
    private void setupComponents() {

        this.components = new ArrayList<>();

        this.title = new Text("Control  Panel", this.globalContext);
        this.title.getStyle().font = this.globalContext.gameFont().deriveFont(32.0f);
        this.title.fitSize();
        this.components.add(this.title);

        this.playerName = new Text("undefined", this.globalContext);
        this.playerName.getStyle().font = this.globalContext.gameFont().deriveFont(20.0f);
        this.playerName.fitSize();
        this.components.add(this.playerName);

        this.timer = new Text("undefined", this.globalContext);
        this.timer.getStyle().font = this.globalContext.gameFont().deriveFont(20.0f);
        this.timer.fitSize();
        this.components.add(this.timer);

        ArrayList<WallType> wallTypes = new ArrayList<>(Arrays.asList(WallType.values()));

        this.wallSelector = new Selector<>(wallTypes, this.globalContext);
        this.wallSelector.getStyle().height = 44;
        this.wallSelector.getStyle().font = this.globalContext.gameFont().deriveFont(20.0f);
        this.components.add(this.wallSelector);

        this.remainingWalls = new Text("0", this.globalContext);
        this.remainingWalls.getStyle().font = this.globalContext.gameFont().deriveFont(16.0f);
        this.remainingWalls.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);
        this.remainingWalls.fitSize();
        this.components.add(this.remainingWalls);

        this.updatePlayerText();
        this.updateTimer();
        this.updateRemainingWalls();
    }
}
