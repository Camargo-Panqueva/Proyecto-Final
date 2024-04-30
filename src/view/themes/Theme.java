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
    public final Color primaryColor;

    /**
     * The secondary color of the theme.
     */
    public final Color secondaryColor;

    /**
     * The background color of the theme.
     */
    public final Color backgroundColor;

    /**
     * The background contrast color of the theme.
     */
    public final Color backgroundContrastColor;

    /**
     * The foreground color of the theme.
     */
    public final Color foregroundColor;

    /**
     * Creates a new Theme with the given colors.
     *
     * @param primaryColor            the primary color of the theme.
     * @param secondaryColor          the secondary color of the theme.
     * @param backgroundColor         the background color of the theme.
     * @param backgroundContrastColor the background contrast color of the theme.
     * @param foregroundColor         the foreground color of the theme.
     */
    public Theme(Color primaryColor, Color secondaryColor, Color backgroundColor, Color backgroundContrastColor, Color foregroundColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.backgroundContrastColor = backgroundContrastColor;
    }
}