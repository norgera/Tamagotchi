import ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        // Initialize and launch the main menu
        javax.swing.SwingUtilities.invokeLater(() -> {
            MainMenu menu = new MainMenu();
            menu.setVisible(true);
        });
    }
}
