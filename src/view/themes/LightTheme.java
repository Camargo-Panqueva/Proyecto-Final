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
public final class LightTheme extends Theme {

    private final static Color background = new Color(0xFFFFFF);

    private final static Color backgroundBright = new Color(0xF0F0F0);

    private final static Color backgroundDimmed = new Color(0x90D7D7D7, true);

    private final static Color foreground = new Color(0x272727);

    private final static Color foregroundBright = new Color(0x333333);

    private final static Color foregroundDimmed = new Color(0x40191919, true);

    private final static Color primary = new Color(185, 168, 141, 255);

    private final static Color primaryBright = new Color(205, 192, 173, 255);

    private final static Color primaryDimmed = new Color(185, 168, 141, 127);

    private final static Color red = new Color(209, 71, 71, 255);

    private final static Color redBright = new Color(219, 112, 112, 255);

    private final static Color redDimmed = new Color(209, 71, 71, 127);

    private final static Color yellow = new Color(232, 232, 48, 255);

    private final static Color yellowBright = new Color(237, 237, 94, 255);

    private final static Color yellowDimmed = new Color(232, 232, 48, 127);

    private final static Color green = new Color(71, 209, 71, 255);

    private final static Color greenBright = new Color(112, 219, 112, 255);

    private final static Color greenDimmed = new Color(71, 209, 71, 127);

    private final static Color blue = new Color(48, 156, 232, 255);

    private final static Color blueBright = new Color(94, 178, 237, 255);

    private final static Color blueDimmed = new Color(48, 156, 232, 127);

    private final static Color purple = new Color(159, 60, 221, 255);

    private final static Color purpleBright = new Color(180, 103, 228, 255);

    private final static Color purpleDimmed = new Color(159, 60, 221, 127);

    private final static Color pink = new Color(232, 48, 192, 255);

    private final static Color pinkBright = new Color(237, 94, 206, 255);

    private final static Color pinkDimmed = new Color(232, 48, 192, 127);

    /**
     * Creates a new LightTheme with the default colors.
     * <p>
     * This constructor creates a new LightTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public LightTheme() {
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