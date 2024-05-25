package view.scene;

import controller.dto.ServiceResponse;
import controller.states.GlobalState;
import view.context.GlobalContext;

import java.awt.*;
import java.util.HashMap;

/**
 * Represents a scene manager object that manages the scenes in the game.
 * <p>
 * This class represents a scene manager object that manages the scenes in the game.
 * It provides a structure for managing the scenes in the game and switching between them.
 * The scene manager fetches the current global state and displays the appropriate scene.
 * </p>
 */
public final class SceneManager {

    private final GlobalContext globalContext;
    private final HashMap<GlobalState, Scene> scenes;
    private Scene currentScene;
    private GlobalState lastState;

    /**
     * Creates a new SceneManager with the given global context.
     *
     * @param globalContext the global context for the scene manager.
     */
    public SceneManager(GlobalContext globalContext) {
        this.scenes = new HashMap<>();
        this.globalContext = globalContext;
        this.fetchCurrentGlobalState();
    }

    /**
     * Fetches the current global state and displays the appropriate scene.
     * <p>
     * This method fetches the current global state and displays the appropriate scene.
     * It fetches the current global state from the controller and displays the scene based on the state.
     * </p>
     */
    public void fetchCurrentGlobalState() {
        ServiceResponse<GlobalState> stateResponse = this.globalContext.controller().getGlobalCurrentState();

        if (!stateResponse.ok) {
            //TODO: Handle error
            throw new RuntimeException("Failed to get current global state: " + stateResponse.message);
        }

        GlobalState state = stateResponse.payload;

        if (state == this.lastState) {
            return;
        }

        switch (state) {
            case WELCOME -> {
                if (!this.scenes.containsKey(GlobalState.WELCOME)) {
                    this.scenes.put(GlobalState.WELCOME, new WelcomeScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.WELCOME);
            }
            case SETUP_MATCH_SETTINGS -> {
                if (!this.scenes.containsKey(GlobalState.SETUP_MATCH_SETTINGS)) {
                    this.scenes.put(GlobalState.SETUP_MATCH_SETTINGS, new SettingsScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.SETUP_MATCH_SETTINGS);
            }
            case PLAYING -> {
                if (!this.scenes.containsKey(GlobalState.PLAYING)) {
                    this.scenes.put(GlobalState.PLAYING, new PlayingScene(this.globalContext));
                }

                this.currentScene = this.scenes.get(GlobalState.PLAYING);
            }
            default -> //TODO: Handle error
                    throw new RuntimeException("Unknown state: " + state);
        }

        this.lastState = state;
        this.currentScene.fixCanvasSize();
        this.globalContext.window().getCanvas().setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Updates the current scene.
     * <p>
     * This method updates the current scene.
     * It fetches the current global state and updates the current scene.
     * </p>
     */
    public void update() {
        this.fetchCurrentGlobalState();
        this.currentScene.update();
    }

    /**
     * Renders the current scene.
     * <p>
     * This method renders the current scene.
     * It renders the current scene using the given graphics object.
     * </p>
     *
     * @param graphics2D the graphics object to render the scene.
     */
    public void render(Graphics2D graphics2D) {
        this.currentScene.render(graphics2D);
    }
}
