package src.main.enums;

public enum IncidentStatus {
    /**
     * דווח - התקלה דווחה למערכת אך עדיין לא טופלה
     */
    REPORTED,
    
    /**
     * בטיפול - התקלה נמצאת בתהליך טיפול
     */
    IN_PROGRESS,
    
    /**
     * נפתר - התקלה נפתרה בהצלחה
     */
    RESOLVED,
    
    /**
     * בוטל - הדיווח על התקלה בוטל (למשל, דיווח שגוי)
     */
    CANCELLED
}
