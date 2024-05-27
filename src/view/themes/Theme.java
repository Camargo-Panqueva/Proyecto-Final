package view.themes;

import org.json.JSONObject;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a theme object that can be applied to the game view.
 * <p>
 * This class represents a theme object that can be applied to the game view.
 * It provides a structure for storing theme properties such as colors and fonts.
 * The theme object can be applied to the game view to change its appearance.
 * </p>
 */
public abstract class Theme {

    private final HashMap<ColorName, HashMap<ColorVariant, Color>> colors;
    private final String name;
    private final ThemeType type;

    /**
     * Creates a new Theme with the given colors.
     *
     * @param name             the name of the theme.
     * @param type             the type of the theme.
     * @param primary          the primary color of the theme.
     * @param primaryBright    the primary contrast color of the theme.
     * @param primaryDimmed    the secondary color of the theme.
     * @param background       the background color of the theme.
     * @param backgroundBright the background contrast color of the theme.
     * @param backgroundDimmed the background contrast color of the theme.
     * @param foreground       the foreground color of the theme.
     * @param foregroundBright the foreground contrast color of the theme.
     * @param foregroundDimmed the foreground contrast color of the theme.
     * @param black            the black color of the theme.
     * @param blackBright      the black contrast color of the theme.
     * @param blackDimmed      the black contrast color of the theme.
     * @param white            the white color of the theme.
     * @param whiteBright      the white contrast color of the theme.
     * @param whiteDimmed      the white contrast color of the theme.
     * @param red              the red color of the theme.
     * @param redBright        the red contrast color of the theme.
     * @param redDimmed        the red contrast color of the theme.
     * @param yellow           the yellow color of the theme.
     * @param yellowBright     the yellow contrast color of the theme.
     * @param yellowDimmed     the yellow contrast color of the theme.
     * @param green            the green color of the theme.
     * @param greenBright      the green contrast color of the theme.
     * @param greenDimmed      the green contrast color of the theme.
     * @param cyan             the cyan color of the theme.
     * @param cyanBright       the cyan contrast color of the theme.
     * @param cyanDimmed       the cyan contrast color of the theme.
     * @param blue             the blue color of the theme.
     * @param blueBright       the blue contrast color of the theme.
     * @param blueDimmed       the blue contrast color of the theme.
     * @param magenta          the magenta color of the theme.
     * @param magentaBright    the magenta contrast color of the theme.
     * @param magentaDimmed    the magenta contrast color of the theme.
     */
    public Theme(
            String name,
            ThemeType type,
            Color primary,
            Color primaryBright,
            Color primaryDimmed,
            Color background,
            Color backgroundBright,
            Color backgroundDimmed,
            Color foreground,
            Color foregroundBright,
            Color foregroundDimmed,
            Color white,
            Color whiteBright,
            Color whiteDimmed,
            Color black,
            Color blackBright,
            Color blackDimmed,
            Color red,
            Color redBright,
            Color redDimmed,
            Color yellow,
            Color yellowBright,
            Color yellowDimmed,
            Color green,
            Color greenBright,
            Color greenDimmed,
            Color cyan,
            Color cyanBright,
            Color cyanDimmed,
            Color blue,
            Color blueBright,
            Color blueDimmed,
            Color magenta,
            Color magentaBright,
            Color magentaDimmed
    ) {

        this.colors = new HashMap<>();
        this.name = name;
        this.type = type;

        this.colors.put(ColorName.PRIMARY, new HashMap<>() {{
            put(ColorVariant.NORMAL, primary);
            put(ColorVariant.BRIGHT, primaryBright);
            put(ColorVariant.DIMMED, primaryDimmed);
        }});

        this.colors.put(ColorName.BACKGROUND, new HashMap<>() {{
            put(ColorVariant.NORMAL, background);
            put(ColorVariant.BRIGHT, backgroundBright);
            put(ColorVariant.DIMMED, backgroundDimmed);
        }});

        this.colors.put(ColorName.FOREGROUND, new HashMap<>() {{
            put(ColorVariant.NORMAL, foreground);
            put(ColorVariant.BRIGHT, foregroundBright);
            put(ColorVariant.DIMMED, foregroundDimmed);
        }});

        this.colors.put(ColorName.BLACK, new HashMap<>() {{
            put(ColorVariant.NORMAL, black);
            put(ColorVariant.BRIGHT, blackBright);
            put(ColorVariant.DIMMED, blackDimmed);
        }});

        this.colors.put(ColorName.WHITE, new HashMap<>() {{
            put(ColorVariant.NORMAL, white);
            put(ColorVariant.BRIGHT, whiteBright);
            put(ColorVariant.DIMMED, whiteDimmed);
        }});

        this.colors.put(ColorName.RED, new HashMap<>() {{
            put(ColorVariant.NORMAL, red);
            put(ColorVariant.BRIGHT, redBright);
            put(ColorVariant.DIMMED, redDimmed);
        }});

        this.colors.put(ColorName.YELLOW, new HashMap<>() {{
            put(ColorVariant.NORMAL, yellow);
            put(ColorVariant.BRIGHT, yellowBright);
            put(ColorVariant.DIMMED, yellowDimmed);
        }});

        this.colors.put(ColorName.GREEN, new HashMap<>() {{
            put(ColorVariant.NORMAL, green);
            put(ColorVariant.BRIGHT, greenBright);
            put(ColorVariant.DIMMED, greenDimmed);
        }});

        this.colors.put(ColorName.CYAN, new HashMap<>() {{
            put(ColorVariant.NORMAL, cyan);
            put(ColorVariant.BRIGHT, cyanBright);
            put(ColorVariant.DIMMED, cyanDimmed);
        }});

        this.colors.put(ColorName.BLUE, new HashMap<>() {{
            put(ColorVariant.NORMAL, blue);
            put(ColorVariant.BRIGHT, blueBright);
            put(ColorVariant.DIMMED, blueDimmed);
        }});

        this.colors.put(ColorName.MAGENTA, new HashMap<>() {{
            put(ColorVariant.NORMAL, magenta);
            put(ColorVariant.BRIGHT, magentaBright);
            put(ColorVariant.DIMMED, magentaDimmed);
        }});

        this.colors.put(ColorName.TRANSPARENT, new HashMap<>() {{
            put(ColorVariant.NORMAL, new Color(0, 0, 0, 0));
            put(ColorVariant.BRIGHT, new Color(0, 0, 0, 0));
            put(ColorVariant.DIMMED, new Color(0, 0, 0, 0));
        }});
    }

    public static Theme fromJson(JSONObject json) {
        String themeName = json.getString("name");
        ThemeType themeType = ThemeType.valueOf(json.getString("type").toUpperCase());
        JSONObject colors = json.getJSONObject("colors");

        Color primaryNormal = getColorFromJson(colors, "primary", "normal");
        Color primaryBright = getColorFromJson(colors, "primary", "bright");
        Color primaryDimmed = getColorFromJson(colors, "primary", "dimmed");

        Color backgroundNormal = getColorFromJson(colors, "background", "normal");
        Color backgroundBright = getColorFromJson(colors, "background", "bright");
        Color backgroundDimmed = getColorFromJson(colors, "background", "dimmed");

        Color foregroundNormal = getColorFromJson(colors, "foreground", "normal");
        Color foregroundBright = getColorFromJson(colors, "foreground", "bright");
        Color foregroundDimmed = getColorFromJson(colors, "foreground", "dimmed");

        Color whiteNormal = getColorFromJson(colors, "white", "normal");
        Color whiteBright = getColorFromJson(colors, "white", "bright");
        Color whiteDimmed = getColorFromJson(colors, "white", "dimmed");

        Color blackNormal = getColorFromJson(colors, "black", "normal");
        Color blackBright = getColorFromJson(colors, "black", "bright");
        Color blackDimmed = getColorFromJson(colors, "black", "dimmed");

        Color redNormal = getColorFromJson(colors, "red", "normal");
        Color redBright = getColorFromJson(colors, "red", "bright");
        Color redDimmed = getColorFromJson(colors, "red", "dimmed");

        Color yellowNormal = getColorFromJson(colors, "yellow", "normal");
        Color yellowBright = getColorFromJson(colors, "yellow", "bright");
        Color yellowDimmed = getColorFromJson(colors, "yellow", "dimmed");

        Color greenNormal = getColorFromJson(colors, "green", "normal");
        Color greenBright = getColorFromJson(colors, "green", "bright");
        Color greenDimmed = getColorFromJson(colors, "green", "dimmed");

        Color cyanNormal = getColorFromJson(colors, "cyan", "normal");
        Color cyanBright = getColorFromJson(colors, "cyan", "bright");
        Color cyanDimmed = getColorFromJson(colors, "cyan", "dimmed");

        Color blueNormal = getColorFromJson(colors, "blue", "normal");
        Color blueBright = getColorFromJson(colors, "blue", "bright");
        Color blueDimmed = getColorFromJson(colors, "blue", "dimmed");

        Color magentaNormal = getColorFromJson(colors, "magenta", "normal");
        Color magentaBright = getColorFromJson(colors, "magenta", "bright");
        Color magentaDimmed = getColorFromJson(colors, "magenta", "dimmed");

        return new Theme(
                themeName,
                themeType,
                primaryNormal,
                primaryBright,
                primaryDimmed,
                backgroundNormal,
                backgroundBright,
                backgroundDimmed,
                foregroundNormal,
                foregroundBright,
                foregroundDimmed,
                whiteNormal,
                whiteBright,
                whiteDimmed,
                blackNormal,
                blackBright,
                blackDimmed,
                redNormal,
                redBright,
                redDimmed,
                yellowNormal,
                yellowBright,
                yellowDimmed,
                greenNormal,
                greenBright,
                greenDimmed,
                cyanNormal,
                cyanBright,
                cyanDimmed,
                blueNormal,
                blueBright,
                blueDimmed,
                magentaNormal,
                magentaBright,
                magentaDimmed
        ) {
        };
    }

    private static Color parseHexStringToColor(String hex) {
        if (hex.matches("^#[0-9A-Fa-f]{8}$")) {
            return new Color(new BigInteger(hex.substring(1), 16).intValue(), true);
        } else {
            throw new IllegalArgumentException("Invalid hex color format: " + hex);
        }
    }

    private static Color getColorFromJson(JSONObject colors, String colorName, String variant) {
        return parseHexStringToColor(colors.getJSONObject(colorName).getString(variant));
    }

    /**
     * Gets the color of the given name and variant.
     *
     * @param colorName    the name of the color.
     * @param colorVariant the variant of the color.
     * @return the color of the given name and variant.
     */
    public Color getColor(ColorName colorName, ColorVariant colorVariant) {
        return colors.get(colorName).get(colorVariant);
    }

    /**
     * Gets the color of the given theme color.
     *
     * @param themeColor the theme color.
     * @return the color of the given theme color.
     */
    public Color getColor(ThemeColor themeColor) {
        return colors.get(themeColor.name()).get(themeColor.variant());
    }

    public ArrayList<ColorName> getPlayerColors() {
        ArrayList<ColorName> colorList = new ArrayList<>();

        colorList.add(ColorName.RED);
        colorList.add(ColorName.YELLOW);
        colorList.add(ColorName.GREEN);
        colorList.add(ColorName.CYAN);
        colorList.add(ColorName.BLUE);
        colorList.add(ColorName.MAGENTA);

        return colorList;
    }

    /**
     * Gets the palette colors of the theme.
     * <p>
     * This method gets the palette colors of the theme.
     * It returns a list of colors that can be used for the game palette.
     * </p>
     *
     * @return the palette colors of the theme.
     */
    public ArrayList<ColorName> getPaletteColors() {
        ArrayList<ColorName> colorList = new ArrayList<>();

        colorList.add(ColorName.PRIMARY);
        colorList.add(ColorName.RED);
        colorList.add(ColorName.YELLOW);
        colorList.add(ColorName.GREEN);
        colorList.add(ColorName.CYAN);
        colorList.add(ColorName.BLUE);
        colorList.add(ColorName.MAGENTA);
        colorList.add(ColorName.BLACK);
        colorList.add(ColorName.WHITE);

        return colorList;
    }

    public String getName() {
        return this.name;
    }

    public ThemeType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        // Change all non-alphanumeric characters to spaces and limit to 20 characters, then capitalize the first letter
        return this.name.
                replaceAll("[^a-zA-Z0-9]", " ")
                .substring(0, Math.min(this.name.length(), 17))
                .toLowerCase()
                .replaceFirst("^[a-z]", String.valueOf(this.name.charAt(0)).toUpperCase());
    }

    public enum ThemeType {
        LIGHT,
        DARK
    }
}