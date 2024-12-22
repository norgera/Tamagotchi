package inventory;

/**
 * The Item class represents a general item that can be part of the inventory.
 * It contains common properties such as the name and description, which are 
 * shared by all item types.
 */

public abstract class Item {
    protected String name;
    protected String description;

    /**
     * Constructs an Item with the specified name and description.
     *
     * @param name The name of the item.
     * @param description A brief description of the item.
     */

    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the name of the item.
     *
     * @return The name of the item.
     */

    public String getItemName() {
        return name;
    }

    /**
     * Returns the description of the item.
     *
     * @return The description of the item.
     */

    public String getItemDescription() {
        return description;
    }
}
