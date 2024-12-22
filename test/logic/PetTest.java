package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the {@link Pet} class
 * This test class contains many tests to verify functionality of pet behaviors
 */
class PetTest {

    private Pet pet;
    private PetType type;

    /**
     * sets up the test environment before each test is ran
     * Initializes a pet type and a {@link Pet} with initial values
     */
    @BeforeEach
    void setUp() {
        // PetType constructor (name, maxHealth, maxSleep, maxFullness, maxHappiness)
        type = new PetType("Dog", 100, 100, 100, 100, 5, 5, 5, 5);

        // VitalStatistics object (health, sleep, fullness, happiness)
        VitalStatistics initialStats = new VitalStatistics(50, 50, 50, 50); // Set initial values below max
        pet = new Pet("Buddy", type, initialStats, 0, 0, false, false, false, false);
    }

    /**
     * Tests the feeding functionality for the {@link Pet} class
     * see if feeding increases fullness, while not exceeding maximum value
     */
    @Test
    void testFeed() {
        pet.feed(20); // Feed the pet by 20 points
        assertEquals(70, pet.getVitalStats().getFullness(), "Fullness should be increased by 20");

        pet.feed(50); // Should reach max fullness, which is 100
        assertEquals(type.getMaxFullness(), pet.getVitalStats().getFullness(), "Fullness should not exceed max value");
    }

    /**
     * Test the gift - giving functionality of the {@link Pet} class
     * Verify that gifting increases happiness, and it doesnt exceed mximum value
     */
    @Test
    void testGiveGift() {
        pet.giveGift(30); // Giving gift increasing happiness by 30
        assertEquals(80, pet.getVitalStats().getHappiness(), "Happiness should be increased by 30");

        pet.giveGift(50); // Should reach max happiness, which is 100
        assertEquals(type.getMaxHappiness(), pet.getVitalStats().getHappiness(), "Happiness should not exceed max value");
    }

    /**
     * Test the go - to - bed function of the {@link Pet} class
     */
    @Test
    void testGoToBed() {
        pet.goToBed(); // restore sleep to max
        assertEquals(type.getMaxSleep(), pet.getVitalStats().getSleep(), "Sleep should be set to max value");
    }

    /**
     * test the playing function of the {@link Pet} class
     * test its cooldown
     */
    @Test
    void testPlay() {
        String result = pet.play(30); // increase by 30
        assertEquals("You played with your pet!", result, "Playing should increase happiness");
        assertEquals(80, pet.getVitalStats().getHappiness(), "Happiness should be increased by play value");

        String cooldownResult = pet.play(30); // attempt to play after cooldown
        assertTrue(cooldownResult.contains("Cooldown active!"), "Second play attempt should trigger cooldown");
    }

    /**
     * Test the take - to - vet function of the {@link Pet} class
     * Test its cooldown
     */
    @Test
    void testTakeToVet() {
        pet.getVitalStats().decreaseHealth(40); // decrease health by 40
        String result = pet.takeToVet();
        assertEquals("Your pet visited the vet!", result, "Vet visit should increase health to max");
        assertEquals(type.getMaxHealth(), pet.getVitalStats().getHealth(), "Health should be restored to max after vet visit");

        String cooldownResult = pet.takeToVet(); // attempt visit after cooldown
        assertTrue(cooldownResult.contains("Cooldown active!"), "Second vet visit attempt should trigger cooldown");
    }

    /**
     * Test for the dead state function for the {@link Pet} class
     */
    @Test
    void testDeadState() {
        pet.getVitalStats().decreaseHealth(50); // Health diminishes to 0
        assertTrue(pet.deadState(), "Pet should be dead when health reaches 0");
    }
}
