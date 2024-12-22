package ui;

import javax.swing.*;
import java.awt.*;

public class TutorialScreen extends JFrame {
    private Image tutorialImage;
    private MainMenu mainMenu;

    /**
     * Constructor for initializing the TutorialScreen.
     * Sets up the UI for displaying the tutorial image and a "Back" button.
     *
     * @param mainMenu the reference to the MainMenu instance for navigation.
     */
    public TutorialScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;  // Store reference to MainMenu instance

        setTitle("Tutorial");
        setSize(600, 450);  // Adjust size as needed for your tutorial image
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the tutorial image
        tutorialImage = new ImageIcon(getClass().getResource("/resources/tutorialScreen.png")).getImage();

        // Main panel to display the tutorial image
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(tutorialImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);  // Use absolute positioning

        // Create the "Back" button
        JButton backButton = new JButton("Back"); // Add text to make it recognizable
        backButton.setBounds(10, 380, 70, 30);    // Adjust position and size as needed
        backButton.setVisible(true);              // Ensure the button is visible
        backButton.setFocusable(false);           // Optional: prevent focus border
        backButton.setBackground(Color.LIGHT_GRAY); // Optional: Set a background color
        backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Optional: Add a border

        // Add an action listener for button functionality
        backButton.addActionListener(e -> {
            dispose();  // Close the tutorial screen
            mainMenu.setVisible(true);  // Show the main menu again
        });

        // Add the "Back" button to the main panel
        panel.add(backButton);


        // Add the main panel to the frame
        setContentPane(panel);
    }
}
