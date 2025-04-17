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
    private final Map<Role, List<Employee>> employeesAssignment;

    public Shift(LocalDate date, ShiftType shiftType) {
        this.date = date;
        this.shiftType = shiftType;
        this.employeesAssignment = new HashMap<Role, List<Employee>>();
    }

    public void assignEmployee(Role role, Employee employee) {
        // needs to be checked if the employee is already assigned to this shift
        // needs to be checked if the employee can preforem this role
        if (!employeesAssignment.containsKey(role)) {
            employeesAssignment.put(role, new ArrayList<Employee>());
        }
        employeesAssignment.get(role).add(employee);
    }

    public void unassignEmployee(Role role, Employee employee) {
        if (employeesAssignment.containsKey(role)) {
            employeesAssignment.get(role).remove(employee);
        }
    }

    public Dictionary<Role, Integer> getRequirements(){
        return WeeklyShiftRequirements.getInstance().getRequirements(date.getDayOfWeek(), shiftType);
    }

    public boolean meetTheRequirements() {
        // gets the requirements for this shift
        Dictionary<Role, Integer> requirements = getRequirements();
        // checks if the requirements are met
        for (Role role : java.util.Collections.list(requirements.keys())) {
            if (employeesAssignment.get(role) == null || employeesAssignment.get(role).size() < requirements.get(role)) {
                return false;
            }
        }
        return true;
    }
}
