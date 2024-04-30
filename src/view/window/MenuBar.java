package view.window;

import controller.handlers.MenuHandlers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Represents a menu bar object that can be added to the game window.
 * <p>
 * This class represents a menu bar object that can be added to the game window.
 * It provides a structure for adding menu items to the menu bar.
 * </p>
 */
public final class MenuBar extends JMenuBar {

    /**
     * Creates a new MenuBar with the default menu items.
     */
    public MenuBar() {
        this.setupMenu();
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 30));
    }

    /**
     * Sets up the menu items for the menu bar.
     * <p>
     * This method sets up the menu items for the menu bar.
     * It creates the menu items and adds them to the menu bar.
     * The menu items are created with the appropriate action listeners.
     * </p>
     */
    private void setupMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JMenu aboutMenu = new JMenu("Help");
        aboutMenu.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        //TODO: Replace this static methods with controller getters
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

    /**
     * Creates a new menu item with the given name and action listener.
     * <p>
     * This method creates a new menu item with the given name and action listener.
     * It creates a new JMenuItem object with the given name and action listener.
     * The menu item is created with a hand cursor.
     * </p>
     *
     * @param name   the name of the menu item.
     * @param action the action listener for the menu item.
     * @return the created menu item.
     */
    private JMenuItem createMenuItem(String name, ActionListener action) {
        JMenuItem item = new JMenuItem(name);
        item.addActionListener(action);
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return item;
    }
}
