package view.scene;

import controller.states.GlobalState;
import model.difficulty.DifficultyType;
import model.player.AIProfile;
import model.player.PlayerType;
import model.wall.WallType;
import util.ConsumerFunction;
import view.components.GameComponent;
import view.components.ui.Button;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.components.ui.TextInput;
import view.context.GlobalContext;
import view.input.MouseEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

//TODO: Update docs when different modes are implemented

/**
 * Represents the scene for selecting the game mode.
 * <p>
 * This class represents the scene for selecting the game mode.
 * It provides a basic structure for rendering the game mode selection screen.
 * The scene contains buttons for selecting the single player and multiplayer modes.
 * </p>
 */
public final class SettingsScene extends Scene {

    int paddingX;
    int paddingY;
    int margin;
    int componentHeight;
    int componentWidth;
    int colonWidth;
    Font componentFont;

    private Selector<DifficultyType> difficultySelect;
    private Text specialCellsLabel;
    private Selector<Boolean> specialCellsSelect;
    private Text widthCellsLabel;
    private Selector<Integer> widthCellsSelect;
    private Text heightCellsLabel;
    private Selector<Integer> heightCellsSelect;
    private Text timeLimitLabel;
    private Text colonText;
    private TextInput minutesInput;
    private TextInput secondsInput;
    private ArrayList<Text> wallCountLabels;

    // Render parameters
    private ArrayList<Selector<Integer>> wallCountSelects;
    private ArrayList<TextInput> playerNameInputs;
    private ArrayList<Selector<ColorName>> playerColorSelects;
    private ArrayList<Selector<PlayerType>> playerTypeSelects;
    private ArrayList<Selector<AIProfile>> aiProfileSelects;
    private Button startButton;
    private Button backButton;

    /**
     * Creates a new SelectModeScene with the given context provider.
     *
     * @param globalContext the context provider for the scene.
     */
    public SettingsScene(GlobalContext globalContext) {
        super(globalContext);

        this.paddingX = 20;
        this.paddingY = 20;
        this.margin = 60;
        this.componentFont = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.componentHeight = 46;
        this.componentWidth = (int) (componentHeight * 7.5);
        this.colonWidth = 6;
    }

    /**
     * Adds all components to the scene.
     * <p>
     * This method adds all components to the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void addAllComponents() {
        this.addComponent(this.difficultySelect);
        this.addComponent(this.specialCellsLabel);
        this.addComponent(this.specialCellsSelect);
        this.addComponent(this.widthCellsLabel);
        this.addComponent(this.widthCellsSelect);
        this.addComponent(this.heightCellsLabel);
        this.addComponent(this.heightCellsSelect);
        this.addComponent(this.timeLimitLabel);
        this.addComponent(this.minutesInput);
        this.addComponent(this.colonText);
        this.addComponent(this.secondsInput);
        this.addComponent(this.startButton);
        this.addComponent(this.backButton);

        for (Text wallCountLabel : this.wallCountLabels) {
            this.addComponent(wallCountLabel);
        }
        for (Selector<Integer> wallCountValue : this.wallCountSelects) {
            this.addComponent(wallCountValue);
        }
        for (TextInput playerNameInput : this.playerNameInputs) {
            this.addComponent(playerNameInput);
        }
        for (Selector<ColorName> playerColorInput : this.playerColorSelects) {
            this.addComponent(playerColorInput);
        }
        for (Selector<PlayerType> playerTypeSelect : this.playerTypeSelects) {
            this.addComponent(playerTypeSelect);
        }
        for (Selector<AIProfile> aiProfileSelect : this.aiProfileSelects) {
            this.addComponent(aiProfileSelect);
        }
    }

    /**
     * Sets up the components for the scene.
     * <p>
     * This method sets up the components for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void setupComponents() {

        this.paddingX = 20;
        this.paddingY = 20;
        this.margin = 60;
        this.componentFont = this.globalContext.window().getCanvas().getFont().deriveFont(20.0f);
        this.componentHeight = 46;
        this.componentWidth = (int) (componentHeight * 7.5);
        this.colonWidth = 6;

        this.setupBasicComponents();
        this.setupTimeSelectorComponents();
        this.setupWallCountComponents();
        this.setupPlayerComponents();
        this.setupButtons();
    }

    /**
     * Sets up the events for the scene.
     * <p>
     * This method sets up the events for the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void setupEvents() {

        this.startButton.addMouseListener(MouseEvent.EventType.RELEASED, event -> {
            //TODO: Implement game start
            this.globalContext.controller().createMatch(
                    4,
                    this.widthCellsSelect.getSelectedOption(),
                    this.heightCellsSelect.getSelectedOption(),
                    10
            );
            this.globalContext.controller().setGlobalState(GlobalState.PLAYING);
        });

        this.backButton.addMouseListener(MouseEvent.EventType.RELEASED, event -> {
            this.globalContext.controller().setGlobalState(GlobalState.WELCOME);
        });

        for (int index = 0; index < this.playerColorSelects.size(); index++) {
            Selector<ColorName> colorNameSelector = this.playerColorSelects.get(index);

            int finalIndex = index;

            colorNameSelector.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED, (previousValue, currentValue) -> {
                this.updatePlayerFieldColor(finalIndex, (ColorName) previousValue, (ColorName) currentValue);
            });
        }
    }

    @Override
    protected void fixCanvasSize() {
        int expectedHeight = this.startButton.getStyle().y + this.startButton.getStyle().height + this.margin;

        int expectedWidth = this.startButton.getStyle().x + this.startButton.getStyle().width + this.margin;

        this.globalContext.window().setCanvasHeight(expectedHeight);
        this.globalContext.window().setCanvasWidth(expectedWidth);
    }

    private void setupBasicComponents() {
        ArrayList<DifficultyType> difficultyOptions = new ArrayList<>(Arrays.asList(DifficultyType.values()));
        this.difficultySelect = new Selector<>(difficultyOptions, this.globalContext);
        this.difficultySelect.getStyle().x = this.margin;
        this.difficultySelect.getStyle().y = this.margin;
        this.difficultySelect.getStyle().height = this.componentHeight;
        this.difficultySelect.getStyle().width = this.componentWidth;
        this.difficultySelect.getStyle().font = this.componentFont;

        this.specialCellsLabel = new Text("Special Cells", this.globalContext);
        this.specialCellsLabel.getStyle().x = this.margin;
        this.specialCellsLabel.getStyle().y = this.paddingY + this.difficultySelect.getStyle().height + this.difficultySelect.getStyle().y;
        this.specialCellsLabel.getStyle().height = this.componentHeight;
        this.specialCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.specialCellsLabel.getStyle().font = this.componentFont;
        this.specialCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.specialCellsLabel.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);

        this.specialCellsSelect = new Selector<>(false, this.globalContext);
        this.specialCellsSelect.getStyle().x = this.paddingX + this.specialCellsLabel.getStyle().width + this.specialCellsLabel.getStyle().x;
        this.specialCellsSelect.getStyle().y = this.specialCellsLabel.getStyle().y;
        this.specialCellsSelect.getStyle().height = this.componentHeight;
        this.specialCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.specialCellsSelect.getStyle().font = this.componentFont;
        this.specialCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.specialCellsSelect.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);

        this.widthCellsLabel = new Text("Width", this.globalContext);
        this.widthCellsLabel.getStyle().x = this.margin;
        this.widthCellsLabel.getStyle().y = this.paddingY + this.specialCellsLabel.getStyle().height + this.specialCellsLabel.getStyle().y;
        this.widthCellsLabel.getStyle().height = this.componentHeight;
        this.widthCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.widthCellsLabel.getStyle().font = this.componentFont;
        this.widthCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.widthCellsLabel.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);

        this.widthCellsSelect = new Selector<>(5, 14, this.globalContext);
        this.widthCellsSelect.getStyle().x = this.paddingX + this.widthCellsLabel.getStyle().width + this.widthCellsLabel.getStyle().x;
        this.widthCellsSelect.getStyle().y = this.widthCellsLabel.getStyle().y;
        this.widthCellsSelect.getStyle().height = this.componentHeight;
        this.widthCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.widthCellsSelect.getStyle().font = this.componentFont;
        this.widthCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.widthCellsSelect.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);

        this.heightCellsLabel = new Text("Height", this.globalContext);
        this.heightCellsLabel.getStyle().x = this.margin;
        this.heightCellsLabel.getStyle().y = this.paddingY + this.widthCellsLabel.getStyle().height + this.widthCellsLabel.getStyle().y;
        this.heightCellsLabel.getStyle().height = this.componentHeight;
        this.heightCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.heightCellsLabel.getStyle().font = this.componentFont;
        this.heightCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.heightCellsLabel.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);

        this.heightCellsSelect = new Selector<>(5, 14, this.globalContext);
        this.heightCellsSelect.getStyle().x = this.paddingX + this.heightCellsLabel.getStyle().width + this.heightCellsLabel.getStyle().x;
        this.heightCellsSelect.getStyle().y = this.heightCellsLabel.getStyle().y;
        this.heightCellsSelect.getStyle().height = this.componentHeight;
        this.heightCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.heightCellsSelect.getStyle().font = this.componentFont;
        this.heightCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.heightCellsSelect.getStyle().foregroundColor = new ThemeColor(ColorName.PURPLE, ColorVariant.NORMAL);
    }

    private void setupTimeSelectorComponents() {

        this.timeLimitLabel = new Text("Time", this.globalContext);
        this.timeLimitLabel.getStyle().x = this.margin;
        this.timeLimitLabel.getStyle().y = this.margin + this.heightCellsLabel.getStyle().height + this.heightCellsLabel.getStyle().y;
        this.timeLimitLabel.getStyle().height = this.componentHeight;
        this.timeLimitLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.timeLimitLabel.getStyle().font = this.componentFont;
        this.timeLimitLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.timeLimitLabel.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);

        this.minutesInput = new TextInput(this.globalContext);
        this.minutesInput.getStyle().x = this.paddingX + this.timeLimitLabel.getStyle().width + this.timeLimitLabel.getStyle().x;
        this.minutesInput.getStyle().y = this.timeLimitLabel.getStyle().y;
        this.minutesInput.getStyle().height = this.componentHeight;
        this.minutesInput.getStyle().width = this.componentWidth / 5 - (this.colonWidth + this.paddingX) / 2;
        this.minutesInput.getStyle().font = this.componentFont;
        this.minutesInput.getStyle().paddingX = 13;

        this.colonText = new Text(":", this.globalContext);
        this.colonText.getStyle().x = this.colonWidth + this.minutesInput.getStyle().width + this.minutesInput.getStyle().x;
        this.colonText.getStyle().y = this.minutesInput.getStyle().y;
        this.colonText.getStyle().height = this.componentHeight;
        this.colonText.getStyle().width = this.colonWidth;
        this.colonText.getStyle().font = this.componentFont;

        this.secondsInput = new TextInput(this.globalContext);
        this.secondsInput.getStyle().x = this.colonWidth + this.colonText.getStyle().width + this.colonText.getStyle().x;
        this.secondsInput.getStyle().y = this.minutesInput.getStyle().y;
        this.secondsInput.getStyle().height = this.componentHeight;
        this.secondsInput.getStyle().width = (this.componentWidth / 5) - (this.colonWidth + this.paddingX) / 2;
        this.secondsInput.getStyle().font = this.componentFont;
        this.secondsInput.getStyle().paddingX = 13;
    }

    private void setupWallCountComponents() {
        ArrayList<WallType> wallTypes = new ArrayList<>(Arrays.asList(WallType.values()));
        this.wallCountLabels = new ArrayList<>();
        this.wallCountSelects = new ArrayList<>();

        for (WallType wallType : wallTypes) {
            Text wallCountLabel = new Text(wallType.toString(), this.globalContext);
            wallCountLabel.getStyle().x = this.margin;
            wallCountLabel.getStyle().y = this.paddingY + this.minutesInput.getStyle().height + this.minutesInput.getStyle().y + this.wallCountLabels.size() * (this.paddingY + this.componentHeight);
            wallCountLabel.getStyle().height = this.componentHeight;
            wallCountLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
            wallCountLabel.getStyle().font = this.componentFont;
            wallCountLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
            wallCountLabel.getStyle().foregroundColor = new ThemeColor(ColorName.GREEN, ColorVariant.NORMAL);

            Selector<Integer> wallCountSelector = new Selector<>(0, 10, this.globalContext);
            wallCountSelector.getStyle().x = this.paddingX + wallCountLabel.getStyle().width + wallCountLabel.getStyle().x;
            wallCountSelector.getStyle().y = wallCountLabel.getStyle().y;
            wallCountSelector.getStyle().height = this.componentHeight;
            wallCountSelector.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
            wallCountSelector.getStyle().font = this.componentFont;
            wallCountSelector.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            wallCountSelector.getStyle().foregroundColor = new ThemeColor(ColorName.GREEN, ColorVariant.NORMAL);

            this.wallCountLabels.add(wallCountLabel);
            this.wallCountSelects.add(wallCountSelector);
        }
    }

    private void setupPlayerComponents() {
        int playerCount = 4;
        this.playerNameInputs = new ArrayList<>();
        this.playerColorSelects = new ArrayList<>();
        this.playerTypeSelects = new ArrayList<>();
        this.aiProfileSelects = new ArrayList<>();

        ArrayList<PlayerType> playerTypes = new ArrayList<>(Arrays.asList(PlayerType.values()));
        ArrayList<AIProfile> aiProfiles = new ArrayList<>(Arrays.asList(AIProfile.values()));

        ConsumerFunction<GameComponent> applyFirstRowStyle = component -> {
            component.getStyle().height = this.componentHeight;
            component.getStyle().width = 4 * this.componentWidth / 5;
            component.getStyle().font = this.componentFont;
            component.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            component.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
        };

        ConsumerFunction<GameComponent> applySecondRowStyle = component -> {
            GameComponent lastComponent = this.playerNameInputs.get(this.playerNameInputs.size() - 1);

            component.getStyle().y = lastComponent.getStyle().y + lastComponent.getStyle().height + this.paddingY;
            component.getStyle().height = this.componentHeight;
            component.getStyle().width = lastComponent.getStyle().width;
            component.getStyle().font = this.componentFont;
            component.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            component.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
        };

        ArrayList<ColorName> colorNames = this.globalContext.currentTheme().getPlayerColors();

        for (int index = 0; index < playerCount; index++) {

            TextInput playerNameInput = new TextInput(this.globalContext);
            playerNameInput.getStyle().x = this.componentWidth + 2 * this.margin;
            playerNameInput.getStyle().y = this.margin + 2 * index * (this.paddingY + this.componentHeight);
            applyFirstRowStyle.run(playerNameInput);

            if (index > 1) {
                playerNameInput.getStyle().y = this.timeLimitLabel.getStyle().y + 2 * (index % 2) * (this.paddingY + this.componentHeight);
            }

            this.playerNameInputs.add(playerNameInput);


            Selector<ColorName> playerColorInput = new Selector<>(colorNames, this.globalContext);
            playerColorInput.getStyle().x = playerNameInput.getStyle().x + playerNameInput.getStyle().width + this.paddingX;
            playerColorInput.getStyle().y = playerNameInput.getStyle().y;
            playerColorInput.setSelectedIndex(index % colorNames.size());
            applyFirstRowStyle.run(playerColorInput);
            this.playerColorSelects.add(playerColorInput);

            Selector<PlayerType> playerTypeSelect = new Selector<>(playerTypes, this.globalContext);
            playerTypeSelect.getStyle().x = playerNameInput.getStyle().x;
            applySecondRowStyle.run(playerTypeSelect);
            this.playerTypeSelects.add(playerTypeSelect);

            Selector<AIProfile> aiProfileSelect = new Selector<>(aiProfiles, this.globalContext);
            aiProfileSelect.getStyle().x = playerColorInput.getStyle().x;
            applySecondRowStyle.run(aiProfileSelect);
            this.aiProfileSelects.add(aiProfileSelect);

            this.updatePlayerFieldColor(index, null, colorNames.get(index % colorNames.size()));
        }
    }

    private void setupButtons() {
        this.backButton = new Button("Cancel", this.globalContext);
        this.backButton.getStyle().x = this.playerNameInputs.get(0).getStyle().x;
        this.backButton.getStyle().y = this.aiProfileSelects.get(this.aiProfileSelects.size() - 1).getStyle().y + this.aiProfileSelects.get(this.aiProfileSelects.size() - 1).getStyle().height + this.paddingY;
        this.backButton.getStyle().height = this.componentHeight;
        this.backButton.getStyle().width = 4 * this.componentWidth / 5;
        this.backButton.getStyle().font = this.componentFont;
        this.backButton.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.backButton.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);

        GameComponent lastAiProfile = this.aiProfileSelects.get(this.aiProfileSelects.size() - 1);
        GameComponent lastWallType = this.wallCountSelects.get(this.wallCountSelects.size() - 1);

        this.backButton.getStyle().y = Math.max(lastWallType.getStyle().y, lastAiProfile.getStyle().y);

        this.startButton = new Button("Start", this.globalContext);
        this.startButton.getStyle().x = this.backButton.getStyle().x + this.backButton.getStyle().width + this.paddingX;
        this.startButton.getStyle().y = this.backButton.getStyle().y;
        this.startButton.getStyle().height = this.componentHeight;
        this.startButton.getStyle().width = 4 * this.componentWidth / 5;
        this.startButton.getStyle().font = this.componentFont;
    }

    private void updatePlayerFieldColor(int index, ColorName previousColorName, ColorName colorName) {

        this.playerNameInputs.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.playerColorSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.playerTypeSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.aiProfileSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);

        if (previousColorName == null) {
            return;
        }

        for (int j = 0; j < this.playerColorSelects.size(); j++) {
            Selector<ColorName> colorNameSelector = this.playerColorSelects.get(j);

            if (index != j && colorNameSelector.getSelectedOption() == colorName) {

                colorNameSelector.setSelectedOption(previousColorName);

                colorNameSelector.getStyle().foregroundColor = new ThemeColor(previousColorName, ColorVariant.NORMAL);
                this.playerNameInputs.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, ColorVariant.NORMAL);
                this.playerTypeSelects.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, ColorVariant.NORMAL);
                this.aiProfileSelects.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, ColorVariant.NORMAL);
            }
        }
    }
}
