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

    private final static Color black = new Color(242, 233, 225, 255);
    private final static Color blackBright = new Color(152, 147, 165, 255);
    private final static Color blackDimmed = new Color(230, 213, 198, 140);
    private final static Color red = new Color(180, 99, 122, 255);
    private final static Color redBright = new Color(180, 99, 122, 255);
    private final static Color redDimmed = new Color(163, 78, 103, 140);
    private final static Color green = new Color(40, 105, 131, 255);
    private final static Color greenBright = new Color(40, 105, 131, 255);
    private final static Color greenDimmed = new Color(81, 124, 141, 89);
    private final static Color yellow = new Color(234, 157, 52, 255);
    private final static Color yellowBright = new Color(234, 157, 52, 255);
    private final static Color yellowDimmed = new Color(225, 137, 23, 140);
    private final static Color blue = new Color(86, 148, 159, 255);
    private final static Color blueBright = new Color(86, 148, 159, 255);
    private final static Color blueDimmed = new Color(119, 169, 177, 89);
    private final static Color magenta = new Color(144, 122, 169, 255);
    private final static Color magentaBright = new Color(144, 122, 169, 255);
    private final static Color magentaDimmed = new Color(125, 99, 153, 140);
    private final static Color cyan = new Color(215, 130, 126, 255);
    private final static Color cyanBright = new Color(215, 130, 126, 255);
    private final static Color cyanDimmed = new Color(206, 100, 97, 140);
    private final static Color white = new Color(87, 82, 121, 255);
    private final static Color whiteBright = new Color(87, 82, 121, 255);
    private final static Color whiteDimmed = new Color(115, 111, 143, 89);
    private final static Color background = new Color(250, 244, 237, 255);
    private final static Color backgroundBright = new Color(255, 255, 255, 255);
    private final static Color backgroundDimmed = new Color(242, 226, 207, 140);
    private final static Color foreground = new Color(87, 82, 121, 255);
    private final static Color foregroundBright = new Color(77, 74, 96, 255);
    private final static Color foregroundDimmed = new Color(115, 111, 143, 89);
    private final static Color primary = new Color(87, 82, 121, 255);
    private final static Color primaryBright = new Color(77, 74, 96, 255);
    private final static Color primaryDimmed = new Color(115, 111, 143, 89);

    /**
     * Creates a new LightTheme with the default colors.
     * <p>
     * This constructor creates a new LightTheme with the default colors.
     * The default colors are defined as constants in the class.
     * </p>
     */
    public LightTheme() {
        super(
                "Light",
                ThemeType.LIGHT,
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