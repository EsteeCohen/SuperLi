package transportDev.src.main.ui;

import java.util.Scanner;
import transportDev.src.main.controllers.FacadeController;

public class ReportsUI {
    // :)
    private Scanner scanner;
    private FacadeController facadeController;
    
    public ReportsUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
    }
    
    public void start() {
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    generateTransportReport();
                    break;
                case 2:
                    generateDriverReport();
                    break;
                case 3:
                    generateTruckReport();
                    break;
                case 4:
                    generateOrderReport();
                    break;
                case 5:
                    generateIncidentReport();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid selection, please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== Reports and Statistics ===");
        System.out.println("1. Transport Reports");
        System.out.println("2. Driver Reports");
        System.out.println("3. Truck Reports");
        System.out.println("4. Order Reports");
        System.out.println("5. Incident Reports");
        System.out.println("0. Return to Main Menu");
    }

    private void generateTransportReport() {
        System.out.println("\n=== Transport Reports ===");
        System.out.println("Transport reports functionality will be implemented here.");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    private void generateDriverReport() {
        System.out.println("\n=== Driver Reports ===");
        System.out.println("Driver reports functionality will be implemented here.");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    private void generateTruckReport() {
        System.out.println("\n=== Truck Reports ===");
        System.out.println("Truck reports functionality will be implemented here.");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    private void generateOrderReport() {
        System.out.println("\n=== Order Reports ===");
        System.out.println("Order reports functionality will be implemented here.");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    private void generateIncidentReport() {
        System.out.println("\n=== Incident Reports ===");
        System.out.println("Incident reports functionality will be implemented here.");
        System.out.println("Press Enter to continue");
        scanner.nextLine();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
} 