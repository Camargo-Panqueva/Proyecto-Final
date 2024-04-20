package graphics.menubar;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class Handlers {

    public static ActionListener openHandler() {
        return event -> System.out.println("Open clicked");
    }

    public static ActionListener saveHandler() {
        return event -> System.out.println("Save clicked");
    }

    public static ActionListener exitHandler() {
        return event -> System.exit(0);
    }

    public static ActionListener aboutHandler() {
        return event -> JOptionPane.showMessageDialog(
                null,
                "This is a simple quoridor game",
                "About",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
