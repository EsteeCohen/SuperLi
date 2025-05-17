package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.UserController;
import src.main.entities.User;

public class UserManagementUI {
    private Scanner scanner;
    private UserController userController;
    private String sessionId;
    
    /**
     * User Management UI constructor
     */
    public UserManagementUI(UserController userController, String sessionId) {
        this.scanner = new Scanner(System.in);
        this.userController = userController;
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
        if (!userController.isAuthorized(sessionId, "MANAGE", "USER")) {
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
        
        System.out.println("Select role:");
        displayRoles();
        
        int roleChoice = getIntInput("Your choice: ");
        String role = getRoleByChoice(roleChoice);
        
        if (role == null) {
            System.out.println("Invalid role selection.");
            return;
        }
        
        boolean success = userController.addUser(id, username, password, fullName, role);
        
        if (success) {
            System.out.println("User added successfully!");
        } else {
            System.out.println("Error adding user. Username or ID may already exist in the system.");
        }
    }
    
    /**
     * View a specific user
     */
    private void viewUser() {
        System.out.println("\n=== View User ===");
        String userId = getStringInput("Enter user ID: ");
        
        User user = userController.getUserById(sessionId, userId);
        
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
        System.out.println("\n=== Update User ===");
        String userId = getStringInput("Enter user ID: ");
        
        User user = userController.getUserById(sessionId, userId);
        
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
        
        System.out.println("Select role [" + user.getRole() + "]:");
        displayRoles();
        
        String input = getStringInput("Your choice (leave blank to keep current): ");
        String role;
        
        if (input.isEmpty()) {
            role = user.getRole().toString();
        } else {
            try {
                int roleChoice = Integer.parseInt(input);
                role = getRoleByChoice(roleChoice);
                if (role == null) {
                    System.out.println("Invalid role selection. Role will not be changed.");
                    role = user.getRole().toString();
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Role will not be changed.");
                role = user.getRole().toString();
            }
        }
        
        boolean success = userController.updateUser(userId, username, password, fullName, role);
        
        if (success) {
            System.out.println("User updated successfully!");
        } else {
            System.out.println("Error updating user. Username may already exist in the system.");
        }
    }
    
    /**
     * Deactivate a user
     */
    private void deactivateUser() {
        System.out.println("\n=== Deactivate User ===");
        String userId = getStringInput("Enter user ID to deactivate: ");
        
        User user = userController.getUserById(sessionId, userId);
        
        if (user == null) {
            System.out.println("User not found or you don't have permission to deactivate this user.");
            return;
        }
        
        if (!user.isActive()) {
            System.out.println("The user is already deactivated.");
            return;
        }
        
        // Prevent self-deactivation
        User currentUser = userController.getCurrentUser(sessionId);
        if (currentUser.getId().equals(userId)) {
            System.out.println("You cannot deactivate your current account.");
            return;
        }
        
        System.out.println("Are you sure you want to deactivate user " + user.getFullName() + "? (yes/no)");
        String confirmation = getStringInput("Confirm: ");
        
        if (confirmation.equalsIgnoreCase("yes")) {
            boolean success = userController.deactivateUser(userId);
            
            if (success) {
                System.out.println("User deactivated successfully!");
            } else {
                System.out.println("Error deactivating user.");
            }
        } else {
            System.out.println("Deactivation cancelled.");
        }
    }
    
    /**
     * View all users
     */
    private void viewAllUsers() {
        System.out.println("\n=== List of All Users ===");
        
        List<User> users = userController.getAllUsers(sessionId);
        
        if (users == null || users.isEmpty()) {
            System.out.println("No users in the system or you don't have permission to view the user list.");
            return;
        }
        
        for (User user : users) {
            displayUserDetails(user);
            System.out.println("--------------------");
        }
    }
    
    /**
     * View users by role
     */
    private void viewUsersByRole() {
        System.out.println("\n=== View Users by Role ===");
        
        System.out.println("Select role:");
        displayRoles();
        
        int roleChoice = getIntInput("Your choice: ");
        String role = getRoleByChoice(roleChoice);
        
        if (role == null) {
            System.out.println("Invalid role selection.");
            return;
        }
        
        List<User> users = userController.getUsersByRole(sessionId, role);
        
        if (users == null || users.isEmpty()) {
            System.out.println("No users in this role or you don't have permission to view the list.");
            return;
        }
        
        System.out.println("Users in role " + role + ":");
        for (User user : users) {
            displayUserDetails(user);
            System.out.println("--------------------");
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
        System.out.println("Status: " + (user.isActive() ? "Active" : "Deactivated"));
    }
    
    /**
     * Display available roles
     */
    private void displayRoles() {
        System.out.println("1. System Administrator (SYSTEM_ADMIN)");
        System.out.println("2. Transport Manager (TRANSPORT_MANAGER)");
        System.out.println("3. Dispatcher (DISPATCHER)");
        System.out.println("4. Driver (DRIVER)");
        System.out.println("5. Warehouse Manager (WAREHOUSE_MANAGER)");
        System.out.println("6. Viewer (VIEWER)");
    }
    
    /**
     * Convert role choice to role string
     */
    private String getRoleByChoice(int choice) {
        switch (choice) {
            case 1:
                return "SYSTEM_ADMIN";
            case 2:
                return "TRANSPORT_MANAGER";
            case 3:
                return "DISPATCHER";
            case 4:
                return "DRIVER";
            case 5:
                return "WAREHOUSE_MANAGER";
            case 6:
                return "VIEWER";
            default:
                return null;
        }
    }
    
    /**
     * Get numeric input from user
     */
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer
        return input;
    }
    
    /**
     * Get text input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
