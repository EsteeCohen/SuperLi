package src.main.enums;

public enum OrderStatus {
    /**
     * נוצר - הזמנה חדשה שנוצרה במערכת
     */
    CREATED,
    
    /**
     * בתהליך - הזמנה בתהליך טיפול (שויכה להובלה או בתהליך הכנה)
     */
    IN_PROGRESS,
    
    /**
     * הושלם - הזמנה שהושלמה בהצלחה
     */
    DONE,
    
    /**
     * בוטל - הזמנה שבוטלה
     */
    CANCELLED
}
