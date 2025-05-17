package src.main.ui;

import java.util.Scanner;
import src.main.controllers.*;
import src.main.entities.User;
import src.main.enums.UserRole;
import src.main.ui.*;

public class MainUI {
    private Scanner scanner;
    private String sessionId;
    private User currentUser;
    
    // User interfaces
    private LoginUI loginUI;
    private TransportUI transportUI;
    private OrderUI orderUI;
    private FleetUI fleetUI;
    private SiteUI siteUI;
//    private IncidentUI incidentUI;
    private UserManagementUI userManagementUI;
    
    // Controllers
    private UserController userController;
    private TransportController transportController;
    private OrderController orderController;
    private TruckController truckController;
    private DriverController driverController;
    private SiteController siteController;
    private IncidentController incidentController;
    
    /**
     * Main UI constructor
     */
    public MainUI(UserController userController, TransportController transportController,
                OrderController orderController, TruckController truckController,
                DriverController driverController, SiteController siteController, IncidentController incidentController,
                LoginUI loginUI, TransportUI transportUI, OrderUI orderUI,
                FleetUI fleetUI, SiteUI siteUI, UserManagementUI userManagementUI) {
        
        this.scanner = new Scanner(System.in);
        
        // Define controllers
        this.userController = userController;
        this.transportController = transportController;
        this.orderController = orderController;
        this.truckController = truckController;
        this.driverController = driverController;
        this.siteController = siteController;
        this.incidentController = incidentController;
        this.userManagementUI = userManagementUI;
        
        // Define user interfaces
        this.loginUI = loginUI;
        this.transportUI = transportUI;
        this.orderUI = orderUI;
        this.fleetUI = fleetUI;
        this.siteUI = siteUI;
//        this.incidentUI = incidentUI;
    }
    
    /**
     * Start the system
     */
    public void start() {
        boolean exit = false;

        while (!exit) {
            // Login to system
//            this.sessionId = loginUI.processLogin();
            this.sessionId = userManagementUI.getSessionId();

            // If login fails, return to login screen or exit
            if (sessionId == null) {
                System.out.println("Do you want to try logging in again? (yes/no)");
                String retry = getStringInput("Your choice: ");
                if (!retry.equalsIgnoreCase("yes")) {
                    exit = true;
                }
                continue;
            }

            // Get logged-in user details
            this.currentUser = userController.getCurrentUser(sessionId);

            // Run main menu
            boolean loggedIn = true;
            while (loggedIn) {
                displayMainMenu();
                int choice = getIntInput("Select an option: ");
    
                switch (choice) {
                    case 1:
                        if (hasAccess("TRANSPORT")) transportUI.start();
                        else showAccessDenied();
                        break;
                    case 2:
                        if (hasAccess("FLEET")) fleetUI.start();
                        else showAccessDenied();
                        break;
                    case 3:
                        if (hasAccess("SITE")) siteUI.start();
                        else showAccessDenied();
                        break;
                    case 4:
                        if (hasAccess("ORDER")) orderUI.start();
                        else showAccessDenied();
                        break;
                    case 5:
                        if (hasAccess("USER_MANAGEMENT")) userManagementUI.start();
                        else showAccessDenied();
                        break;
                    case 6:
                        showUserProfile();
                        break;
                    case 0:
                        logout();
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("Invalid option, please try again");
                }
            }
    
            System.out.println("Do you want to log in again? (yes/no)");
            String again = getStringInput("Your choice: ");
            if (!again.equalsIgnoreCase("yes")) {
                exit = true;
            }
        }
    
        System.out.println("Thank you for using the Transport Management System. Goodbye!");
    }
    
    /**
     * Display main menu according to user permissions
     */
    private void displayMainMenu() {
        System.out.println("\n=== Transport Management System - Main Menu ===");
        System.out.println("Hello, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        System.out.println("--------------------------------------");
        
        if (hasAccess("TRANSPORT")) {
            System.out.println("1. Transport Management");
        }
        
        if (hasAccess("FLEET")) {
            System.out.println("2. Driver and Truck Management");
        }
        
        if (hasAccess("SITE")) {
            System.out.println("3. Site Management");
        }
        
        if (hasAccess("ORDER")) {
            System.out.println("4. Order Management");
        }
        
        if (hasAccess("USER_MANAGEMENT")) {
            System.out.println("5. User Management");
        }
        
        System.out.println("6. View User Profile");
        System.out.println("0. Logout");
    }
    
    /**
     * Check permission for specific module
     */
    private boolean hasAccess(String module) {
        switch (module) {
            case "TRANSPORT":
                return userController.isAuthorized(sessionId, "VIEW", "TRANSPORT");
            case "FLEET":
                return userController.isAuthorized(sessionId, "VIEW", "TRUCK") || 
                       userController.isAuthorized(sessionId, "VIEW", "DRIVER");
            case "SITE":
                return userController.isAuthorized(sessionId, "VIEW", "SITE");
            case "ORDER":
                return userController.isAuthorized(sessionId, "VIEW", "ORDER");
            case "SCHEDULE":
                return userController.isAuthorized(sessionId, "VIEW", "SCHEDULE");
            case "INCIDENT":
                return userController.isAuthorized(sessionId, "VIEW", "INCIDENT");
            case "USER_MANAGEMENT":
                return userController.isAuthorized(sessionId, "MANAGE", "USER");
            default:
                return false;
        }
    }
    
    /**
     * Display current user profile
     */
    private void showUserProfile() {
        System.out.println("\n=== User Profile ===");
        System.out.println("ID: " + currentUser.getId());
        System.out.println("Username: " + currentUser.getUsername());
        System.out.println("Full Name: " + currentUser.getFullName());
        System.out.println("Role: " + currentUser.getRole());
        
        // Display access permissions
        System.out.println("\n=== Access Permissions ===");
        if (currentUser.getRole() == UserRole.SYSTEM_ADMIN) {
            System.out.println("- Full access to all modules");
        } else {
            if (hasAccess("TRANSPORT")) {
                System.out.println("- Access to Transport module");
            }
            if (hasAccess("FLEET")) {
                System.out.println("- Access to Driver and Truck module");
            }
            if (hasAccess("SITE")) {
                System.out.println("- Access to Site module");
            }
            if (hasAccess("ORDER")) {
                System.out.println("- Access to Order module");
            }
            if (hasAccess("SCHEDULE")) {
                System.out.println("- Access to Schedule module");
            }
            if (hasAccess("INCIDENT")) {
                System.out.println("- Access to Incident module");
            }
            if (hasAccess("USER_MANAGEMENT")) {
                System.out.println("- Access to User Management");
            }
        }
        
        System.out.println("\nPress Enter to return to the main menu");
        scanner.nextLine();
    }
    
    /**
     * Display access denied message
     */
    private void showAccessDenied() {
        System.out.println("\nYou do not have permission to access this module.");
        System.out.println("Please contact the system administrator if you need access.");
        System.out.println("\nPress Enter to continue");
        scanner.nextLine();
    }
    
    /**
     * Logout from the system
     */
    private void logout() {
        loginUI.logout(sessionId);
        this.sessionId = null;
        this.currentUser = null;
    }
    
    /**
     * Get integer input from user
     */
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
    
    /**
     * Get string input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
