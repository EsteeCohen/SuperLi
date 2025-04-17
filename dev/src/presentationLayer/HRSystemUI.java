package src.presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import src.serviceLayer.HRSystemUIService;

public class HRSystemUI {

    private HRSystemUIService hrService;
    private final Scanner scanner;

    public HRSystemUI() {
        this.hrService = HRSystemUIService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Human Resource System!");
        
        Employee employee = login();

        boolean isRunning = true;
        while (isRunning) {
            isRunning = handleChoice(employee);
        }
        
        scanner.close();
    }

    private Employee login() {

        System.out.print("Enter your ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        Employee employee = HRSystemUIService.login(id, password);
        if (employee != null) {
            System.out.println("Login successful!");
            // ניתוב למסך הבא לפי תפקיד המשתמש
        } else {
            System.out.println("Login failed. Please try again.");
            return login();
        }
    }

    private void displayMenuForEmployee(Employee employee) {
        if (employee.getRole().contains("manager")) {
            displayHRManagerMenu();
        }
        else{
            displayEmployeeMenu();
        }
    }
    private void displayHRManagerMenu() {
        System.out.println("\n=== HR Manager Menu ===");
        System.out.println("1. Register new employee");
        System.out.println("2. Enter availability");
        System.out.println("3. Assign roles to employee");
        System.out.println("4. Create new role");
        System.out.println("5. View and manage shift schedule");
        System.out.println("6. Search employee by ID");
        System.out.println("0. Exit");
    }

    private void displayEmployeeMenu() {
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Enter availability");
        System.out.println("2. View my shifts");
        System.out.println("0. Exit");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean handleChoice(Employee employee) {
        displayMenuForEmployee(employee);
        int choice = getUserChoice();
        
        if (choice == 0) {
            System.out.println("Exiting the system. Goodbye!");
            return false;
        }
        if (employee.getRole().contains("manager")) {
            return handleHRManagerChoice(choice);        }
        else{
            return handleEmployeeChoice(choice, employee);
        }return true;
    }

    private boolean handleHRManagerChoice(int choice) {
        switch (choice) {
            case 1:
                hrService.registerEmployeeUI(scanner);
                break;
            case 2:
                hrService.enterAvailabilityUI(scanner);
                break;
            case 3:
                hrService.assignRoleUI(scanner);
                break;
            case 4:
                hrService.createRoleUI(scanner);
                break;
            case 5:
                hrService.viewOrAssignShiftsUI(scanner);
                break;
            case 6:
                hrService.searchEmployeeUI(scanner);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }

    private boolean handleEmployeeChoice(int choice, Employee employee) {
        switch (choice) {
            case 1:
                hrService.enterAvailabilityUI(scanner, employee);
                break;
            case 2:
                hrService.viewShiftsForEmployee(employee);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }
} 