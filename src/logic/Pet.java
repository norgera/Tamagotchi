package logic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ui.GameplayScreen;

public class Pet {

    private String name;
    private int number;
    private PetType type;
    private VitalStatistics vitalStats;
    private long lastPlay;
    private long lastVetVisit;
    private boolean isDead = false;
    private boolean isAngry = false;
    private boolean isSleeping = false;
    private boolean isHungry = false;

    /**
     * Pet constructor
     */
    public Pet(String name, PetType type) {
        this.name = name;
        this.type = type;
        this.vitalStats = new VitalStatistics(
                type.getMaxHealth(),
                type.getMaxSleep(),
                type.getMaxFullness(),
                type.getMaxHappiness()
        );
    }

    /**
     * Pet Constructor to load existing pet
     *
     * @param petName
     * @param type
     * @param vitalStats
     * @param lastPlay
     * @param lastVetVisit
     * @param isSleeping
     * @param isHungry
     * @param isAngry
     * @param isDead
     */
    public Pet(String petName, PetType type, VitalStatistics vitalStats, long lastPlay, long lastVetVisit, boolean isSleeping, boolean isHungry, boolean isAngry, boolean isDead) {
        name = petName;
        this.type = type;
        this.vitalStats = vitalStats;
        this.lastPlay = lastPlay;
        this.lastVetVisit = lastVetVisit;
        this.isSleeping = isSleeping;
        this.isHungry = isHungry;
        this.isAngry = isAngry;
        this.isDead = isDead;
    }

    /**
     * Feed the pet a certain amount of food values
     */
    public void feed(int foodValue) {
        // Get the new fullness value after feeding the pet the food value
        int newFullness = Math.min(type.getMaxFullness(), vitalStats.getFullness() + foodValue); // If the foodValue + the current fullness exceeds the max fullness, then new fullness is the pet types max fullness value

        // Increase the fullness by the difference between new and old fullness
        vitalStats.increaseFullness(newFullness - vitalStats.getFullness());


    }

    /**
     * Method to give pet gifts to increase happiness
     */
    public void giveGift(int giftValue) {
        // Get the new happiness value by adding the play value which increases the pets happiness by the amount of the "play value"
        int newHappiness = Math.min(type.getMaxHappiness(), vitalStats.getHappiness() + giftValue);

        // Increase the happiness by the difference between new happiness and current happiness
        vitalStats.increaseHappiness(newHappiness - vitalStats.getHappiness());

    }

    /**
     * Method to put the pet to bed and increase the sleep to maximum
     */
    public void goToBed() {
        // check pet's current sleep value
        int currentSleepValue = vitalStats.getSleep();

        // Increase sleep by the difference between max sleep and currentSleepValue to reach the max
        vitalStats.increaseSleep(type.getMaxSleep() - currentSleepValue);

    }

    /**
     * Playing with the pet allows player to increase pets happiness
     */
//    public void play(int playValue) {
//        long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
//        long cooldownTime = 0; // 5 minutes in milliseconds
//
//        // Check if the cooldown time is over
//        if (currentTime - lastPlay > cooldownTime) {
//
//            // Get the new happiness value by adding the play value which increases the pets happiness by the amount of the "play value"
//            int newHappiness = Math.min(type.getMaxHappiness(), vitalStats.getHappiness() + playValue);
//
//            // Increase the happiness by the difference between new happiness and current happiness
//            vitalStats.increaseHappiness(newHappiness - vitalStats.getHappiness());
//
//            cooldownTime += 300000;
//
//            // Set the lastPlay time to current
//            lastPlay = currentTime;
//
//        } else {
//            System.out.println(currentTime - lastPlay);
//        }
//    }

    public String play(int playValue) {
        long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
        long cooldownTime = 60000; // Cooldown period: 5 minutes in milliseconds

        // Check if the cooldown time is over
        if (currentTime - lastPlay >= cooldownTime) {
            // Play with the pet immediately

            // Calculate the new happiness value
            int newHappiness = Math.min(type.getMaxHappiness(), vitalStats.getHappiness() + playValue);

            // Increase happiness
            vitalStats.increaseHappiness(newHappiness - vitalStats.getHappiness());

            // Set the lastPlay time to the current time (start cooldown)
            lastPlay = currentTime;

            //System.out.println("Playing with your pet! Happiness increased.");
            return "You played with your pet!";

        } else {
            // Calculate remaining cooldown time
            long remainingTime = (cooldownTime - (currentTime - lastPlay)) / 1000; // In seconds
            System.out.println("Cooldown active! Please wait " + remainingTime + " seconds before playing again.");
            return "Cooldown active! Please wait " + remainingTime + " seconds before playing with you pet again.";
        }
    }

    /**
     * Exercise pet method
     */
    public void exercise(int healthValue, int sleepValue, int fullnessValue) {
        // Get the new health after increasing health by the given health value
        int newHealth = Math.min(type.getMaxHealth(), vitalStats.getHealth() + healthValue);

        // Increase the health of the pet
        vitalStats.increaseHealth(type.getMaxHealth() - newHealth);

        // new sleep value after subtracting the amount of sleep the pet will lose after the exercise
        int newSleep = Math.max(0, vitalStats.getSleep() - sleepValue);

        // Decrease the sleep value for the pet
        vitalStats.decreaseSleep(type.getMaxSleep() - newSleep);

        // new fullness value after subtracting the amount of fullness the pet will lose after the exercise
        int newFullness = Math.max(0, vitalStats.getFullness() - fullnessValue);

        // Decrease the fullness value for the pet
        vitalStats.decreaseFullness(type.getMaxFullness() - newFullness);

    }

    /**
     * Method for taking pet to the vet and increase health to max value
     */
    public String takeToVet() {
        long currentTime = System.currentTimeMillis(); // Get the current time in milliseconds
        long cooldownTime = 60000; // Cooldown period: 5 minutes in milliseconds

        // Check if the cooldown time is over
        if (currentTime - lastVetVisit >= cooldownTime) {
            // Restore pet's health to max
            int healthToIncrease = type.getMaxHealth() - vitalStats.getHealth();
            vitalStats.increaseHealth(healthToIncrease);

            // Set the lastVetVisit time to the current time (start cooldown)
            lastVetVisit = currentTime;

            return "Your pet visited the vet!";
        } else {
            // Calculate remaining cooldown time
            long remainingTime = (cooldownTime - (currentTime - lastVetVisit)) / 1000; // In seconds
            return "Cooldown active! You can visit the vet again in " + remainingTime + " seconds.";
        }
    }


    /**
     * Returns a list off all the pets stats
     */
    public List<Integer> getCurentSprite() {
        // Create a list containing all the vital statistics
        List<Integer> vitalStatsList = new ArrayList<>();
        vitalStatsList.add(vitalStats.getHealth());
        vitalStatsList.add(vitalStats.getFullness());
        vitalStatsList.add(vitalStats.getHappiness());
        vitalStatsList.add(vitalStats.getSleep());

        // Return the list of all stats
        return vitalStatsList;
    }

    /**
     * Checks if the pet is dead or not
     */
    public boolean deadState() {
        // If any of the vital stats are 0, the pet is in a dead state
        // If the pet is not dead return false
        return vitalStats.getHealth() == 0 || vitalStats.getFullness() == 0 || vitalStats.getHappiness() == 0 || vitalStats.getSleep() == 0;
    }



    public PetType getType() {
        return type;
    }


    public void setVitalStats(VitalStatistics vitalStats) {
        this.vitalStats = vitalStats;
    }


    private Map<String, Integer> foodInventory = new HashMap<>();
    private Map<String, Integer> giftInventory = new HashMap<>();

    public Map<String, Integer> getFoodInventory() {
        return foodInventory;
    }

    public void setFoodInventory(Map<String, Integer> foodInventory) {
        this.foodInventory = foodInventory;
    }

    public Map<String, Integer> getGiftInventory() {
        return giftInventory;
    }

    public void setGiftInventory(Map<String, Integer> giftInventory) {
        this.giftInventory = giftInventory;
    }



    // Getters and setters for states
    public boolean isSleeping() {
        return isSleeping;
    }


    public boolean isHungry() {
        return isHungry;
    }


    public boolean isAngry() {
        return isAngry;
    }


    public long getLastVetVisit() {
        return lastVetVisit;
    }

    public long getLastPlay() {
        return lastPlay;
    }


    public boolean isDead() {
        return isDead;
    }

    public String getName() {
        return name;
    }

    public VitalStatistics getVitalStats() {
        return vitalStats;
    }

    public void checkStates() {
        // Dead state: health reaches 0
        if (vitalStats.getHealth() <= 0) {
            isDead = true;
        } else {
            isDead = false;
        }

        // Sleeping state: sleep reaches 0, or pet is actively sleeping
        if (!isDead && vitalStats.getSleep() <= 0) {
            isSleeping = true;
        } else if (vitalStats.getSleep() >= type.getMaxSleep()) {
            isSleeping = false;
        }

        // Hungry state: fullness reaches critical threshold
        if (!isDead && vitalStats.getFullness() < type.getMaxFullness() * 0.25) {
            isHungry = true;
        } else {
            isHungry = false;
        }

        // Angry state: happiness reaches 0
        if (!isDead && vitalStats.getHappiness() <= 0) {
            isAngry = true;
        } else if (vitalStats.getHappiness() >= type.getMaxHappiness() / 2) {
            isAngry = false;
        }
    }
}


