package view.scene;

import controller.states.GlobalState;
import view.context.ContextProvider;

import java.awt.*;
import java.util.HashMap;

public final class SceneManager {

    private final ContextProvider contextProvider;
    private final HashMap<GlobalState, Scene> scenes;
    private Scene currentScene;

    public SceneManager(ContextProvider contextProvider) {
        this.scenes = new HashMap<>();
        this.contextProvider = contextProvider;
    }

    public void fetchCurrentGlobalState() {
        GlobalState state = this.contextProvider.controller().getGlobalCurrentState();

        switch (state) {
            case WELCOME -> {
                if (!this.scenes.containsKey(GlobalState.WELCOME)) {
                    this.scenes.put(GlobalState.WELCOME, new WelcomeScene(this.contextProvider));
                }

                this.currentScene = this.scenes.get(GlobalState.WELCOME);
            }
            case SELECTING_GAME_MODE -> {
                if (!this.scenes.containsKey(GlobalState.SELECTING_GAME_MODE)) {
                    this.scenes.put(GlobalState.SELECTING_GAME_MODE, new SelectModeScene(this.contextProvider));
                }

                this.currentScene = this.scenes.get(GlobalState.SELECTING_GAME_MODE);
            }
            default -> {
                System.out.println("Invalid state");
            }
        }
    }

    public void update() {
        this.fetchCurrentGlobalState();
        this.currentScene.update();
    }

    public void render(Graphics2D graphics2D) {
        this.currentScene.render(graphics2D);
    }
}
