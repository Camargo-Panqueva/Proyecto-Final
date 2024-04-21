package view;

import controller.main.GameController;
import model.states.BaseState;
import util.ConcurrentLoop;
import view.window.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

import static view.Constants.*;

public final class GameView {

    private final GameController controller;
    private final Window window;

    private BufferStrategy bufferStrategy;
    private ConcurrentLoop renderLoop;


    private int currentFPS;

    public GameView(GameController controller) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 UnsupportedLookAndFeelException e) {
            System.out.println("Could not set system look and feel");
        }

        this.currentFPS = 0;

        this.controller = controller;
        this.window = new Window(INITIAL_WINDOW_SIZE, "Quoridor"); // TODO: Set window size from controller data
    }

    public void start() {
        this.window.makeVisible();

        this.renderLoop = new ConcurrentLoop(this::render, 60, "View");
        this.renderLoop.start();
        this.renderLoop.setTickConsumer(fps -> this.currentFPS = fps);
    }

    public void stop() {
        this.renderLoop.stop();
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
        graphics.setColor(WINDOW_BACKGROUND);
        graphics.fillRect(0, 0, this.window.getCanvasSize(), this.window.getCanvasSize());

        graphics.setColor(WHITE);
        graphics.drawString("FPS: " + this.currentFPS, 6, 16);
    }

    private void renderCurrentState(Graphics2D graphics) {
        BaseState currentState = this.controller.getCurrentState();

        if (currentState.getStateType() == BaseState.StateType.WELCOME) {

            graphics.setColor(TEXT_COLOR);

            String title = "Quoridor!";
            String subTitle = "By Camargo & Panqueva";

            int centerX = this.window.getCanvasSize() / 2;

            int titleMarginTop = this.window.getCanvasSize() / 4;
            int titleUnderlineMargin = 10;
            int titleUnderlineHeight = 8;
            int titleFontSize = (int) (this.window.getCanvasSize() * 1.25 / title.length());

            graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, titleFontSize));
            int titleWidth = graphics.getFontMetrics().stringWidth(title);
            graphics.fillRect(
                    centerX - titleWidth / 2,
                    titleMarginTop + titleUnderlineMargin,
                    titleWidth,
                    titleUnderlineHeight
            );
            graphics.drawString(title, centerX - titleWidth / 2, titleMarginTop);

            int subTitleMarginTop = titleMarginTop + 48;
            int subTitleFontSize = 20;

            graphics.setFont(graphics.getFont().deriveFont(Font.BOLD, subTitleFontSize));
            int subTitleWidth = graphics.getFontMetrics().stringWidth(subTitle);
            graphics.drawString(subTitle, centerX - subTitleWidth / 2, subTitleMarginTop);
        }
    }
}
