package ui;

import logic.ParentalControls;

import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame {
    private static MainMenu mainMenuInstance;
    private Image backgroundImage;
    private static ParentalControlsScreen parentalControlsScreenInstance;
    private static TutorialScreen tutorialScreenInstance;  // Define tutorialScreenInstance
    private ParentalControls parentalControls;

    /**
     * Constructor for initializing the MainMenu.
     * Sets up the main menu UI with buttons for navigation and background image.
     */
    public MainMenu() {
        parentalControls = new ParentalControls("files/parental_controls.csv");

        setTitle("Virtual Pet Game");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/homeScreen.png")).getImage();

        // Set up layout and buttons
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);

        // Create invisible buttons to match clickable areas
        JButton newGameButton = createButton(120, 160, 160, 30);
        JButton loadGameButton = createButton(120, 210, 160, 30);
        JButton tutorialButton = createButton(120, 260, 160, 30);
        JButton parentalControlsButton = createButton(120, 320, 160, 30);
        JButton exitButton = createButton(120, 390, 160, 30);

        mainPanel.add(newGameButton);
        mainPanel.add(loadGameButton);
        mainPanel.add(tutorialButton);
        mainPanel.add(parentalControlsButton);
        mainPanel.add(exitButton);

        // Add action listeners to buttons
        setupButton(newGameButton, this::startNewGame);
        setupButton(loadGameButton, this::loadGame);
        setupButton(tutorialButton, this::showTutorial);
        setupButton(parentalControlsButton, this::showParentalControls);
        setupButton(exitButton, () -> {
            parentalControls.stopTracking();        // Call parental controls method
            parentalControls.saveToCSV("files/parental_controls.csv");
            System.exit(0);                  // Exit the application
        });

        setContentPane(mainPanel);
    }

    /**
     * Creates a transparent button with specified dimensions.
     *
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured as a transparent button.
     */
    private JButton createButton(int x, int y, int width, int height) {
        JButton button = new JButton();
        button.setBounds(x, y, width, height);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    /**
     * Displays the tutorial screen.
     * Initializes and shows the tutorial screen if not already created.
     */
    private void showTutorial() {
        if (tutorialScreenInstance == null) {
            tutorialScreenInstance = new TutorialScreen(this);  // Pass this MainMenu instance
        }
        this.setVisible(false);  // Hide main menu
        tutorialScreenInstance.setVisible(true);  // Show tutorial screen
    }

    /**
     * Starts a new game.
     * Checks parental controls before transitioning to the new game screen.
     */
    private void startNewGame() {
        if (parentalControls.isGameBlocked()) {
            JOptionPane.showMessageDialog(this, "You are not allowed to start a new game at this time.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
        NewGameScreen newGameScreen = new NewGameScreen(this);
        newGameScreen.setVisible(true);
        this.setVisible(false);
    }

    /**
     * Loads a saved game.
     * Checks parental controls before transitioning to the load game screen.
     */
    private void loadGame() {
        if (parentalControls.isGameBlocked()) {
            JOptionPane.showMessageDialog(this, "You are not allowed to load a game at this time.", "Access Denied", JOptionPane.WARNING_MESSAGE);
            return;
        }
        LoadGameScreen loadGameScreen = new LoadGameScreen(this);
        loadGameScreen.setVisible(true);
        this.setVisible(false);
    }

    /**
     * Shows the parental controls screen.
     * Transitions to a password screen before granting access to parental controls.
     */
    private void showParentalControls() {
        // Change to open the password screen first
        ParentalControlsPasswordScreen passwordScreen = new ParentalControlsPasswordScreen(this);
        passwordScreen.setVisible(true);  // Show password screen
        this.setVisible(false);  // Hide main menu
    }


    /**
     * Sets up an action for a button.
     * Associates the button with a specified runnable action.
     *
     * @param button the JButton to configure.
     * @param action the action to associate with the button.
     */
    private void setupButton(JButton button, Runnable action) {
        button.addActionListener(e -> action.run());
    }

    /**
     * Retrieves the singleton instance of the MainMenu.
     * Ensures only one instance of the main menu is created.
     *
     * @return the singleton instance of MainMenu.
     */
    public static MainMenu getInstance() {
        if (mainMenuInstance == null) {
            mainMenuInstance = new MainMenu();
        }
        return mainMenuInstance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainMenu.getInstance().setVisible(true);
        });
    }
}
