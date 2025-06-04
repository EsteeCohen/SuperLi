package transportDev.src.main.ui;

import java.util.Scanner;
import transportDev.src.main.controllers.FacadeController;

public class LoginUI {
    // :)
    private Scanner scanner;
    private FacadeController facadeController;
    
    /**
     * Login UI constructor
     */
    public LoginUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
    }
    
    /**
     * Display login screen
     */
    public String login() {
        displayLoginHeader();
        
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            attempts++;
            
            String username = getStringInput("Username: ");
            if (username.isEmpty()) {
                System.out.println("Error: Username cannot be empty.");
                continue;
            }
            
            String password = getStringInput("Password: ");
            if (password.isEmpty()) {
                System.out.println("Error: Password cannot be empty.");
                continue;
            }
            
            String sessionId = facadeController.login(username, password);
            
            if (sessionId != null) {
                System.out.println("\nLogin successful!");
                String userName = facadeController.getCurrentUserName(sessionId);
                String userRole = facadeController.getCurrentUserRole(sessionId);
                System.out.println("Welcome, " + userName + " (" + userRole + ")");
                pressEnterToContinue();
                return sessionId;
            } else {
                System.out.println("\nInvalid username or password.");
                
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("You have " + (MAX_ATTEMPTS - attempts) + " attempt(s) remaining.");
                    if (!confirmContinue("Try again?")) {
                        break;
                    }
                } else {
                    System.out.println("Maximum login attempts exceeded.");
                }
            }
        }
        
        return null;
    }
    
    private void displayLoginHeader() {
        System.out.println("\n" + "=".repeat(40));
        System.out.println("=== Transport Management System ===");
        System.out.println("=".repeat(40));
        System.out.println();
    }
    
    private void displayDemoUsers() {
        // Security: Don't display usernames and passwords
        // Users should know their own credentials
    }
    
    private void showLoadingAnimation() {
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(300);
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
    
    /**
     * Handle logout
     */
    public boolean logout(String sessionId) {
        System.out.println("\n[LOGOUT] LOGOUT PROCESS");
        System.out.println("─".repeat(15));
        
        boolean success = facadeController.logout(sessionId);
        
        if (success) {
            System.out.println("[OK] Successfully logged out!");
            System.out.println("[SECURE] Session terminated securely.");
            System.out.println("[BYE] Thank you for using Transport Management System!");
        } else {
            System.out.println("[WARN] Logout warning: Session may not have been properly terminated.");
        }
        
        return success;
    }
    
    /**
     * Get text input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    // Helper methods
    private boolean confirmContinue(String message) {
        System.out.print(message + " (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("yes") || answer.equals("y");
    }
    
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
}
