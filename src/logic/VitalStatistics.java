package logic;

public class VitalStatistics {

    private int health;
    private int sleep;
    private int fullness;
    private int happiness;


    /**
     * VitalStatistics class construtor
     */
    public VitalStatistics(int health, int sleep, int fullness, int happiness) {
        this.health = health;
        this.sleep = sleep;
        this.fullness = fullness;
        this.happiness = happiness;
    }


    /**
     * Increase the health attribute
     */
    public void increaseHealth(int value) {
        health += value;
    }


    /**
     * Decrease the health attribute
     */
    public void decreaseHealth(int value) {
        health -= value;
    }


    /**
     * Increase the sleep attribute
     */
    public void increaseSleep(int value) {
        sleep += value;
    }


    /**
     * Decrease the sleep attribute
     */
    public void decreaseSleep(int value) {
        sleep -= value;
    }


    /**
     * Increase the fullness attribute
     */
    public void increaseFullness(int value) {
        fullness += value;
    }


    /**
     * Decrease the fullness attribute
     */
    public void decreaseFullness(int value) {
        fullness -= value;
    }

    /**
     * Increase the happiness attribute
     */
    public void increaseHappiness(int value) {
        happiness += value;
    }


    /**
     * Decrease the happiness attribute
     */
    public void decreaseHappiness(int value) {
        happiness -= value;
    }


    /**
     * Getter methods for all attributes
     */
    public int getHealth() {
        return health;
    }

    public int getHappiness() {
        return happiness;
    }

    public int getFullness() {
        return fullness;
    }

    public int getSleep() {
        return sleep;
    }

    // Add setter methods
    public void setHealth(int health) {
        this.health = health;
    }

    public void setSleep(int sleep) {
        this.sleep = sleep;
    }

    public void setFullness(int fullness) {
        this.fullness = fullness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }


}
