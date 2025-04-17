package domainLayer;

import domainLayer.Enums.ShiftType;
import java.time.LocalDate;
import java.util.Dictionary;
import java.util.Hashtable;

public class ShiftFacade {
    private Dictionary<String, ShiftDL> shifts;

    // singleton
    private static ShiftFacade instance = null;
    public static ShiftFacade getInstance() {
        if (instance == null) {
            instance = new ShiftFacade();
        }
        return instance;
    }

    private ShiftFacade() {
        this.shifts = new Hashtable<String,ShiftDL>();
    }
}
