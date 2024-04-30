package view.themes;

import java.awt.*;

public final class LightTheme extends Theme {

    private final static Color primaryColor = new Color(0x642332);
    private final static Color secondaryColor = new Color(0x642332);
    private final static Color backgroundColor = new Color(0xF0F0F0);
    private final static Color backgroundContrastColor = new Color(0xDADADA);
    private final static Color foregroundColor = new Color(0x262626);

    public LightTheme() {
        super(primaryColor, secondaryColor, backgroundColor, backgroundContrastColor, foregroundColor);
    }
}