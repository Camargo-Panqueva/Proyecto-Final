package view.context;

import view.input.Mouse;
import view.window.Window;

public record ContextProvider(Window window, Mouse mouse) {
}
