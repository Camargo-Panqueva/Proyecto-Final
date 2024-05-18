package view.scene;

import controller.states.GlobalState;
import view.context.GlobalContext;

import java.awt.*;
import java.util.HashMap;

public final class SceneManager {

    private final GlobalContext globalContext;
    private final HashMap<GlobalState, Scene> scenes;
    private Scene currentScene;

    public SceneManager(GlobalContext globalContext) {
        this.scenes = new HashMap<>();
        this.globalContext = globalContext;
        this.fetchCurrentGlobalState();
    }

    public void fetchCurrentGlobalState() {
        GlobalState state = this.globalContext.controller().getGlobalCurrentState();

        switch (state) {
            case WELCOME -> {
                if (!this.scenes.containsKey(GlobalState.WELCOME)) {
                    this.scenes.put(GlobalState.WELCOME, new WelcomeScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.WELCOME);
            }
            //TODO: Change scene name to settings
            case SELECTING_GAME_MODE -> {
                if (!this.scenes.containsKey(GlobalState.SELECTING_GAME_MODE)) {
                    this.scenes.put(GlobalState.SELECTING_GAME_MODE, new SettingsScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.SELECTING_GAME_MODE);
            }
            case PLAYING -> {
                if (!this.scenes.containsKey(GlobalState.PLAYING)) {
                    this.scenes.put(GlobalState.PLAYING, new PlayingScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.PLAYING);
            }
            default -> {
                //TODO: Handle invalid state error
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
