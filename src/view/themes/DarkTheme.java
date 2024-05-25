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

    private final static Color black = new Color(50, 52, 74, 255);
    private final static Color blackBright = new Color(68, 75, 106, 255);
    private final static Color blackDimmed = new Color(82, 83, 93, 89);
    private final static Color red = new Color(247, 118, 142, 255);
    private final static Color redBright = new Color(255, 122, 147, 255);
    private final static Color redDimmed = new Color(244, 70, 105, 140);
    private final static Color green = new Color(158, 206, 106, 255);
    private final static Color greenBright = new Color(185, 242, 124, 255);
    private final static Color greenDimmed = new Color(135, 193, 68, 140);
    private final static Color yellow = new Color(224, 175, 104, 255);
    private final static Color yellowBright = new Color(255, 158, 100, 255);
    private final static Color yellowDimmed = new Color(215, 151, 62, 140);
    private final static Color blue = new Color(122, 162, 247, 255);
    private final static Color blueBright = new Color(125, 166, 255, 255);
    private final static Color blueDimmed = new Color(74, 131, 244, 140);
    private final static Color magenta = new Color(173, 142, 230, 255);
    private final static Color magentaBright = new Color(187, 154, 247, 255);
    private final static Color magentaDimmed = new Color(142, 100, 221, 140);
    private final static Color cyan = new Color(68, 157, 171, 255);
    private final static Color cyanBright = new Color(13, 185, 215, 255);
    private final static Color cyanDimmed = new Color(103, 176, 187, 89);
    private final static Color white = new Color(120, 124, 153, 255);
    private final static Color whiteBright = new Color(172, 176, 208, 255);
    private final static Color whiteDimmed = new Color(96, 100, 126, 140);
    private final static Color background = new Color(26, 27, 38, 255);
    private final static Color backgroundBright = new Color(15, 15, 16, 255);
    private final static Color backgroundDimmed = new Color(56, 56, 59, 89);
    private final static Color foreground = new Color(169, 177, 214, 255);
    private final static Color foregroundBright = new Color(204, 208, 230, 255);
    private final static Color foregroundDimmed = new Color(134, 146, 198, 140);
    private final static Color primary = new Color(169, 177, 214, 255);
    private final static Color primaryBright = new Color(204, 208, 230, 255);
    private final static Color primaryDimmed = new Color(134, 146, 198, 140);

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
                white,
                whiteBright,
                whiteDimmed,
                black,
                blackBright,
                blackDimmed,
                red,
                redBright,
                redDimmed,
                yellow,
                yellowBright,
                yellowDimmed,
                green,
                greenBright,
                greenDimmed,
                cyan,
                cyanBright,
                cyanDimmed,
                blue,
                blueBright,
                blueDimmed,
                magenta,
                magentaBright,
                magentaDimmed
        );
    }
}