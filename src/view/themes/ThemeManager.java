package view.themes;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Represents a theme manager that can be used to manage themes in the game view.
 * <p>
 * This class represents a theme manager that can be used to manage themes in the game view.
 * It provides a structure for managing themes and notifying listeners when the theme changes.
 * </p>
 */
public final class ThemeManager {

    private final ArrayList<Theme> themes;
    private Theme currentTheme;
    private int currentThemeIndex;

    /**
     * Creates a new ThemeManager with the given theme.
     */
    public ThemeManager() {
        this.themes = new ArrayList<>();
        this.loadThemes();
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
        this.currentThemeIndex = (this.currentThemeIndex + 1) % this.themes.size();
        this.currentTheme = this.themes.get(this.currentThemeIndex);
    }

    private void loadThemes() {
        this.themes.add(new LightTheme());
        this.themes.add(new DarkTheme());

        this.currentThemeIndex = 0;
        this.currentTheme = this.themes.get(this.currentThemeIndex);

        try {
            InputStream defaultThemes = getClass().getResourceAsStream("/resources/themes/themes.json");
            Path themesPath = Paths.get("themes.json");
            File themesFile = themesPath.toFile();
            String content = null;

            if (themesFile.exists() && !themesFile.isDirectory() && themesFile.canRead()) {
                content = new String(Files.readAllBytes(themesPath));
                System.out.println("Loaded themes from source directory.");
            } else if (defaultThemes != null) {
                content = new String(defaultThemes.readAllBytes());
                System.out.println("Loaded themes from default resources.");
            } else {
                System.out.println("Failed to load themes from file. Proceeding with default themes.");
                return;
            }

            JSONArray json = new JSONArray(content);

            for (int i = 0; i < json.length(); i++) {
                this.themes.add(Theme.fromJson(json.getJSONObject(i)));
            }
        } catch (IOException e) {
            System.out.println("Failed to load themes from file. Proceeding with default themes. Error: " + e.getMessage());
        }
    }

    public ArrayList<Theme> getThemes() {
        return this.themes;
    }
}
