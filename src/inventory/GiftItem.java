package inventory;

/**
 * The GiftItem class represents an item that can be given to the pet to 
 * increase its happiness. It extends the Item class and adds functionality 
 * specific to gift items, such as the happiness value, which determines 
 * how much happiness the gift increases.
 */

public class GiftItem extends Item {
        private int happinessValue;

        /**
         * Constructs a GiftItem with the specified name, description, and happiness value.
         *
         * @param name The name of the gift item.
         * @param description A brief description of the gift item.
         * @param happinessValue The value indicating how much happiness the gift increases.
         */

        public GiftItem(String name, String description, int happinessValue) {
                super(name, description);
                this.happinessValue = happinessValue;
        }

        /**
        * Returns the happiness value of the gift item.
        * This value determines how much the gift item increases the pet's happiness.
        *
        * @return The happiness value of the gift item.
        */

        public int getHappinessValue() {
                return happinessValue;
        }
}