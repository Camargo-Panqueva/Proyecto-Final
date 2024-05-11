package view.context;

import controller.GameController;
import view.input.Keyboard;
import view.input.Mouse;
import view.themes.ThemeManager;
import view.window.Window;

/**
 * Provides a context for the game view.
 * <p>
 * This class provides a context for the game view.
 * It contains references to the window, controller, mouse, and theme manager.
 * This context is used to provide the necessary dependencies to the components of the game view.
 * It is used to provide a single point of access to the game view's dependencies.
 * This class is used to reduce the number of dependencies that need to be passed to the components of the game view.
 * </p>
 *
 * @param window
 * @param controller
 * @param mouse
 * @param themeManager
 */
public record ContextProvider(Window window, GameController controller, Mouse mouse, Keyboard keyboard,
                              ThemeManager themeManager) {
}
