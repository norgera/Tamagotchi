package ui;

import javax.swing.*;
import javax.swing.SpinnerDateModel;
import java.awt.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.Calendar;
import logic.ParentalControls;

public class ParentalControlsScreen extends JFrame {
    private static ParentalControlsScreen parentalControlsScreenInstance;
    private static AdvancedLimitsScreen advancedLimitsScreenInstance;

    private ParentalControls parentalControls = new ParentalControls("files/parental_controls.csv");

    private Image backgroundImage;
    private JLabel totalPlayTimeLabel;
    private JLabel avgPlayTimeLabel;
    private JLabel gamePlayabilityLabel;

    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;

    /**
     * Private constructor for initializing the ParentalControlsScreen.
     * Sets up the UI components for managing parental controls.
     */
    private ParentalControlsScreen() {
        setTitle("Parental Controls");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/parentalControls.png")).getImage();

        // Create main panel with custom painting for background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        mainPanel.setLayout(null);

        // Add UI components
        addLimitsCheckbox(mainPanel);
        configureTimeSpinners(mainPanel);
        addPlayTimeLabels(mainPanel);
        addResetButton(mainPanel);
        addAdvancedButton(mainPanel);
        addBackButton(mainPanel);
        addGamePlayabilityLabel(mainPanel);
        addRestoreButtons(mainPanel);

        setContentPane(mainPanel);
    }

    /**
     * Adds a checkbox to enable or disable daily playtime limits.
     * Updates the parental controls state based on the checkbox value.
     *
     * @param mainPanel the panel where the checkbox will be added.
     */
    private void addLimitsCheckbox(JPanel mainPanel) {
        // Initialize the checkbox and set its state based on the current parental controls state
        JCheckBox enableLimitsCheckBox = new JCheckBox("Limit Playtime");
        enableLimitsCheckBox.setBounds(240, 180, 150, 30);
        enableLimitsCheckBox.setOpaque(false);
        enableLimitsCheckBox.setForeground(Color.BLACK);

        // Set initial state of the checkbox
        enableLimitsCheckBox.setSelected(parentalControls.isDailyPlayableTimeEnabled());

        // Add action listener to handle user interactions
        enableLimitsCheckBox.addActionListener(e -> {
            if (enableLimitsCheckBox.isSelected()) {
                parentalControls.enableDailyPlayableTime();
            } else {
                parentalControls.disableDailyPlayableTime();
            }
            parentalControls.saveToCSV("files/parental_controls.csv");
        });

        mainPanel.add(enableLimitsCheckBox);
    }

    /**
     * Configures the start and end time spinners for setting time-of-day limits.
     * Updates the parental controls state when the spinners are adjusted.
     *
     * @param mainPanel the panel where the spinners will be added.
     */
    private void configureTimeSpinners(JPanel mainPanel) {
        JLabel startLabel = new JLabel("Start:");
        startLabel.setBounds(240, 240, 50, 20);
        startLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mainPanel.add(startLabel);

        startTimeSpinner = createTimeSpinner(parentalControls.getLimitTimeOfDayStart());
        startTimeSpinner.setBounds(290, 240, 70, 30);
        mainPanel.add(startTimeSpinner);

        JLabel endLabel = new JLabel("End:");
        endLabel.setBounds(240, 280, 50, 20);
        endLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mainPanel.add(endLabel);

        endTimeSpinner = createTimeSpinner(parentalControls.getLimitTimeOfDayEnd());
        endTimeSpinner.setBounds(290, 280, 70, 30);
        mainPanel.add(endTimeSpinner);

        startTimeSpinner.addChangeListener(e -> updateTimeLimits());
        endTimeSpinner.addChangeListener(e -> updateTimeLimits());
    }

    /**
     * Adds labels to display total and average playtime statistics.
     *
     * @param mainPanel the panel where the labels will be added.
     */
    private void addPlayTimeLabels(JPanel mainPanel) {
        totalPlayTimeLabel = new JLabel("Total: " + parentalControls.getTotalPlaytime());
        totalPlayTimeLabel.setBounds(420, 190, 150, 30);
        totalPlayTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mainPanel.add(totalPlayTimeLabel);

        avgPlayTimeLabel = new JLabel("Avg: " + parentalControls.returnAverageSessionLength());
        avgPlayTimeLabel.setBounds(420, 230, 150, 30);
        avgPlayTimeLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        mainPanel.add(avgPlayTimeLabel);
    }

    /**
     * Adds a reset button to reset playtime statistics and update the labels.
     *
     * @param mainPanel the panel where the reset button will be added.
     */
    private void addResetButton(JPanel mainPanel) {
        JButton resetStatsButton = createButton("Reset Stats", 420, 310, 135, 35);
        resetStatsButton.addActionListener(e -> {
            parentalControls.stopTracking();
            parentalControls.resetTracking();
            parentalControls.saveToCSV("files/parental_controls.csv");
            totalPlayTimeLabel.setText("Total: " + parentalControls.getTotalPlaytime());
            avgPlayTimeLabel.setText("Avg: " + parentalControls.returnAverageSessionLength());
            JOptionPane.showMessageDialog(this, "Play statistics reset.");
            parentalControls.startTracking();
            parentalControls.saveToCSV("files/parental_controls.csv");
        });
        mainPanel.add(resetStatsButton);
    }

    /**
     * Adds a button to open the AdvancedLimitsScreen for more control options.
     *
     * @param mainPanel the panel where the advanced button will be added.
     */
    private void addAdvancedButton(JPanel mainPanel) {
        JButton advancedButton = createButton("Advanced", 225, 313, 148, 35);
        advancedButton.addActionListener(e -> {
            if (advancedLimitsScreenInstance == null) {
                advancedLimitsScreenInstance = new AdvancedLimitsScreen();
            }
            advancedLimitsScreenInstance.setVisible(true);
            this.setVisible(false);
        });
        mainPanel.add(advancedButton);
    }

    /**
     * Adds a back button to return to the MainMenu.
     *
     * @param mainPanel the panel where the back button will be added.
     */
    private void addBackButton(JPanel mainPanel) {
        JButton backButton = createButton("Back", 10, 380, 90, 30);
        backButton.addActionListener(e -> {
            this.setVisible(false);
            MainMenu.getInstance().setVisible(true);
        });
        mainPanel.add(backButton);
    }

    /**
     * Adds a label to display whether the game is currently playable or restricted.
     * Updates the label dynamically based on parental controls.
     *
     * @param mainPanel the panel where the label will be added.
     */
    private void addGamePlayabilityLabel(JPanel mainPanel) {
        String label = parentalControls.isGameBlocked() ? "Game is restricted." : "Game is playable.";
        gamePlayabilityLabel = new JLabel(label);
        gamePlayabilityLabel.setBounds(420, 270, 150, 30);
        mainPanel.add(gamePlayabilityLabel);

        Timer timer = new Timer(100, e -> {
            boolean isPlayable = !parentalControls.isGameBlocked(); // Updated logic
            gamePlayabilityLabel.setText(isPlayable ? "Game is playable." : "Game is restricted.");
        });
        timer.start();
    }

    /**
     * Updates the time-of-day limits based on the values in the start and end time spinners.
     * Saves the updated limits to the CSV file.
     */
    private void updateTimeLimits() {
        String startTime = ((JSpinner.DateEditor) startTimeSpinner.getEditor()).getFormat().format(startTimeSpinner.getValue());
        String endTime = ((JSpinner.DateEditor) endTimeSpinner.getEditor()).getFormat().format(endTimeSpinner.getValue());
        parentalControls.setTimeOfDayLimit(startTime, endTime);
//        System.out.println("Start: " + startTime + "\nEnd: " + endTime + "\n");
        parentalControls.saveToCSV("files/parental_controls.csv");
    }

    /**
     * Creates a time spinner initialized with the specified time.
     *
     * @param time the LocalTime to initialize the spinner with.
     * @return a JSpinner configured for time selection.
     */
    private JSpinner createTimeSpinner(LocalTime time) {
        // Convert LocalTime to java.util.Date
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, time.getHour());
        calendar.set(Calendar.MINUTE, time.getMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Create SpinnerDateModel and initialize it with the converted time
        SpinnerDateModel model = new SpinnerDateModel(calendar.getTime(), null, null, Calendar.MINUTE);
        JSpinner spinner = new JSpinner(model);

        // Set up the editor to display time in HH:mm format
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinner, "HH:mm");
        spinner.setEditor(editor);

        return spinner;
    }

    /**
     * Adds buttons for reviving pets to the panel.
     * Each button corresponds to a specific pet slot.
     *
     * @param mainPanel the panel where the buttons will be added.
     */
    private void addRestoreButtons(JPanel mainPanel) {
        JButton revivePet1Button = createButton("Revive Pet 1", 45, 185, 145, 35);
        revivePet1Button.addActionListener(e -> handleRevivePet(1));
        mainPanel.add(revivePet1Button);

        JButton revivePet2Button = createButton("Revive Pet 2", 45, 235, 145, 35);
        revivePet2Button.addActionListener(e -> handleRevivePet(2));
        mainPanel.add(revivePet2Button);

        JButton revivePet3Button = createButton("Revive Pet 3", 45, 285, 145, 35);
        revivePet3Button.addActionListener(e -> handleRevivePet(3));
        mainPanel.add(revivePet3Button);
    }

    /**
     * Handles the action of reviving a pet in a specific slot.
     *
     * @param petId the ID of the pet to revive.
     */
    private void handleRevivePet(int petId) {
        try {
            String message = parentalControls.resetPet(petId); // Replace with your method to revive the pet
            JOptionPane.showMessageDialog(this, message);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reviving Pet " + petId + ": " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Creates a styled button with specified text and dimensions.
     *
     * @param text   the text to display on the button.
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured with the specified properties.
     */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("SansSerif", Font.BOLD, 12));
        return button;
    }


    @Override
    public void setVisible(boolean visible) {
        parentalControls.loadFromCSV("files/parental_controls.csv");
        super.setVisible(visible);
        if (visible) {
            boolean isPlayable = !parentalControls.isGameBlocked();
            gamePlayabilityLabel.setText(isPlayable ? "Game is playable." : "Game is restricted.");
        }
    }

    /**
     * Retrieves the singleton instance of ParentalControlsScreen.
     * Ensures only one instance of the screen is created.
     *
     * @return the singleton instance of ParentalControlsScreen.
     */
    public static ParentalControlsScreen getInstance() {
        if (parentalControlsScreenInstance == null) {
            parentalControlsScreenInstance = new ParentalControlsScreen();
        }
        return parentalControlsScreenInstance;
    }
}
