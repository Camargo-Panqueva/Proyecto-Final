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

    private final static Color primary = new Color(0x5F1F2E);

    private final static Color primaryBright = new Color(0x852C41);

    private final static Color primaryDimmed = new Color(0x905F1F2E, true);

    private final static Color background = new Color(0xF0F0F0);

    private final static Color backgroundBright = new Color(0xFFFFFF);

    private final static Color backgroundDimmed = new Color(0x90BBBBBB, true);

    private final static Color foreground = new Color(0x272727);

    private final static Color foregroundBright = new Color(0x333333);

    private final static Color foregroundDimmed = new Color(0x40191919, true);

    private final static Color red = new Color(0xBF4040);

    private final static Color redBright = new Color(0xD98C8C);

    private final static Color redDimmed = new Color(0x40732626, true);

    private final static Color green = new Color(0x80BF40);

    private final static Color greenBright = new Color(0xB3D98C);

    private final static Color greenDimmed = new Color(0x404D7326, true);

    private final static Color blue = new Color(0x40BFBF);

    private final static Color blueBright = new Color(0x8CD9D9);

    private final static Color blueDimmed = new Color(0x40267373, true);

    private final static Color purple = new Color(0x8040BF);

    private final static Color purpleBright = new Color(0xB38CD9);

    private final static Color purpleDimmed = new Color(0x404D2673, true);

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