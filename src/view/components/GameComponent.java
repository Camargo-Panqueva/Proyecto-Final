package view.components;

import util.ConsumerFunction;
import view.context.GlobalContext;
import view.context.Style;
import view.input.KeyboardEvent;
import view.input.Mouse;
import view.input.MouseEvent;
import view.themes.ThemeColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * Represents a component that can be rendered on the screen and interacted with.
 * <p>
 * This class is the base class for all components that can be rendered on the screen and interacted with.
 * It provides a basic structure for handling mouse events and rendering the component on the screen.
 * <p>
 */
public abstract class GameComponent {

    protected final GlobalContext globalContext;
    private final HashMap<MouseEvent.EventType, ArrayList<ConsumerFunction<MouseEvent>>> mouseEventHandlers;
    private final HashMap<ComponentEvent, ArrayList<BiConsumer<Object, Object>>> componentEventHandlers;
    protected Style style;
    protected boolean isMouseEntered;
    protected boolean isMousePressed;
    protected boolean isDisabled;
    protected boolean hasFocus;

    /**
     * Creates a new GameComponent with the given context provider.
     *
     * @param globalContext the context provider for the component.
     */
    public GameComponent(GlobalContext globalContext) {
        this.mouseEventHandlers = new HashMap<>();
        this.componentEventHandlers = new HashMap<>();
        this.globalContext = globalContext;
        this.style = new Style();

        this.setupDefaultEventListeners();
        this.setupDefaultStyle();
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
     * Sets up the default style for the component.
     */
    protected abstract void setupDefaultStyle();

    /**
     * Sets up the default event listeners for the component.
     */
    protected void setupDefaultEventListeners() {
        this.addMouseListener(MouseEvent.EventType.ENTER, _event -> this.globalContext.window().getCanvas().setCursor(this.style.cursor));
        this.addMouseListener(MouseEvent.EventType.LEAVE, _event -> this.globalContext.window().getCanvas().setCursor(Cursor.getDefaultCursor()));
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
        if (this.isDisabled) {
            return;
        }
        Mouse mouse = this.globalContext.mouse();
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

    /**
     * Adds an event listener for the given keyboard event.
     *
     * @param eventType the keyboard event type to listen for.
     * @param handler   the event handler to run when the event is dispatched.
     */
    public void addKeyListener(KeyboardEvent.EventType eventType, ConsumerFunction<KeyboardEvent> handler) {
        this.globalContext.keyboard().addEventHandler(eventType, handler);
    }

    /**
     * Adds an event listener for the given component event.
     *
     * @param eventType the component event type to listen for.
     * @param handler   the event handler to run when the event is dispatched.
     */
    public void addComponentListener(ComponentEvent eventType, BiConsumer<Object, Object> handler) {
        if (!this.componentEventHandlers.containsKey(eventType)) {
            this.componentEventHandlers.put(eventType, new ArrayList<>());
        }

        this.componentEventHandlers.get(eventType).add(handler);
    }

    /**
     * Dispatches a mouse event to the event handlers.
     *
     * @param eventType the type of the mouse event.
     * @param event     the mouse event to dispatch.
     */
    protected void dispatchMouseEvent(MouseEvent.EventType eventType, MouseEvent event) {
        if (this.mouseEventHandlers.containsKey(eventType)) {
            for (ConsumerFunction<MouseEvent> function : this.mouseEventHandlers.get(eventType)) {
                function.run(event);
            }
        }
    }

    /**
     * Dispatches a component event to the event handlers.
     *
     * @param eventType     the type of the component event.
     * @param previousValue the previous value of the component.
     * @param newValue      the new value of the component.
     */
    protected void dispatchComponentEvent(ComponentEvent eventType, Object previousValue, Object newValue) {
        if (this.componentEventHandlers.containsKey(eventType)) {
            for (BiConsumer<Object, Object> function : this.componentEventHandlers.get(eventType)) {
                function.accept(previousValue, newValue);
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
     * Returns whether the component is disabled.
     *
     * @return whether the component is disabled.
     */
    public boolean isDisabled() {
        return isDisabled;
    }

    /**
     * Sets the disabled state of the component.
     *
     * @param disabled the disabled state of the component.
     */
    public void setDisabled(boolean disabled) {
        this.isDisabled = disabled;
        this.style.foregroundColor = new ThemeColor(this.style.foregroundColor.name(), disabled ? ThemeColor.ColorVariant.DIMMED : ThemeColor.ColorVariant.NORMAL);
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
     * Enum representing the different component events.
     */
    public enum ComponentEvent {

        /**
         * The event that is dispatched when the value of the component changes.
         */
        VALUE_CHANGED
    }
}
