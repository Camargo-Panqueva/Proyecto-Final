package view.window;

import controller.GameController;
import controller.dto.ServiceResponse;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Represents a menu bar object that can be added to the game window.
 * <p>
 * This class represents a menu bar object that can be added to the game window.
 * It provides a structure for adding menu items to the menu bar.
 * </p>
 */
public final class MenuBar extends JMenuBar {

    private final GameController controller;
    private FileFilter fileFilter;

    /**
     * Creates a new MenuBar with the default menu items.
     */
    public MenuBar(GameController controller) {

        this.controller = controller;

        this.setupFilter();
        this.setupMenu();
        this.setPreferredSize(new Dimension(this.getPreferredSize().width, 30));
    }

    private void setupFilter() {
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.getName().toLowerCase().endsWith(".qpg") || f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "QuoriPOOB Game Files (*.qpg)";
            }
        };
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
        JMenuItem openItem = createMenuItem("Open", this::openFileHandler);
        JMenuItem saveItem = createMenuItem("Save", this::saveFileHandler);
        JMenuItem exitItem = createMenuItem("Exit", this::exitHandler);
        JMenuItem aboutItem = createMenuItem("About", this::aboutHandler);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        aboutMenu.add(aboutItem);

        this.add(fileMenu);
        this.add(aboutMenu);
    }

    private void saveFileHandler(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(this.fileFilter);

        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath() + (file.getPath().endsWith(".qpg") ? "" : ".qpg");

            ServiceResponse<Void> response = this.controller.saveMatch(path);

            if (!response.ok) {
                //TODO: Improve error message
                JOptionPane.showMessageDialog(null, response.message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Match saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void openFileHandler(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(this.fileFilter);
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            ServiceResponse<Void> response = this.controller.loadMatch(file.getAbsolutePath());

            if (!response.ok) {
                //TODO: Improve error message
                JOptionPane.showMessageDialog(null, response.message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            JOptionPane.showMessageDialog(null, "Match loaded successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void exitHandler(ActionEvent event) {
        System.exit(0);
    }

    private void aboutHandler(ActionEvent event) {
        JOptionPane.showMessageDialog(
                null,
                "QuoriPOOB Game\nVersion 1.0\n\nDeveloped by:\n- Juan Camargo & Tomas Panqueva",
                "About", JOptionPane.INFORMATION_MESSAGE
        );
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
