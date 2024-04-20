package graphics;

import javax.swing.*;

public final class MenuBar extends JMenuBar {

    public MenuBar() {
        this.setupMenu();
    }

    private void setupMenu() {
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("About");

        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem exitItem = new JMenuItem("Exit");

        JMenuItem aboutItem = new JMenuItem("About us");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        aboutMenu.add(aboutItem);

        this.add(fileMenu);
        this.add(aboutMenu);
    }
}
