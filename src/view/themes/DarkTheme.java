package view.themes;

import java.awt.*;

public final class DarkTheme extends Theme {

    private final static Color primaryColor = new Color(0xD75A70);
    private final static Color secondaryColor = new Color(0x642332);
    private final static Color backgroundColor = new Color(0x262626);
    private final static Color foregroundColor = new Color(0xF0F0F0);

    public DarkTheme() {
        super(primaryColor, secondaryColor, backgroundColor, foregroundColor);
    }
}
