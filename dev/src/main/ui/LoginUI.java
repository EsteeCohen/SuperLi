package src.main.ui;

import java.util.Scanner;

import src.main.controllers.UserController;
import src.main.entities.User;

public class LoginUI {
    private Scanner scanner;
    private UserController userController;
    
    /**
     * Login UI constructor
     */
    public LoginUI(UserController userController) {
        this.scanner = new Scanner(System.in);
        this.userController = userController;
    }
    
    /**
     * Display login screen
     */
    public void displayLoginScreen() {
        System.out.println("\n=== Transport Management System - Login ===");
        System.out.println("Please enter your login details:");
    }
    
    /**
     * Handle login process
     * @return Session ID and information about the logged-in user, or null if login failed
     */
    public String processLogin() {
        displayLoginScreen();
        
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            String username = getStringInput("Username: ");
            String password = getStringInput("Password: ");
            
            String sessionId = userController.login(username, password);
            
            if (sessionId != null) {
                User user = userController.getCurrentUser(sessionId);
                System.out.println("\nWelcome, " + user.getFullName() + "!");
                System.out.println("Role: " + user.getRole());
                return sessionId;
            } else {
                attempts++;
                System.out.println("Login failed. Username or password incorrect.");
                
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("Remaining attempts: " + (MAX_ATTEMPTS - attempts));
                } else {
                    System.out.println("Maximum login attempts exceeded. Please try again later.");
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Handle logout
     */
    public void logout(String sessionId) {
        if (userController.logout(sessionId)) {
            System.out.println("You have successfully logged out of the system.");
        } else {
            System.out.println("Error during logout process.");
        }
    }
    
    /**
     * Get text input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
