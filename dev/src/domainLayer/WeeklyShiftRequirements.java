package domainLayer;

import domainLayer.Enums.ShiftType;
import java.time.DayOfWeek;
import java.util.Dictionary;
import java.util.Hashtable;

public class WeeklyShiftRequirements {
    private Dictionary<String, Dictionary<RolePL, Integer>> weeklyRequirements;

    // Singleton 
    private static WeeklyShiftRequirements instance = null;
    public static WeeklyShiftRequirements getInstance() {
        if (instance == null) {
            instance = new WeeklyShiftRequirements();
        }
        return instance;
    }

    private WeeklyShiftRequirements() {
        this.weeklyRequirements = new Hashtable<String, Dictionary<RolePL, Integer>>();
    }

    public void setRequirements(DayOfWeek day, ShiftType shift, RolePL role, int quantity) {
        String dayString = day.toString() + "_" + shift.toString();
        // Check if the day and shift combination already exists in the dictionary
        if(weeklyRequirements.get(dayString) == null) {
            weeklyRequirements.put(dayString, new Hashtable<RolePL, Integer>());
        }
        // Check if the role already exists for the given day and shift combination
        if(weeklyRequirements.get(dayString).get(role) == null) {
            weeklyRequirements.get(dayString).put(role, 0);
        }
        // Update the quantity for the role
        weeklyRequirements.get(dayString).put(role, quantity);
    }

    public Dictionary<RolePL, Integer> getRequirements(DayOfWeek day, ShiftType shift) {
        String dayString = day.toString() + "_" + shift.toString();
        // Check if the day and shift combination exists in the dictionary
        if(weeklyRequirements.get(dayString) != null) {
            return weeklyRequirements.get(dayString);
        } else {
            return null; // or return an empty dictionary if preferred
        }
    }

}
