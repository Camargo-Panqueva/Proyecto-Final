package view.input;

public class KeyboardEvent {

    public static final int VK_LEFT = 37;
    public static final int VK_RIGHT = 39;
    public static final int VK_UP = 38;
    public static final int VK_DOWN = 40;
    public static final int VK_W = 87;
    public static final int VK_A = 65;
    public static final int VK_S = 83;
    public static final int VK_D = 68;
    public static final int VK_ENTER = 10;
    public static final int VK_ESCAPE = 27;
    public static final int VK_BACKSPACE = 8;
    public static final int VK_SPACE = 32;

    public final Keyboard keyboard;
    public final int keyCode;
    public final char keyChar;

    public KeyboardEvent(Keyboard keyboard, int keyCode, char keyChar) {
        this.keyboard = keyboard;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    public boolean isAlphaNumeric() {
        return Character.isLetterOrDigit(this.keyChar);
    }

    public enum EventType {
        TYPED,
        PRESSED,
        RELEASED
    }
}
