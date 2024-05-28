package view.window;

import controller.GameController;
import controller.dto.ServiceResponse;
import util.Logger;

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

    /**
     * Sets up the file filter for the file chooser.
     * <p>
     * This method creates a new FileFilter object and assigns it to the fileFilter field.
     * The created FileFilter object overrides two methods:
     * - accept(File f): This method checks if the file should be accepted by the filter.
     * It accepts files that have a name ending with ".qpg" or directories.
     * - getDescription(): This method returns the description of the files that the filter accepts.
     * </p>
     */
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

    /**
     * Handles the action event for saving a file.
     * <p>
     * This method is triggered when the user selects the "Save" option in the file menu.
     * It opens a file chooser dialog with a custom file filter that accepts only ".qpg" files.
     * If the user approves the file selection, it gets the selected file and appends ".qpg" to the file name if it doesn't already end with ".qpg".
     * Then, it attempts to save the current match to the selected file path using the game controller.
     * If the save operation fails, it logs an error message and shows an error dialog with the failure message.
     * If the save operation is successful, it logs a success message and shows a success dialog.
     * </p>
     *
     * @param event the action event that triggered this method
     */
    private void saveFileHandler(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(this.fileFilter);

        int option = fileChooser.showSaveDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            String path = file.getPath() + (file.getPath().endsWith(".qpg") ? "" : ".qpg");

            ServiceResponse<Void> response = this.controller.saveMatch(path);

            if (!response.ok) {
                Logger.error("Failed to save match: " + response.message);
                JOptionPane.showMessageDialog(null, response.message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Logger.success(String.format("Match saved successfully to \"%s\"", path));
            JOptionPane.showMessageDialog(null, "Match saved successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Handles the action event for opening a file.
     * <p>
     * This method is triggered when the user selects the "Open" option in the file menu.
     * It opens a file chooser dialog with a custom file filter that accepts only ".qpg" files.
     * If the user approves the file selection, it gets the selected file and attempts to load the match from the selected file path using the game controller.
     * If the load operation fails, it logs an error message.
     * If the load operation is successful, it logs a success message.
     * </p>
     *
     * @param event the action event that triggered this method
     */
    private void openFileHandler(ActionEvent event) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(this.fileFilter);
        int option = fileChooser.showOpenDialog(this);

        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            ServiceResponse<Void> response = this.controller.loadMatch(file.getAbsolutePath());

            if (!response.ok) {
                Logger.error("Failed to load match: " + response.message);
                return;
            }

            Logger.success(String.format("Match \"%s\" loaded successfully", file.getName()));
        }
    }

    /**
     * Handles the action event for exiting the game.
     * <p>
     * This method is triggered when the user selects the "Exit" option in the file menu.
     * It logs a message and exits the game with a status code of 0.
     * </p>
     *
     * @param event the action event that triggered this method
     */
    private void exitHandler(ActionEvent event) {
        Logger.info("Exiting game from exit option in menu bar. Goodbye!");
        System.exit(0);
    }

    /**
     * Handles the action event for showing about dialog.
     * <p>
     * This method is triggered when the user selects the "About" option in the help menu.
     * It shows an information dialog with the game information.
     * </p>
     *
     * @param event the action event that triggered this method
     */
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
