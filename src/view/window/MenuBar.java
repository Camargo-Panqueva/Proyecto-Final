package view.window;

import controller.handlers.MenuHandlers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public final class MenuBar extends JMenuBar {

    public MenuBar() {
        this.setupMenu();
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 30));
    }

    private void setupMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JMenu aboutMenu = new JMenu("Help");
        aboutMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JMenuItem openItem = createMenuItem("Open", MenuHandlers.openHandler());
        JMenuItem saveItem = createMenuItem("Save", MenuHandlers.saveHandler());
        JMenuItem exitItem = createMenuItem("Exit", MenuHandlers.exitHandler());
        JMenuItem aboutItem = createMenuItem("About", MenuHandlers.aboutHandler());

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
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return item;
    }
}
