package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import logic.Pet;
import logic.PetType;
import logic.SaveManager;
import logic.VitalStatistics;

public class SaveLoadGameScreen extends JFrame {
    private Image backgroundImage;
    private Pet pet;
    private GameplayScreen parentGameplayScreen;

    /**
     * Constructor for initializing the SaveLoadGameScreen.
     * Sets up the UI for saving and loading game slots.
     *
     * @param pet                  the pet object representing the current game state.
     * @param parentGameplayScreen the reference to the GameplayScreen instance for navigation.
     */
    public SaveLoadGameScreen(Pet pet, GameplayScreen parentGameplayScreen) {
        this.pet = pet;
        this.parentGameplayScreen = parentGameplayScreen;

        setTitle("Save/Load Game");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/Load_Save.png")).getImage();

        // Initialize UI components
        initializeUI();
    }


    private void initializeUI() {
        // Main panel to display the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Use absolute positioning

        // Invisible Save buttons
        JButton saveSlot1 = createInvisibleButton(125, 185, 130, 30); // Position and size for Save Slot 1
        JButton saveSlot2 = createInvisibleButton(125, 230, 130, 30); // Save Slot 2
        JButton saveSlot3 = createInvisibleButton(125, 270, 130, 30); // Save Slot 3

        // Invisible Load buttons
        JButton loadSlot1 = createInvisibleButton(345, 185, 130, 30); // Position and size for Load Slot 1
        JButton loadSlot2 = createInvisibleButton(345, 230, 130, 30); // Load Slot 2
        JButton loadSlot3 = createInvisibleButton(345, 270, 130, 30); // Load Slot 3

        // Invisible "Back" button
        JButton backButton = createInvisibleButton(10, 380, 80, 30); // Position bottom-left
        backButton.addActionListener(e -> {
            this.setVisible(false); // Hide the SaveLoadGameScreen
            parentGameplayScreen.setVisible(true); // Show the GameplayScreen
        });

        // Add action listeners for Save slots
        saveSlot1.addActionListener(e -> saveGame(1));
        saveSlot2.addActionListener(e -> saveGame(2));
        saveSlot3.addActionListener(e -> saveGame(3));

        // Add action listeners for Load slots
        loadSlot1.addActionListener(e -> loadGame(1));
        loadSlot2.addActionListener(e -> loadGame(2));
        loadSlot3.addActionListener(e -> loadGame(3));

        // Add components to the panel
        panel.add(saveSlot1);
        panel.add(saveSlot2);
        panel.add(saveSlot3);
        panel.add(loadSlot1);
        panel.add(loadSlot2);
        panel.add(loadSlot3);
        panel.add(backButton);

        // Add the panel to the frame
        setContentPane(panel);
    }

    /**
     * Creates an invisible button with specified dimensions.
     *
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured as an invisible button.
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
     * Saves the current game state to a specified slot.
     *
     * @param slot the slot number where the game will be saved.
     */
    private void saveGame(int slot) {
        String filePath = "src/files/slot" + slot + ".csv";
        try {
            SaveManager.savePet(filePath, pet);
            JOptionPane.showMessageDialog(this, "Game saved in Slot " + slot + "!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to save game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Loads a game from a specified slot and transitions to a new GameplayScreen.
     *
     * @param slot the slot number from which the game will be loaded.
     */
    private void loadGame(int slot) {
        String filePath = "src/files/slot" + slot + ".csv";
        try {
            Pet loadedPet = SaveManager.loadPet(filePath);
            JOptionPane.showMessageDialog(this, "Game loaded from Slot " + slot + "!", "Load Game", JOptionPane.INFORMATION_MESSAGE);

            // Close the SaveLoadGameScreen
            this.setVisible(false);

            // Close the old GameplayScreen
            parentGameplayScreen.dispose();

            // Create a new GameplayScreen with the loaded pet
            GameplayScreen gameplayScreen = new GameplayScreen(loadedPet);
            gameplayScreen.setVisible(true);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load game: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to test the UI
    //public static void main(String[] args) {
        //SwingUtilities.invokeLater(() -> {
            // Create a dummy pet for testing
            //PetType petType = new PetType("Dog", 100, 100, 100, 100, 5, 5, 5, 5);
            //VitalStatistics stats = new VitalStatistics(80, 90, 70, 100);
            //Pet pet = new Pet("Charlie", petType);
            //pet.setVitalStats(stats);

            // Create a dummy GameplayScreen
            //GameplayScreen gameplayScreen = new GameplayScreen(pet);
            //gameplayScreen.setVisible(false); // Hide it for testing

            // Show the SaveLoadGameScreen
            //SaveLoadGameScreen screen = new SaveLoadGameScreen(pet, gameplayScreen);
            //screen.setVisible(true);
        //});
    //}
}
