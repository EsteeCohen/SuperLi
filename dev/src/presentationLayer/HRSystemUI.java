package src.presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import src.serviceLayer.HRSystemUIService;
import src.serviceLayer.RoleService;
import src.serviceLayer.ShiftService;

public class HRSystemUI {

    private HRSystemUIService hrService;
    private RoleService roleService;
    private ShiftService shiftService;


    private final Scanner scanner;

    public HRSystemUI() {
        this.hrService = HRSystemUIService.getInstance();

        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Human Resource System!");
        
        EmployeePL employee = login();

        boolean isRunning = true;
        while (isRunning) {
            isRunning = handleChoice(employee);
        }
        
        scanner.close();
    }

    private EmployeePL login() {

        System.out.print("Enter your ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        EmployeePL employee = new EmployeePL(hrService.login(id, password));
        System.out.println("Login successful!");
        return employee; // החזרת העובד המחובר
    }

    private void displayMenuForEmployee(EmployeePL employee) {
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

    private boolean handleChoice(EmployeePL employee) {
        displayMenuForEmployee(employee);
        int choice = getUserChoice();
        
        if (choice == 0) {
            System.out.println("Exiting the system. Goodbye!");
            return false;
        }
        if (employee.getRole().contains("manager")) {
            return handleHRManagerChoice(choice, employee);        }
        else{
            return handleEmployeeChoice(choice, employee);
        }
    }

    private boolean handleHRManagerChoice(int choice,EmployeePL employee) {
        switch (choice) {
            case 1:
                RegistrationPresentation registration = new RegistrationPresentation(hrService, scanner);
                registration.registerNewEmployee();
                break;
            case 2:
                AvailabilityForm availabilityForm = new AvailabilityForm(hrService, scanner);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 3:
                RoleAssignmentPresentation roleAssignment = new RoleAssignmentPresentation(roleService, scanner);
                roleAssignment.assignRoleToEmployee();
                break;
            case 4:
                RoleCreationPresentation roleCreation = new RoleCreationPresentation(roleService, scanner);
                roleCreation.createNewRole();
                break;
            case 5:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner);
                shiftsTable.showShiftTable();
                shiftsTable.assignEmployeeToShift();
                break;
            case 6:
                EmployeeSearchPresentation employeeSearch = new EmployeeSearchPresentation(hrService, scanner);
                employeeSearch.searchEmployee();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }

    private boolean handleEmployeeChoice(int choice, EmployeePL employee) {
        switch (choice) {
            case 1:
                AvailabilityForm availabilityForm = new AvailabilityForm(hrService, scanner);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 2:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner);
                shiftsTable.showShiftTable();                
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }
} 