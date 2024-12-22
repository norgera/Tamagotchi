package ui;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import logic.Pet;
import logic.PetType;
import logic.SaveManager;
import logic.VitalStatistics;

public class GameplayScreen extends JFrame {
    private Image backgroundImage;
    private Map<String, Integer> foodInventory;
    private Map<String, Integer> giftInventory;
    VitalStatistics stats;
    JPanel statsPanel;
    JLabel petSpriteLabel; // JLabel for the pet sprite
    Pet pet;
    private Timer decayTimer;
    private int decayInterval = 60000;
    private Timer spriteMovementTimer; // Timer for the sprite movement
    private boolean movingUp = true; // Direction of movement

    private String currentSpritePath = "/resources/dogIdle.png"; // Default sprite path

    // Declare buttons as instance variables
    JButton sleepButton;
    JButton playButton;
    JButton exerciseButton;
    JButton vetButton;
    JButton feedButton;
    JButton giveGiftButton;
    JButton viewInventoryButton;
    JButton exitButton;
    JButton saveLoadButton;
    JButton miniGame;
    private Timer sleepRecoveryTimer; // Timer for faster sleep recovery

    // Score label
    JLabel scoreLabel;
    int score = 0; // Initial score

    // List all possible items (Foods and gift)
    private String[] possibleItems = {"Apple", "Banana", "Carrot", "Ball", "Toy Mouse", "Puzzle"};

    /**
     * Constructor for loading an existing pet.
     * Initializes the gameplay screen with the provided pet's state and stats.
     *
     * @param pet the pet object representing the player's pet.
     */
    public GameplayScreen(Pet pet) {
        this.pet = pet;
        this.stats = pet.getVitalStats();

        // Safely load food and gift inventories or initialize them if null
        foodInventory = pet.getFoodInventory() != null ? pet.getFoodInventory() : new HashMap<>();
        giftInventory = pet.getGiftInventory() != null ? pet.getGiftInventory() : new HashMap<>();

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/gameScreen.png")).getImage();

        setTitle("Gameplay Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the decay timer
        decayTimer = new Timer(decayInterval, e -> applyDecay(pet.getType()));
        decayTimer.start();

        // Initialize UI components
        initializeUI();
    }

    /**
     * Constructor for starting a new game.
     * Sets up a new pet with default stats and inventories.
     *
     * @param petName the name of the new pet.
     * @param petType the type of the new pet.
     */
    public GameplayScreen(String petName, PetType petType) {
        // Initialize the VitalStatistics object with half stats
        int initialHealth = petType.getMaxHealth() / 2;
        int initialSleep = petType.getMaxSleep() / 2;
        int initialFullness = petType.getMaxFullness() / 2;
        int initialHappiness = petType.getMaxHappiness() / 2;

        VitalStatistics stats = new VitalStatistics(initialHealth, initialSleep, initialFullness, initialHappiness);

        // Initialize the Pet object
        Pet pet = new Pet(petName, petType);
        pet.setVitalStats(stats);

        // Default inventories for a new game
        foodInventory = new HashMap<>();
        foodInventory.put("Apple", 3);
        foodInventory.put("Banana", 2);
        foodInventory.put("Carrot", 5);

        giftInventory = new HashMap<>();
        giftInventory.put("Ball", 1);
        giftInventory.put("Toy Mouse", 2);
        giftInventory.put("Puzzle", 1);

        // Set inventories in the pet object
        pet.setFoodInventory(foodInventory);
        pet.setGiftInventory(giftInventory);

        // Assign pet and stats
        this.pet = pet;
        this.stats = pet.getVitalStats();

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/gameScreen.png")).getImage();

        setTitle("Gameplay Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize the decay timer
        decayTimer = new Timer(decayInterval, e -> applyDecay(pet.getType()));
        decayTimer.start();

        // Initialize UI components
        initializeUI();
    }

    private void initializeUI() {
        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);
        setupSpriteMovement();

        // Display welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + pet.getName() + " (" + pet.getType().getTypeName() + ")!");
        welcomeLabel.setBounds(50, 20, 700, 30);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(welcomeLabel);

        // Pet sprite label for displaying the pet image
        petSpriteLabel = new JLabel();
        petSpriteLabel.setBounds(200, 100, 400, 300); // Adjust these values to fit within the blank part of the screen
        petSpriteLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(petSpriteLabel);

        // Set the initial pet sprite image
        updatePetSprite(currentSpritePath);
        // "Exit" Button
        exitButton = createInvisibleButton(19, 16, 80, 42);
        exitButton.addActionListener(e -> handleExit());
        mainPanel.add(exitButton);

        // "Save/Load" Button
        saveLoadButton = createInvisibleButton(644, 14, 130, 42);
        saveLoadButton.addActionListener(e -> handleSaveLoad());
        mainPanel.add(saveLoadButton);

        // Stats Panel for health bars
        statsPanel = new JPanel(null);
        statsPanel.setBounds(500, 440, 300, 160);
        statsPanel.setOpaque(false);

        // Add individual stat rows using dynamic values
        refreshStatsPanel();

        // Add stats panel to the main panel
        mainPanel.add(statsPanel);

        // Invisible Sleep Button
        sleepButton = createInvisibleButton(63, 455, 160, 35);
        sleepButton.addActionListener(e -> {
            if (pet.isAngry()) {
                JOptionPane.showMessageDialog(this, "Your pet is angry and refuses to sleep!");
                return;
            }
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is already sleeping!");
                return;
            }
            pet.goToBed();
            pet.checkStates();
            enforceStatLimits();
            refreshStatsPanel();
            JOptionPane.showMessageDialog(this, "Your pet is now well-rested!");
        });
        mainPanel.add(sleepButton);

        // Play Button
        playButton = createInvisibleButton(255, 505, 80, 35);
        playButton.addActionListener(e -> {
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is sleeping and cannot play right now!");
                return;
            }
            JOptionPane.showMessageDialog(this,pet.play(1));
            pet.checkStates();
            enforceStatLimits();
            refreshStatsPanel();
        });
        mainPanel.add(playButton);

        // Exercise Button
        exerciseButton = createInvisibleButton(338, 505, 120, 35);
        exerciseButton.addActionListener(e -> {
            if (pet.isAngry()) {
                JOptionPane.showMessageDialog(this, "Your pet is angry and refuses to exercise!");
                return;
            }
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is sleeping and cannot exercise right now!");
                return;
            }
            pet.exercise(1, 1, 1);
            pet.checkStates();
            enforceStatLimits();
            refreshStatsPanel();
            JOptionPane.showMessageDialog(this, "You exercised your pet!");
        });
        mainPanel.add(exerciseButton);

        // Vet Button
        vetButton = createInvisibleButton(63, 505, 190, 35);
        vetButton.addActionListener(e -> {
            if (pet.isAngry()) {
                JOptionPane.showMessageDialog(this, "Your pet is angry and refuses to go to the vet!");
                return;
            }
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is sleeping and cannot go to the vet right now!");
                return;
            }
            JOptionPane.showMessageDialog(this, pet.takeToVet());
            pet.checkStates();
            enforceStatLimits();
            refreshStatsPanel();
        });
        mainPanel.add(vetButton);

        // Invisible Feed Button
        feedButton = createInvisibleButton(220, 455, 100, 35);
        feedButton.addActionListener(e -> {
            if (pet.isAngry()) {
                JOptionPane.showMessageDialog(this, "Your pet is angry and refuses to eat!");
                return;
            }
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is sleeping and cannot eat right now!");
                return;
            }
            openFeedPopup();
        });
        mainPanel.add(feedButton);

        // Invisible Give Gift Button
        giveGiftButton = createInvisibleButton(330, 450, 135, 35);
        giveGiftButton.addActionListener(e -> {
            if (pet.isSleeping()) {
                JOptionPane.showMessageDialog(this, "Your pet is sleeping and cannot receive gifts right now!");
                return;
            }
            openGiveGiftPopup();
        });
        mainPanel.add(giveGiftButton);

        // Invisible View Inventory Button
        viewInventoryButton = createInvisibleButton(590, 373, 150, 35);
        viewInventoryButton.addActionListener(e -> openInventoryPopup());
        mainPanel.add(viewInventoryButton);

        // Set up score label
        scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setBounds(300, 42, 200, 30); // Position at the top-center
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.BLACK);
        mainPanel.add(scoreLabel);

        // Mini Game Button
        miniGame = createInvisibleButton(60, 373, 150, 35); // Position and size
        miniGame.addActionListener(e -> handleMathQuestion()); // Handle click
        mainPanel.add(miniGame); // Add to panel

        // Add the main panel to the frame
        setContentPane(mainPanel);

    }
    /**
     * Updates the pet sprite with the specified image path.
     *
     * @param imagePath the relative path to the sprite image.
     */
    private void updatePetSprite(String imagePath) {
        try {
            java.net.URL imageURL = getClass().getResource(imagePath);
            if (imageURL == null) {
                throw new Exception("Sprite not found: " + imagePath);
            }
            ImageIcon icon = new ImageIcon(imageURL);
            petSpriteLabel.setIcon(icon);
        } catch (Exception e) {
            System.err.println("Failed to load sprite: " + imagePath + " - " + e.getMessage());
            petSpriteLabel.setIcon(new ImageIcon(getClass().getResource("/resources/defaultIdle.png")));
        }
    }

    /**
     * Handles the "Exit" button action.
     * Stops all timers and transitions to the main menu.
     */
    private void handleExit() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to exit to the main menu?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            stopAllTimers(); // Stop all active timers
            decayTimer.stop(); // Stop the decay timer
            this.setVisible(false); // Hide the GameplayScreen
            MainMenu.getInstance().setVisible(true); // Show the MainMenu
        }
    }
    /**
     * Stops all active timers in the gameplay screen.
     */
    private void stopAllTimers() {
        if (decayTimer != null && decayTimer.isRunning()) {
            decayTimer.stop();
        }
        if (spriteMovementTimer != null && spriteMovementTimer.isRunning()) {
            spriteMovementTimer.stop();
        }
    }

    /**
     * Handles the "Save/Load" button action.
     * Transitions to the Save/Load screen while passing the current pet's state.
     */
    private void handleSaveLoad() {
        stopAllTimers(); // Stop all active timers
        this.setVisible(false); // Hide the GameplayScreen
        SaveLoadGameScreen saveLoadGameScreen = new SaveLoadGameScreen(pet, this); // Pass the current pet and GameplayScreen
        saveLoadGameScreen.setVisible(true); // Show the SaveLoadGameScreen
    }
    /**
     * Creates a row in the stats panel with aligned labels and stat circles.
     *
     * @param labelText   the label text for the stat.
     * @param statValue   the current value of the stat.
     * @param maxStatValue the maximum value of the stat.
     * @param yPosition   the vertical position of the row.
     * @param isWarning   whether the stat is in a warning state.
     * @return a JPanel representing the stat row.
     */
    private JPanel createAlignedStatRow(String labelText, int statValue, int maxStatValue, int yPosition, boolean isWarning) {
        int redCircles = statValue; // Number of filled circles
        int whiteCircles = maxStatValue - statValue; // Number of unfilled circles

        JPanel rowPanel = new JPanel(null);
        rowPanel.setBounds(0, yPosition, 300, 30);
        rowPanel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setBounds(0, 5, 80, 20);
        label.setFont(new Font("Arial", Font.BOLD, 14));

        // Change color based on warning status
        if (isWarning) {
            label.setForeground(Color.RED); // Warning: Red text
        } else {
            label.setForeground(Color.BLACK); // Normal: Black text
        }
        rowPanel.add(label);

        int xOffset = 85; // Starting position for circles
        for (int i = 0; i < redCircles; i++) {
            JLabel circle = createCircle(Color.RED);
            circle.setBounds(xOffset, 5, 20, 20);
            rowPanel.add(circle);
            xOffset += 25;
        }

        for (int i = 0; i < whiteCircles; i++) {
            JLabel circle = createCircle(Color.WHITE);
            circle.setBounds(xOffset, 5, 20, 20);
            rowPanel.add(circle);
            xOffset += 25;
        }

        return rowPanel;
    }

    /**
     * Creates a circle label for displaying a stat.
     *
     * @param color the color of the circle.
     * @return a JLabel representing the circle.
     */
    private JLabel createCircle(Color color) {
        JLabel circle = new JLabel();
        circle.setOpaque(true);
        circle.setBackground(color); // Circle color
        circle.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add black border
        return circle;
    }

    /**
     * Creates an invisible button with the specified dimensions.
     *
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured to be invisible.
     */
    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height); // Position and size of the button
        button.setOpaque(false); // Make the button transparent
        button.setContentAreaFilled(false); // No background fill
        button.setBorderPainted(false); // No border
        button.setFocusPainted(false); // No focus outline
        return button;
    }

    /**
     * Opens a custom popup for selecting food to feed the pet.
     */
    private void openFeedPopup() {
        openItemPopup("Select Food", foodInventory,
                "You fed your pet:");
    }

    /**
     * Opens a custom popup for selecting a gift to give to the pet.
     */
    private void openGiveGiftPopup() {
        openItemPopup("Select Gift", giftInventory,
                "You gave your pet:");
    }

    /**
     * Opens a popup displaying the current inventory of food and gifts.
     */
    private void openInventoryPopup() {
        JDialog inventoryDialog = new JDialog(this, "Current Inventory", true);
        inventoryDialog.setSize(300, 300);
        inventoryDialog.setLocationRelativeTo(this);
        inventoryDialog.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Current Items:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        inventoryDialog.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<String> inventoryListModel = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : foodInventory.entrySet()) {
            inventoryListModel.addElement(entry.getValue() + "x " + entry.getKey());
        }
        for (Map.Entry<String, Integer> entry : giftInventory.entrySet()) {
            inventoryListModel.addElement(entry.getValue() + "x " + entry.getKey());
        }
        JList<String> inventoryList = new JList<>(inventoryListModel);
        inventoryList.setFont(new Font("Arial", Font.PLAIN, 14));
        inventoryDialog.add(new JScrollPane(inventoryList), BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> inventoryDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        inventoryDialog.add(buttonPanel, BorderLayout.SOUTH);

        inventoryDialog.setVisible(true);
    }

    /**
     * Generalized method for opening a popup to select an item from the inventory.
     *
     * @param title         the title of the popup.
     * @param inventory     the map of items in the inventory.
     * @param actionMessage the message to display upon selecting an item.
     */
    private void openItemPopup(String title, Map<String, Integer> inventory, String actionMessage) {
        JDialog itemDialog = new JDialog(this, title, true);
        itemDialog.setSize(300, 300);
        itemDialog.setLocationRelativeTo(this);
        itemDialog.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel(title + ":");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 14));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        itemDialog.add(instructionLabel, BorderLayout.NORTH);

        DefaultListModel<String> itemListModel = new DefaultListModel<>();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            itemListModel.addElement(entry.getValue() + "x " + entry.getKey());
        }
        JList<String> itemList = new JList<>(itemListModel);
        itemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        itemList.setFont(new Font("Arial", Font.PLAIN, 14));
        itemDialog.add(new JScrollPane(itemList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton selectButton = new JButton("Select");
        JButton cancelButton = new JButton("Cancel");

        selectButton.addActionListener(e -> {
            String selectedItem = itemList.getSelectedValue();
            if (selectedItem != null) {
                String itemName = selectedItem.split(" ", 2)[1];
                performAction(inventory, itemName, actionMessage);
                itemDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(itemDialog, "Please select an item.");
            }
        });

        cancelButton.addActionListener(e -> itemDialog.dispose());

        buttonPanel.add(selectButton);
        buttonPanel.add(cancelButton);
        itemDialog.add(buttonPanel, BorderLayout.SOUTH);

        itemDialog.setVisible(true);
    }

    /**
     * Performs an action (e.g., feeding or giving a gift) with the selected item.
     *
     * @param inventory     the map of items in the inventory.
     * @param itemName      the name of the selected item.
     * @param actionMessage the message to display after performing the action.
     */
    private void performAction(Map<String, Integer> inventory, String itemName, String actionMessage) {
        if (inventory.containsKey(itemName)) {
            int quantity = inventory.get(itemName);
            if (quantity > 0) {
                inventory.put(itemName, quantity - 1); // Decrease item count
                JOptionPane.showMessageDialog(this, actionMessage + " " + itemName + "!");

                // Update stats based on the action
                if (actionMessage.contains("fed")) {
                    pet.feed(2); // Feeding increases fullness
                    updateScore(5); // Increase score for feeding
                } else if (actionMessage.contains("gave your pet")) {
                    pet.giveGift(2); // Giving gifts increases happiness
                    updateScore(5); // Increase score for feeding
                }

                // Update the pet's inventory
                pet.setFoodInventory(foodInventory);
                pet.setGiftInventory(giftInventory);

                // ** Fix: Ensure pet's states are updated after modifying stats **
                pet.checkStates(); // Update the pet's states based on new stats

                // ** Fix: Check if pet is no longer angry and display a message **
                if (!pet.isAngry() && actionMessage.contains("gave your pet")) {
                    JOptionPane.showMessageDialog(this, "Your pet is no longer angry!");
                }

                // Ensure the stats are updated and the UI reflects the changes
                enforceStatLimits();
                refreshStatsPanel();
            } else {
                JOptionPane.showMessageDialog(this, "You don't have any " + itemName + " left!");
            }
        }
    }

    /**
     * Ensures that all pet stats are within their valid limits.
     */
    private void enforceStatLimits() {
        stats.setHealth(Math.min(stats.getHealth(), pet.getType().getMaxHealth()));
        stats.setSleep(Math.min(stats.getSleep(), pet.getType().getMaxSleep()));
        stats.setFullness(Math.min(stats.getFullness(), pet.getType().getMaxFullness()));
        stats.setHappiness(Math.min(stats.getHappiness(), pet.getType().getMaxHappiness()));

        // Ensure stats do not fall below 0
        stats.setHealth(Math.max(stats.getHealth(), 0));
        stats.setSleep(Math.max(stats.getSleep(), 0));
        stats.setFullness(Math.max(stats.getFullness(), 0));
        stats.setHappiness(Math.max(stats.getHappiness(), 0));
    }
    /**
     * Refreshes the stats panel to reflect the current state of the pet's stats.
     */
    private void refreshStatsPanel() {
        statsPanel.removeAll(); // Clear all components in the stats panel

        statsPanel.add(createAlignedStatRow(
                "Health:",
                stats.getHealth(),
                pet.getType().getMaxHealth(),
                0,
                stats.getHealth() < pet.getType().getMaxHealth() * 0.25
        ));
        statsPanel.add(createAlignedStatRow(
                "Sleep:",
                stats.getSleep(),
                pet.getType().getMaxSleep(),
                30,
                stats.getSleep() < pet.getType().getMaxSleep() * 0.25
        ));
        statsPanel.add(createAlignedStatRow(
                "Fullness:",
                stats.getFullness(),
                pet.getType().getMaxFullness(),
                60,
                stats.getFullness() < pet.getType().getMaxFullness() * 0.25
        ));
        statsPanel.add(createAlignedStatRow(
                "Happiness:",
                stats.getHappiness(),
                pet.getType().getMaxHappiness(),
                90,
                stats.getHappiness() < pet.getType().getMaxHappiness() * 0.25
        ));

        statsPanel.revalidate(); // Refresh the layout of the panel
        statsPanel.repaint();    // Repaint the panel to display updated stat rows

        updatePetSpriteBasedOnMood(); // Update the sprite based on the pet's current mood
    }

    /**
     * Updates the pet sprite based on its current mood and stats.
     */
    private void updatePetSpriteBasedOnMood() {
        currentSpritePath = determineMoodSprite(); // Determine the correct sprite path
        updatePetSprite(currentSpritePath); // Update the JLabel with the new sprite
    }
    /**
     * Applies decay to the pet's stats over time.
     *
     * @param petType the type of the pet, which determines decay rates.
     */
    private void applyDecay(PetType petType) {
        if (!this.isVisible()) {
            return; // Prevent decay logic from running if the screen is exited
        }

        int criticalStatsCount = 0;

        // Handle sleep decay
        if (pet.isSleeping()) {
            if (sleepRecoveryTimer == null || !sleepRecoveryTimer.isRunning()) {
                startSleepRecoveryTimer(); // Start sleep recovery timer
            }
        } else {
            stats.decreaseSleep(petType.getSleepDecayRate());
            if (stats.getSleep() <= 0) {
                stats.setSleep(0);
                stats.decreaseHealth(2); // Apply health penalty for exhaustion
                pet.checkStates();
                JOptionPane.showMessageDialog(this, "Your pet has fallen asleep due to exhaustion.");
                disableInteraction(); // Disable interactions
                startSleepRecoveryTimer(); // Start sleep recovery
            } else if (stats.getSleep() < pet.getType().getMaxSleep() * 0.25) {
                criticalStatsCount++; // Sleep is critical
            }
        }

        // Handle fullness decay
        stats.decreaseFullness(petType.getFullnessDecayRate());
        if (stats.getFullness() <= 0) {
            stats.setFullness(0);
            stats.decreaseHealth(1); // Health decreases due to hunger
            pet.checkStates();
            if (!pet.isHungry()) {
                JOptionPane.showMessageDialog(this, "Your pet is hungry!");
            }
        } else {
            if (pet.isHungry() && stats.getFullness() >= pet.getType().getMaxFullness() * 0.25) {
                pet.checkStates();
                JOptionPane.showMessageDialog(this, "Your pet is no longer hungry.");
            }
            if (stats.getFullness() < pet.getType().getMaxFullness() * 0.25) {
                criticalStatsCount++; // Fullness is critical
            }
        }

        // Handle happiness decay
        if (pet.isHungry()) {
            stats.decreaseHappiness(petType.getHappinessDecayRate() * 2); // Faster decay when hungry
        } else {
            stats.decreaseHappiness(petType.getHappinessDecayRate());
        }
        if (stats.getHappiness() <= 0) {
            stats.setHappiness(0);
            pet.checkStates();
            if (pet.isAngry()) {
                JOptionPane.showMessageDialog(this, "Your pet is angry and refuses to cooperate!");
            }
        } else {
            if (pet.isAngry() && stats.getHappiness() >= pet.getType().getMaxHappiness() / 2) {
                pet.checkStates();
                JOptionPane.showMessageDialog(this, "Your pet has calmed down.");
            }
            if (stats.getHappiness() < pet.getType().getMaxHappiness() * 0.25) {
                criticalStatsCount++; // Happiness is critical
            }
        }

        // Apply health penalty based on the number of critical stats
        if (criticalStatsCount > 0 && !pet.isSleeping()) {
            stats.decreaseHealth(criticalStatsCount);
        }

        // Update UI
        enforceStatLimits();
        pet.setVitalStats(stats);
        pet.checkStates();
        refreshStatsPanel();

        // Handle death if health drops to or below 0
        if (stats.getHealth() <= 0) {
            pet.checkStates();
            handleDeath();
        }
    }


    /**
     * Handles the death of the pet.
     * Stops all timers and transitions to the Game Over screen.
     */
    private void handleDeath() {
        stopAllTimers(); // Stop all active timers
        JOptionPane.showMessageDialog(this, "Your pet has died. Game over!");

        // Transition to the Game Over screen
        this.setVisible(false); // Hide the GameplayScreen
        new GameOverScreen().setVisible(true); // Show the Game Over screen
    }

    /**
     * Determines the appropriate sprite for the pet based on its mood and stats.
     *
     * @return the relative path to the mood sprite.
     */
    private String determineMoodSprite() {
        String petType = pet.getType().getTypeName().toLowerCase(); // Retrieve pet type name
        String basePath = "/resources/";
        String moodSprite;

        if (pet.isDead()) {
            moodSprite = basePath + petType + "Dead.png";
        } else if (pet.isSleeping()) {
            moodSprite = basePath + petType + "Sleep.png";
        } else if (pet.isHungry()) {
            moodSprite = basePath + petType + "Hungry.png";
        } else if (pet.isAngry()) {
            moodSprite = basePath + petType + "Angry.png";
        } else if (stats.getHealth() < pet.getType().getMaxHealth() * 0.25) {
            moodSprite = basePath + petType + "Sick.png";
        } else {
            moodSprite = basePath + petType + "Idle.png";
        }

        return moodSprite;
    }
    /**
     * Sets up the pet sprite's movement animation.
     */
    private void setupSpriteMovement() {
        spriteMovementTimer = new Timer(300, e -> {
            if (!this.isVisible()) {
                return; // Skip updates if the screen is not visible
            }
            // Determine the mood sprite dynamically
            currentSpritePath = determineMoodSprite(); // Ensure the sprite path is up to date
            updatePetSprite(currentSpritePath); // Dynamically update the sprite

            // Get the current position of the sprite
            int currentY = petSpriteLabel.getY();

            // Move up or down by 1 pixel
            if (movingUp) {
                petSpriteLabel.setLocation(petSpriteLabel.getX(), currentY - 1);
                movingUp = false; // Immediately switch direction
            } else {
                petSpriteLabel.setLocation(petSpriteLabel.getX(), currentY + 1);
                movingUp = true; // Immediately switch direction
            }

            // Repaint the label to reflect the new position and sprite
            petSpriteLabel.repaint();
        });

        spriteMovementTimer.start(); // Start the timer
    }

    /**
     * Disables interaction buttons while the pet is in an inactive state.
     */
    private void disableInteraction() {
        sleepButton.setEnabled(false);
        playButton.setEnabled(false);
        exerciseButton.setEnabled(false);
        vetButton.setEnabled(false);
        feedButton.setEnabled(false);
        giveGiftButton.setEnabled(false);
        viewInventoryButton.setEnabled(true);
        miniGame.setEnabled(true);
        saveLoadButton.setEnabled(true);
    }
    /**
     * Enables interaction buttons when the pet becomes active again.
     */
    private void enableInteraction() {
        sleepButton.setEnabled(true);
        playButton.setEnabled(true);
        exerciseButton.setEnabled(true);
        vetButton.setEnabled(true);
        feedButton.setEnabled(true);
        giveGiftButton.setEnabled(true);
        viewInventoryButton.setEnabled(true);
        miniGame.setEnabled(true);
        saveLoadButton.setEnabled(true);
    }
    /**
     * Starts the timer for recovering the pet's sleep stat.
     */
    private void startSleepRecoveryTimer() {
        if (sleepRecoveryTimer != null && sleepRecoveryTimer.isRunning()) {
            JOptionPane.showMessageDialog(this, "Your pet is asleep!");
            return; // Timer is already running
        }

        sleepRecoveryTimer = new Timer(1000, e -> { // Increment sleep every 1 second
            stats.increaseSleep(5); // Adjust the increment as needed for faster recovery
            pet.checkStates();
            refreshStatsPanel(); // Update UI

            // Check if sleep has reached the maximum
            if (stats.getSleep() >= pet.getType().getMaxSleep()) {
                stats.setSleep(pet.getType().getMaxSleep()); // Cap sleep at maximum
                pet.checkStates();
                stopSleepRecoveryTimer(); // Stop the timer
                JOptionPane.showMessageDialog(this, "Your pet has woken up and is ready to play!");
                enableInteraction(); // Re-enable all buttons
            }
        });
        sleepRecoveryTimer.start(); // Start the timer
    }
    /**
     * Stops the sleep recovery timer.
     */
    private void stopSleepRecoveryTimer() {
        if (sleepRecoveryTimer != null && sleepRecoveryTimer.isRunning()) {
            sleepRecoveryTimer.stop(); // Stop the timer
        }
    }

    /**
     * Updates the player's score and refreshes the score label.
     *
     * @param increment the amount to increase the score by.
     */
    public void updateScore(int increment) {
        score += increment; // Increment the score
        scoreLabel.setText("Score: " + score); // Update the label
    }

    /**
     * Handles the math mini-game interaction.
     * Presents a math question to the player and rewards them for correct answers.
     */
    private void handleMathQuestion() {
        // Generate two random numbers for the math question
        int num1 = (int) (Math.random() * 10) + 1; // Random number between 1 and 10
        int num2 = (int) (Math.random() * 10) + 1;

        // Randomly choose an operation (+, -, *)
        String[] operations = {"+", "-", "*"};
        String operation = operations[(int) (Math.random() * operations.length)];

        // Calculate the correct answer
        int correctAnswer = switch (operation) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            default -> 0;
        };

        // Show the math question in a JOptionPane and get the user's answer
        String userInput = JOptionPane.showInputDialog(
                this,
                "What is " + num1 + " " + operation + " " + num2 + "?",
                "Math Question",
                JOptionPane.QUESTION_MESSAGE
        );

        // Validate the user's answer
        if (userInput != null) {
            try {
                int userAnswer = Integer.parseInt(userInput);
                if (userAnswer == correctAnswer) {
                    JOptionPane.showMessageDialog(this, "Correct! +10 points!");
                    updateScore(10); // Increase the score
                    addRandomItemToInventory(); // Add a random item to the inventory
                } else {
                    JOptionPane.showMessageDialog(this, "Incorrect! The correct answer was " + correctAnswer + ".");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a number.");
            }
        }
    }

    /**
     * Adds a random item to the inventory as a reward for completing a task.
     */
    private void addRandomItemToInventory() {
        // Select a random item from the list
        String randomItem = possibleItems[(int) (Math.random() * possibleItems.length)];

        // Determine whether it's food or a gift (you can customize this logic)
        if (randomItem.equals("Apple") || randomItem.equals("Banana") || randomItem.equals("Carrot")) {
            // Add to food inventory
            foodInventory.put(randomItem, foodInventory.getOrDefault(randomItem, 0) + 1);
        } else {
            // Add to gift inventory
            giftInventory.put(randomItem, giftInventory.getOrDefault(randomItem, 0) + 1);
        }

        // Notify the user
        JOptionPane.showMessageDialog(this, "You received a " + randomItem + "!");
    }



    @Override
    public void dispose() {
        // Stop the timers when the GameplayScreen is closed
        if (decayTimer != null) {
            decayTimer.stop();
        }
        if (spriteMovementTimer != null) {
            spriteMovementTimer.stop();
        }
        super.dispose();
    }

    //public static void main(String[] args) {
    //    SwingUtilities.invokeLater(() -> {
    //        // Simulate a pet name and type for debugging
    //        String petName = "DebugPet";
     //       PetType debugType = new PetType("Cat", 7, 6, 5, 7, 1, 2, 2, 0);
//
  //          GameplayScreen gameplayScreen = new GameplayScreen(petName, debugType);
    //        gameplayScreen.setVisible(true);
      //  });
    //}
}
