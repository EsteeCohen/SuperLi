package transportDev.src.main.ui;

import java.util.Scanner;

import transportDev.src.main.controllers.FacadeController;
import transportDev.src.main.services.Factory;
public class MainUI {
    // :)
    private Scanner scanner;
    private FacadeController facadeController;
    private String sessionId;
    
    // UI Components
    private LoginUI loginUI;
    private TransportUI transportUI;
    private OrderUI orderUI;
    private FleetUI fleetUI;
    private SiteUI siteUI;
    private UserManagementUI userManagementUI;
    private ReportsUI reportsUI;

    public MainUI(FacadeController facadeController, LoginUI loginUI, TransportUI transportUI, 
                  OrderUI orderUI, FleetUI fleetUI, SiteUI siteUI, 
                  UserManagementUI userManagementUI, ReportsUI reportsUI) {
        Factory factory = new Factory();
        this.scanner = new Scanner(System.in);
        this.facadeController = factory.getFacadeController();
        this.loginUI = loginUI;
        this.transportUI = transportUI;
        this.orderUI = orderUI;
        this.fleetUI = fleetUI;
        this.siteUI = siteUI;
        this.userManagementUI = userManagementUI;
        this.reportsUI = reportsUI;
    }

    public void start() {
        System.out.println("=== Transport Management System ===");
        
        // Login process
        while (sessionId == null) {
            sessionId = loginUI.login();
            if (sessionId == null) {
                if (!confirmContinue("Try again?")) {
                    System.out.println("Goodbye!");
                    return;
                }
            }
        }
        
        // Main application loop
        runMainMenu();
    }
    
    private void runMainMenu() {
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            
            int choice = getIntInput("Select option: ");
            
            switch (choice) {
                case 1:
                    transportManagement();
                    break;
                case 2:
                    itemsAndShipmentManagement();
                    break;
                case 3:
                    fleetManagement();
                    break;
                case 4:
                    siteManagement();
                    break;
                case 5:
                    reportsAndStatistics();
                    break;
                case 6:
                    userSettings();
                    break;
                case 0:
                    exit = confirmLogout();
                    break;
                default:
                    System.out.println("Invalid option. Please select a number from 0-6.");
                    pressEnterToContinue();
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== Transport Management System ===");
        System.out.println();
        System.out.println("1. Transport Management");
        System.out.println("2. Items and Shipments Management");
        System.out.println("3. Fleet Management");
        System.out.println("4. Site Management");
        System.out.println("5. Reports and Statistics");
        System.out.println("6. User Settings");
        System.out.println("0. Logout");
        System.out.println();
    }
    
    private void transportManagement() {
        transportUI.start();
    }
    
    private void itemsAndShipmentManagement() {
        orderUI.start();
    }
    
    private void fleetManagement() {
        fleetUI.start();
    }
    
    private void siteManagement() {
        siteUI.start();
    }
    
    private void reportsAndStatistics() {
        reportsUI.start();
    }
    
    private void userSettings() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n=== User Settings ===");
            System.out.println();
            System.out.println("1. User Management");
            System.out.println("2. Change Password");
            System.out.println("3. View Profile");
            System.out.println("0. Back to Main Menu");
            System.out.println();
            
            int choice = getIntInput("Select option: ");
            switch (choice) {
                case 1:
                    if (facadeController.isAuthorized(sessionId, "MANAGE", "USER")) {
                        userManagementUI.start();
                    } else {
                        System.out.println("You don't have permission to manage users.");
                        pressEnterToContinue();
                    }
                    break;
                case 2:
                    changePassword();
                    break;
                case 3:
                    viewProfile();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
                    pressEnterToContinue();
            }
        }
    }
    
    private void changePassword() {
        System.out.println("\n=== Change Password ===");
        
        String currentPassword = getStringInput("Enter current password: ");
        String newPassword = getStringInput("Enter new password: ");
        String confirmPassword = getStringInput("Confirm new password: ");
        
        if (!newPassword.equals(confirmPassword)) {
            System.out.println("Passwords don't match.");
            pressEnterToContinue();
            return;
        }
        
        boolean success = facadeController.changePassword(sessionId, currentPassword, newPassword);
        if (success) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Check your current password.");
        }
        pressEnterToContinue();
    }
    
    private void viewProfile() {
        System.out.println("\n=== User Profile ===");
        
        String userName = facadeController.getCurrentUserName(sessionId);
        String userRole = facadeController.getCurrentUserRole(sessionId);
        
        System.out.println("Name: " + userName);
        System.out.println("Role: " + userRole);
        System.out.println("Session ID: " + sessionId);
            
        pressEnterToContinue();
    }
    
    private boolean confirmLogout() {
        boolean logout = confirmContinue("Are you sure you want to logout?");
        
        if (logout) {
            facadeController.logout(sessionId);
            System.out.println("Successfully logged out. Goodbye!");
        }
        
        return logout;
    }
    
    // Helper methods
    private boolean confirmContinue(String message) {
        System.out.print(message + " (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("yes") || answer.equals("y");
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
