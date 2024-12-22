package logic;

import inventory.Inventory;
import logic.Pet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GameData {
    private Pet pet;
    private Inventory foodInventory;
    private Inventory giftInventory;
    private int score;

    public GameData(Pet pet, Inventory foodInventory, Inventory giftInventory) {
        this.pet = pet;
        this.foodInventory = foodInventory;
        this.giftInventory = giftInventory;
        this.score = 0;
    }

    // Getters
    public Pet getPet() { return pet; }
    public Inventory getFoodInventory() { return foodInventory; }
    public Inventory getGiftInventory() { return giftInventory; }
    public int getScore() { return score; }

    // Method to update the score
    public void increaseScore(int points) {
        score += points;
    }

    public void decreaseScore(int points) {
        score -= points;
        if (score < 0) {
            score = 0;
        }
    }
}



