package view.input;

import util.ConsumerFunction;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;

public final class Keyboard implements KeyListener {

    private final boolean[] keys;

    private final HashMap<KeyboardEvent.EventType, ArrayList<ConsumerFunction<KeyboardEvent>>> keyEventHandlers;

    public Keyboard() {
        this.keyEventHandlers = new HashMap<>();
        this.keys = new boolean[256];
    }

    private void dispatchEvent(KeyboardEvent.EventType eventType, KeyEvent event) {
        ArrayList<ConsumerFunction<KeyboardEvent>> handlers = this.keyEventHandlers.get(eventType);

        if (handlers != null) {
            for (ConsumerFunction<KeyboardEvent> handler : handlers) {
                handler.run(new KeyboardEvent(this, event.getKeyCode(), event.getKeyChar()));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        this.keys[event.getKeyCode()] = true;
        this.dispatchEvent(KeyboardEvent.EventType.TYPED, event);
    }

    @Override
    public void keyPressed(KeyEvent event) {
        this.keys[event.getKeyCode()] = true;
        this.dispatchEvent(KeyboardEvent.EventType.PRESSED, event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        this.keys[event.getKeyCode()] = false;
        this.dispatchEvent(KeyboardEvent.EventType.RELEASED, event);
    }

    public void addEventHandler(KeyboardEvent.EventType eventType, ConsumerFunction<KeyboardEvent> handler) {
        if (!this.keyEventHandlers.containsKey(eventType)) {
            this.keyEventHandlers.put(eventType, new ArrayList<>());
        }

        this.keyEventHandlers.get(eventType).add(handler);
    }

    public boolean isKeyPressed(int keyCode) {
        return this.keys[keyCode];
    }
}
