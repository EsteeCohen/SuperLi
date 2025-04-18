package domainLayer;

import domainLayer.Enums.ShiftType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftDL {
    private final LocalDate date;
    private final ShiftType shiftType;
    private final Map<RoleDL, List<EmployeeDL>> employeesAssignment;

    public ShiftDL(LocalDate date, ShiftType shiftType) {
        this.date = date;
        this.shiftType = shiftType;
        this.employeesAssignment = new HashMap<RoleDL, List<EmployeeDL>>();
    }

    public void assignEmployee(RoleDL role, EmployeeDL employee) {
        if (!employeesAssignment.containsKey(role)) {
            employeesAssignment.put(role, new ArrayList<EmployeeDL>());
        }
        employeesAssignment.get(role).add(employee);
    }

    public void unassignEmployee(RoleDL role, EmployeeDL employee) {
        if (employeesAssignment.containsKey(role)) {
            employeesAssignment.get(role).remove(employee);
        }
    }

    public Dictionary<RoleDL, Integer> getRequirements(){
        return WeeklyShiftRequirements.getInstance().getRequirements(date.getDayOfWeek(), shiftType);
    }

    public boolean meetTheRequirements() {
        // gets the requirements for this shift
        Dictionary<RoleDL, Integer> requirements = getRequirements();
        // checks if the requirements are met
        for (RoleDL role : java.util.Collections.list(requirements.keys())) {
            if (employeesAssignment.get(role) == null || employeesAssignment.get(role).size() < requirements.get(role)) {
                return false;
            }
        }
        return true;
    }

    public LocalDate getDate() {
        return date;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Map<RoleDL, List<EmployeeDL>> getEmployeesAssignment() {
        return employeesAssignment;
    }

}
