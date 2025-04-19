package src.main.controllers;

import java.util.List;
import src.main.entities.User;
import src.main.enums.UserRole;
import src.main.services.UserService;


// בקר משתמשים
public class UserController {
    private UserService userService;
    
    // constructor
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // יצירת משתמש חדש
    public boolean addUser(String id, String username, String password, String fullName, String role) {
        try {
            UserRole userRole = UserRole.valueOf(role);
            return userService.addUser(id, username, password, fullName, userRole);
        } catch (IllegalArgumentException e) {
            return false; // תפקיד לא תקין
        }
    }
    
    // עדכון פרטי משתמש
    public boolean updateUser(String id, String username, String password, String fullName, String role) {
        try {
            UserRole userRole = UserRole.valueOf(role);
            return userService.updateUser(id, username, password, fullName, userRole);
        } catch (IllegalArgumentException e) {
            return false; // תפקיד לא תקין
        }
    }
    
    // השבתת משתמש
    public boolean deactivateUser(String id) {
        return userService.deactivateUser(id);
    }
    
    // התחברות למערכת
    public String login(String username, String password) {
        User user = userService.authenticateUser(username, password);
        
        if (user == null) {
            return null;
        }
        
        return userService.createSession(user.getId());
    }
    
    // התנתקות מהמערכת
    public boolean logout(String sessionId) {
        return userService.logout(sessionId);
    }
    
    //בדיקת הרשאות לפעולה מסוימת
    public boolean isAuthorized(String sessionId, String operation, String resourceType) {
        return userService.hasPermission(sessionId, operation, resourceType);
    }
    
    // מחזיר את המשתמש הנוכחי
    public User getCurrentUser(String sessionId) {
        return userService.getUserBySessionId(sessionId);
    }
    
    // מחזיר את כל המשתמשים
    public List<User> getAllUsers(String sessionId) {
        if (!isAuthorized(sessionId, "VIEW", "USER")) {
            return null;
        }
        
        return userService.getAllUsers();
    }
    
    // מחזיר משתמשים לפי מזהה
    public User getUserById(String sessionId, String userId) {
        if (!isAuthorized(sessionId, "VIEW", "USER")) {
            return null;
        }
        
        return userService.getUserById(userId);
    }
    
    // מחזיר משתמשים לפי תפקיד
    public List<User> getUsersByRole(String sessionId, String role) {
        if (!isAuthorized(sessionId, "VIEW", "USER")) {
            return null;
        }
        
        try {
            UserRole userRole = UserRole.valueOf(role);
            return userService.getUsersByRole(userRole);
        } catch (IllegalArgumentException e) {
            return null; // תפקיד לא תקין
        }
    }
}
