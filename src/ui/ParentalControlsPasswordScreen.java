package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ParentalControlsPasswordScreen extends JFrame {
    private static final String PARENTAL_CONTROL_PASSWORD = "2212";  // Replace with desired password
    private Image backgroundImage;
    private MainMenu mainMenu;

    /**
     * Constructor for initializing the ParentalControlsPasswordScreen.
     * Sets up the UI for entering a password to access parental controls.
     *
     * @param mainMenu the reference to the MainMenu instance for navigation.
     */
    public ParentalControlsPasswordScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("Parental Controls");
        setSize(600, 450);  // Set the fixed size for the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);  // Disable resizing to keep layout static

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/parentalControllsPasswordScreen.png")).getImage();

        // Create main panel with custom painting for background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw background image
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);  // Use absolute positioning for a static layout

        // Create and configure the password field
        JTextField passwordField = new JTextField();
        passwordField.setBounds(200, 210, 200, 20);  // Adjusted position and size for visibility
        passwordField.setBorder(new LineBorder(Color.BLACK, 2));  // Add a black border with 2-pixel width
        passwordField.setOpaque(true);  // Make sure the text field is not transparent
        passwordField.setBackground(Color.WHITE);  // Set background color to white for better visibility
        panel.add(passwordField);  // Add the password field to the panel

        // Enter button (bottom right)
        JButton enterButton = createInvisibleButton(500, 380, 70, 30);  // Adjusted position and size
        enterButton.addActionListener(e -> {
            if (passwordField.getText().equals(PARENTAL_CONTROL_PASSWORD)) {
                this.setVisible(false);  // Hide password screen
                ParentalControlsScreen.getInstance().setVisible(true);  // Open main parental controls screen
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        panel.add(enterButton);

        // Back button (bottom left)
        JButton backButton = createInvisibleButton(10, 380, 70, 30);  // Adjusted position and size
        backButton.addActionListener(e -> {
            this.setVisible(false);  // Hide this screen
            mainMenu.setVisible(true);  // Show the main menu screen
        });
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
        button.setBounds(x, y, width, height);  // Position and size based on mockup
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu mainMenu = new MainMenu();
            mainMenu.setVisible(true);
            ParentalControlsPasswordScreen frame = new ParentalControlsPasswordScreen(mainMenu);
            frame.setVisible(true);
        });
    }
}
