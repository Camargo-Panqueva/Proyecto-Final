package view.input;

/**
 * Represents a keyboard event that can be dispatched to event handlers.
 * <p>
 * This class represents a keyboard event that can be dispatched to event handlers.
 * It provides a structure for handling keyboard events such as key presses and releases.
 * The keyboard event contains the keyboard object that dispatched the event, the key code,
 * and the key character of the event.
 * </p>
 */
public class KeyboardEvent {

    /**
     * The key code for the left arrow key.
     */
    public static final int VK_LEFT = 37;

    /**
     * The key code for the right arrow key.
     */
    public static final int VK_RIGHT = 39;

    /**
     * The key code for the up arrow key.
     */
    public static final int VK_UP = 38;

    /**
     * The key code for the down arrow key.
     */
    public static final int VK_DOWN = 40;

    /**
     * The key code for the W key.
     */
    public static final int VK_W = 87;

    /**
     * The key code for the A key.
     */
    public static final int VK_A = 65;

    /**
     * The key code for the S key.
     */
    public static final int VK_S = 83;

    /**
     * The key code for the D key.
     */
    public static final int VK_D = 68;

    /**
     * The key code for the R key.
     */
    public static final int VK_R = 82;

    /**
     * The key code for the Enter key.
     */
    public static final int VK_ENTER = 10;

    /**
     * The key code for the Escape key.
     */
    public static final int VK_ESCAPE = 27;

    /**
     * The key code for the Backspace key.
     */
    public static final int VK_BACKSPACE = 8;

    /**
     * The key code for the Space key.
     */
    public static final int VK_SPACE = 32;

    /**
     * The key code for the F3 key.
     */
    public static final int VK_F3 = 114;

    /**
     * The keyboard that dispatched the event.
     */
    public final Keyboard keyboard;

    /**
     * The code of the key that was pressed.
     */
    public final int keyCode;

    /**
     * The character of the key that was pressed.
     */
    public final char keyChar;

    /**
     * Creates a new KeyboardEvent with the given keyboard, key code, and key character.
     *
     * @param keyboard the keyboard that dispatched the event.
     * @param keyCode  the code of the key that was pressed.
     * @param keyChar  the character of the key that was pressed.
     */
    public KeyboardEvent(Keyboard keyboard, int keyCode, char keyChar) {
        this.keyboard = keyboard;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    /**
     * Checks if the key is a letter.
     *
     * @return true if the key is a letter, false otherwise.
     */
    public boolean isAlphaNumeric() {
        return Character.isLetterOrDigit(this.keyChar);
    }

    /**
     * Enum representing the type of the keyboard event.
     */
    public enum EventType {

        /**
         * Represents an event where a key is typed.
         */
        TYPED,

        /**
         * Represents an event where a key is pressed.
         */
        PRESSED,

        /**
         * Represents an event where a key is released.
         */
        RELEASED
    }
}
