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

    /**
     * See {@link Theme#primary}.
     */
    private final static Color primary = new Color(0xE8626B);

    /**
     * See {@link Theme#primaryBright}.
     */
    private final static Color primaryBright = new Color(0xE54C56);

    /**
     * See {@link Theme#primaryDimmed}.
     */
    private final static Color primaryDimmed = new Color(0x40E8626B, true);

    /**
     * See {@link Theme#background}.
     */
    private final static Color background = new Color(0x272727);

    /**
     * See {@link Theme#backgroundBright}.
     */
    private final static Color backgroundBright = new Color(0x191919);

    /**
     * See {@link Theme#backgroundDimmed}.
     */
    private final static Color backgroundDimmed = new Color(0x333333);

    /**
     * See {@link Theme#foreground}.
     */
    private final static Color foreground = new Color(0xF0F0F0);

    /**
     * See {@link Theme#foregroundBright}.
     */
    private final static Color foregroundBright = new Color(0xFFFFFF);

    /**
     * See {@link Theme#foregroundDimmed}.
     */
    private final static Color foregroundDimmed = new Color(0xE2E2E2);

    /**
     * See {@link Theme#red}.
     */
    private final static Color red = new Color(0xF2A6A6);

    /**
     * See {@link Theme#redBright}.
     */
    private final static Color redBright = new Color(0xF7D4D4);

    /**
     * See {@link Theme#redDimmed}.
     */
    private final static Color redDimmed = new Color(0xD19494);

    /**
     * See {@link Theme#green}.
     */
    private final static Color green = new Color(0xCCF2A6);

    /**
     * See {@link Theme#greenBright}.
     */
    private final static Color greenBright = new Color(0xE6F7D4);

    /**
     * See {@link Theme#greenDimmed}.
     */
    private final static Color greenDimmed = new Color(0xB3D194);

    /**
     * See {@link Theme#blue}.
     */
    private final static Color blue = new Color(0xA6F2F2);

    /**
     * See {@link Theme#blueBright}.
     */
    private final static Color blueBright = new Color(0xD4F7F7);

    /**
     * See {@link Theme#blueDimmed}.
     */
    private final static Color blueDimmed = new Color(0x94D1D1);

    /**
     * See {@link Theme#purple}.
     */
    private final static Color purple = new Color(0xCCA6F2);

    /**
     * See {@link Theme#purpleBright}.
     */
    private final static Color purpleBright = new Color(0xE6D4F7);

    /**
     * See {@link Theme#purpleDimmed}.
     */
    private final static Color purpleDimmed = new Color(0xB394D1);

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