package view.themes;

import java.awt.*;

public abstract class Theme {

    public final Color primaryColor;
    public final Color secondaryColor;

    public final Color backgroundColor;
    public final Color foregroundColor;

    public Theme(Color primaryColor, Color secondaryColor, Color backgroundColor, Color foregroundColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }
}