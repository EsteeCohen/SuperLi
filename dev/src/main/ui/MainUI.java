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
    
    // ממשקי משתמש
    private LoginUI loginUI;
    private TransportUI transportUI;
    private OrderUI orderUI;
    private FleetUI fleetUI;
    private SiteUI siteUI;
    private ScheduleUI scheduleUI;
//    private IncidentUI incidentUI;
    private UserManagementUI userManagementUI;
    
    // בקרים
    private UserController userController;
    private TransportController transportController;
    private OrderController orderController;
    private TruckController truckController;
    private DriverController driverController;
    private SiteController siteController;
    private ScheduleController scheduleController;
    private IncidentController incidentController;
    
    /**
     * בנאי לממשק הראשי
     */
    public MainUI(UserController userController, TransportController transportController,
                OrderController orderController, TruckController truckController,
                DriverController driverController, SiteController siteController,
                ScheduleController scheduleController, IncidentController incidentController,
                LoginUI loginUI, TransportUI transportUI, OrderUI orderUI,
                FleetUI fleetUI, SiteUI siteUI, ScheduleUI scheduleUI, UserManagementUI userManagementUI) {
        
        this.scanner = new Scanner(System.in);
        
        // הגדרת בקרים
        this.userController = userController;
        this.transportController = transportController;
        this.orderController = orderController;
        this.truckController = truckController;
        this.driverController = driverController;
        this.siteController = siteController;
        this.scheduleController = scheduleController;
        this.incidentController = incidentController;
        this.userManagementUI = userManagementUI;
        
        // הגדרת ממשקי משתמש
        this.loginUI = loginUI;
        this.transportUI = transportUI;
        this.orderUI = orderUI;
        this.fleetUI = fleetUI;
        this.siteUI = siteUI;
        this.scheduleUI = scheduleUI;
//        this.incidentUI = incidentUI;
    }
    
    /**
     * התחלת המערכת
     */
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            // התחברות למערכת
            this.sessionId = loginUI.processLogin();
            
            // אם ההתחברות נכשלה, נחזור למסך ההתחברות או נסיים
            if (sessionId == null) {
                System.out.println("האם לנסות להתחבר שוב? (כן/לא)");
                String retry = getStringInput("בחירתך: ");
                if (!retry.equalsIgnoreCase("כן")) {
                    exit = true;
                }
                continue;
            }
            
            // קבלת פרטי המשתמש המחובר
            this.currentUser = userController.getCurrentUser(sessionId);
            
            // הפעלת התפריט הראשי
            boolean loggedIn = true;
            while (loggedIn) {
                displayMainMenu();
                int choice = getIntInput("בחר אפשרות: ");
                
                switch (choice) {
                    case 1:
                        if (hasAccess("TRANSPORT")) {
                            transportUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
                    case 2:
                        if (hasAccess("FLEET")) {
                            fleetUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
                    case 3:
                        if (hasAccess("SITE")) {
                            siteUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
                    case 4:
                        if (hasAccess("ORDER")) {
                            orderUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
                    case 5:
                        if (hasAccess("SCHEDULE")) {
                            scheduleUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
//                    case 6:
//                        if (hasAccess("INCIDENT")) {
//                            incidentUI.start();
//                        } else {
//                            showAccessDenied();
//                        }
//                        break;
                    case 7:
                        if (hasAccess("USER_MANAGEMENT")) {
                            userManagementUI = new UserManagementUI(userController, sessionId);
                            userManagementUI.start();
                        } else {
                            showAccessDenied();
                        }
                        break;
                    case 8:
                        showUserProfile();
                        break;
                    case 0:
                        logout();
                        loggedIn = false;
                        break;
                    default:
                        System.out.println("אפשרות לא תקינה, נסה שנית");
                }
            }
        }
        
        System.out.println("תודה שהשתמשת במערכת ניהול ההובלות. להתראות!");
    }
    
    /**
     * הצגת התפריט הראשי בהתאם להרשאות המשתמש
     */
    private void displayMainMenu() {
        System.out.println("\n=== מערכת ניהול הובלות - תפריט ראשי ===");
        System.out.println("שלום, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        System.out.println("--------------------------------------");
        
        if (hasAccess("TRANSPORT")) {
            System.out.println("1. ניהול הובלות");
        }
        
        if (hasAccess("FLEET")) {
            System.out.println("2. ניהול נהגים ומשאיות");
        }
        
        if (hasAccess("SITE")) {
            System.out.println("3. ניהול אתרים");
        }
        
        if (hasAccess("ORDER")) {
            System.out.println("4. ניהול הזמנות");
        }
        
        if (hasAccess("SCHEDULE")) {
            System.out.println("5. ניהול לוחות זמנים");
        }
        
        if (hasAccess("INCIDENT")) {
            System.out.println("6. ניהול תקלות");
        }
        
        if (hasAccess("USER_MANAGEMENT")) {
            System.out.println("7. ניהול משתמשים");
        }
        
        System.out.println("8. הצגת פרופיל משתמש");
        System.out.println("0. התנתקות");
    }
    
    /**
     * בדיקת הרשאה למודול מסוים
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
     * הצגת פרופיל המשתמש הנוכחי
     */
    private void showUserProfile() {
        System.out.println("\n=== פרופיל משתמש ===");
        System.out.println("מזהה: " + currentUser.getId());
        System.out.println("שם משתמש: " + currentUser.getUsername());
        System.out.println("שם מלא: " + currentUser.getFullName());
        System.out.println("תפקיד: " + currentUser.getRole());
        
        // הצגת הרשאות הגישה
        System.out.println("\n=== הרשאות גישה ===");
        if (currentUser.getRole() == UserRole.SYSTEM_ADMIN) {
            System.out.println("- הרשאה מלאה לכל המודולים");
        } else {
            if (hasAccess("TRANSPORT")) {
                System.out.println("- גישה למודול הובלות");
            }
            if (hasAccess("FLEET")) {
                System.out.println("- גישה למודול נהגים ומשאיות");
            }
            if (hasAccess("SITE")) {
                System.out.println("- גישה למודול אתרים");
            }
            if (hasAccess("ORDER")) {
                System.out.println("- גישה למודול הזמנות");
            }
            if (hasAccess("SCHEDULE")) {
                System.out.println("- גישה למודול לוחות זמנים");
            }
            if (hasAccess("INCIDENT")) {
                System.out.println("- גישה למודול תקלות");
            }
            if (hasAccess("USER_MANAGEMENT")) {
                System.out.println("- גישה לניהול משתמשים");
            }
        }
        
        System.out.println("\nלחץ Enter כדי לחזור לתפריט הראשי");
        scanner.nextLine();
    }
    
    /**
     * הצגת הודעת חוסר הרשאה
     */
    private void showAccessDenied() {
        System.out.println("\nאין לך הרשאה לגשת למודול זה.");
        System.out.println("אנא פנה למנהל המערכת אם הינך זקוק לגישה.");
        System.out.println("\nלחץ Enter כדי להמשיך");
        scanner.nextLine();
    }
    
    /**
     * התנתקות מהמערכת
     */
    private void logout() {
        loginUI.logout(sessionId);
        this.sessionId = null;
        this.currentUser = null;
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
