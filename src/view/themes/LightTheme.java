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

    /**
     * See {@link Theme#primary}.
     */
    private final static Color primary = new Color(0x5F1F2E);

    /**
     * See {@link Theme#primaryBright}.
     */
    private final static Color primaryBright = new Color(0x852C41);

    /**
     * See {@link Theme#primaryDimmed}.
     */
    private final static Color primaryDimmed = new Color(0x905F1F2E, true);

    /**
     * See {@link Theme#background}.
     */
    private final static Color background = new Color(0xF0F0F0);

    /**
     * See {@link Theme#backgroundBright}.
     */
    private final static Color backgroundBright = new Color(0xFFFFFF);

    /**
     * See {@link Theme#backgroundDimmed}.
     */
    private final static Color backgroundDimmed = new Color(0xE2E2E2);

    /**
     * See {@link Theme#foreground}.
     */
    private final static Color foreground = new Color(0x272727);

    /**
     * See {@link Theme#foregroundBright}.
     */
    private final static Color foregroundBright = new Color(0x333333);

    /**
     * See {@link Theme#foregroundDimmed}.
     */
    private final static Color foregroundDimmed = new Color(0x191919);

    /**
     * Creates a new LightTheme with the default colors.
     * <p>
     * This constructor creates a new LightTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public LightTheme() {
        super(primary, primaryBright, primaryDimmed, background, backgroundBright, backgroundDimmed, foreground, foregroundBright, foregroundDimmed);
    }
}