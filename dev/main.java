import employeeDev.src.presentationLayer.UserInputManager;
import employeeDev.src.serviceLayer.Factory;
import java.util.Scanner;
import transportDev.src.main.TransportApp;

public class main {
    public static void main(String[] args) {
        try{
            // Load the SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found. Please ensure it is included in your classpath.");
            return;
        }

        Factory employeeFactory = new Factory();
        TransportApp transportApp = new TransportApp(employeeFactory);
        employeeFactory.setTransportScheduleService(transportApp.getITransportScheduleService());

        boolean isEmpty = employeeFactory.getSiteService().getAllSites().isEmpty()
                       && transportApp.getTruckService().getAllTrucks().isEmpty();
        if (isEmpty) {
            System.out.println("=======================================================");
            System.out.println("  First run detected: the database appears to be empty.");
            System.out.println("  To get started:");
            System.out.println("  1. Open the HR system and add sites and employees.");
            System.out.println("  2. Open the Transport system and add trucks.");
            System.out.println("  3. Assign driver roles to employees in the HR system.");
            System.out.println("=======================================================");
            System.out.println();
        }

        System.out.println("=== please pick a system ===");
        System.out.println("1. Employee Management System");
        System.out.println("2. Transport Management System");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        Scanner scanner = new Scanner(System.in);
        int choice = Integer.parseInt(UserInputManager.getUserInput(scanner));
            switch (choice) {
                case 1:
                    System.out.println("Starting Employee Management System...");
                    employeeFactory.startSystem();
                    break;
                case 2:
                    System.out.println("Starting Transport Management System...");
                    transportApp.startSystem();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }
}
