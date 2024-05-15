package view.themes;

import java.awt.*;

/**
 * Represents a theme object that can be applied to the game view.
 * <p>
 * This class represents a theme object that can be applied to the game view.
 * It provides a structure for storing theme properties such as colors and fonts.
 * The theme object can be applied to the game view to change its appearance.
 * </p>
 */
public abstract class Theme {

    /**
     * The primary color of the theme.
     */
    public final Color primary;

    /**
     * The primary bright color of the theme.
     */
    public final Color primaryBright;

    /**
     * The primary dimmed color of the theme.
     */
    public final Color primaryDimmed;

    /**
     * The background color of the theme.
     */
    public final Color background;

    /**
     * The background bright color of the theme.
     */
    public final Color backgroundBright;

    /**
     * The background dimmed color of the theme.
     */
    public final Color backgroundDimmed;

    /**
     * The foreground color of the theme.
     */
    public final Color foreground;

    /**
     * The foreground bright color of the theme.
     */
    public final Color foregroundBright;

    /**
     * The foreground dimmed color of the theme.
     */
    public final Color foregroundDimmed;

    /**
     * Creates a new Theme with the given colors.
     *
     * @param primary          the primary color of the theme.
     * @param primaryDimmed    the secondary color of the theme.
     * @param background       the background color of the theme.
     * @param backgroundDimmed the background contrast color of the theme.
     * @param foreground       the foreground color of the theme.
     */
    public Theme(Color primary, Color primaryBright, Color primaryDimmed, Color background, Color backgroundBright, Color backgroundDimmed, Color foreground, Color foregroundBright, Color foregroundDimmed) {
        this.primary = primary;
        this.primaryBright = primaryBright;
        this.primaryDimmed = primaryDimmed;

        this.background = background;
        this.backgroundBright = backgroundBright;
        this.backgroundDimmed = backgroundDimmed;

        this.foreground = foreground;
        this.foregroundBright = foregroundBright;
        this.foregroundDimmed = foregroundDimmed;
    }
}