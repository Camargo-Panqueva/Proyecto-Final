package view;

import com.sun.management.OperatingSystemMXBean;
import controller.GameController;
import util.ConcurrentLoop;
import view.context.GlobalContext;
import view.input.Keyboard;
import view.input.Mouse;
import view.scene.SceneManager;
import view.themes.DarkTheme;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;
import view.themes.ThemeManager;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;
import java.util.List;

/**
 * The GameView class is responsible for rendering the game state and handling user input.
 *
 * <p>It uses a {@link GlobalContext} to store the game state and a {@link SceneManager} to manage the game scenes.
 * It also uses a {@link ThemeManager} to manage the game themes and a {@link Window} to display the game.
 * And it uses a {@link Mouse} and a {@link Keyboard} to handle user input.
 * </p>
 */
public final class GameView {

    private final GlobalContext globalContext;
    private final GameController controller;
    private final SceneManager sceneManager;
    private final ThemeManager themeManager;
    private final Window window;
    private final Mouse mouse;
    private final Keyboard keyboard;

    private ConcurrentLoop renderLoop;
    private ConcurrentLoop updateLoop;
    private BufferStrategy bufferStrategy;

    /**
     * Creates a new GameView with the given controller.
     *
     * @param controller the controller for the view.
     */
    public GameView(GameController controller) {
        this.setupGraphicsEnvironment();

        this.controller = controller;
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.themeManager = new ThemeManager(new DarkTheme());
        this.window = new Window(600, "Quoridor"); // TODO: Set window size from controller data
        this.window.getCanvas().addMouseListener(this.mouse);
        this.window.getCanvas().addKeyListener(this.keyboard);
        this.window.getCanvas().setFont(new Font("Yoster Island Regular", Font.PLAIN, 16));

        this.globalContext = new GlobalContext(this.window, this.controller, this.mouse, this.keyboard, this.themeManager);
        this.sceneManager = new SceneManager(this.globalContext);
    }

    /**
     * Starts the game view.
     *
     * <p>It makes the window visible and starts the render and update loops.</p>
     */
    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 60, "View render");
        this.updateLoop = new ConcurrentLoop(this::update, 60, "View update");

        this.renderLoop.start();
        this.updateLoop.start();
    }

    /**
     * Stops the game view.
     *
     * <p>It stops the render and update loops.</p>
     */
    public void stop() {
        this.renderLoop.stop();
        this.updateLoop.stop();
    }

    /**
     * Updates the game view.
     *
     * <p>It updates the mouse, the scene manager and the window.</p>
     */
    private void update() {
        this.mouse.update(this.window);
        this.sceneManager.update();
    }

    /**
     * Renders the game view.
     *
     * <p>It renders the background, the current state and the performance information.</p>
     */
    private void render() {

        if (this.bufferStrategy == null) {
            this.window.getCanvas().createBufferStrategy(2);
            this.bufferStrategy = this.window.getCanvas().getBufferStrategy();
        }

        Graphics2D graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        this.renderBackground(graphics);
        this.renderCurrentState(graphics);
        this.renderPerformance(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    /**
     * Renders the background of the game view.
     *
     * <p>It renders the background with the current theme color.</p>
     *
     * @param graphics the graphics object to render the background.
     */
    private void renderBackground(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.BACKGROUND, ColorVariant.NORMAL));
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());
    }

    /**
     * Renders the performance information of the game view.
     *
     * <p>It renders the frames per second, the ticks per second, the mouse position, the CPU load and the memory usage.</p>
     *
     * @param graphics the graphics object to render the performance information.
     */
    private void renderPerformance(Graphics2D graphics) {
        graphics.setColor(this.globalContext.currentTheme().getColor(ColorName.FOREGROUND, ColorVariant.NORMAL));
        graphics.setFont(new Font("Arial", Font.PLAIN, 12));

        FontMetrics fontMetrics = graphics.getFontMetrics();
        int canvasSize = this.window.getCanvasSize();

        graphics.drawString(String.format("FPS: %d", this.renderLoop.getCurrentTPS()), 6, 16);
        graphics.drawString(String.format("TPS: %d", this.updateLoop.getCurrentTPS()), 6, 32);

        Point mousePosition = this.mouse.getMousePosition();
        String mouseText = String.format("Mouse: [%d, %d]", mousePosition.x, mousePosition.y);
        graphics.drawString(mouseText, 6, 48);

        OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        double cpuLoad = osBean.getCpuLoad() * 100;
        String cpuText = String.format("CPU: %.2f%%", cpuLoad);
        graphics.drawString(cpuText, canvasSize - fontMetrics.stringWidth(cpuText) - 6, 16);

        MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
        long memoryUsed = memoryMXBean.getHeapMemoryUsage().getUsed() / 1024 / 1024;
        long memoryMax = memoryMXBean.getHeapMemoryUsage().getMax() / 1024 / 1024;
        String memoryText = String.format("Memory: %d / %d MB", memoryUsed, memoryMax);
        graphics.drawString(memoryText, canvasSize - fontMetrics.stringWidth(memoryText) - 6, 32);

        double upTimeNanoSeconds = (double) ManagementFactory.getRuntimeMXBean().getUptime() * 1_000_000;

        long renderThreadCpuTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(this.renderLoop.getThread().getId());
        long updateThreadCpuTime = ManagementFactory.getThreadMXBean().getThreadCpuTime(this.updateLoop.getThread().getId());

        String uptimeText = String.format("Uptime: %.0f s", upTimeNanoSeconds / 1_000_000_000);
        String renderThreadCPU = String.format("Render thread time: %.1f%%", 100 * renderThreadCpuTime / upTimeNanoSeconds);
        String updateThreadCPU = String.format("Update thread time: %.1f%%", 100 * updateThreadCpuTime / upTimeNanoSeconds);

        graphics.drawString(renderThreadCPU, canvasSize - fontMetrics.stringWidth(renderThreadCPU) - 6, 48);
        graphics.drawString(updateThreadCPU, canvasSize - fontMetrics.stringWidth(updateThreadCPU) - 6, 64);
        graphics.drawString(uptimeText, canvasSize - fontMetrics.stringWidth(uptimeText) - 6, 80);
    }

    /**
     * Renders the current state of the game view.
     *
     * <p>It renders the current state of the game using the scene manager.</p>
     *
     * @param graphics the graphics object to render the current state.
     */
    private void renderCurrentState(Graphics2D graphics) {
        this.sceneManager.render(graphics);
    }

    /**
     * Sets up the graphics environment of the game view.
     *
     * <p>It sets the system look and feel and registers the game font.</p>
     */
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
            InputStream inputStream = getClass().getResourceAsStream("/resources/fonts/yoster.ttf");
            if (inputStream != null) {
                Font gameFont = Font.createFont(Font.TRUETYPE_FONT, inputStream);
                if (!availableFontFamilyNames.contains(gameFont.getFontName())) {
                    GE.registerFont(gameFont);
                }
            } else {
                JOptionPane.showMessageDialog(null, "No se pudo cargar la fuente.");
            }
        } catch (FontFormatException | IOException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }
}
