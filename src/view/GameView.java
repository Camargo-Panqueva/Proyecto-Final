package view;

import controller.main.GameController;
import model.states.BaseState;
import util.ConcurrentLoop;
import view.context.ContextProvider;
import view.input.Mouse;
import view.scene.WelcomeScene;
import view.themes.ThemeManager;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public final class GameView {

    private final GameController controller;
    private final ContextProvider contextProvider;
    private final ThemeManager themeManager;
    private final Window window;
    private final Mouse mouse;

    private final WelcomeScene welcomeScene;

    private ConcurrentLoop renderLoop;
    private ConcurrentLoop updateLoop;
    private BufferStrategy bufferStrategy;

    private int currentFPS;
    private int currentUPS;

    public GameView(GameController controller) {
        this.setupGraphicsEnvironment();

        this.currentFPS = 0;

        this.controller = controller;
        this.mouse = new Mouse();
        this.themeManager = new ThemeManager();
        this.window = new Window(600, "Quoridor"); // TODO: Set window size from controller data
        this.window.getCanvas().addMouseListener(this.mouse);
        this.window.getCanvas().setFont(new Font("Yoster Island Regular", Font.PLAIN, 16));

        this.contextProvider = new ContextProvider(this.window, this.controller, this.mouse, this.themeManager);
        this.welcomeScene = new WelcomeScene(this.contextProvider);
    }

    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 16, "View render");
        this.renderLoop.start();
        this.renderLoop.setTickConsumer(fps -> this.currentFPS = fps);

        this.updateLoop = new ConcurrentLoop(this::update, 60, "View update");
        this.updateLoop.start();
        this.updateLoop.setTickConsumer(ups -> this.currentUPS = ups);
    }

    public void stop() {
        this.renderLoop.stop();
        this.updateLoop.stop();
    }

    private void update() {
        this.mouse.update(this.window);
        this.welcomeScene.update();
    }

    private void render() {

        if (bufferStrategy == null) {
            this.window.getCanvas().createBufferStrategy(2);
            this.bufferStrategy = this.window.getCanvas().getBufferStrategy();
        }

        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        this.renderBackground(graphics);
        this.renderCurrentState(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    private void renderBackground(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(this.themeManager.getCurrentTheme().backgroundColor);
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());

        graphics.setColor(this.themeManager.getCurrentTheme().foregroundColor);
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.drawString("FPS: " + this.currentFPS, 6, 16);
        graphics.drawString("UPS: " + this.currentUPS, 6, 32);

        Point mousePosition = this.mouse.getMousePosition();
        String mouseText = String.format("Mouse: [%d, %d]", mousePosition.x, mousePosition.y);
        graphics.drawString(mouseText, 6, 48);
    }

    private void renderCurrentState(Graphics2D graphics) {
        BaseState currentState = this.controller.getCurrentState();

        if (currentState.getStateType() == BaseState.StateType.WELCOME) {
            this.welcomeScene.render(graphics);
        }
    }

    private void setupGraphicsEnvironment() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        List<String> availableFontFamilyNames = Arrays.asList(GE.getAvailableFontFamilyNames());

        try {
            File fontFile = new File("res/fonts/yoster.ttf");
            Font gameFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            if (!availableFontFamilyNames.contains(gameFont.getFontName())) {
                GE.registerFont(gameFont);
            }
        } catch (FontFormatException | IOException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }
}
