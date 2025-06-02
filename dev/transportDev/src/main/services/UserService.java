package transportDev.src.main.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import transportDev.src.main.entities.User;
import transportDev.src.main.enums.UserRole;

public class UserService {
    private List<User> users;
    private Map<String, String> activeSessions; // sessionId -> userId
    
    //בנאי לשירות ניהול משתמשים
    public UserService() {
        this.users = new ArrayList<>();
        this.activeSessions = new HashMap<>();
        initializeSystem(); // אתחול נתוני משתמשים לדוגמה
    }
    
    //הוספת משתמש חדש
    public boolean addUser(String id, String username, String password, String fullName, UserRole role) {
        // בדיקה שאין משתמש עם אותו שם משתמש
        if (getUserByUsername(username) != null) {
            return false;
        }
        
        // בדיקה שאין משתמש עם אותו מזהה
        if (getUserById(id) != null) {
            return false;
        }
        
        // יצירת משתמש חדש והוספתו לרשימה
        User newUser = new User(id, username, password, fullName, role);
        users.add(newUser);
        
        return true;
    }
    
    //עדכון פרטי משתמש
    public boolean updateUser(String id, String username, String password, String fullName, UserRole role) {
        User user = getUserById(id);
        
        if (user == null) {
            return false;
        }
        
        // בדיקה שאין משתמש אחר עם אותו שם משתמש
        User existingUser = getUserByUsername(username);
        if (existingUser != null && !existingUser.getId().equals(id)) {
            return false;
        }
        
        // עדכון פרטי המשתמש
        user.setUsername(username);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setFullName(fullName);
        user.setRole(role);
        
        return true;
    }
    
    //עדכון סיסמת משתמש
    public boolean updateUserPassword(String userId, String newPassword) {
        User user = getUserById(userId);
        if (user == null || newPassword == null || newPassword.trim().isEmpty()) {
            return false;
        }
        
        user.setPassword(newPassword);
        return true;
    }
    
    //השבתת משתמש
    public boolean deactivateUser(String id) {
        User user = getUserById(id);
        
        if (user == null) {
            return false;
        }
        
        user.setActive(false);
        
        // ניתוק המשתמש אם הוא מחובר
        for (Map.Entry<String, String> entry : new HashMap<>(activeSessions).entrySet()) {
            if (entry.getValue().equals(id)) {
                activeSessions.remove(entry.getKey());
            }
        }
        
        return true;
    }
    
    //אימות משתמש (התחברות)
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        
        if (user == null || !user.isActive()) {
            return null;
        }
        
        if (user.getPassword().equals(password)) {
            return user;
        }
        
        return null;
    }
    
    //יצירת מזהה הפעלה למשתמש שהתחבר
    public String createSession(String userId) {
        // יצירת מזהה הפעלה ייחודי
        String sessionId = "SESSION_" + System.currentTimeMillis() + "_" + userId;
        activeSessions.put(sessionId, userId);
        return sessionId;
    }
    
    //ניתוק משתמש (התנתקות)
    public boolean logout(String sessionId) {
        return activeSessions.remove(sessionId) != null;
    }
    
    //קבלת משתמש לפי מזהה הפעלה
    public User getUserBySessionId(String sessionId) {
        String userId = activeSessions.get(sessionId);
        if (userId == null) {
            return null;
        }
        
        return getUserById(userId);
    }
    
    //בדיקה אם למשתמש יש הרשאה לפעולה מסוימת
    public boolean hasPermission(String sessionId, String operation, String resourceType) {
        User user = getUserBySessionId(sessionId);
        
        if (user == null) {
            return false;
        }
        
        return user.hasPermission(operation, resourceType);
    }
    
    //קבלת משתמש לפי מזהה
    public User getUserById(String id) {
        return users.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    //קבלת משתמש לפי שם משתמש
    public User getUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }
    
    //קבלת כל המשתמשים
    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }
    
    //קבלת משתמשים לפי תפקיד
    public List<User> getUsersByRole(UserRole role) {
        return users.stream()
                .filter(u -> u.getRole() == role)
                .collect(Collectors.toList());
    }
    
    //קבלת משתמשים פעילים בלבד
    public List<User> getActiveUsers() {
        return users.stream()
                .filter(User::isActive)
                .collect(Collectors.toList());
    }
    
    //אתחול מערכת המשתמשים
    public void initializeSystem() {
        // הוספת משתמשים לדוגמה למערכת
        addUser("admin123", "admin", "admin123", "מנהל מערכת", UserRole.SYSTEM_ADMIN);
        addUser("manager456", "manager", "manager123", "מנהל הובלות", UserRole.TRANSPORT_MANAGER);
        addUser("driver789", "driver1", "driver123", "נהג מערכת", UserRole.DRIVER);
        addUser("viewer321", "viewer", "viewer123", "צופה מערכת", UserRole.VIEWER);
    }
}
