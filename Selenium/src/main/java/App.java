import javax.swing.SwingUtilities;

public class App {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DriverService driverService = new DriverService();
            UserInterfaceController uiController = new UserInterfaceController(driverService);
            uiController.createAndShowUI();
        });
    }

}
