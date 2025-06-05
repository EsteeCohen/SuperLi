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
