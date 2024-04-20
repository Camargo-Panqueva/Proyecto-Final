package graphics.menubar;

import javax.swing.*;
import java.awt.event.ActionListener;

public final class MenuBar extends JMenuBar {

    public MenuBar() {
        this.setupMenu();
    }

    private void setupMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("Help");

        JMenuItem openItem = createMenuItem("Open", Handlers.openHandler());
        JMenuItem saveItem = createMenuItem("Save", Handlers.saveHandler());
        JMenuItem exitItem = createMenuItem("Exit", Handlers.exitHandler());
        JMenuItem aboutItem = createMenuItem("About", Handlers.aboutHandler());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        aboutMenu.add(aboutItem);

        this.add(fileMenu);
        this.add(aboutMenu);
    }

    private JMenuItem createMenuItem(String name, ActionListener action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        return item;
    }
}
