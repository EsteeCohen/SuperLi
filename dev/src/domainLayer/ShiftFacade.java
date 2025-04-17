package domainLayer;

import java.util.Dictionary;
import java.util.Hashtable;

public class ShiftFacade {
    private Dictionary<String, ShiftDL> shifts;

    private ShiftFacade() {
        this.shifts = new Hashtable<String,ShiftDL>();
    }
}
