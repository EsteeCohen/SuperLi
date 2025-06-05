package employeeDev.src.presentationLayer;

import employeeDev.src.serviceLayer.AssigningService;
import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.Factory;
import employeeDev.src.serviceLayer.RoleService;
import employeeDev.src.serviceLayer.ShiftService;
import employeeDev.src.serviceLayer.SiteService;
import java.util.Scanner;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public class HRSystemUI {

    private final RoleService roleService;
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final AssigningService assigningService;
    private final SiteService siteService;

    private final String ADMIN_ROLE_NAME = "HrManager";
    private final String SHIFT_MNG_ROLE = "Shift Manager";
    private final String WAREHOUSEMAN = "Warehouseman";
    private final String DRIVER = "Driver";
    private final String SITE_NAME = "MainSite";
    private final String ADMIN_ID = "admin";


    private final Scanner scanner;

    public HRSystemUI() {
        Factory factory = new Factory();
        this.roleService = factory.getRoleService();
        this.shiftService = factory.getShiftService();
        this.employeeService = factory.getEmployeeService();
        this.assigningService = factory.getAssigningService();
        this.siteService = factory.getSiteService();

        this.scanner = new Scanner(System.in);
        // uncomment the following line to initialize the system with default values not from DB
        // Initialize();
    }

    private void Initialize() {
        // Registering the admin
        roleService.createRole(ADMIN_ROLE_NAME);
        roleService.createRole(SHIFT_MNG_ROLE);
        roleService.createRole(WAREHOUSEMAN);
        roleService.createRole(DRIVER);
        siteService.addSite(SITE_NAME, "Main Warehouse", "123 Main St", "123-456-7890", ShippingZone.CENTER.name());
        Site defSite = siteService.getSiteByName(SITE_NAME);
        employeeService.registerAdmin(ADMIN_ID,defSite);
        assigningService.initializeWeeklyRequirements();
        siteService.getAllSites().forEach(site -> {
            shiftService.createShiftsForNextWeek(site);
        });
        
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
        System.out.println("4. Unassign role from employee");
        System.out.println("5. Create new role");
        System.out.println("6. View and manage shift schedule");
        System.out.println("7. Search employee by ID");
        System.out.println("8. Update employee details");
        System.out.println("9. View corrent shifts and employees");
        System.out.println("0. Exit");
    }

    private void displayEmployeeMenu() {
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. Enter availability");
        System.out.println("2. View my shifts");
        System.out.println("3. View corrent shifts and employees");
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
                RegistrationPresentation registration = new RegistrationPresentation(employeeService,siteService, scanner);
                registration.registerNewEmployee();
                break;
            case 2:
                AvailabilityForm availabilityForm = new AvailabilityForm(scanner, shiftService, siteService);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 3:
                RoleAssignmentPresentation roleAssignment = new RoleAssignmentPresentation(employeeService, roleService, scanner);
                roleAssignment.assignRoleToEmployee();
                break;
            case 4:
                RoleUnassignmentPresentation roleUnassignment = new RoleUnassignmentPresentation(employeeService, shiftService, siteService, scanner);
                roleUnassignment.unassignRoleFromEmployee();
                break;
            case 5:
                RoleCreationPresentation roleCreation = new RoleCreationPresentation(roleService, scanner);
                roleCreation.createNewRole();
                break;
            case 6:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner, assigningService, employeeService, siteService);
                shiftsTable.showShiftTable();
                shiftsTable.manageShiftOptions();
                break;
            case 7:
                EmployeeSearchPresentation employeeSearch = new EmployeeSearchPresentation(employeeService, scanner);
                employeeSearch.searchEmployee();
                break;
            case 8:
                EmployeeUpdatePresentation employeeUpdate = new EmployeeUpdatePresentation(employeeService, scanner);
                employeeUpdate.updateEmployeeDetails();
                break;
            case 9:
                CurrentShiftsTablePresentation currentShifts = new CurrentShiftsTablePresentation(shiftService, siteService, scanner);
                currentShifts.showCurrentShiftsAndEmployees();
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
                AvailabilityForm availabilityForm = new AvailabilityForm(scanner, shiftService, siteService);
                availabilityForm.showAvailabilityForm(employee.getID());
                break;
            case 2:
                ShiftsTablePresentation shiftsTable = new ShiftsTablePresentation(shiftService, scanner, assigningService, employeeService, siteService);
                shiftsTable.showEmployeeShift(employee.getID());
                break;
            case 3:
                CurrentShiftsTablePresentation currentShifts = new CurrentShiftsTablePresentation(shiftService, siteService, scanner);
                currentShifts.showCurrentShiftsAndEmployees();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
        return true;
    }
}