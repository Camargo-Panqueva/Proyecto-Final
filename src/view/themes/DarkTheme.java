package view.themes;

import java.awt.*;

/**
 * Represents a dark theme object that can be applied to the game view.
 * <p>
 * This class represents a dark theme object that can be applied to the game view.
 * It provides a structure for storing theme properties such as colors and fonts.
 * The dark theme object can be applied to the game view to change its appearance.
 * </p>
 */
public final class DarkTheme extends Theme {

    /**
     * See {@link Theme#primaryColor}.
     */
    private final static Color primaryColor = new Color(0xD75A70);

    /**
     * See {@link Theme#secondaryColor}.
     */
    private final static Color secondaryColor = new Color(0x59D75A70, true);

    /**
     * See {@link Theme#backgroundColor}.
     */
    private final static Color backgroundColor = new Color(0x262626);

    /**
     * See {@link Theme#backgroundContrastColor}.
     */
    private final static Color backgroundContrastColor = new Color(0x1A1A1A);

    /**
     * See {@link Theme#foregroundColor}.
     */
    private final static Color foregroundColor = new Color(0xF0F0F0);

    /**
     * Creates a new DarkTheme with the default colors.
     * <p>
     * This constructor creates a new DarkTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public DarkTheme() {
        super(primaryColor, secondaryColor, backgroundColor, backgroundContrastColor, foregroundColor);
    }
}
