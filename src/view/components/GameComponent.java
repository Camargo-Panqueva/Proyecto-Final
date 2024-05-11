package view.components;

import util.ConsumerFunction;
import view.context.ContextProvider;
import view.context.Style;
import view.input.KeyboardEvent;
import view.input.Mouse;
import view.input.MouseEvent;
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
    private final HashMap<MouseEvent.EventType, ArrayList<ConsumerFunction<MouseEvent>>> mouseEventHandlers;
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
        this.addMouseListener(MouseEvent.EventType.ENTER, _event -> this.contextProvider.window().getCanvas().setCursor(this.style.cursor));
        this.addMouseListener(MouseEvent.EventType.LEAVE, _event -> this.contextProvider.window().getCanvas().setCursor(Cursor.getDefaultCursor()));
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
        GameComponent component = this;

        MouseEvent event = new MouseEvent(mouse, component);

        if (this.getBounds().contains(mouse.getMousePosition())) {
            this.dispatchMouseEvent(MouseEvent.EventType.HOVER, event);

            if (mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {

                if (!this.isMousePressed) {
                    this.isMousePressed = true;
                }

                if (!this.hasFocus) {
                    this.hasFocus = true;
                    this.dispatchMouseEvent(MouseEvent.EventType.GET_FOCUS, event);
                }

                this.dispatchMouseEvent(MouseEvent.EventType.PRESSED, event);
            } else {

                if (this.isMousePressed) {
                    this.isMousePressed = false;
                    this.dispatchMouseEvent(MouseEvent.EventType.RELEASED, event);
                }
            }

            if (!this.isMouseEntered) {
                this.isMouseEntered = true;
                this.dispatchMouseEvent(MouseEvent.EventType.ENTER, event);
            }
        } else {

            //TODO: Delete this comment after check that the code below allows drag and drop functionality
            if (mouse.isButtonPressed(Mouse.LEFT_BUTTON)) {
                if (this.hasFocus) {
                    this.hasFocus = false;
                    this.dispatchMouseEvent(MouseEvent.EventType.LOSE_FOCUS, event);
                }
            } else {
                this.isMousePressed = false;
            }

            if (this.isMouseEntered) {
                this.isMouseEntered = false;
                this.dispatchMouseEvent(MouseEvent.EventType.LEAVE, event);
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
     * @param eventType the mouse event type to listen for.
     * @param handler   the event handler to run when the event is dispatched.
     */
    public void addMouseListener(MouseEvent.EventType eventType, ConsumerFunction<MouseEvent> handler) {
        if (!this.mouseEventHandlers.containsKey(eventType)) {
            this.mouseEventHandlers.put(eventType, new ArrayList<>());
        }

        this.mouseEventHandlers.get(eventType).add(handler);
    }

    public void addKeyListener(KeyboardEvent.EventType eventType, ConsumerFunction<KeyboardEvent> handler) {
        this.contextProvider.keyboard().addEventHandler(eventType, handler);
    }

    /**
     * Dispatches a mouse event to the event handlers.
     *
     * @param eventType the type of the mouse event.
     * @param event     the mouse event to dispatch.
     */
    public void dispatchMouseEvent(MouseEvent.EventType eventType, MouseEvent event) {
        if (this.mouseEventHandlers.containsKey(eventType)) {
            for (ConsumerFunction<MouseEvent> function : this.mouseEventHandlers.get(eventType)) {
                function.run(event);
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


}
