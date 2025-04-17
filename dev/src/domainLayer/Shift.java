package domainLayer;

import domainLayer.Enums.ShiftType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Shift {
    private final LocalDate date;
    private final ShiftType shiftType;
    private final Map<RolePL, List<EmployeeSL>> employeesAssignment;

    public Shift(LocalDate date, ShiftType shiftType) {
        this.date = date;
        this.shiftType = shiftType;
        this.employeesAssignment = new HashMap<RolePL, List<EmployeeSL>>();
    }

    public void assignEmployee(RolePL role, EmployeeSL employee) {
        // needs to be checked if the employee is already assigned to this shift
        // needs to be checked if the employee can preforem this role
        if (!employeesAssignment.containsKey(role)) {
            employeesAssignment.put(role, new ArrayList<EmployeeSL>());
        }
        employeesAssignment.get(role).add(employee);
    }

    public void unassignEmployee(RolePL role, EmployeeSL employee) {
        if (employeesAssignment.containsKey(role)) {
            employeesAssignment.get(role).remove(employee);
        }
    }

    public Dictionary<RolePL, Integer> getRequirements(){
        return WeeklyShiftRequirements.getInstance().getRequirements(date.getDayOfWeek(), shiftType);
    }

    public boolean meetTheRequirements() {
        // gets the requirements for this shift
        Dictionary<RolePL, Integer> requirements = getRequirements();
        // checks if the requirements are met
        for (RolePL role : java.util.Collections.list(requirements.keys())) {
            if (employeesAssignment.get(role) == null || employeesAssignment.get(role).size() < requirements.get(role)) {
                return false;
            }
        }
        return true;
    }
}
