package src.main.ui;

import java.util.Scanner;

import src.main.controllers.UserController;
import src.main.entities.User;

public class LoginUI {
    private Scanner scanner;
    private UserController userController;
    
    /**
     * בנאי לממשק התחברות
     */
    public LoginUI(UserController userController) {
        this.scanner = new Scanner(System.in);
        this.userController = userController;
    }
    
    /**
     * הצגת מסך התחברות
     */
    public void displayLoginScreen() {
        System.out.println("\n=== מערכת ניהול הובלות - התחברות ===");
        System.out.println("אנא הזן את פרטי ההתחברות שלך:");
    }
    
    /**
     * טיפול בתהליך ההתחברות
     * @return מזהה הפעלה ומידע על המשתמש המחובר, או null אם ההתחברות נכשלה
     */
    public String processLogin() {
        displayLoginScreen();
        
        int attempts = 0;
        final int MAX_ATTEMPTS = 3;
        
        while (attempts < MAX_ATTEMPTS) {
            String username = getStringInput("שם משתמש: ");
            String password = getStringInput("סיסמה: ");
            
            String sessionId = userController.login(username, password);
            
            if (sessionId != null) {
                User user = userController.getCurrentUser(sessionId);
                System.out.println("\nברוך הבא, " + user.getFullName() + "!");
                System.out.println("תפקיד: " + user.getRole());
                return sessionId;
            } else {
                attempts++;
                System.out.println("התחברות נכשלה. שם משתמש או סיסמה שגויים.");
                
                if (attempts < MAX_ATTEMPTS) {
                    System.out.println("נסיונות נותרים: " + (MAX_ATTEMPTS - attempts));
                } else {
                    System.out.println("מספר נסיונות ההתחברות עבר את המותר. נסה שוב מאוחר יותר.");
                    return null;
                }
            }
        }
        
        return null;
    }
    
    /**
     * טיפול בהתנתקות
     */
    public void logout(String sessionId) {
        if (userController.logout(sessionId)) {
            System.out.println("התנתקת מהמערכת בהצלחה.");
        } else {
            System.out.println("שגיאה בתהליך ההתנתקות.");
        }
    }
    
    /**
     * קבלת קלט טקסט מהמשתמש
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
