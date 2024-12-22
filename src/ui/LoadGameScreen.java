package ui;

import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import logic.Pet;
import logic.SaveManager;

public class LoadGameScreen extends JFrame {
    private Image backgroundImage;
    private MainMenu mainMenu;

    /**
     * Constructor for initializing the LoadGameScreen.
     * Sets up the UI for selecting a save slot to load a game.
     *
     * @param mainMenu the reference to the MainMenu instance for navigation.
     */
    public LoadGameScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;  // Store reference to MainMenu instance

        setTitle("Load Game");
        setSize(600, 450);  // Adjusted size for the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/loadScreen.png")).getImage();

        // Main panel to display the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);  // Use absolute positioning for buttons

        // Create invisible buttons for save slots
        JButton slot1Button = createInvisibleButton(220, 170, 160, 25);
        JButton slot2Button = createInvisibleButton(220, 210, 160, 25);
        JButton slot3Button = createInvisibleButton(220, 250, 160, 25);

        // Create an invisible "Back" button
        JButton backButton = createInvisibleButton(10, 380, 80, 30);

        // Add action listeners to buttons
        slot1Button.addActionListener(e -> loadGameSlot(1));
        slot2Button.addActionListener(e -> loadGameSlot(2));
        slot3Button.addActionListener(e -> loadGameSlot(3));
        backButton.addActionListener(e -> {
            dispose();  // Close the Load Game screen
            mainMenu.setVisible(true);  // Show the main menu
        });

        // Add buttons to the panel
        panel.add(slot1Button);
        panel.add(slot2Button);
        panel.add(slot3Button);
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
     * @return a JButton configured to be invisible.
     */
    private JButton createInvisibleButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);  // Position and size of the button
        button.setOpaque(false);  // Make the button transparent
        button.setContentAreaFilled(false);  // No background fill
        button.setBorderPainted(false);  // No border
        button.setFocusPainted(false);  // No focus outline
        return button;
    }

    /**
     * Loads the game from the specified save slot.
     * Transitions to GameplayScreen if the save is successfully loaded.
     *
     * @param slot the save slot number to load the game from.
     */
    private void loadGameSlot(int slot) {
        String filePath = "src/files/slot" + slot + ".csv";
        try {
            // Load the pet data using SaveManager
            Pet loadedPet = SaveManager.loadPet(filePath);

            // Transition to GameplayScreen with the loaded pet
            GameplayScreen gameplayScreen = new GameplayScreen(loadedPet);
            gameplayScreen.setVisible(true);

            // Close the LoadGameScreen
            dispose();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load save slot " + slot + ": " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
