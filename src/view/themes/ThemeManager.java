package view.themes;

/**
 * Represents a theme manager that can be used to manage themes in the game view.
 * <p>
 * This class represents a theme manager that can be used to manage themes in the game view.
 * It provides a structure for managing themes and notifying listeners when the theme changes.
 * </p>
 */
public final class ThemeManager {

    private Theme currentTheme;

    /**
     * Creates a new ThemeManager with the default light theme.
     */
    public ThemeManager() {
        this.currentTheme = new LightTheme();
    }

    /**
     * Creates a new ThemeManager with the given theme.
     *
     * @param theme the theme to use for the manager.
     */
    public ThemeManager(Theme theme) {
        this.currentTheme = theme;
    }

    /**
     * Gets the current theme of the manager.
     *
     * @return the current theme of the manager.
     */
    public Theme getCurrentTheme() {
        return this.currentTheme;
    }

    /**
     * Sets the theme of the manager.
     * <p>
     * This method sets the theme of the manager and notifies all listeners.
     * </p>
     *
     * @param theme the theme to set for the manager.
     */
    public void setTheme(Theme theme) {
        this.currentTheme = theme;
    }

    /**
     * Toggles the theme of the manager.
     * <p>
     * This method toggles the theme of the manager between light and dark themes.
     * It notifies all listeners when the theme changes.
     * </p>
     */
    public void toggleTheme() {
        if (this.currentTheme instanceof LightTheme) {
            this.currentTheme = new DarkTheme();
        } else {
            this.currentTheme = new LightTheme();
        }
    }
}
