package src.main.enums;

public enum IncidentType {
    /**
     * תקלת משאית - תקלה טכנית במשאית
     */
    TRUCK_BREAKDOWN,
    
    /**
     * נהג לא זמין - הנהג לא זמין מסיבה כלשהי (מחלה, עיכוב וכו')
     */
    DRIVER_UNAVAILABLE,
    
    /**
     * חריגת משקל - משקל ההובלה חורג מהמותר
     */
    WEIGHT_OVERLOAD,
    
    /**
     * אחר - סוג תקלה שאינו נכלל בקטגוריות הקיימות
     */
    OTHER
    // :)
}
