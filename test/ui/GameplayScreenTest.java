package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.*;
import logic.Pet;
import logic.PetType;
import logic.VitalStatistics;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the unit test for the {@link GameplayScreen} class
 */
public class GameplayScreenTest {

    private GameplayScreen gameplayScreen;

    /**
     * This is the set up method that will run before each test to initialize
     * gameplay screen
     * This creates a new {@link Pet} instance
     * Initializes the {@link GameplayScreen} with it
     */
    @BeforeEach
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            // Creating a pet and gameplay screen for testing
            PetType petType = new PetType("Dog", 100, 100, 100, 100, 5, 5, 5, 5);
            VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
            Pet pet = new Pet("Buddy", petType, stats, System.currentTimeMillis(), System.currentTimeMillis(), false, false, false, false);
            gameplayScreen = new GameplayScreen(pet);
            gameplayScreen.setVisible(true);
        });
    }

    /**
     * This test check if the title of the {@link GameplayScreen} is set properly
     */
    @Test
    public void testGameplayScreenTitle() {
        SwingUtilities.invokeLater(() -> {
            assertEquals("Gameplay Screen", gameplayScreen.getTitle(), "Gameplay screen title should be 'Gameplay Screen'");
        });
    }

    /**
     * This test checks the pet object in the {@link GameplayScreen} isnt null
     */
    @Test
    public void testPetNotNull() {
        SwingUtilities.invokeLater(() -> {
            assertNotNull(gameplayScreen.pet, "Pet should not be null");
        });
    }

    /**
     * This test checks the stats panel in {@link GameplayScreen} is initalized
     */
    @Test
    public void testStatsPanelNotNull() {
        SwingUtilities.invokeLater(() -> {
            assertNotNull(gameplayScreen.statsPanel, "Stats panel should not be null");
        });
    }

    /**
     * This test verifies the {@link GameplayScreen} buttons are initialized
     */
    @Test
    public void testUIButtonsNotNull() {
        SwingUtilities.invokeLater(() -> {
            assertNotNull(gameplayScreen.sleepButton, "Sleep button should not be null");
            assertNotNull(gameplayScreen.playButton, "Play button should not be null");
            assertNotNull(gameplayScreen.exerciseButton, "Exercise button should not be null");
            assertNotNull(gameplayScreen.vetButton, "Vet button should not be null");
            assertNotNull(gameplayScreen.feedButton, "Feed button should not be null");
            assertNotNull(gameplayScreen.giveGiftButton, "Give Gift button should not be null");
            assertNotNull(gameplayScreen.viewInventoryButton, "View Inventory button should not be null");
            assertNotNull(gameplayScreen.exitButton, "Exit button should not be null");
            assertNotNull(gameplayScreen.saveLoadButton, "Save/Load button should not be null");
            assertNotNull(gameplayScreen.miniGame, "Mini Game button should not be null");
        });
    }

    @Test
    public void testScoreInitialValue() {
        SwingUtilities.invokeLater(() -> {
            assertEquals(0, gameplayScreen.score, "Initial score should be 0");
            assertEquals("Score: 0", gameplayScreen.scoreLabel.getText(), "Initial score label should be 'Score: 0'");
        });
    }

    /**
     * This test verify initial score of {@link GameplayScreen} is set to 0
     * as well as the score label initialized to "score: 0"
     */
    @Test
    public void testPetStatsAreInitialized() {
        SwingUtilities.invokeLater(() -> {
            assertEquals(50, gameplayScreen.stats.getHealth(), "Pet health should be initialized to 50");
            assertEquals(50, gameplayScreen.stats.getSleep(), "Pet sleep should be initialized to 50");
            assertEquals(50, gameplayScreen.stats.getFullness(), "Pet fullness should be initialized to 50");
            assertEquals(50, gameplayScreen.stats.getHappiness(), "Pet happiness should be initialized to 50");
        });
    }

    /**
     * This test verifies that pet sprite label is initialized properly
     */
    @Test
    public void testSpriteLabelExists() {
        SwingUtilities.invokeLater(() -> {
            assertNotNull(gameplayScreen.petSpriteLabel, "Pet sprite label should exist");
        });
    }

    /**
     * This test verifies that clicking exit hides the {@link GameplayScreen}
     */
    @Test
    public void testExitButtonAction() {
        SwingUtilities.invokeLater(() -> {
            gameplayScreen.exitButton.doClick(); // simulate clicking exit
            assertFalse(gameplayScreen.isVisible(), "Gameplay screen should be hidden after clicking exit button");
        });
    }

    /**
     * This test verifies that scoring is updated correctly
     * Ensures that both the numerical and score label reflect the change
     */
    @Test
    public void testUpdateScore() {
        SwingUtilities.invokeLater(() -> {
            gameplayScreen.updateScore(10); // update score by +10
            assertEquals(10, gameplayScreen.score, "Score should be updated to 10");
            assertEquals("Score: 10", gameplayScreen.scoreLabel.getText(), "Score label should be updated to 'Score: 10'");
        });
    }
}
