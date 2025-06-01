package domainLayer;

import domainLayer.Enums.ShiftType;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

// this class manages the generic weekly shift requirements for different roles in shifts

public class WeeklyShiftRequirements {
    private final Map<String, Map<RoleDL, Integer>> weeklyRequirements;
    private static WeeklyShiftRequirements instance = null;
    public static WeeklyShiftRequirements getInstance() {
        if (instance == null) {
            instance = new WeeklyShiftRequirements();
        }
        return instance;
    }

    private WeeklyShiftRequirements() {
        this.weeklyRequirements = new HashMap<String, Map<RoleDL, Integer>>();
        weeklyRequirements.put(DayOfWeek.SUNDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.SUNDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.MONDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.MONDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.TUESDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.TUESDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.WEDNESDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.WEDNESDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.THURSDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.THURSDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.FRIDAY.toString() + "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.FRIDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.SATURDAY.toString() +  "_" + ShiftType.MORNING, new HashMap<RoleDL,Integer>());
        weeklyRequirements.put(DayOfWeek.SATURDAY.toString() + "_" + ShiftType.EVENING, new HashMap<RoleDL,Integer>());
    }
    public void setRequirements(DayOfWeek day, ShiftType shift, RoleDL role, int quantity) {
        String dayString = day.toString() + "_" + shift.toString();
        // Check if the day and shift combination already exists in the map
        if(weeklyRequirements.get(dayString) == null) {
            weeklyRequirements.put(dayString, new HashMap<RoleDL, Integer>());
        }
        // Check if the role already exists for the given day and shift combination
        if(weeklyRequirements.get(dayString).get(role) == null) {
            weeklyRequirements.get(dayString).put(role, 0);
        }
        // Update the quantity for the role
        weeklyRequirements.get(dayString).put(role, quantity);
    }

    public Map<RoleDL, Integer> getRequirements(DayOfWeek day, ShiftType shift) {
        String dayString = day.toString() + "_" + shift.toString();
        // Check if the day and shift combination exists in the dictionary
        if(weeklyRequirements.get(dayString) != null) {
            return weeklyRequirements.get(dayString);
        } else {
            return null; // or return an empty dictionary if preferred
        }
    }

    public void setRequirementsToAll(RoleDL role, int quantity){
        for (String key : weeklyRequirements.keySet()){
            weeklyRequirements.get(key).put(role, quantity);
        }
    }

}
