package logic;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link VitalStatistics} class
 * This test the functionality of increasing the vitals of the pet
 */
public class VitalStatisticsTest {

    /**
     * Tests the increase in health for the pet
     * Ensure the incrementation of health is correct
     */
    @Test
    public void testIncreaseHealth() { // Initializes vital values of 50
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.increaseHealth(10); // increase by +10
        assertEquals(60, stats.getHealth(), "Health should increase by 10");
    }

    /**
     * Tests the decrease in health for the pet
     * Ensure the decrementatio of health is correct
     */
    @Test
    public void testDecreaseHealth() { // initalizes vitals to 50
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.decreaseHealth(20); // decrease by -20
        assertEquals(30, stats.getHealth(), "Health should decrease by 20");
    }

    /**
     * Tests the increase in sleep for the pet
     * Ensure the incrementation of sleep is correct
     */
    @Test
    public void testIncreaseSleep() { // initalizes vitals to 50
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.increaseSleep(15); // increase sleep by +15
        assertEquals(65, stats.getSleep(), "Sleep should increase by 15");
    }

    /**
     * Tests the decrease in sleep for the pet
     * Ensure the decrementation of sleep is correct
     */
    @Test
    public void testDecreaseSleep() { // initalizes vitals to 50
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.decreaseSleep(10); // decrease sleep by 10
        assertEquals(40, stats.getSleep(), "Sleep should decrease by 10");
    }

    /**
     * Tests the increase in fullness for the pet
     * Ensure the incrementation of fullness is correct
     */
    @Test
    public void testIncreaseFullness() {
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.increaseFullness(20);
        assertEquals(70, stats.getFullness(), "Fullness should increase by 20");
    }

    /**
     * Tests the decrease in fullness for the pet
     * Ensure the decrementation of fullness is correct
     */
    @Test
    public void testDecreaseFullness() {
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.decreaseFullness(25);
        assertEquals(25, stats.getFullness(), "Fullness should decrease by 25");
    }

    /**
     * Tests the increase in happiness for the pet
     * Ensure the incrementation of happiness is correct
     */
    @Test
    public void testIncreaseHappiness() {
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.increaseHappiness(5);
        assertEquals(55, stats.getHappiness(), "Happiness should increase by 5");
    }

    /**
     * Tests the decrease in happiness for the pet
     * Ensure the decrementation of hapiness is correct
     */
    @Test
    public void testDecreaseHappiness() {
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        stats.decreaseHappiness(15);
        assertEquals(35, stats.getHappiness(), "Happiness should decrease by 15");
    }

    /**
     * Tests the setter methods for the vitals and verifies they are set correctly
     */
    @Test
    public void testSetters() {
        VitalStatistics stats = new VitalStatistics(50, 50, 50, 50);
        // This sets new values for each vital
        stats.setHealth(80);
        stats.setSleep(70);
        stats.setFullness(60);
        stats.setHappiness(90);

        // verifies each vital is correclty updated
        assertEquals(80, stats.getHealth(), "Health should be set to 80");
        assertEquals(70, stats.getSleep(), "Sleep should be set to 70");
        assertEquals(60, stats.getFullness(), "Fullness should be set to 60");
        assertEquals(90, stats.getHappiness(), "Happiness should be set to 90");
    }
}
