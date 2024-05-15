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
     * The red color of the theme.
     */
    public final Color red;

    /**
     * The red bright color of the theme.
     */
    public final Color redBright;

    /**
     * The red dimmed color of the theme.
     */
    public final Color redDimmed;

    /**
     * The green color of the theme.
     */
    public final Color green;

    /**
     * The green bright color of the theme.
     */
    public final Color greenBright;

    /**
     * The green dimmed color of the theme.
     */
    public final Color greenDimmed;

    /**
     * The blue color of the theme.
     */
    public final Color blue;

    /**
     * The blue bright color of the theme.
     */
    public final Color blueBright;

    /**
     * The blue dimmed color of the theme.
     */
    public final Color blueDimmed;

    /**
     * The purple color of the theme.
     */
    public final Color purple;

    /**
     * The purple bright color of the theme.
     */
    public final Color purpleBright;

    /**
     * The purple dimmed color of the theme.
     */
    public final Color purpleDimmed;


    /**
     * Creates a new Theme with the given colors.
     *
     * @param primary          the primary color of the theme.
     * @param primaryDimmed    the secondary color of the theme.
     * @param background       the background color of the theme.
     * @param backgroundDimmed the background contrast color of the theme.
     * @param foreground       the foreground color of the theme.
     */
    public Theme(
            Color primary,
            Color primaryBright,
            Color primaryDimmed,
            Color background,
            Color backgroundBright,
            Color backgroundDimmed,
            Color foreground,
            Color foregroundBright,
            Color foregroundDimmed,
            Color red,
            Color redBright,
            Color redDimmed,
            Color green,
            Color greenBright,
            Color greenDimmed,
            Color blue,
            Color blueBright,
            Color blueDimmed,
            Color purple,
            Color purpleBright,
            Color purpleDimmed) {

        this.primary = primary;
        this.primaryBright = primaryBright;
        this.primaryDimmed = primaryDimmed;

        this.background = background;
        this.backgroundBright = backgroundBright;
        this.backgroundDimmed = backgroundDimmed;

        this.foreground = foreground;
        this.foregroundBright = foregroundBright;
        this.foregroundDimmed = foregroundDimmed;

        this.red = red;
        this.redBright = redBright;
        this.redDimmed = redDimmed;

        this.green = green;
        this.greenBright = greenBright;
        this.greenDimmed = greenDimmed;

        this.blue = blue;
        this.blueBright = blueBright;
        this.blueDimmed = blueDimmed;

        this.purple = purple;
        this.purpleBright = purpleBright;
        this.purpleDimmed = purpleDimmed;
    }
}