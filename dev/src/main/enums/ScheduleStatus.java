package src.main.enums;

public enum ScheduleStatus {
    /**
     * טיוטה - עדיין בתכנון
     */
    DRAFT,
    
    /**
     * מאושר - אושר על ידי מנהל
     */
    CONFIRMED,
    
    /**
     * בביצוע - נמצא בתהליך ביצוע
     */
    IN_PROGRESS,
    
    /**
     * הושלם - כל הפעילויות הושלמו
     */
    COMPLETED,
    
    /**
     * בוטל - לוח הזמנים בוטל
     */
    CANCELLED
}
