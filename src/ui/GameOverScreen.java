package ui;

import javax.swing.*;
import java.awt.*;

public class GameOverScreen extends JFrame {
    private Image backgroundImage;

    /**
     * Constructor for initializing the GameOverScreen.
     * Sets up the UI with a background image and options for starting a new game or loading a saved game.
     */
    public GameOverScreen() {
        setTitle("Game Over");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/gameOver.png")).getImage();

        // Set up the layout and buttons
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);

        // Create buttons
        JButton newGameButton = createButton(210, 257, 163, 30);
        JButton loadGameButton = createButton(448, 257, 163, 30);

        // Add buttons to the panel
        mainPanel.add(newGameButton);
        mainPanel.add(loadGameButton);

        // Add action listeners to buttons
        newGameButton.addActionListener(e -> startNewGame());
        loadGameButton.addActionListener(e -> loadGame());

        setContentPane(mainPanel);
    }

    /**
     * Creates a custom button with specified dimensions.
     *
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured with the specified properties.
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
     * Handles the action for starting a new game.
     * Transitions to the MainMenu and opens the NewGameScreen.
     */
    private void startNewGame() {
        // Transition to MainMenu, then open NewGameScreen
        MainMenu mainMenu = MainMenu.getInstance();
        NewGameScreen newGameScreen = new NewGameScreen(mainMenu);
        newGameScreen.setVisible(true);
        this.setVisible(false);
    }

    /**
     * Handles the action for loading a saved game.
     * Transitions to the MainMenu and opens the LoadGameScreen.
     */
    private void loadGame() {
        // Transition to MainMenu, then open LoadGameScreen
        MainMenu mainMenu = MainMenu.getInstance();
        LoadGameScreen loadGameScreen = new LoadGameScreen(mainMenu);
        loadGameScreen.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameOverScreen().setVisible(true));
    }
}