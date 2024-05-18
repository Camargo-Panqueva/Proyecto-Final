package view.themes;

import java.awt.*;
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

    /**
     * Creates a new Theme with the given colors.
     *
     * @param primary          the primary color of the theme.
     * @param primaryDimmed    the secondary color of the theme.
     * @param background       the background color of the theme.
     * @param backgroundDimmed the background contrast color of the theme.
     * @param foreground       the foreground color of the theme.
     */
    public Theme(
            Color primary,
            Color primaryBright,
            Color primaryDimmed,
            Color background,
            Color backgroundBright,
            Color backgroundDimmed,
            Color foreground,
            Color foregroundBright,
            Color foregroundDimmed,
            Color red,
            Color redBright,
            Color redDimmed,
            Color green,
            Color greenBright,
            Color greenDimmed,
            Color blue,
            Color blueBright,
            Color blueDimmed,
            Color purple,
            Color purpleBright,
            Color purpleDimmed) {

        this.colors = new HashMap<>();

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

        this.colors.put(ColorName.RED, new HashMap<>() {{
            put(ColorVariant.NORMAL, red);
            put(ColorVariant.BRIGHT, redBright);
            put(ColorVariant.DIMMED, redDimmed);
        }});

        this.colors.put(ColorName.GREEN, new HashMap<>() {{
            put(ColorVariant.NORMAL, green);
            put(ColorVariant.BRIGHT, greenBright);
            put(ColorVariant.DIMMED, greenDimmed);
        }});

        this.colors.put(ColorName.BLUE, new HashMap<>() {{
            put(ColorVariant.NORMAL, blue);
            put(ColorVariant.BRIGHT, blueBright);
            put(ColorVariant.DIMMED, blueDimmed);
        }});

        this.colors.put(ColorName.PURPLE, new HashMap<>() {{
            put(ColorVariant.NORMAL, purple);
            put(ColorVariant.BRIGHT, purpleBright);
            put(ColorVariant.DIMMED, purpleDimmed);
        }});

        this.colors.put(ColorName.TRANSPARENT, new HashMap<>() {{
            put(ColorVariant.NORMAL, new Color(0, 0, 0, 0));
            put(ColorVariant.BRIGHT, new Color(0, 0, 0, 0));
            put(ColorVariant.DIMMED, new Color(0, 0, 0, 0));
        }});
    }

    public Color getColor(ColorName colorName, ColorVariant colorVariant) {
        return colors.get(colorName).get(colorVariant);
    }

    public ArrayList<Color> getColors() {
        ArrayList<Color> colorList = new ArrayList<>();
        for (HashMap<ColorVariant, Color> colorVariant : colors.values()) {
            colorList.addAll(colorVariant.values());
        }
        return colorList;
    }

    public enum ColorName {
        PRIMARY,
        BACKGROUND,
        FOREGROUND,
        RED,
        GREEN,
        BLUE,
        PURPLE,
        TRANSPARENT
    }

    public enum ColorVariant {
        NORMAL,
        BRIGHT,
        DIMMED
    }
}