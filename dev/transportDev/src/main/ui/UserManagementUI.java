package transportDev.src.main.ui;

import java.util.List;
import java.util.Scanner;
import transportDev.src.main.controllers.FacadeController;
import transportDev.src.main.entities.User;
import transportDev.src.main.enums.UserRole;

public class UserManagementUI {
    // :)
    private Scanner scanner;
    private FacadeController facadeController;
    private String sessionId;
    
    /**
     * User Management UI constructor
     */
    public UserManagementUI(FacadeController facadeController, String sessionId) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    /**
     * Start User Management interface
     */
    public void start() {
        // Check permission to manage users
        if (!facadeController.isAuthorized(sessionId, "MANAGE", "USER")) {
            System.out.println("You do not have permission to manage users in the system.");
            return;
        }
        
        boolean exit = false;
        
        while (!exit) {
            displayMenu();
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    addNewUser();
                    break;
                case 2:
                    viewUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deactivateUser();
                    break;
                case 5:
                    viewAllUsers();
                    break;
                case 6:
                    viewUsersByRole();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }
    
    /**
     * Display User Management menu
     */
    private void displayMenu() {
        System.out.println("\n=== User Management ===");
        System.out.println("1. Add New User");
        System.out.println("2. View User");
        System.out.println("3. Update User");
        System.out.println("4. Deactivate User");
        System.out.println("5. View All Users");
        System.out.println("6. View Users by Role");
        System.out.println("0. Return to Main Menu");
    }
    
    /**
     * Add a new user
     */
    private void addNewUser() {
        System.out.println("\n=== Add New User ===");
        
        String id = getStringInput("Enter user ID: ");
        String username = getStringInput("Enter username: ");
        String password = getStringInput("Enter password: ");
        String fullName = getStringInput("Enter full name: ");
        
        System.out.println("\nSelect role:");
        for (UserRole role : UserRole.values()) {
            System.out.println("- " + role);
        }
        
        String roleStr = getStringInput("Enter role: ");
        
        boolean success = facadeController.addUser(id, username, password, fullName, roleStr);
        
        if (success) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("Error adding user. Please check the input values and try again.");
        }
    }
    
    /**
     * View a specific user
     */
    private void viewUser() {
        String userId = getStringInput("Enter user ID: ");
        User user = facadeController.getCurrentUser(userId);
        
        if (user != null) {
            displayUserDetails(user);
        } else {
            System.out.println("User not found or you don't have permission to view this user.");
        }
    }
    
    /**
     * Update user details
     */
    private void updateUser() {
        String userId = getStringInput("Enter user ID: ");
        User user = facadeController.getCurrentUser(sessionId);
        
        if (user == null) {
            System.out.println("User not found or you don't have permission to update this user.");
            return;
        }
        
        displayUserDetails(user);
        
        System.out.println("\nEnter new details (leave blank to keep current values):");
        
        String username = getStringInput("Username [" + user.getUsername() + "]: ");
        if (username.isEmpty()) {
            username = user.getUsername();
        }
        
        String password = getStringInput("New password (leave blank to keep current): ");
        
        String fullName = getStringInput("Full name [" + user.getFullName() + "]: ");
        if (fullName.isEmpty()) {
            fullName = user.getFullName();
        }
        
        System.out.println("\nCurrent role: " + user.getRole());
        System.out.println("Available roles:");
        for (UserRole role : UserRole.values()) {
            if (role != user.getRole()) {
                System.out.println("- " + role);
            }
        }
        
        String roleStr = getStringInput("Enter new role (leave blank to keep current): ");
        String role = roleStr.isEmpty() ? user.getRole().toString() : roleStr;
        
        boolean success = facadeController.updateUser(userId, username, password, fullName, role);
        
        if (success) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Error updating user. Please check the input values and try again.");
        }
    }
    
    /**
     * Deactivate a user
     */
    private void deactivateUser() {
        String userId = getStringInput("Enter user ID to deactivate: ");
        User user = facadeController.getCurrentUser(sessionId);
        
        if (user == null) {
            System.out.println("User not found or you don't have permission to deactivate this user.");
            return;
        }
        
        String confirm = getStringInput("Are you sure you want to deactivate this user? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            boolean success = facadeController.deactivateUser(userId);
            if (success) {
                System.out.println("User deactivated successfully.");
            } else {
                System.out.println("Failed to deactivate user.");
            }
        }
    }
    
    /**
     * View all users
     */
    private void viewAllUsers() {
        List<User> users = facadeController.getAllUsers(sessionId);
        
        if (users.isEmpty()) {
            System.out.println("No users in the system.");
        } else {
            System.out.println("\n=== All Users ===");
            for (User user : users) {
                displayUserDetails(user);
                System.out.println("------------------------");
            }
        }
    }
    
    /**
     * View users by role
     */
    private void viewUsersByRole() {
        System.out.println("\nAvailable roles:");
        for (UserRole role : UserRole.values()) {
            System.out.println("- " + role);
        }
        
        String roleStr = getStringInput("Enter role: ");
        try {
            UserRole role = UserRole.valueOf(roleStr.toUpperCase());
            List<User> users = facadeController.getAllUsers(sessionId);
            boolean found = false;
            
            System.out.println("\n=== Users with role: " + role + " ===");
            for (User user : users) {
                if (user.getRole() == role) {
                    displayUserDetails(user);
                    System.out.println("------------------------");
                    found = true;
                }
            }
            
            if (!found) {
                System.out.println("No users found with role: " + role);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid role.");
        }
    }
    
    /**
     * Display user details
     */
    private void displayUserDetails(User user) {
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUsername());
        System.out.println("Full Name: " + user.getFullName());
        System.out.println("Role: " + user.getRole());
        System.out.println("Status: " + (user.isActive() ? "Active" : "Inactive"));
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
