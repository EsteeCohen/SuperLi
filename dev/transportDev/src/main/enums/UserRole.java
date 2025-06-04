package src.main.enums;

public enum UserRole {
    // :)
    /**
     * מנהל מערכת - הרשאות מלאות לכל פעולות המערכת
     */
    SYSTEM_ADMIN,
    
    /**
     * מנהל הובלות - ניהול מלא של הובלות, לוחות זמנים ותקלות
     */
    TRANSPORT_MANAGER,
    
 
    
    /**
     * נהג - צפייה בהובלות ולוחות זמנים שלו בלבד
     */
    DRIVER,
    

    
    /**
     * צופה - צפייה בלבד בכל המידע
     */
    VIEWER
}
