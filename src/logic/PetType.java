package logic;

/**
 * Initialize the 3 predetermined pets in the game class
 * */
public class PetType {

    private String typeName;
    private int maxHealth;
    private int maxSleep;
    private int maxFullness;
    private int maxHappiness;
    private int fullnessDecayRate;
    private int sleepDecayRate;
    private int happinessDecayRate;
    private int healthDecayRate;

    public PetType(String typeName, int maxHealth, int maxSleep, int maxFullness, int maxHappiness, int fullnessDecayRate, int sleepDecayRate, int happinessDecayRate, int healthDecayRate) {
        this.typeName = typeName;
        this.maxHealth = maxHealth;
        this.maxSleep = maxSleep;
        this.maxFullness = maxFullness;
        this.maxHappiness = maxHappiness;
        this.fullnessDecayRate = fullnessDecayRate;
        this.sleepDecayRate = sleepDecayRate;
        this.happinessDecayRate = happinessDecayRate;
        this.healthDecayRate = healthDecayRate;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getMaxHappiness() {
        return maxHappiness;
    }

    public int getMaxFullness() {
        return maxFullness;
    }

    public int getMaxSleep() {
        return maxSleep;
    }

    public int getFullnessDecayRate() {
        return fullnessDecayRate;
    }

    public int getSleepDecayRate() {
        return sleepDecayRate;
    }

    public int getHappinessDecayRate() {
        return happinessDecayRate;
    }

    public int getHealthDecayRate() {
        return  healthDecayRate;
    }

}
