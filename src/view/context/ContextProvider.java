package view.context;

import controller.main.GameController;
import view.input.Mouse;
import view.themes.ThemeManager;
import view.window.Window;

public record ContextProvider(Window window, GameController controller, Mouse mouse, ThemeManager themeManager) {
}
