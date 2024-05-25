package view.themes;

/**
 * Represents a theme color that can be used to style components in the game view.
 * <p>
 * This class represents a theme color that can be used to style components in the game view.
 * It provides a structure for managing color names and variants in the theme.
 * </p>
 */
public final class ThemeColor {

    private final ColorName name;
    private final ColorVariant variant;

    /**
     * Creates a new ThemeColor with the given color name and variant.
     *
     * @param colorName    the name of the color.
     * @param colorVariant the variant of the color.
     */
    public ThemeColor(ColorName colorName, ColorVariant colorVariant) {
        this.name = colorName;
        this.variant = colorVariant;
    }

    /**
     * Gets the name of the color.
     *
     * @return the name of the color.
     */
    public ColorName name() {
        return this.name;
    }

    /**
     * Gets the variant of the color.
     *
     * @return the variant of the color.
     */
    public ColorVariant variant() {
        return this.variant;
    }

    /**
     * Represents the name of a color in the theme.
     * <p>
     * This enum represents the name of a color in the theme.
     * It provides a structure for managing the names of colors in the theme.
     * </p>
     */
    public enum ColorName {
        PRIMARY("Primary"),
        BACKGROUND("Background"),
        FOREGROUND("Foreground"),
        RED("Red"),
        GREEN("Green"),
        YELLOW("Yellow"),
        BLUE("Blue"),
        MAGENTA("Magenta"),
        CYAN("Cyan"),
        WHITE("White"),
        BLACK("Black"),
        TRANSPARENT("Transparent");

        private final String name;

        ColorName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }

    /**
     * Represents the variant of a color in the theme.
     * <p>
     * This enum represents the variant of a color in the theme.
     * It provides a structure for managing the variants of colors in the theme.
     * </p>
     */
    public enum ColorVariant {
        NORMAL,
        BRIGHT,
        DIMMED
    }
}
