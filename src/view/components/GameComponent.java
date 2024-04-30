package view.components;

import util.ConsumerFunction;
import view.context.ContextProvider;
import view.context.Style;
import view.input.Mouse;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a component that can be rendered on the screen and interacted with.
 * <p>
 * This class is the base class for all components that can be rendered on the screen and interacted with.
 * It provides a basic structure for handling mouse events and rendering the component on the screen.
 * <p>
 */
public abstract class GameComponent {

    protected final ContextProvider contextProvider;
    private final HashMap<MouseEvent, ArrayList<ConsumerFunction<Mouse>>> mouseEventHandlers;
    protected Style style;
    protected boolean isMouseEntered;
    protected boolean isMousePressed;
    protected boolean hasFocus;

    /**
     * Creates a new GameComponent with the given context provider.
     *
     * @param contextProvider the context provider for the component.
     */
    public GameComponent(ContextProvider contextProvider) {
        this.mouseEventHandlers = new HashMap<>();
        this.contextProvider = contextProvider;
        this.style = new Style();

        this.setupDefaultEventListeners();
        this.setupDefaultStyle();
        this.contextProvider.themeManager().addListener(this::handleThemeChange);
    }

    /**
     * Updates the component's logic.
     */
    public abstract void update();

    /**
     * Renders the component on the screen.
     *
     * @param graphics the graphics object to render the component with.
     */
    public abstract void render(Graphics2D graphics);

    /**
     * Fits the component's size to its content.
     */
    public abstract void fitSize();

    /**
     * Handles a theme change event.
     *
     * @param theme the new theme.
     */
    protected abstract void handleThemeChange(Theme theme);

    /**
     * Sets up the default style for the component.
     */
    protected abstract void setupDefaultStyle();

    /**
     * Sets up the default event listeners for the component.
     */
    protected void setupDefaultEventListeners() {
        this.addEventListener(MouseEvent.ENTER, _ -> this.contextProvider.window().getCanvas().setCursor(this.style.cursor));
        this.addEventListener(MouseEvent.LEAVE, _ -> this.contextProvider.window().getCanvas().setCursor(Cursor.getDefaultCursor()));
    }

    /**
     * Polls the mouse events for the component.
     *
     * <p>
     * This method checks if the mouse is hovering over the component, if the mouse is pressed, if the mouse has
     * entered the component, and if the mouse has left the component. It then dispatches the appropriate mouse
     * events to the event handlers.
     * <br>
     * This method should be called in the {@link #update()} method of the component.
     * <br>
     * </p>
     */
    protected void pollMouseEvents() {
        Mouse mouse = this.contextProvider.mouse();

        if (this.getBounds().contains(mouse.getMousePosition())) {
            this.dispatchMouseEvent(MouseEvent.HOVER, mouse);

            if (mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {

                if (!this.isMousePressed) {
                    this.isMousePressed = true;
                    this.dispatchMouseEvent(MouseEvent.CLICK, mouse);
                }

                if (!this.hasFocus) {
                    this.hasFocus = true;
                    this.dispatchMouseEvent(MouseEvent.GET_FOCUS, mouse);
                }

                this.dispatchMouseEvent(MouseEvent.PRESSED, mouse);
            } else {

                if (this.isMousePressed) {
                    this.isMousePressed = false;
                    this.dispatchMouseEvent(MouseEvent.RELEASED, mouse);
                }
            }

            if (!this.isMouseEntered) {
                this.isMouseEntered = true;
                this.dispatchMouseEvent(MouseEvent.ENTER, mouse);
            }
        } else {
            if (this.hasFocus && mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
                this.hasFocus = false;
                this.dispatchMouseEvent(MouseEvent.LOSE_FOCUS, mouse);
            }

            if (this.isMouseEntered) {
                this.isMouseEntered = false;
                this.dispatchMouseEvent(MouseEvent.LEAVE, mouse);
            }
        }
    }

    /**
     * Gets the center point of the component.
     *
     * @return the center point of the component.
     */
    public Point getCenter() {
        int centerX = this.style.x + this.style.width / 2;
        int centerY = this.style.y + this.style.height / 2;

        return new Point(centerX, centerY);
    }

    /**
     * Gets the bounds of the component.
     *
     * @return the bounds of the component.
     */
    public Rectangle getBounds() {
        return new Rectangle(this.style.x, this.style.y, this.style.width, this.style.height);
    }

    /**
     * Adds an event listener for the given mouse event.
     *
     * @param event   the mouse event to listen for.
     * @param handler the handler for the event.
     */
    public void addEventListener(MouseEvent event, ConsumerFunction<Mouse> handler) {
        if (!this.mouseEventHandlers.containsKey(event)) {
            this.mouseEventHandlers.put(event, new ArrayList<>());
        }

        this.mouseEventHandlers.get(event).add(handler);
    }

    /**
     * Dispatches a mouse event to the event handlers.
     *
     * @param event the mouse event to dispatch.
     * @param mouse the mouse object to dispatch the event with.
     */
    public void dispatchMouseEvent(MouseEvent event, Mouse mouse) {
        if (this.mouseEventHandlers.containsKey(event)) {
            for (ConsumerFunction<Mouse> function : this.mouseEventHandlers.get(event)) {
                function.run(mouse);
            }
        }
    }

    /**
     * Sets the cursor for the component.
     *
     * @param cursor the cursor to set.
     */
    public void setCursor(Cursor cursor) {
        this.style.cursor = cursor;
    }

    /**
     * Gets the style of the component.
     *
     * @return the style of the component.
     */
    public Style getStyle() {
        return style;
    }

    /**
     * Represents a mouse event that can be dispatched to the event handlers.
     */
    public enum MouseEvent {
        HOVER,
        CLICK,
        ENTER,
        LEAVE,
        PRESSED,
        RELEASED,
        GET_FOCUS,
        LOSE_FOCUS
    }
}
