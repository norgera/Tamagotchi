package ui;

import java.awt.*;
import javax.swing.*;
import logic.PetType;

public class NewGameScreen extends JFrame {
    private Image backgroundImage;
    private MainMenu mainMenu;
    private PetType selectedPetType = null; // To store the selected PetType
    private PetType[] petTypes;

    /**
     * Constructor for initializing the NewGameScreen.
     * Sets up the UI for selecting a pet and entering its name to start a new game.
     *
     * @param mainMenu the reference to the MainMenu instance for navigation.
     */
    public NewGameScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu; // Store reference to MainMenu instance

        setTitle("New Game");
        setSize(600, 450); // Adjusted size for the screen
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/newGameScreen.png")).getImage();

        // Main panel to display the background image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null); // Use absolute positioning

        // Initialize pet types
        petTypes = new PetType[3];
        petTypes[0] = new PetType("Brock", 7, 6, 5, 7, 1, 2, 2, 0);
        petTypes[1] = new PetType("Sunny", 7, 5, 7, 6, 1, 1, 2, 0);
        petTypes[2] = new PetType("Berry", 7, 7, 6, 5, 1, 2, 1, 0);

        // Create invisible buttons for selecting pets
        JButton pet1Button = createInvisibleButton(100, 120, 100, 25); // Position and size for Pet 1
        JButton pet2Button = createInvisibleButton(250, 120, 100, 25); // Position and size for Pet 2
        JButton pet3Button = createInvisibleButton(400, 120, 100, 25); // Position and size for Pet 3

        // Create a text field for entering the pet's name
        JTextField nameField = new JTextField();
        nameField.setBounds(195, 350, 200, 30); // Centered position below pets
        nameField.setHorizontalAlignment(JTextField.CENTER); // Align text to the center

        // Create an invisible "Back" button
        JButton backButton = createInvisibleButton(10, 380, 80, 30); // Positioned bottom-left

        // Create an invisible "Start" button
        JButton startButton = createInvisibleButton(510, 380, 80, 30); // Positioned bottom-right

        // Add action listeners for buttons
        pet1Button.addActionListener(e -> selectPet(0)); // Pet index 0 corresponds to Brock
        pet2Button.addActionListener(e -> selectPet(1)); // Pet index 1 corresponds to Sunny
        pet3Button.addActionListener(e -> selectPet(2)); // Pet index 2 corresponds to Berry

        backButton.addActionListener(e -> {
            dispose(); // Close the New Game screen
            mainMenu.setVisible(true); // Show the main menu
        });

        startButton.addActionListener(e -> {
            // Check if pet is selected and name is provided
            String petName = nameField.getText().trim();
            if (selectedPetType == null) {
                JOptionPane.showMessageDialog(this, "Please select a pet!");
            } else if (petName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a name for your pet!");
            } else {
                // Open GameplayScreen and pass the selected pet details
                GameplayScreen gameplayScreen = new GameplayScreen(petName, selectedPetType);
                gameplayScreen.setVisible(true);
                dispose(); // Close the New Game screen
            }
        });

        // Add components to the panel
        panel.add(pet1Button);
        panel.add(pet2Button);
        panel.add(pet3Button);
        panel.add(nameField);
        panel.add(backButton);
        panel.add(startButton);

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
     * Handles pet selection by updating the selected PetType.
     *
     * @param petIndex the index of the selected pet in the petTypes array.
     */
    private void selectPet(int petIndex) {
        selectedPetType = petTypes[petIndex]; // Store the selected PetType
        JOptionPane.showMessageDialog(this, selectedPetType.getTypeName() + " selected!");
    }
}
