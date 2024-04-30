package view.themes;

import util.ConsumerFunction;

import java.util.ArrayList;

/**
 * Represents a theme manager that can be used to manage themes in the game view.
 * <p>
 * This class represents a theme manager that can be used to manage themes in the game view.
 * It provides a structure for managing themes and notifying listeners when the theme changes.
 * </p>
 */
public final class ThemeManager {

    /**
     * A list of listeners that are notified when the theme changes.
     * <p>
     * This list contains all listeners that are notified when the theme changes.
     * Each listener is a function that takes a theme as an argument.
     * </p>
     */
    private final ArrayList<ConsumerFunction<Theme>> listeners;
    private Theme currentTheme;

    /**
     * Creates a new ThemeManager with the default light theme.
     */
    public ThemeManager() {
        this.currentTheme = new LightTheme();
        this.listeners = new ArrayList<>();
    }

    /**
     * Creates a new ThemeManager with the given theme.
     *
     * @param theme the theme to use for the manager.
     */
    public ThemeManager(Theme theme) {
        this.currentTheme = theme;
        this.listeners = new ArrayList<>();
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
        this.notifyListeners();
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
        this.notifyListeners();
    }

    /**
     * Adds a listener to the manager.
     * <p>
     * This method adds a listener to the manager.
     * The listener is notified when the theme changes.
     * </p>
     *
     * @param listener the listener to add to the manager.
     */
    public void addListener(ConsumerFunction<Theme> listener) {
        this.listeners.add(listener);
    }

    /**
     * Notifies all listeners of the manager.
     * <p>
     * This method notifies all listeners of the manager.
     * It runs each listener with the current theme as an argument.
     * </p>
     */
    private void notifyListeners() {
        for (ConsumerFunction<Theme> listener : this.listeners) {
            listener.run(this.currentTheme);
        }
    }
}
