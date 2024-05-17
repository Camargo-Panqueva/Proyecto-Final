package view.scene;

import view.components.GameComponent;
import view.context.GlobalContext;

import java.awt.*;
import java.util.ArrayList;

/**
 * Represents a scene that can be rendered on the screen.
 * <p>
 * This class is the base class for all scenes that can be rendered on the screen.
 * It provides a basic structure for handling components and events in a scene.
 * </p>
 */
public abstract class Scene {

    protected final ArrayList<GameComponent> components;
    protected final GlobalContext globalContext;

    /**
     * Creates a new Scene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public Scene(GlobalContext globalContext) {
        this.components = new ArrayList<>();
        this.globalContext = globalContext;

        this.setupComponents();
        this.setupEvents();
        this.addAllComponents();
    }

    /**
     * Updates the scene's logic.
     * <p>
     * This method updates the logic of all components in the scene.
     * It runs the {@link GameComponent#update} method for each component.
     * This method is called once per update cycle.
     * </p>
     */
    public void update() {
        this.components.forEach(GameComponent::update);
    }

    /**
     * Renders the scene on the screen.
     * <p>
     * This method renders all components in the scene.
     * It runs the {@link GameComponent#render} method for each component.
     * This method is called once per render cycle.
     * </p>
     *
     * @param graphics the graphics object to render the scene with.
     */
    public void render(Graphics2D graphics) {
        this.components.forEach(component -> component.render(graphics));
    }

    /**
     * Adds a component to the scene.
     *
     * @param component the component to add.
     */
    public void addComponent(GameComponent component) {
        this.components.add(component);
    }

    /**
     * Sets up the components in the scene.
     * <p>
     * This method is called once when the scene is created.
     * It should be used to create and add components to the scene.
     * It's called automatically by the constructor.
     * </p>
     */
    protected abstract void addAllComponents();

    /**
     * Sets up the components in the scene.
     * <p>
     * This method is called once when the scene is created.
     * It should be used to create and add components to the scene.
     * It's called automatically by the constructor.
     * </p>
     */
    protected abstract void setupComponents();

    /**
     * Sets up the events for the scene.
     * <p>
     * This method is called once when the scene is created.
     * It should be used to set up event listeners for the scene.
     * It's called automatically by the constructor.
     * </p>
     */
    protected abstract void setupEvents();
}
