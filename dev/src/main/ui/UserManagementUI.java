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
     * בנאי לממשק ניהול משתמשים
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
     * התחלת ממשק ניהול משתמשים
     */
    public void start() {
        // בדיקת הרשאה לניהול משתמשים
        if (!userController.isAuthorized(sessionId, "MANAGE", "USER")) {
            System.out.println("אין לך הרשאה לנהל משתמשים במערכת.");
            return;
        }
        
        boolean exit = false;
        
        while (!exit) {
            displayMenu();
            int choice = getIntInput("בחר אפשרות: ");
            
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
                    System.out.println("אפשרות לא תקינה, נסה שנית");
            }
        }
    }
    
    /**
     * הצגת תפריט ניהול משתמשים
     */
    private void displayMenu() {
        System.out.println("\n=== ניהול משתמשים ===");
        System.out.println("1. הוספת משתמש חדש");
        System.out.println("2. צפייה במשתמש");
        System.out.println("3. עדכון משתמש");
        System.out.println("4. השבתת משתמש");
        System.out.println("5. צפייה בכל המשתמשים");
        System.out.println("6. צפייה במשתמשים לפי תפקיד");
        System.out.println("0. חזרה לתפריט הראשי");
    }
    
    /**
     * הוספת משתמש חדש
     */
    private void addNewUser() {
        System.out.println("\n=== הוספת משתמש חדש ===");
        
        String id = getStringInput("הזן מזהה משתמש: ");
        String username = getStringInput("הזן שם משתמש: ");
        String password = getStringInput("הזן סיסמה: ");
        String fullName = getStringInput("הזן שם מלא: ");
        
        System.out.println("בחר תפקיד:");
        displayRoles();
        
        int roleChoice = getIntInput("בחירתך: ");
        String role = getRoleByChoice(roleChoice);
        
        if (role == null) {
            System.out.println("בחירת תפקיד לא תקינה.");
            return;
        }
        
        boolean success = userController.addUser(id, username, password, fullName, role);
        
        if (success) {
            System.out.println("המשתמש נוסף בהצלחה!");
        } else {
            System.out.println("שגיאה בהוספת המשתמש. ייתכן ששם המשתמש או המזהה כבר קיימים במערכת.");
        }
    }
    
    /**
     * צפייה במשתמש ספציפי
     */
    private void viewUser() {
        System.out.println("\n=== צפייה במשתמש ===");
        String userId = getStringInput("הזן מזהה משתמש: ");
        
        User user = userController.getUserById(sessionId, userId);
        
        if (user != null) {
            displayUserDetails(user);
        } else {
            System.out.println("משתמש לא נמצא או שאין לך הרשאה לצפות במשתמש זה.");
        }
    }
    
    /**
     * עדכון פרטי משתמש
     */
    private void updateUser() {
        System.out.println("\n=== עדכון משתמש ===");
        String userId = getStringInput("הזן מזהה משתמש: ");
        
        User user = userController.getUserById(sessionId, userId);
        
        if (user == null) {
            System.out.println("משתמש לא נמצא או שאין לך הרשאה לעדכן משתמש זה.");
            return;
        }
        
        displayUserDetails(user);
        
        System.out.println("\nהזן את הפרטים החדשים (השאר ריק כדי לא לשנות):");
        
        String username = getStringInput("שם משתמש [" + user.getUsername() + "]: ");
        if (username.isEmpty()) {
            username = user.getUsername();
        }
        
        String password = getStringInput("סיסמה חדשה (השאר ריק כדי לא לשנות): ");
        
        String fullName = getStringInput("שם מלא [" + user.getFullName() + "]: ");
        if (fullName.isEmpty()) {
            fullName = user.getFullName();
        }
        
        System.out.println("בחר תפקיד [" + user.getRole() + "]:");
        displayRoles();
        
        String input = getStringInput("בחירתך (השאר ריק כדי לא לשנות): ");
        String role;
        
        if (input.isEmpty()) {
            role = user.getRole().toString();
        } else {
            try {
                int roleChoice = Integer.parseInt(input);
                role = getRoleByChoice(roleChoice);
                if (role == null) {
                    System.out.println("בחירת תפקיד לא תקינה. התפקיד לא ישתנה.");
                    role = user.getRole().toString();
                }
            } catch (NumberFormatException e) {
                System.out.println("קלט לא תקין. התפקיד לא ישתנה.");
                role = user.getRole().toString();
            }
        }
        
        boolean success = userController.updateUser(userId, username, password, fullName, role);
        
        if (success) {
            System.out.println("המשתמש עודכן בהצלחה!");
        } else {
            System.out.println("שגיאה בעדכון המשתמש. ייתכן ששם המשתמש כבר קיים במערכת.");
        }
    }
    
    /**
     * השבתת משתמש
     */
    private void deactivateUser() {
        System.out.println("\n=== השבתת משתמש ===");
        String userId = getStringInput("הזן מזהה משתמש להשבתה: ");
        
        User user = userController.getUserById(sessionId, userId);
        
        if (user == null) {
            System.out.println("משתמש לא נמצא או שאין לך הרשאה להשבית משתמש זה.");
            return;
        }
        
        if (!user.isActive()) {
            System.out.println("המשתמש כבר מושבת.");
            return;
        }
        
        // מניעת השבתה עצמית
        User currentUser = userController.getCurrentUser(sessionId);
        if (currentUser.getId().equals(userId)) {
            System.out.println("לא ניתן להשבית את חשבונך הנוכחי.");
            return;
        }
        
        System.out.println("האם אתה בטוח שברצונך להשבית את המשתמש " + user.getFullName() + "? (כן/לא)");
        String confirmation = getStringInput("אישור: ");
        
        if (confirmation.equalsIgnoreCase("כן")) {
            boolean success = userController.deactivateUser(userId);
            
            if (success) {
                System.out.println("המשתמש הושבת בהצלחה!");
            } else {
                System.out.println("שגיאה בהשבתת המשתמש.");
            }
        } else {
            System.out.println("פעולת ההשבתה בוטלה.");
        }
    }
    
    /**
     * צפייה בכל המשתמשים
     */
    private void viewAllUsers() {
        System.out.println("\n=== רשימת כל המשתמשים ===");
        
        List<User> users = userController.getAllUsers(sessionId);
        
        if (users == null || users.isEmpty()) {
            System.out.println("אין משתמשים במערכת או שאין לך הרשאה לצפות ברשימת המשתמשים.");
            return;
        }
        
        for (User user : users) {
            displayUserDetails(user);
            System.out.println("--------------------");
        }
    }
    
    /**
     * צפייה במשתמשים לפי תפקיד
     */
    private void viewUsersByRole() {
        System.out.println("\n=== צפייה במשתמשים לפי תפקיד ===");
        
        System.out.println("בחר תפקיד:");
        displayRoles();
        
        int roleChoice = getIntInput("בחירתך: ");
        String role = getRoleByChoice(roleChoice);
        
        if (role == null) {
            System.out.println("בחירת תפקיד לא תקינה.");
            return;
        }
        
        List<User> users = userController.getUsersByRole(sessionId, role);
        
        if (users == null || users.isEmpty()) {
            System.out.println("אין משתמשים בתפקיד זה או שאין לך הרשאה לצפות ברשימה.");
            return;
        }
        
        System.out.println("משתמשים בתפקיד " + role + ":");
        for (User user : users) {
            displayUserDetails(user);
            System.out.println("--------------------");
        }
    }
    
    /**
     * הצגת פרטי משתמש
     */
    private void displayUserDetails(User user) {
        System.out.println("מזהה: " + user.getId());
        System.out.println("שם משתמש: " + user.getUsername());
        System.out.println("שם מלא: " + user.getFullName());
        System.out.println("תפקיד: " + user.getRole());
        System.out.println("סטטוס: " + (user.isActive() ? "פעיל" : "מושבת"));
    }
    
    /**
     * הצגת התפקידים האפשריים
     */
    private void displayRoles() {
        System.out.println("1. מנהל מערכת (SYSTEM_ADMIN)");
        System.out.println("2. מנהל הובלות (TRANSPORT_MANAGER)");
        System.out.println("3. מתכנן מסלולים (DISPATCHER)");
        System.out.println("4. נהג (DRIVER)");
        System.out.println("5. מנהל מחסן (WAREHOUSE_MANAGER)");
        System.out.println("6. צופה (VIEWER)");
    }
    
    /**
     * המרת בחירת תפקיד למחרוזת תפקיד
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
     * קבלת קלט מספרי מהמשתמש
     */
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("אנא הזן מספר תקין.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // ניקוי ה-buffer
        return input;
    }
    
    /**
     * קבלת קלט טקסט מהמשתמש
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
