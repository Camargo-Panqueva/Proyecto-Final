package view.themes;

import java.awt.*;

/**
 * Represents a light theme object that can be applied to the game view.
 * <p>
 * This class represents a light theme object that can be applied to the game view.
 * It provides a structure for storing theme properties such as colors and fonts.
 * The light theme object can be applied to the game view to change its appearance.
 * </p>
 */
public final class DarkTheme extends Theme {

    private final static Color background = new Color(0x272727);

    private final static Color backgroundBright = new Color(0x191919);

    private final static Color backgroundDimmed = new Color(0x50464646, true);

    private final static Color foreground = new Color(0xF0F0F0);

    private final static Color foregroundBright = new Color(0xFFFFFF);

    private final static Color foregroundDimmed = new Color(0x50E2E2E2, true);

    private final static Color primary = new Color(210, 196, 172, 255);

    private final static Color primaryBright = new Color(228, 219, 205, 255);

    private final static Color primaryDimmed = new Color(210, 196, 172, 89);

    private final static Color red = new Color(239, 143, 143, 255);

    private final static Color redBright = new Color(245, 188, 188, 255);

    private final static Color redDimmed = new Color(239, 143, 143, 89);

    private final static Color yellow = new Color(242, 242, 166, 255);

    private final static Color yellowBright = new Color(249, 249, 210, 255);

    private final static Color yellowDimmed = new Color(242, 242, 166, 89);

    private final static Color green = new Color(166, 242, 166, 255);

    private final static Color greenBright = new Color(210, 249, 210, 255);

    private final static Color greenDimmed = new Color(166, 242, 166, 89);

    private final static Color blue = new Color(103, 179, 233, 255);

    private final static Color blueBright = new Color(148, 201, 240, 255);

    private final static Color blueDimmed = new Color(103, 179, 233, 89);

    private final static Color purple = new Color(202, 143, 239, 255);

    private final static Color purpleBright = new Color(223, 188, 245, 255);

    private final static Color purpleDimmed = new Color(202, 143, 239, 89);

    private final static Color pink = new Color(242, 166, 226, 255);

    private final static Color pinkBright = new Color(249, 210, 240, 255);

    private final static Color pinkDimmed = new Color(242, 166, 226, 89);


    /**
     * Creates a new LightTheme with the default colors.
     * <p>
     * This constructor creates a new LightTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public DarkTheme() {
        super(
                primary,
                primaryBright,
                primaryDimmed,
                background,
                backgroundBright,
                backgroundDimmed,
                foreground,
                foregroundBright,
                foregroundDimmed,
                red,
                redBright,
                redDimmed,
                green,
                greenBright,
                greenDimmed,
                blue,
                blueBright,
                blueDimmed,
                purple,
                purpleBright,
                purpleDimmed
        );
    }
}