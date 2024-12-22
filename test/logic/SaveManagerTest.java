package logic;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SaveManager} class.
 * This class verifies the functionality of saving and loading a pet's state to and from a CSV file.
 */
public class SaveManagerTest {

    private File tempFile;

    /**
     * Test that sets up the environment before each test
     * Makes a temporary file to be used for loading/saving files
     * @throws IOException if an I/O error occurs
     */
    @BeforeEach
    public void setUp() throws IOException {
        // Create a temporary file for testing
        tempFile = File.createTempFile("test_pet", ".csv");
        tempFile.deleteOnExit();  // Delete the file when JVM exits
    }

    /**
     * Clean up the test environment after each test
     */
    @AfterEach
    public void tearDown() {
        // deletes the temporary file
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    /**
     * Tests the functionality of the pet being saved to the file and then loading it back
     * Ensure that the saved/loaded pet both have same attributes
     */
    @Test
    public void testSaveAndLoadPet() {
        try {
            // Create a PetType, VitalStatistics, and Pet
            PetType type = new PetType("Dog", 100, 100, 100, 100, 5, 5, 5, 5);
            VitalStatistics stats = new VitalStatistics(80, 90, 70, 100);
            Pet pet = new Pet("Buddy", type, stats, System.currentTimeMillis(), System.currentTimeMillis(),
                    false, false, false, false);

            // inventory data for the pet
            pet.getFoodInventory().put("Bone", 3);
            pet.getGiftInventory().put("Toy", 1);

            // pet nsaved to the temp file
            SaveManager.savePet(tempFile.getAbsolutePath(), pet);

            // Load the pet
            Pet loadedPet = SaveManager.loadPet(tempFile.getAbsolutePath());

            // Assert that the loaded pet has the same attributes as its original
            assertNotNull(loadedPet, "Loaded pet should not be null");
            assertEquals(pet.getName(), loadedPet.getName(), "Names should match");
            assertEquals(pet.getType().getTypeName(), loadedPet.getType().getTypeName(), "Pet types should match");
            assertEquals(pet.getVitalStats().getHealth(), loadedPet.getVitalStats().getHealth(), "Health should match");
            assertEquals(pet.getVitalStats().getSleep(), loadedPet.getVitalStats().getSleep(), "Sleep should match");
            assertEquals(pet.getVitalStats().getFullness(), loadedPet.getVitalStats().getFullness(), "Fullness should match");
            assertEquals(pet.getVitalStats().getHappiness(), loadedPet.getVitalStats().getHappiness(), "Happiness should match");
            assertEquals(pet.getLastPlay(), loadedPet.getLastPlay(), "Last play times should match");
            assertEquals(pet.getLastVetVisit(), loadedPet.getLastVetVisit(), "Last vet visit times should match");
            assertEquals(pet.isSleeping(), loadedPet.isSleeping(), "Sleeping states should match");
            assertEquals(pet.isHungry(), loadedPet.isHungry(), "Hungry states should match");
            assertEquals(pet.isAngry(), loadedPet.isAngry(), "Angry states should match");
            assertEquals(pet.isDead(), loadedPet.isDead(), "Dead states should match");

            // Assert inventory matches
            assertEquals(pet.getFoodInventory(), loadedPet.getFoodInventory(), "Food inventory should match");
            assertEquals(pet.getGiftInventory(), loadedPet.getGiftInventory(), "Gift inventory should match");

        } catch (IOException e) { // failure if error is caught
            fail("IOException occurred during the test: " + e.getMessage());
        }
    }
}
