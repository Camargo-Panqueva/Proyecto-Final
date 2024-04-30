package view.components;

import util.ConsumerFunction;
import view.context.ContextProvider;
import view.context.Style;
import view.input.Mouse;
import view.themes.Theme;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class GameComponent {

    protected final ContextProvider contextProvider;
    protected Style style;

    private final HashMap<MouseEvent, ArrayList<ConsumerFunction<Mouse>>> mouseEventHandlers;

    protected boolean isMouseEntered;
    protected boolean isMousePressed;
    protected boolean hasFocus;

    public GameComponent(ContextProvider contextProvider) {
        this.mouseEventHandlers = new HashMap<>();
        this.contextProvider = contextProvider;
        this.style = new Style();

        this.setupDefaultEventListeners();
        this.setupDefaultStyle();
        this.contextProvider.themeManager().addListener(this::handleThemeChange);
    }

    public abstract void update();

    public abstract void render(Graphics2D graphics);

    public abstract void fitSize();

    protected abstract void handleThemeChange(Theme theme);
    protected abstract void setupDefaultStyle();

    protected void setupDefaultEventListeners() {
        this.addEventListener(MouseEvent.ENTER, _ -> this.contextProvider.window().getCanvas().setCursor(this.style.cursor));
        this.addEventListener(MouseEvent.LEAVE, _ -> this.contextProvider.window().getCanvas().setCursor(Cursor.getDefaultCursor()));
    }

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

    public Point getCenter() {
        int centerX = this.style.x + this.style.width / 2;
        int centerY = this.style.y + this.style.height / 2;

        return new Point(centerX, centerY);
    }

    public Rectangle getBounds() {
        return new Rectangle(this.style.x, this.style.y, this.style.width, this.style.height);
    }

    public void addEventListener(MouseEvent event, ConsumerFunction<Mouse> handler) {
        if (!this.mouseEventHandlers.containsKey(event)) {
            this.mouseEventHandlers.put(event, new ArrayList<>());
        }

        this.mouseEventHandlers.get(event).add(handler);
    }

    public void dispatchMouseEvent(MouseEvent event, Mouse mouse) {
        if (this.mouseEventHandlers.containsKey(event)) {
            for (ConsumerFunction<Mouse> function : this.mouseEventHandlers.get(event)) {
                function.run(mouse);
            }
        }
    }

    public void setCursor(Cursor cursor) {
        this.style.cursor = cursor;
    }

    public Style getStyle() {
        return style;
    }

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
