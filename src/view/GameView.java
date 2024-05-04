package view;

import com.sun.management.OperatingSystemMXBean;
import controller.main.GameController;
import util.ConcurrentLoop;
import view.context.ContextProvider;
import view.input.Mouse;
import view.scene.SceneManager;
import view.themes.ThemeManager;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;
import java.util.List;

public final class GameView {

    private final ContextProvider contextProvider;
    private final GameController controller;
    private final SceneManager sceneManager;
    private final ThemeManager themeManager;
    private final Window window;
    private final Mouse mouse;

    private ConcurrentLoop renderLoop;
    private ConcurrentLoop updateLoop;
    private BufferStrategy bufferStrategy;

    public GameView(GameController controller) {
        this.setupGraphicsEnvironment();

        this.controller = controller;
        this.mouse = new Mouse();
        this.themeManager = new ThemeManager();
        this.window = new Window(600, "Quoridor"); // TODO: Set window size from controller data
        this.window.getCanvas().addMouseListener(this.mouse);
        this.window.getCanvas().setFont(new Font("Yoster Island Regular", Font.PLAIN, 16));

        this.contextProvider = new ContextProvider(this.window, this.controller, this.mouse, this.themeManager);
        this.sceneManager = new SceneManager(this.contextProvider);
    }

    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 60, "View render");
        this.updateLoop = new ConcurrentLoop(this::update, 60, "View update");

        this.renderLoop.start();
        this.updateLoop.start();
    }

    public void stop() {
        this.renderLoop.stop();
        this.updateLoop.stop();
    }

    private void update() {
        this.mouse.update(this.window);
        this.sceneManager.update();
    }

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

    private void renderBackground(Graphics2D graphics) {
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setColor(this.themeManager.getCurrentTheme().backgroundColor);
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());
    }

    private void renderPerformance(Graphics2D graphics) {
        graphics.setColor(this.themeManager.getCurrentTheme().foregroundColor);
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

    private void renderCurrentState(Graphics2D graphics) {
        this.sceneManager.render(graphics);
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
            File fontFile = new File("src/resources/fonts/yoster.ttf");
            Font gameFont = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            if (!availableFontFamilyNames.contains(gameFont.getFontName())) {
                GE.registerFont(gameFont);
            }
        } catch (FontFormatException | IOException exception) {
            JOptionPane.showMessageDialog(null, exception.getMessage());
        }
    }
}
