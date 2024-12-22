package inventory;

/**
 * The FoodItem class represents an item that can be used to feed the pet.
 * It extends the Item class and adds functionality specific to food items,
 * such as the fullness value, which determines how much hunger the food satisfies.
 */

public class FoodItem extends Item {
    private int fullnessValue;

    /**
     * Constructs a FoodItem with the specified name, description, and fullness value.
     *
     * @param name The name of the food item.
     * @param description A brief description of the food item.
     * @param fullnessValue The value indicating how much hunger the food satisfies.
     */

    public FoodItem(String name, String description, int fullnessValue) {
        super(name, description);
        this.fullnessValue = fullnessValue;
    }

    /**
     * Returns the fullness value of the food item.
     * This value determines how much the food item satisfies the pet's hunger.
     *
     * @return The fullness value of the food item.
     */
     
    public int getFullnessValue() {
        return fullnessValue;

    }
}
