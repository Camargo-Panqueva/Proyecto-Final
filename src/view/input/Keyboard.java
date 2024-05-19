package view.input;

import util.ConsumerFunction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a keyboard object that can be used to handle keyboard input.
 * <p>
 * This class represents a keyboard object that can be used to handle keyboard input.
 * It provides a structure for handling keyboard events such as key presses and releases.
 * The keyboard object can be used to check if a key is pressed and to add event handlers.
 * </p>
 */
public final class Keyboard implements KeyListener {

    private final boolean[] keys;

    private final HashMap<KeyboardEvent.EventType, ArrayList<ConsumerFunction<KeyboardEvent>>> keyEventHandlers;

    /**
     * Creates a new Keyboard object with the default key states.
     * <p>
     * This constructor creates a new Keyboard object with the default key states.
     * It initializes the key states and event handlers for the keyboard object.
     * </p>
     */
    public Keyboard() {
        this.keyEventHandlers = new HashMap<>();
        this.keys = new boolean[256];
    }

    /**
     * Dispatches a keyboard event to the registered event handlers.
     * <p>
     * This method dispatches a keyboard event to the registered event handlers.
     * It calls the event handlers for the given event type with the event data.
     * </p>
     *
     * @param eventType the type of the keyboard event.
     * @param event     the keyboard event data.
     */
    private void dispatchEvent(KeyboardEvent.EventType eventType, KeyEvent event) {
        ArrayList<ConsumerFunction<KeyboardEvent>> handlers = this.keyEventHandlers.get(eventType);

        if (handlers != null) {
            for (ConsumerFunction<KeyboardEvent> handler : handlers) {
                handler.run(new KeyboardEvent(this, event.getKeyCode(), event.getKeyChar()));
            }
        }
    }

    /**
     * Handles a key typed event.
     * <p>
     * This method handles a key typed event.
     * It sets the key state to true and dispatches the event to the event handlers.
     * </p>
     *
     * @param event the key typed event data.
     */
    @Override
    public void keyTyped(KeyEvent event) {
        if (event.getKeyCode() >= this.keys.length) return;

        this.keys[event.getKeyCode()] = true;
        this.dispatchEvent(KeyboardEvent.EventType.TYPED, event);
    }

    /**
     * Handles a key pressed event.
     * <p>
     * This method handles a key pressed event.
     * It sets the key state to true and dispatches the event to the event handlers.
     * </p>
     *
     * @param event the key pressed event data.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() >= this.keys.length) return;

        this.keys[event.getKeyCode()] = true;
        this.dispatchEvent(KeyboardEvent.EventType.PRESSED, event);
    }

    /**
     * Handles a key released event.
     * <p>
     * This method handles a key released event.
     * It sets the key state to false and dispatches the event to the event handlers.
     * </p>
     *
     * @param event the key released event data.
     */
    @Override
    public void keyReleased(KeyEvent event) {
        if (event.getKeyCode() >= this.keys.length) return;

        this.keys[event.getKeyCode()] = false;
        this.dispatchEvent(KeyboardEvent.EventType.RELEASED, event);
    }

    /**
     * Adds an event handler for the given event type.
     * <p>
     * This method adds an event handler for the given event type.
     * It adds the event handler to the list of event handlers for the given event type.
     * </p>
     *
     * @param eventType the type of the keyboard event.
     * @param handler   the event handler to add.
     */
    public void addEventHandler(KeyboardEvent.EventType eventType, ConsumerFunction<KeyboardEvent> handler) {
        if (!this.keyEventHandlers.containsKey(eventType)) {
            this.keyEventHandlers.put(eventType, new ArrayList<>());
        }

        this.keyEventHandlers.get(eventType).add(handler);
    }

    /**
     * Returns whether the given key is pressed.
     * <p>
     * This method returns whether the given key is pressed.
     * It returns the state of the key in the key states array.
     * If the key is pressed, the method returns true, otherwise it returns false.
     * </p>
     *
     * @param keyCode the code of the key to check.
     * @return whether the key is pressed.
     */
    public boolean isKeyPressed(int keyCode) {
        return this.keys[keyCode];
    }
}
