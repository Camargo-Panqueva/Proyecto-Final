package view.themes;

public final class ThemeColor {

    private final ColorName name;
    private final ColorVariant variant;

    public ThemeColor(ColorName colorName, ColorVariant colorVariant) {
        this.name = colorName;
        this.variant = colorVariant;
    }

    public ColorName name() {
        return this.name;
    }

    public ColorVariant variant() {
        return this.variant;
    }

    public enum ColorName {
        PRIMARY("Primary"),
        BACKGROUND("Background"),
        FOREGROUND("Foreground"),
        RED("Red"),
        GREEN("Green"),
        BLUE("Blue"),
        PURPLE("Purple"),
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

    public enum ColorVariant {
        NORMAL,
        BRIGHT,
        DIMMED
    }
}
