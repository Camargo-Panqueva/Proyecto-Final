package view.themes;

import util.ConsumerFunction;

import java.util.ArrayList;

public final class ThemeManager {

    private final ArrayList<ConsumerFunction<Theme>> listeners;
    private Theme currentTheme;

    public ThemeManager() {
        this.currentTheme = new LightTheme();
        this.listeners = new ArrayList<>();
    }

    public ThemeManager(Theme theme) {
        this.currentTheme = theme;
        this.listeners = new ArrayList<>();
    }

    public Theme getCurrentTheme() {
        return this.currentTheme;
    }

    public void setTheme(Theme theme) {
        this.currentTheme = theme;
        this.notifyListeners();
    }

    public void toggleTheme() {
        if (this.currentTheme instanceof LightTheme) {
            this.currentTheme = new DarkTheme();
        } else {
            this.currentTheme = new LightTheme();
        }
        this.notifyListeners();
    }

    public void addListener(ConsumerFunction<Theme> listener) {
        this.listeners.add(listener);
    }

    private void notifyListeners() {
        for (ConsumerFunction<Theme> listener : this.listeners) {
            listener.run(this.currentTheme);
        }
    }
}
