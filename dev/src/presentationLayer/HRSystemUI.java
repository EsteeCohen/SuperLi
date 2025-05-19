package presentationLayer;

import java.util.Scanner;
import serviceLayer.AssigningService;
import serviceLayer.EmployeeService;
import serviceLayer.Factory;
import serviceLayer.RoleService;
import serviceLayer.ShiftService;

public class HRSystemUI {

    private RoleService roleService;
    private ShiftService shiftService;
    private EmployeeService employeeService;
    private AssigningService assigningService;

    private final String ADMIN_ROLE_NAME = "HrManager";
    private final String SHIFT_MNG_ROLE = "Shift Manager";

    private final Scanner scanner;

    public HRSystemUI() {
        Factory factory = new Factory();
        this.roleService = factory.getRoleService();
        this.shiftService = factory.getShiftService();
        this.employeeService = factory.getEmployeeService();
        this.assigningService = factory.getAssigningService();

        this.scanner = new Scanner(System.in);

        Initialize();
    }

    private void Initialize() {
        // Registering the admin
        roleService.createRole(ADMIN_ROLE_NAME);
        roleService.createRole(SHIFT_MNG_ROLE);
        employeeService.registerAdmin();
        shiftService.createShiftsForNextWeek();
        assigningService.initializeRequirements();
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
        while (true) {
            String id = UserInputManager.promptForString(scanner, "Enter your ID (or 'q' to cancel): ", "Login cancelled.", "q");
            if (id == null) return null;
            String password = UserInputManager.promptForString(scanner, "Enter your password (or 'q' to cancel): ", "Login cancelled.", "q");
            if (password == null) return null;
            try {
                EmployeePL employee = new EmployeePL(employeeService.login(id, password));
                System.out.println("Login successful!");
                return employee;
            } catch (NullPointerException e) {
                System.out.println("Login failed! Please check your ID and password.");
            }
        }
    }

    private void displayMenuForEmployee(EmployeePL employee) {
        if (employee.getRoles().contains(ADMIN_ROLE_NAME)) {
            displayHRManagerMenu();
        } else {
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
        System.out.println("7. Update employee details");
        System.out.println("0. Exit");
    }

    private void displayEmployeeMenu() {
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Enter availability");
        System.out.println("2. View my shifts");
        System.out.println("0. Exit");
    }

    private int getUserChoice() {
        String input = UserInputManager.getUserInput(scanner);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }

    private boolean handleChoice(EmployeePL employee) {
        displayMenuForEmployee(employee);
        int choice = getUserChoice();

        if (choice == 0) {
            System.out.println("Exit...");
            return false; // Exit the current loop
        }
        if (employee.getRoles().contains(ADMIN_ROLE_NAME)) {
            return handleHRManagerChoice(choice, employee);
        } else {
            return handleEmployeeChoice(choice, employee);
        }
    }

    private boolean handleHRManagerChoice(int choice, EmployeePL employee) {
        switch (choice) {
            case 1:
                RegistrationPresentation registration = new RegistrationPresentation(employeeService, scanner);
                registration.registerNewEmployee();
                break;
            case 2:
                AvailabilityForm availabilityForm = new AvailabilityForm(scanner, shiftService);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 3:
                RoleAssignmentPresentation roleAssignment = new RoleAssignmentPresentation(employeeService, roleService, scanner);
                roleAssignment.assignRoleToEmployee();
                break;
            case 4:
                RoleCreationPresentation roleCreation = new RoleCreationPresentation(roleService, scanner);
                roleCreation.createNewRole();
                break;
            case 5:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner, assigningService, employeeService);
                shiftsTable.showShiftTable();
                shiftsTable.assignEmployeeToShift();
                break;
            case 6:
                EmployeeSearchPresentation employeeSearch = new EmployeeSearchPresentation(employeeService, scanner);
                employeeSearch.searchEmployee();
                break;
            case 7:
                EmployeeUpdatePresentation employeeUpdate = new EmployeeUpdatePresentation(employeeService, scanner);
                employeeUpdate.updateEmployeeDetails();
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
                AvailabilityForm availabilityForm = new AvailabilityForm(scanner, shiftService);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 2:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner, assigningService, employeeService);
                shiftsTable.showShiftTable();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }
}