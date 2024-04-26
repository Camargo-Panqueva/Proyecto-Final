package view.scene;

import view.components.GameComponent;
import view.context.ContextProvider;

import java.awt.*;
import java.util.ArrayList;

public abstract class Scene {

    protected final ArrayList<GameComponent> components;
    protected final ContextProvider contextProvider;

    public Scene(ContextProvider contextProvider) {
        this.components = new ArrayList<>();
        this.contextProvider = contextProvider;

        this.setupComponents();
        this.setupEvents();
        this.addAllComponents();
    }

    public void update() {
        this.components.forEach(GameComponent::update);
    }

    public void render(Graphics2D graphics) {
        this.components.forEach(component -> component.render(graphics));
    }

    public void addComponent(GameComponent component) {
        this.components.add(component);
    }

    protected abstract void addAllComponents();
    protected abstract void setupComponents();
    protected abstract void setupEvents();
}
