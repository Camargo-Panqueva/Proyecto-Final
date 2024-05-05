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
     * See {@link Theme#primaryColor}.
     */
    private final static Color primaryColor = new Color(0x642332);

    /**
     * See {@link Theme#secondaryColor}.
     */
    private final static Color secondaryColor = new Color(0x59642332, true);

    /**
     * See {@link Theme#backgroundColor}.
     */
    private final static Color backgroundColor = new Color(0xF0F0F0);

    /**
     * See {@link Theme#backgroundContrastColor}.
     */
    private final static Color backgroundContrastColor = new Color(0xDADADA);

    /**
     * See {@link Theme#foregroundColor}.
     */
    private final static Color foregroundColor = new Color(0x262626);

    /**
     * Creates a new LightTheme with the default colors.
     * <p>
     * This constructor creates a new LightTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public LightTheme() {
        super(primaryColor, secondaryColor, backgroundColor, backgroundContrastColor, foregroundColor);
    }
}