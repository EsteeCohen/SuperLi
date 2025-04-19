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
     * חסימת דרך - חסימת כביש או חלק ממסלול הנסיעה
     */
    ROAD_CLOSURE,
    
    /**
     * תנאי מזג אוויר קשים - גשם כבד, שלג, ערפל וכו'
     */
    WEATHER_CONDITION,
    
    /**
     * סירוב לקבל משלוח - אתר יעד מסרב לקבל את המשלוח
     */
    DELIVERY_REFUSAL,
    
    /**
     * חריגת משקל - משקל ההובלה חורג מהמותר
     */
    WEIGHT_OVERLOAD,
    
    /**
     * אחר - סוג תקלה שאינו נכלל בקטגוריות הקיימות
     */
    OTHER
}
