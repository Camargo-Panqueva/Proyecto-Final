package view.scene;

import controller.dto.PlayerSetupTransferObject;
import controller.dto.SetupTransferObject;
import controller.states.GlobalState;
import model.cell.CellType;
import model.difficulty.DifficultyType;
import model.player.AIProfile;
import model.player.PlayerType;
import model.wall.WallType;
import util.Logger;
import view.components.GameComponent;
import view.components.ui.Button;
import view.components.ui.Selector;
import view.components.ui.Text;
import view.components.ui.TextInput;
import view.context.GlobalContext;
import view.context.Style;
import view.input.MouseEvent;
import view.themes.ThemeColor;
import view.themes.ThemeColor.ColorName;
import view.themes.ThemeColor.ColorVariant;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents the settings scene of the game.
 * <p>
 * This class represents the settings scene of the game.
 * It provides a structure for setting up the game settings.
 * The settings scene is used to set up the game settings before starting a match.
 * The settings scene allows the player to set up the game settings such as the board size,
 * the special cells, the difficulty, the time limit, the player setups, and the wall counts.
 * The settings scene also allows the player to start a match with the given settings.
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
    private Text cellTypeTitle;
    private Text wallTypeTitle;
    private Text cellCountTitle;
    private Text wallCountTitle;
    private ArrayList<Text> cellTypeLabels;
    private ArrayList<Selector<Integer>> cellTypeSelects;
    private ArrayList<Text> wallCountLabels;
    private ArrayList<Selector<Integer>> wallCountSelects;

    // Render parameters
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
        this.componentFont = this.globalContext.gameFont().deriveFont(20.0f);
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
        this.addComponent(this.cellTypeTitle);
        this.addComponent(this.cellCountTitle);
        this.addComponent(this.wallTypeTitle);
        this.addComponent(this.wallCountTitle);

        for (Text wallCountLabel : this.wallCountLabels) {
            this.addComponent(wallCountLabel);
        }
        for (Selector<Integer> wallCountValue : this.wallCountSelects) {
            this.addComponent(wallCountValue);
        }
        for (Text cellTypeLabel : this.cellTypeLabels) {
            this.addComponent(cellTypeLabel);
        }
        for (Selector<Integer> cellTypeSelect : this.cellTypeSelects) {
            this.addComponent(cellTypeSelect);
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
        this.componentFont = this.globalContext.gameFont().deriveFont(20.0f);
        this.componentHeight = 46;
        this.componentWidth = componentHeight * 9;
        this.colonWidth = 6;

        this.setupBasicComponents();
        this.setupCellCountComponents();
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
            SetupTransferObject setupTransferObject = new SetupTransferObject(
                    this.widthCellsSelect.getSelectedOption(),
                    this.heightCellsSelect.getSelectedOption(),
                    this.specialCellsSelect.getSelectedOption(),
                    this.difficultySelect.getSelectedOption(),
                    this.getTotalSeconds(),
                    this.getCellCounts(),
                    this.getWallCounts(),
                    this.getPlayerSetups()
            );

            var response = this.globalContext.controller().createMatch(setupTransferObject);

            if (!response.ok) {
                Logger.error(response.message);
                JOptionPane.showMessageDialog(null, response.message, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Logger.success("Match created successfully");
        });

        this.backButton.addMouseListener(MouseEvent.EventType.RELEASED, event -> this.globalContext.controller().setGlobalState(GlobalState.WELCOME));

        this.difficultySelect.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                (_previousValue, _currentValue) -> this.updateDisabledTime()
        );
        this.specialCellsSelect.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                (_previousValue, _currentValue) -> this.updateDisabledCells()
        );
        this.widthCellsSelect.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                (_previousValue, _currentValue) -> this.updateMaxWallCount(this.getMaxWallCount())
        );
        this.heightCellsSelect.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                (_previousValue, _currentValue) -> this.updateMaxWallCount(this.getMaxWallCount())
        );

        for (int index = 0; index < this.playerColorSelects.size(); index++) {
            Selector<ColorName> colorNameSelector = this.playerColorSelects.get(index);
            Selector<PlayerType> playerTypeSelect = this.playerTypeSelects.get(index);
            TextInput playerNameInput = this.playerNameInputs.get(index);

            int finalIndex = index;

            colorNameSelector.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                    (previousValue, currentValue) -> this.updatePlayerFieldColor(finalIndex, (ColorName) previousValue, (ColorName) currentValue)
            );

            playerTypeSelect.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                    (_previousValue, _currentValue) -> this.updateDisabledAiProfiles(finalIndex)
            );

            playerNameInput.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                    (_previousValue, _currentValue) -> this.updateDisabledPlayers()
            );
        }


        for (Selector<Integer> wallCountSelector : this.wallCountSelects) {
            wallCountSelector.addComponentListener(GameComponent.ComponentEvent.VALUE_CHANGED,
                    (_previousValue, _currentValue) -> this.updateMaxWallCount(this.getMaxWallCount())
            );
        }
    }

    /**
     * Updates the scene.
     * <p>
     * This method updates the scene.
     * It's a protected and self-implemented method by the class.
     * </p>
     */
    @Override
    protected void fixCanvasSize() {
        int expectedHeight = this.startButton.getStyle().y + this.startButton.getStyle().height + this.margin;
        int expectedWidth = this.wallCountTitle.getStyle().x + this.wallCountTitle.getStyle().width + this.margin;

        this.globalContext.window().setCanvasHeight(expectedHeight);
        this.globalContext.window().setCanvasWidth(expectedWidth);
    }

    /**
     * Set up the basic components for the scene.
     * <p>
     * This method sets up the basic components for the scene.
     * It creates the difficulty selector, special cells selector, width and height selectors.
     * It also sets up the labels for each component.
     * </p>
     */
    private void setupBasicComponents() {
        ColorName componentsColor = ColorName.RED;

        ArrayList<DifficultyType> difficultyOptions = new ArrayList<>(Arrays.asList(DifficultyType.values()));
        this.difficultySelect = new Selector<>(difficultyOptions, this.globalContext);
        this.difficultySelect.getStyle().x = this.margin;
        this.difficultySelect.getStyle().y = this.margin;
        this.difficultySelect.getStyle().height = this.componentHeight;
        this.difficultySelect.getStyle().width = this.componentWidth;
        this.difficultySelect.getStyle().font = this.componentFont;

        this.timeLimitLabel = new Text("Time", this.globalContext);
        this.timeLimitLabel.getStyle().x = this.margin;
        this.timeLimitLabel.getStyle().y = this.paddingY + this.difficultySelect.getStyle().height + this.difficultySelect.getStyle().y;
        this.timeLimitLabel.getStyle().height = this.componentHeight;
        this.timeLimitLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.timeLimitLabel.getStyle().font = this.componentFont;
        this.timeLimitLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.timeLimitLabel.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);

        this.minutesInput = new TextInput(this.globalContext, "0");
        this.minutesInput.getStyle().x = this.paddingX + this.timeLimitLabel.getStyle().width + this.timeLimitLabel.getStyle().x;
        this.minutesInput.getStyle().y = this.timeLimitLabel.getStyle().y;
        this.minutesInput.getStyle().height = this.componentHeight;
        this.minutesInput.getStyle().width = this.componentWidth / 5 - (this.colonWidth + this.paddingX) / 2;
        this.minutesInput.getStyle().font = this.componentFont;
        this.minutesInput.getStyle().paddingX = 13;
        this.minutesInput.getStyle().textAlignment = Style.TextAlignment.CENTER;
        this.minutesInput.setValue("1");

        this.colonText = new Text(":", this.globalContext);
        this.colonText.getStyle().x = this.colonWidth + this.minutesInput.getStyle().width + this.minutesInput.getStyle().x;
        this.colonText.getStyle().y = this.minutesInput.getStyle().y;
        this.colonText.getStyle().height = this.componentHeight;
        this.colonText.getStyle().width = this.colonWidth;
        this.colonText.getStyle().font = this.componentFont;

        this.secondsInput = new TextInput(this.globalContext, "0");
        this.secondsInput.getStyle().x = this.colonWidth + this.colonText.getStyle().width + this.colonText.getStyle().x;
        this.secondsInput.getStyle().y = this.minutesInput.getStyle().y;
        this.secondsInput.getStyle().height = this.componentHeight;
        this.secondsInput.getStyle().width = (this.componentWidth / 5) - (this.colonWidth + this.paddingX) / 2;
        this.secondsInput.getStyle().font = this.componentFont;
        this.secondsInput.getStyle().paddingX = 13;
        this.secondsInput.getStyle().textAlignment = Style.TextAlignment.CENTER;
        this.secondsInput.setValue("0");

        this.updateDisabledTime();

        this.specialCellsLabel = new Text("Special Cells", this.globalContext);
        this.specialCellsLabel.getStyle().x = this.margin;
        this.specialCellsLabel.getStyle().y = this.paddingY + this.timeLimitLabel.getStyle().height + this.timeLimitLabel.getStyle().y;
        this.specialCellsLabel.getStyle().height = this.componentHeight;
        this.specialCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.specialCellsLabel.getStyle().font = this.componentFont;
        this.specialCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.specialCellsLabel.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

        this.specialCellsSelect = new Selector<>(false, this.globalContext);
        this.specialCellsSelect.getStyle().x = this.paddingX + this.specialCellsLabel.getStyle().width + this.specialCellsLabel.getStyle().x;
        this.specialCellsSelect.getStyle().y = this.specialCellsLabel.getStyle().y;
        this.specialCellsSelect.getStyle().height = this.componentHeight;
        this.specialCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.specialCellsSelect.getStyle().font = this.componentFont;
        this.specialCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.specialCellsSelect.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

        this.widthCellsLabel = new Text("Width", this.globalContext);
        this.widthCellsLabel.getStyle().x = this.margin;
        this.widthCellsLabel.getStyle().y = this.paddingY + this.specialCellsLabel.getStyle().height + this.specialCellsLabel.getStyle().y;
        this.widthCellsLabel.getStyle().height = this.componentHeight;
        this.widthCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.widthCellsLabel.getStyle().font = this.componentFont;
        this.widthCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.widthCellsLabel.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

        this.widthCellsSelect = new Selector<>(5, 14, this.globalContext);
        this.widthCellsSelect.getStyle().x = this.paddingX + this.widthCellsLabel.getStyle().width + this.widthCellsLabel.getStyle().x;
        this.widthCellsSelect.getStyle().y = this.widthCellsLabel.getStyle().y;
        this.widthCellsSelect.getStyle().height = this.componentHeight;
        this.widthCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.widthCellsSelect.getStyle().font = this.componentFont;
        this.widthCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.widthCellsSelect.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.widthCellsSelect.setSelectedOption(9);

        this.heightCellsLabel = new Text("Height", this.globalContext);
        this.heightCellsLabel.getStyle().x = this.margin;
        this.heightCellsLabel.getStyle().y = this.paddingY + this.widthCellsLabel.getStyle().height + this.widthCellsLabel.getStyle().y;
        this.heightCellsLabel.getStyle().height = this.componentHeight;
        this.heightCellsLabel.getStyle().width = 3 * (this.componentWidth - this.paddingX) / 5;
        this.heightCellsLabel.getStyle().font = this.componentFont;
        this.heightCellsLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.heightCellsLabel.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

        this.heightCellsSelect = new Selector<>(5, 14, this.globalContext);
        this.heightCellsSelect.getStyle().x = this.paddingX + this.heightCellsLabel.getStyle().width + this.heightCellsLabel.getStyle().x;
        this.heightCellsSelect.getStyle().y = this.heightCellsLabel.getStyle().y;
        this.heightCellsSelect.getStyle().height = this.componentHeight;
        this.heightCellsSelect.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 5;
        this.heightCellsSelect.getStyle().font = this.componentFont;
        this.heightCellsSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
        this.heightCellsSelect.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.heightCellsSelect.setSelectedOption(9);
    }

    /**
     * Set up the cell count components for the scene.
     * <p>
     * This method sets up the cell count components for the scene.
     * It creates the cell type labels and selectors for each cell type.
     * It also sets up the style for each component.
     * </p>
     */
    private void setupCellCountComponents() {
        ColorName componentsColor = ColorName.MAGENTA;

        this.cellTypeTitle = new Text("Cell Type", this.globalContext);
        this.cellTypeTitle.getStyle().x = this.difficultySelect.getStyle().x + this.difficultySelect.getStyle().width + this.margin;
        this.cellTypeTitle.getStyle().y = this.difficultySelect.getStyle().y;
        this.cellTypeTitle.getStyle().height = this.componentHeight;
        this.cellTypeTitle.getStyle().width = (this.componentWidth - this.paddingX) / 2;
        this.cellTypeTitle.getStyle().font = this.componentFont;
        this.cellTypeTitle.getStyle().backgroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.cellTypeTitle.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);

        this.cellCountTitle = new Text("Count", this.globalContext);
        this.cellCountTitle.getStyle().x = this.paddingX + this.cellTypeTitle.getStyle().width + this.cellTypeTitle.getStyle().x;
        this.cellCountTitle.getStyle().y = this.cellTypeTitle.getStyle().y;
        this.cellCountTitle.getStyle().height = this.componentHeight;
        this.cellCountTitle.getStyle().width = 2 * (this.componentWidth - this.paddingX) / 3;
        this.cellCountTitle.getStyle().font = this.componentFont;
        this.cellCountTitle.getStyle().backgroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.cellCountTitle.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);

        ArrayList<CellType> cellTypes = new ArrayList<>(Arrays.asList(CellType.values()));
        this.cellTypeLabels = new ArrayList<>();
        this.cellTypeSelects = new ArrayList<>();

        for (CellType cellType : cellTypes) {
            Text cellTypeLabel = new Text(cellType.toString(), this.globalContext);
            cellTypeLabel.getStyle().x = this.cellTypeTitle.getStyle().x;
            cellTypeLabel.getStyle().y = this.paddingY +
                    this.cellTypeTitle.getStyle().height +
                    this.cellTypeTitle.getStyle().y +
                    this.cellTypeLabels.size() * (this.paddingY + this.componentHeight);

            cellTypeLabel.getStyle().height = this.componentHeight;
            cellTypeLabel.getStyle().width = this.cellTypeTitle.getStyle().width;
            cellTypeLabel.getStyle().font = this.componentFont;
            cellTypeLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
            cellTypeLabel.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

            Selector<Integer> cellTypeSelector = new Selector<>(0, 15, this.globalContext);
            cellTypeSelector.getStyle().x = this.paddingX + cellTypeLabel.getStyle().width + cellTypeLabel.getStyle().x;
            cellTypeSelector.getStyle().y = cellTypeLabel.getStyle().y;
            cellTypeSelector.getStyle().height = this.componentHeight;
            cellTypeSelector.getStyle().width = this.cellCountTitle.getStyle().width;
            cellTypeSelector.getStyle().font = this.componentFont;
            cellTypeSelector.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            cellTypeSelector.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

            this.cellTypeLabels.add(cellTypeLabel);
            this.cellTypeSelects.add(cellTypeSelector);
        }

        this.updateDisabledCells();
    }

    /**
     * Set up the wall count components for the scene.
     * <p>
     * This method sets up the wall count components for the scene.
     * It creates the wall count labels and selectors for each wall type.
     * It also sets up the style for each component.
     * </p>
     */
    private void setupWallCountComponents() {
        ColorName componentsColor = ColorName.BLUE;

        this.wallTypeTitle = new Text("Wall Type", this.globalContext);
        this.wallTypeTitle.getStyle().x = this.cellCountTitle.getStyle().x + this.cellCountTitle.getStyle().width + this.margin;
        this.wallTypeTitle.getStyle().y = this.cellCountTitle.getStyle().y;
        this.wallTypeTitle.getStyle().height = this.componentHeight;
        this.wallTypeTitle.getStyle().width = (this.componentWidth - this.paddingX) / 2;
        this.wallTypeTitle.getStyle().font = this.componentFont;
        this.wallTypeTitle.getStyle().backgroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.wallTypeTitle.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);

        this.wallCountTitle = new Text("Count", this.globalContext);
        this.wallCountTitle.getStyle().x = this.paddingX + this.wallTypeTitle.getStyle().width + this.wallTypeTitle.getStyle().x;
        this.wallCountTitle.getStyle().y = this.wallTypeTitle.getStyle().y;
        this.wallCountTitle.getStyle().height = this.componentHeight;
        this.wallCountTitle.getStyle().width = this.cellCountTitle.getStyle().width;
        this.wallCountTitle.getStyle().font = this.componentFont;
        this.wallCountTitle.getStyle().backgroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);
        this.wallCountTitle.getStyle().foregroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.NORMAL);

        ArrayList<WallType> wallTypes = new ArrayList<>(Arrays.asList(WallType.values()));
        this.wallCountLabels = new ArrayList<>();
        this.wallCountSelects = new ArrayList<>();

        for (WallType wallType : wallTypes) {
            Text wallCountLabel = new Text(wallType.toString(), this.globalContext);
            wallCountLabel.getStyle().x = this.wallTypeTitle.getStyle().x;
            wallCountLabel.getStyle().y = this.paddingY +
                    this.wallTypeTitle.getStyle().height +
                    this.wallTypeTitle.getStyle().y +
                    this.wallCountLabels.size() * (this.paddingY + this.componentHeight);

            wallCountLabel.getStyle().height = this.componentHeight;
            wallCountLabel.getStyle().width = this.wallTypeTitle.getStyle().width;
            wallCountLabel.getStyle().font = this.componentFont;
            wallCountLabel.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
            wallCountLabel.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

            Selector<Integer> wallCountSelector = new Selector<>(0, 15, this.globalContext);
            wallCountSelector.getStyle().x = this.paddingX + wallCountLabel.getStyle().width + wallCountLabel.getStyle().x;
            wallCountSelector.getStyle().y = wallCountLabel.getStyle().y;
            wallCountSelector.getStyle().height = this.componentHeight;
            wallCountSelector.getStyle().width = this.wallCountTitle.getStyle().width;
            wallCountSelector.getStyle().font = this.componentFont;
            wallCountSelector.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            wallCountSelector.getStyle().foregroundColor = new ThemeColor(componentsColor, ColorVariant.NORMAL);

            this.wallCountLabels.add(wallCountLabel);
            this.wallCountSelects.add(wallCountSelector);
        }

        this.updateMaxWallCount(this.getMaxWallCount());
    }

    /**
     * Sets up the player components for the scene.
     * <p>
     * This method sets up the player components for the scene.
     * It creates the player name inputs, color selectors, player type selectors and AI profile selectors.
     * It also sets up the style for each component.
     * </p>
     */
    private void setupPlayerComponents() {
        int playerCount = 4;
        this.playerNameInputs = new ArrayList<>();
        this.playerColorSelects = new ArrayList<>();
        this.playerTypeSelects = new ArrayList<>();
        this.aiProfileSelects = new ArrayList<>();

        ArrayList<PlayerType> playerTypes = new ArrayList<>(Arrays.asList(PlayerType.values()));
        ArrayList<AIProfile> aiProfiles = new ArrayList<>(Arrays.asList(AIProfile.values()));

        ArrayList<ColorName> colorNames = this.globalContext.currentTheme().getPlayerColors();

        for (int index = 0; index < playerCount; index++) {

            TextInput playerNameInput = new TextInput(this.globalContext, String.format("Enter Player %d", index + 1));
            playerNameInput.setMaxLength(9);
            playerNameInput.getStyle().x = this.cellTypeTitle.getStyle().x;
            playerNameInput.getStyle().y =
                    playerNameInput.getStyle().height = this.componentHeight;
            playerNameInput.getStyle().width = this.cellTypeTitle.getStyle().width;
            playerNameInput.getStyle().font = this.componentFont;
            playerNameInput.getStyle().textAlignment = Style.TextAlignment.CENTER;
            this.playerNameInputs.add(playerNameInput);

            if (index == 0) {
                playerNameInput.getStyle().y = this.margin + this.heightCellsLabel.getStyle().y + this.heightCellsLabel.getStyle().height;
            } else {
                playerNameInput.getStyle().y = this.playerNameInputs.get(index - 1).getStyle().y + playerNameInput.getStyle().height + this.paddingY;
            }


            Selector<ColorName> playerColorInput = new Selector<>(colorNames, this.globalContext);
            playerColorInput.getStyle().x = playerNameInput.getStyle().x + playerNameInput.getStyle().width + this.paddingX;
            playerColorInput.getStyle().y = playerNameInput.getStyle().y;
            playerColorInput.getStyle().height = this.componentHeight;
            playerColorInput.getStyle().width = this.cellCountTitle.getStyle().width;
            playerColorInput.getStyle().font = this.componentFont;
            playerColorInput.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            playerColorInput.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
            playerColorInput.setSelectedIndex(index % colorNames.size());

            this.playerColorSelects.add(playerColorInput);

            Selector<PlayerType> playerTypeSelect = new Selector<>(playerTypes, this.globalContext);
            playerTypeSelect.getStyle().x = this.wallTypeTitle.getStyle().x;
            playerTypeSelect.getStyle().y = playerNameInput.getStyle().y;
            playerTypeSelect.getStyle().height = this.componentHeight;
            playerTypeSelect.getStyle().width = this.wallTypeTitle.getStyle().width;
            playerTypeSelect.getStyle().font = this.componentFont;
            playerTypeSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            playerTypeSelect.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
            this.playerTypeSelects.add(playerTypeSelect);

            Selector<AIProfile> aiProfileSelect = new Selector<>(aiProfiles, this.globalContext);
            aiProfileSelect.getStyle().x = playerTypeSelect.getStyle().x + playerTypeSelect.getStyle().width + this.paddingX;
            aiProfileSelect.getStyle().y = playerNameInput.getStyle().y;
            aiProfileSelect.getStyle().height = this.componentHeight;
            aiProfileSelect.getStyle().width = this.wallCountTitle.getStyle().width;
            aiProfileSelect.getStyle().font = this.componentFont;
            aiProfileSelect.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.BRIGHT);
            aiProfileSelect.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);
            this.aiProfileSelects.add(aiProfileSelect);

            this.updatePlayerFieldColor(index, null, colorNames.get(index % colorNames.size()));
            this.updateDisabledAiProfiles(index);
        }

        this.updateDisabledPlayers();
    }

    /**
     * Sets up the buttons for the scene.
     *
     * <p>
     * This method sets up the buttons for the scene.
     * It creates the start and back buttons.
     * It also sets up the style for each component.
     * <p>
     */
    private void setupButtons() {
        GameComponent lastPlayerType = this.playerTypeSelects.get(this.playerTypeSelects.size() - 1);

        this.backButton = new Button("Cancel", this.globalContext);
        this.backButton.getStyle().x = this.wallTypeTitle.getStyle().x;
        this.backButton.getStyle().y = lastPlayerType.getStyle().y + lastPlayerType.getStyle().height + this.margin;
        this.backButton.getStyle().height = this.componentHeight;
        this.backButton.getStyle().width = this.wallTypeTitle.getStyle().width;
        this.backButton.getStyle().font = this.componentFont;
        this.backButton.getStyle().backgroundColor = new ThemeColor(ColorName.BACKGROUND, ColorVariant.DIMMED);
        this.backButton.getStyle().foregroundColor = new ThemeColor(ColorName.FOREGROUND, ColorVariant.NORMAL);

        this.startButton = new Button("Start", this.globalContext);
        this.startButton.getStyle().x = this.backButton.getStyle().x + this.backButton.getStyle().width + this.paddingX;
        this.startButton.getStyle().y = this.backButton.getStyle().y;
        this.startButton.getStyle().height = this.componentHeight;
        this.startButton.getStyle().width = this.wallCountTitle.getStyle().width;
        this.startButton.getStyle().font = this.componentFont;
    }

    /**
     * Updates the player field color.
     * <p>
     * This method updates the player field color.
     * It changes the color of the player field based on the selected color.
     * It also updates the color of the other player fields if they have the same color.
     * </p>
     *
     * @param index             the index of the player field.
     * @param previousColorName the previous color name.
     * @param colorName         the color name.
     */
    private void updatePlayerFieldColor(int index, ColorName previousColorName, ColorName colorName) {

        this.playerNameInputs.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.playerNameInputs.get(index).getStyle().borderColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.playerColorSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.playerTypeSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);
        this.aiProfileSelects.get(index).getStyle().foregroundColor = new ThemeColor(colorName, ColorVariant.NORMAL);

        if (previousColorName == null) {
            return;
        }

        for (int j = 0; j < this.playerColorSelects.size(); j++) {
            Selector<ColorName> colorNameSelector = this.playerColorSelects.get(j);
            TextInput playerNameInput = this.playerNameInputs.get(j);
            ColorVariant variant = playerNameInput.getValue().isEmpty() ? ColorVariant.DIMMED : ColorVariant.NORMAL;

            if (index != j && colorNameSelector.getSelectedOption() == colorName) {

                colorNameSelector.setSelectedOption(previousColorName);

                colorNameSelector.getStyle().foregroundColor = new ThemeColor(previousColorName, variant);
                this.playerTypeSelects.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, variant);
                this.aiProfileSelects.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, variant);
                this.playerNameInputs.get(j).getStyle().foregroundColor = new ThemeColor(previousColorName, ColorVariant.NORMAL);
            }

        }
        this.updateDisabledPlayers();
    }

    /**
     * Updates the maximum wall count.
     * <p>
     * This method updates the maximum wall count.
     * It changes the maximum wall count based on the selected width and height.
     * It also updates the maximum wall count for each wall type.
     * </p>
     *
     * @param maxWallCount the maximum wall count.
     */
    private void updateMaxWallCount(int maxWallCount) {

        for (Selector<Integer> wallCountSelector : this.wallCountSelects) {
            if (wallCountSelector.getSelectedOption() > 0) {
                wallCountSelector.setMax(maxWallCount - this.getCurrentWallCount() + wallCountSelector.getSelectedOption());
            }
        }
    }

    /**
     * Updates the disabled players.
     * <p>
     * This method updates the disabled players.
     * It disables the player fields if the player name is empty.
     * It also updates the disabled player fields based on the player type and AI profile.
     * </p>
     */
    private void updateDisabledPlayers() {
        for (int index = 0; index < this.playerNameInputs.size(); index++) {
            TextInput playerNameInput = this.playerNameInputs.get(index);

            boolean isDisabled = playerNameInput.getValue().isEmpty();

            this.playerColorSelects.get(index).setDisabled(isDisabled);
            this.playerTypeSelects.get(index).setDisabled(isDisabled);
            this.aiProfileSelects.get(index).setDisabled(isDisabled);

            this.updateDisabledAiProfiles(index);
        }
    }

    /**
     * Updates the disabled cells.
     * <p>
     * This method updates the disabled cells.
     * It disables the cell type fields if the special cells are disabled.
     * </p>
     */
    private void updateDisabledCells() {
        boolean cellsDisabled = this.specialCellsSelect.getSelectedOption().equals(false);
        for (Selector<Integer> cellTypeSelector : this.cellTypeSelects) {
            cellTypeSelector.setDisabled(cellsDisabled);
        }
        for (Text cellTypeLabel : this.cellTypeLabels) {
            cellTypeLabel.setDisabled(cellsDisabled);
        }
    }

    /**
     * Updates the disabled time.
     * <p>
     * This method updates the disabled time.
     * It disables the time limit fields if the difficulty is normal.
     * </p>
     */
    private void updateDisabledTime() {
        boolean timeDisabled = this.difficultySelect.getSelectedOption().equals(DifficultyType.NORMAL);
        this.timeLimitLabel.setDisabled(timeDisabled);
        this.colonText.setDisabled(timeDisabled);
        this.minutesInput.setDisabled(timeDisabled);
        this.secondsInput.setDisabled(timeDisabled);
    }

    /**
     * Updates the disabled AI profiles.
     * <p>
     * This method updates the disabled AI profiles.
     * It disables the AI profile fields if the player type is not AI or the player name is empty.
     * </p>
     *
     * @param index the index of the AI profile field.
     */
    private void updateDisabledAiProfiles(int index) {
        Selector<AIProfile> aiProfileSelect = this.aiProfileSelects.get(index);
        PlayerType playerType = this.playerTypeSelects.get(index).getSelectedOption();
        aiProfileSelect.setDisabled(!playerType.equals(PlayerType.AI) || this.playerNameInputs.get(index).getValue().isEmpty());
    }

    /**
     * Gets the wall counts.
     * <p>
     * This method gets the wall counts.
     * It returns a hash map with the wall type and the wall count.
     * </p>
     *
     * @return the wall counts.
     */
    private HashMap<WallType, Integer> getWallCounts() {
        HashMap<WallType, Integer> wallCounts = new HashMap<>();

        for (int index = 0; index < this.wallCountSelects.size(); index++) {
            Selector<Integer> wallCountSelector = this.wallCountSelects.get(index);
            WallType wallType = WallType.values()[index];

            wallCounts.put(wallType, wallCountSelector.getSelectedOption());
        }

        return wallCounts;
    }

    /**
     * Gets the player setups.
     * <p>
     * This method gets the player setups.
     * It returns an array list with the player setups.
     * </p>
     *
     * @return the player setups.
     */
    private ArrayList<PlayerSetupTransferObject> getPlayerSetups() {
        ArrayList<PlayerSetupTransferObject> playerSetups = new ArrayList<>();

        for (int index = 0; index < this.playerNameInputs.size(); index++) {
            TextInput playerNameInput = this.playerNameInputs.get(index);

            if (playerNameInput.getValue().isEmpty()) {
                continue;
            }

            Selector<PlayerType> playerTypeSelect = this.playerTypeSelects.get(index);
            Selector<AIProfile> aiProfileSelect = this.aiProfileSelects.get(index);

            PlayerSetupTransferObject playerSetup = new PlayerSetupTransferObject(
                    playerNameInput.getValue(),
                    playerTypeSelect.getSelectedOption(),
                    aiProfileSelect.getSelectedOption()
            );

            playerSetups.add(playerSetup);
        }

        return playerSetups;
    }

    /**
     * Gets the cell counts.
     * <p>
     * This method gets the cell counts.
     * It returns a hash map with the cell type and the cell count.
     * </p>
     *
     * @return the cell counts.
     */
    private HashMap<CellType, Integer> getCellCounts() {
        HashMap<CellType, Integer> cellCounts = new HashMap<>();

        for (int index = 0; index < this.cellTypeSelects.size(); index++) {
            Selector<Integer> cellTypeSelector = this.cellTypeSelects.get(index);
            CellType cellType = CellType.values()[index];

            cellCounts.put(cellType, cellTypeSelector.getSelectedOption());
        }

        return cellCounts;
    }

    /**
     * Gets the total seconds.
     * <p>
     * This method gets the total seconds.
     * It returns the total seconds based on the minutes and seconds input fields.
     * </p>
     *
     * @return the total seconds.
     */
    private int getTotalSeconds() {
        int minutes = Integer.parseInt(this.minutesInput.getValue());
        int seconds = Integer.parseInt(this.secondsInput.getValue());

        return minutes * 60 + seconds;
    }

    /**
     * Gets the maximum wall count.
     * <p>
     * This method gets the maximum wall count.
     * It returns the maximum wall count based on the selected width and height.
     * </p>
     *
     * @return the maximum wall count.
     */
    private int getMaxWallCount() {
        int width = this.widthCellsSelect.getSelectedOption();
        int height = this.heightCellsSelect.getSelectedOption();

        return Math.min(width, height) + 1;
    }

    /**
     * Gets the current wall count.
     * <p>
     * This method gets the current wall count.
     * It returns the current wall count based on the selected wall counts.
     * </p>
     *
     * @return the current wall count.
     */
    private int getCurrentWallCount() {
        int wallCount = 0;

        for (Selector<Integer> wallCountSelector : this.wallCountSelects) {
            wallCount += wallCountSelector.getSelectedOption();
        }

        return wallCount;
    }
}
