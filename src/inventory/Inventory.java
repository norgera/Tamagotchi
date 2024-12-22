package inventory;
import java.util.Map;
import java.util.HashMap;

/**
 * The Inventory class represents the collection of items and their quantities
 * that a player owns in the game. It provides methods to add, remove, and
 * query items in the inventory.
 */

public class Inventory {

    private Map<Item, Integer> items;

    /**
     * Constructs an empty Inventory.
     * Initializes the map to store items and their quantities.
     */

    public Inventory() {
        items = new HashMap<>();
    }

    /**
     * Adds a specified quantity of an item to the inventory.
     * If the item already exists in the inventory, its quantity will be updated.
     *
     * @param item The item to be added to the inventory.
     * @param quantity The quantity of the item to be added.
     */

    public void addItem(Item item, int quantity) {
        items.put(item, items.getOrDefault(item, 0) + quantity);
    }

    /**
     * Removes a specified quantity of an item from the inventory.
     * If the quantity to be removed exceeds the current quantity, it will be adjusted accordingly.
     *
     * @param item The item to be removed from the inventory.
     * @param quantity The quantity of the item to be removed.
     */

    public void removeItem(Item item, int quantity) {
        int currentQuantity = items.getOrDefault(item, 0);
        if (currentQuantity >= quantity) {
            items.put(item, currentQuantity - quantity);
        }
    }

    /**
     * Returns the quantity of a specific item in the inventory.
     * If the item is not in the inventory, returns 0.
     *
     * @param item The item whose quantity is to be retrieved.
     * @return The quantity of the specified item.
     */

    public int getItemQuantity(Item item) {
        return items.getOrDefault(item, 0);
    }

}