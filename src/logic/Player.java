package logic;
import inventory.Inventory;

public class Player {

    private String playerName;
    private Inventory inventory;

    /**
    * Constructor for the player class
     */
    public Player(String playerName) {
        this.playerName = playerName;
        this.inventory = new Inventory();
    }

    // Get player name method
    public String getPlayerName() {
        return playerName;
    }

    // Get Inventory method
    public Inventory getInventory() {
        return inventory;
    }
}





