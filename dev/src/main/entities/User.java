package src.main.entities;

import src.main.enums.UserRole;

public class User {
    private final String id;
    private String username;
    private String password;
    private String fullName;
    private UserRole role;
    private boolean active;
    
    //בנאי
    public User(String id, String username, String password, String fullName, UserRole role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.active = true;
    }
    
    // Getters and Setters
    
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    // בדיקת סיסמה
    public boolean checkPassword(String password) {
        return this.password != null && this.password.equals(password);
    }
    
    //בדיקה אם למשתמש יש הרשאה מסוימת
    public boolean hasPermission(String operation, String resourceType) {
        switch (this.role) {
            case SYSTEM_ADMIN:
                // מנהל מערכת יכול לעשות הכל
                return true;
            
            case TRANSPORT_MANAGER:
                // מנהל הובלות יכול לנהל הובלות, לוחות זמנים, תקלות, ולצפות בכל המידע
                if (operation.startsWith("VIEW") || 
                    resourceType.equals("TRANSPORT") || 
                    resourceType.equals("SCHEDULE") ||
                    resourceType.equals("INCIDENT")) {
                    return true;
                }
                // מנהל הובלות יכול גם לצפות בנהגים, משאיות ואתרים
                if (operation.equals("VIEW") && 
                    (resourceType.equals("DRIVER") || 
                     resourceType.equals("TRUCK") ||
                     resourceType.equals("SITE"))) {
                    return true;
                }
                return false;
            
            
            case DRIVER:
                // נהג יכול לצפות רק בהובלות ולוחות זמנים שלו
                return operation.equals("VIEW") && 
                       (resourceType.equals("TRANSPORT") || resourceType.equals("SCHEDULE"));
            
            
            
            case VIEWER:
                // צופה יכול רק לצפות במידע
                return operation.equals("VIEW");
            
            default:
                return false;
        }
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
