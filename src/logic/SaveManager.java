package logic;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@code SaveManager} class provides methods to save and load the state of a pet
 * to and from a CSV file. It handles serialization and deserialization of pet data,
 * including pet type, vital statistics, and status flags.
 */
public class SaveManager {

    /**
     * The CSV header used when writing pet data to a file.
     */
    private static final String CSV_HEADER = "typeName,maxHealth,maxSleep,maxFullness,maxHappiness," +
            "fullnessDecayRate,sleepDecayRate,happinessDecayRate,healthDecayRate,name,type,vitalStats," +
            "lastPlay,lastVetVisit,isSleeping,isHungry,isAngry,isDead,health,sleep,fullness,happiness," +
            "foodInventory,giftInventory";


    /**
     * Saves a single pet's state to a CSV file.
     *
     * @param filePath the path to the CSV file where the pet's data will be saved
     * @param pet      the {@code Pet} object representing the pet's state
     * @throws IOException if an I/O error occurs during writing to the file
     */
    public static void savePet(String filePath, Pet pet) throws IOException {
        // Ensure the parent directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs(); // Create the directory
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Write the CSV header
            writer.write(CSV_HEADER);
            writer.newLine();

            // Write the pet's data
            writer.write(formatPetData(pet));
        }
    }


    /**
     * Loads a single pet's state from a CSV file.
     *
     * @param filePath the path to the CSV file from which the pet's data will be loaded
     * @return a {@code Pet} object representing the loaded pet's state
     * @throws IOException if an I/O error occurs during reading from the file
     *                     or if the file does not contain valid pet data
     */
    public static Pet loadPet(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Skip the header
            reader.readLine();

            // Read the pet data (assumes single pet per file)
            String line = reader.readLine();
            if (line != null) {
                String[] data = line.split(",");
                return parsePetData(data);
            }
        }
        throw new IOException("No pet data found in the file: " + filePath);
    }

    /**
     * Formats a pet's data into a CSV row.
     *
     * @param pet the {@code Pet} object whose data is to be formatted
     * @return a {@code String} representing the pet's data in CSV format
     */
    private static String formatPetData(Pet pet) {
        PetType type = pet.getType();
        VitalStatistics stats = pet.getVitalStats();

        // Serialize inventories to a string
        String foodInventory = serializeInventory(pet.getFoodInventory());
        String giftInventory = serializeInventory(pet.getGiftInventory());

        return String.join(",",
                type.getTypeName(),
                String.valueOf(type.getMaxHealth()),
                String.valueOf(type.getMaxSleep()),
                String.valueOf(type.getMaxFullness()),
                String.valueOf(type.getMaxHappiness()),
                String.valueOf(type.getFullnessDecayRate()),
                String.valueOf(type.getSleepDecayRate()),
                String.valueOf(type.getHappinessDecayRate()),
                String.valueOf(type.getHealthDecayRate()),
                pet.getName(),
                type.getTypeName(),
                stats.getHealth() + "|" + stats.getSleep() + "|" + stats.getFullness() + "|" + stats.getHappiness(),
                String.valueOf(pet.getLastPlay()),
                String.valueOf(pet.getLastVetVisit()),
                String.valueOf(pet.isSleeping()),
                String.valueOf(pet.isHungry()),
                String.valueOf(pet.isAngry()),
                String.valueOf(pet.isDead()),
                String.valueOf(stats.getHealth()),
                String.valueOf(stats.getSleep()),
                String.valueOf(stats.getFullness()),
                String.valueOf(stats.getHappiness()),
                foodInventory, // Serialized food inventory
                giftInventory  // Serialized gift inventory
        );
    }

    // Helper method to serialize inventory
    private static String serializeInventory(Map<String, Integer> inventory) {
        if (inventory == null || inventory.isEmpty()) {
            return ""; // Return empty string for empty or null inventories
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            if (sb.length() > 0) sb.append(";");
            sb.append(entry.getKey()).append(":").append(entry.getValue());
        }
        return sb.toString();
    }



    /**
     * Parses a pet's data from an array of CSV values.
     *
     * @param data an array of {@code String} values representing the pet's data
     * @return a {@code Pet} object constructed from the provided data
     */
    private static Pet parsePetData(String[] data) {
        // PetType fields
        String typeName = data[0];
        int maxHealth = Integer.parseInt(data[1]);
        int maxSleep = Integer.parseInt(data[2]);
        int maxFullness = Integer.parseInt(data[3]);
        int maxHappiness = Integer.parseInt(data[4]);
        int fullnessDecayRate = Integer.parseInt(data[5]);
        int sleepDecayRate = Integer.parseInt(data[6]);
        int happinessDecayRate = Integer.parseInt(data[7]);
        int healthDecayRate = Integer.parseInt(data[8]);

        // Pet fields
        String petName = data[9];
        String petType = data[10];

        // VitalStatistics (split by "|")
        String[] vitalStatsData = data[11].split("\\|");
        int health = Integer.parseInt(vitalStatsData[0]);
        int sleep = Integer.parseInt(vitalStatsData[1]);
        int fullness = Integer.parseInt(vitalStatsData[2]);
        int happiness = Integer.parseInt(vitalStatsData[3]);

        long lastPlay = Long.parseLong(data[12]);
        long lastVetVisit = Long.parseLong(data[13]);

        boolean isSleeping = Boolean.parseBoolean(data[14]);
        boolean isHungry = Boolean.parseBoolean(data[15]);
        boolean isAngry = Boolean.parseBoolean(data[16]);
        boolean isDead = Boolean.parseBoolean(data[17]);

        // Deserialize inventories
        Map<String, Integer> foodInventory = deserializeInventory(data[22]);
        Map<String, Integer> giftInventory = deserializeInventory(data[23]);

        // Create PetType and VitalStatistics
        PetType type = new PetType(typeName, maxHealth, maxSleep, maxFullness, maxHappiness,
                fullnessDecayRate, sleepDecayRate, happinessDecayRate, healthDecayRate);

        VitalStatistics vitalStats = new VitalStatistics(health, sleep, fullness, happiness);

        // Create and return Pet object
        Pet pet = new Pet(petName, type, vitalStats, lastPlay, lastVetVisit, isSleeping, isHungry, isAngry, isDead);
        pet.setFoodInventory(foodInventory);
        pet.setGiftInventory(giftInventory);

        return pet;
    }


    private static Map<String, Integer> deserializeInventory(String inventoryData) {
        Map<String, Integer> inventory = new HashMap<>();
        if (inventoryData != null && !inventoryData.isEmpty()) {
            String[] items = inventoryData.split(";");
            for (String item : items) {
                try {
                    String[] parts = item.split(":");
                    if (parts.length == 2) {
                        inventory.put(parts[0], Integer.parseInt(parts[1]));
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing inventory item: " + item);
                }
            }
        }
        return inventory; // Return an empty map if no data
    }



    /*
     * Example usage:
     *
     * public static void main(String[] args) throws IOException {
     *     Pet pet = new Pet(
     *             "Charlie",
     *             new PetType("Dog", 100, 100, 100, 100, 5, 5, 5, 5),
     *             new VitalStatistics(80, 90, 70, 100),
     *             System.currentTimeMillis(),
     *             System.currentTimeMillis(),
     *             false, false, false, true
     *     );
     *     SaveManager.savePet("files/pet1.csv", pet);
     *     Pet pet2 = SaveManager.loadPet("files/pet1.csv");
     *     System.out.println("Pet type: " + pet2.getType().getTypeName());
     *     System.out.println("Pet name: " + pet2.getName());
     * }
     */

}
