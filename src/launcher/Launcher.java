package launcher;

import game.Game;

import javax.swing.*;

public class Launcher {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.out.println("Could not set system look and feel");
        }

        Game game = new Game();
        game.start();
    }
}
