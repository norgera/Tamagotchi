package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import logic.ParentalControls;

public class AdvancedLimitsScreen extends JFrame {
    private static AdvancedLimitsScreen advancedLimitsScreenInstance;
    private Image backgroundImage;
    private ParentalControls parentalControls;

    /**
     * Private constructor for initializing the AdvancedLimitsScreen.
     * Sets up the UI components and logic for advanced parental controls.
     */
    AdvancedLimitsScreen() {
        setTitle("Advanced Limits Menu");
        setSize(600, 450);  // Set the fixed size for the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load the background image
        backgroundImage = new ImageIcon(getClass().getResource("/resources/advancedParentalControls.png")).getImage();

        // Create main panel with custom painting for background
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(null);  // Use absolute positioning for a static layout

        // Initialize logic.logic.ParentalControls instance
        parentalControls = new ParentalControls("files/parental_controls.csv");

        // Limit Playable Days Section
        JPanel daysPanel = createSectionPanel("", 40, 140, 150, 250);
        JCheckBox daysToggle = createToggleSwitch();
        daysToggle.setBounds(20, 10, 80, 30);

        // Set initial state based on isLimitPlayableDaysEnabled()
        daysToggle.setSelected(parentalControls.isLimitPlayableDaysEnabled());
        daysToggle.setText(daysToggle.isSelected() ? "On" : "Off");

        // Add action listener to handle toggle changes
        daysToggle.addActionListener(e -> {
            if (daysToggle.isSelected()) {
                daysToggle.setText("On");
                parentalControls.enableLimitPlayableDays();
            } else {
                daysToggle.setText("Off");
                parentalControls.disableLimitPlayableDays();
            }
            parentalControls.saveToCSV("files/parental_controls.csv");
        });

        daysPanel.add(daysToggle);

        // Checkboxes for each day
        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        Map<String, JCheckBox> dayCheckBoxes = new HashMap<>();

        for (int i = 0; i < days.length; i++) {
            String dayLower = days[i].toLowerCase();
            JCheckBox dayCheckBox = new JCheckBox(days[i]);
            dayCheckBox.setBounds(20, 40 + i * 25, 100, 20);
            dayCheckBox.setOpaque(false);
            dayCheckBox.setSelected(parentalControls.limitedDays.get(dayLower));
            daysPanel.add(dayCheckBox);
            dayCheckBoxes.put(dayLower, dayCheckBox);
        }
        panel.add(daysPanel);

        // Limit Daily Playtime Section
        JPanel dailyPanel = createSectionPanel("", 225, 100, 150, 250);
        JCheckBox dailyToggle = createToggleSwitch();
        dailyToggle.setBounds(30, 50, 80, 30);
        dailyToggle.setSelected(parentalControls.isDailyTimeLimitEnabled());
        if (dailyToggle.isSelected()) {
            dailyToggle.setText("On");
        } else {
            dailyToggle.setText("Off");
        }
        dailyPanel.add(dailyToggle);

        JTextField dailyTimeInput = new JTextField("XX:XX per Day");
        String dailyLimitTime = String.format("%02d:%02d", parentalControls.limitDailyPlaytime.toHours(), parentalControls.limitDailyPlaytime.toMinutesPart());
        dailyTimeInput.setText(dailyLimitTime);
        dailyTimeInput.setBounds(35, 80, 80, 30);
        dailyTimeInput.setHorizontalAlignment(SwingConstants.CENTER);
        dailyTimeInput.setBorder(new LineBorder(Color.BLACK, 1));
        dailyPanel.add(dailyTimeInput);
        panel.add(dailyPanel);

        // Limit Weekly Playtime Section
        JPanel weeklyPanel = createSectionPanel("", 390, 100, 150, 250);
        JCheckBox weeklyToggle = createToggleSwitch();
        weeklyToggle.setBounds(50, 50, 80, 30);
        weeklyToggle.setSelected(parentalControls.isWeeklyTimeLimitEnabled());
        if (weeklyToggle.isSelected()) {
            weeklyToggle.setText("On");
        } else {
            weeklyToggle.setText("Off");
        }
        weeklyPanel.add(weeklyToggle);

        JTextField weeklyTimeInput = new JTextField("XX:XX per Week");
        String weeklyLimitTime = String.format("%02d:%02d", parentalControls.limitWeeklyPlaytime.toHours(), parentalControls.limitWeeklyPlaytime.toMinutesPart());
        weeklyTimeInput.setText(weeklyLimitTime);
        weeklyTimeInput.setBounds(55, 80, 80, 30);
        weeklyTimeInput.setHorizontalAlignment(SwingConstants.CENTER);
        weeklyTimeInput.setBorder(new LineBorder(Color.BLACK, 1));
        weeklyPanel.add(weeklyTimeInput);
        panel.add(weeklyPanel);

        // Back Button
        JButton backButton = createButton("", 10, 380, 70, 30);  // Positioned at bottom left
        backButton.addActionListener(e -> {
            this.setVisible(false);  // Hide AdvancedLimitsScreen
            ParentalControlsScreen.getInstance().setVisible(true);  // Show ParentalControlsScreen
        });
        panel.add(backButton);

        setContentPane(panel);

        // Event Listeners
        daysToggle.addActionListener(e -> {
            boolean isEnabled = daysToggle.isSelected();
            if (isEnabled) {
                daysToggle.setText("On");
                parentalControls.enableLimitPlayableDays();
            } else {
                daysToggle.setText("Off");
                parentalControls.disableLimitPlayableDays();
            }
            parentalControls.saveToCSV("files/parental_controls.csv");
        });

        for (Map.Entry<String, JCheckBox> entry : dayCheckBoxes.entrySet()) {
            String day = entry.getKey();
            JCheckBox checkBox = entry.getValue();
            checkBox.addActionListener(e -> {
                if (checkBox.isSelected()) {
                    parentalControls.enableLimitOnDay(day);
                } else {
                    parentalControls.disableLimitOnDay(day);
                }
                parentalControls.saveToCSV("files/parental_controls.csv");
            });
        }

        dailyToggle.addActionListener(e -> {
            boolean isEnabled = dailyToggle.isSelected();
            if (isEnabled) {
                dailyToggle.setText("On");
                parentalControls.enableDailyTimeLimit();
            } else {
                dailyToggle.setText("Off");
                parentalControls.disableDailyTimeLimit();
            }
            parentalControls.saveToCSV("files/parental_controls.csv");
        });

        dailyTimeInput.addActionListener(e -> {
            String timeText = dailyTimeInput.getText();
            if (timeText.matches("\\d{1,2}:\\d{2}")) {
                parentalControls.setTotalDailyPlaytimeLimit(timeText);
                parentalControls.saveToCSV("files/parental_controls.csv");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter time in HH:mm format", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        weeklyToggle.addActionListener(e -> {
            boolean isEnabled = weeklyToggle.isSelected();
            if (isEnabled) {
                weeklyToggle.setText("On");
                parentalControls.enableWeeklyTimeLimit();
            } else {
                weeklyToggle.setText("Off");
                parentalControls.disableWeeklyTimeLimit();
            }
            parentalControls.saveToCSV("files/parental_controls.csv");
        });

        weeklyTimeInput.addActionListener(e -> {
            String timeText = weeklyTimeInput.getText();
            if (timeText.matches("\\d{1,2}:\\d{2}")) {
                parentalControls.setTotalWeeklyPlaytimeLimit(timeText);
                parentalControls.saveToCSV("files/parental_controls.csv");
            } else {
                JOptionPane.showMessageDialog(this, "Please enter time in HH:mm format", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Creates a section panel for organizing controls within the advanced limits screen.
     *
     * @param title  the title of the section.
     * @param x      the x-coordinate of the panel.
     * @param y      the y-coordinate of the panel.
     * @param width  the width of the panel.
     * @param height the height of the panel.
     * @return a JPanel configured for the section.
     */
    private JPanel createSectionPanel(String title, int x, int y, int width, int height) {
        JPanel panel = new JPanel(null);
        panel.setBounds(x, y, width, height);
        panel.setOpaque(false);  // Transparent background

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setBounds(0, 10, width, 20);
        panel.add(titleLabel);

        return panel;
    }

    /**
     * Creates a toggle switch using a JCheckBox, styled to resemble a toggle button.
     *
     * @return a JCheckBox configured as a toggle switch.
     */
    private JCheckBox createToggleSwitch() {
        JCheckBox toggleSwitch = new JCheckBox("Off");
        toggleSwitch.setOpaque(false);
        toggleSwitch.addActionListener(e -> {
            if (toggleSwitch.isSelected()) {
                toggleSwitch.setText("On");
            } else {
                toggleSwitch.setText("Off");
            }
        });
        return toggleSwitch;
    }

    /**
     * Creates an invisible button for handling actions in the UI.
     *
     * @param text   the text to display on the button.
     * @param x      the x-coordinate of the button.
     * @param y      the y-coordinate of the button.
     * @param width  the width of the button.
     * @param height the height of the button.
     * @return a JButton configured as an invisible button.
     */
    private JButton createButton(String text, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        return button;
    }


    /**
     * Retrieves the singleton instance of AdvancedLimitsScreen.
     * Ensures only one instance of the screen is created.
     *
     * @return the singleton instance of AdvancedLimitsScreen.
     */
    public static AdvancedLimitsScreen getInstance() {
        if (advancedLimitsScreenInstance == null) {
            advancedLimitsScreenInstance = new AdvancedLimitsScreen();
        }
        return advancedLimitsScreenInstance;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdvancedLimitsScreen.getInstance().setVisible(true);
        });
    }
}
