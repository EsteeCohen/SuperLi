package domainLayer;

import domainLayer.Enums.ShiftType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftDL {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RoleDL, List<EmployeeDL>> employeesAssignment;

    // Main constructor
    public ShiftDL(LocalDateTime startTime, LocalDateTime endTime, ShiftType shiftType) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = shiftType;
        this.employeesAssignment = new HashMap<>();
    }

    // Simplified constructor (e.g., for testing or specific use cases)
    public ShiftDL(LocalDateTime startTime, LocalDateTime endTime, String shiftTypeString) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = ShiftType.valueOf(shiftTypeString.toUpperCase());
        this.employeesAssignment = new HashMap<>();
    }

    // Assign an employee to a role in the shift
    public void assignEmployee(RoleDL role, EmployeeDL employee) {
        employeesAssignment.computeIfAbsent(role, k -> new ArrayList<>()).add(employee);
    }

    // Unassign an employee from a role in the shift
    public void unassignEmployee(RoleDL role, EmployeeDL employee) {
        if (employeesAssignment.containsKey(role)) {
            employeesAssignment.get(role).remove(employee);
            if (employeesAssignment.get(role).isEmpty()) {
                employeesAssignment.remove(role); // Clean up empty roles
            }
        }
    }

    // Get the requirements for this shift
    public Dictionary<RoleDL, Integer> getRequirements() {
        // Use startTime.toLocalDate() to get the date for requirements
        return WeeklyShiftRequirements.getInstance().getRequirements(startTime.toLocalDate().getDayOfWeek(), shiftType);
    }

    // Check if the shift meets the requirements
    public boolean meetTheRequirements() {
        Dictionary<RoleDL, Integer> requirements = getRequirements();
        for (RoleDL role : java.util.Collections.list(requirements.keys())) {
            if (employeesAssignment.get(role) == null || employeesAssignment.get(role).size() < requirements.get(role)) {
                return false;
            }
        }
        return true;
    }

    // Getters
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Map<RoleDL, List<EmployeeDL>> getEmployeesAssignment() {
        return employeesAssignment;
    }

    // Get all employees assigned to the shift
    public List<EmployeeDL> getAllAssignedEmployees() {
        List<EmployeeDL> allEmployees = new ArrayList<>();
        for (List<EmployeeDL> employees : employeesAssignment.values()) {
            allEmployees.addAll(employees);
        }
        return allEmployees;
    }

    // Check if an employee is assigned to the shift
    public boolean isEmployeeAssigned(EmployeeDL employee) {
        for (List<EmployeeDL> employees : employeesAssignment.values()) {
            if (employees.contains(employee)) {
                return true;
            }
        }
        return false;
    }

    // Get the number of employees assigned to a specific role
    public int getAssignedEmployeeCount(RoleDL role) {
        return employeesAssignment.getOrDefault(role, new ArrayList<>()).size();
    }
}
