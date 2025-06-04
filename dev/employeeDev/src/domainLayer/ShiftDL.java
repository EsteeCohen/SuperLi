package employeeDev.src.domainLayer;

import employeeDev.src.domainLayer.Enums.ShiftType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;

public class ShiftDL {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RoleDL, List<EmployeeDL>> employeesAssignment;
    private final Map<RoleDL, Integer> requirements;
    private final Site site;

    // Main constructor
    public ShiftDL(LocalDateTime startTime, LocalDateTime endTime, ShiftType shiftType, Site site) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = shiftType;
        this.employeesAssignment = new HashMap<>();
        this.requirements = WeeklyShiftRequirements.getInstance().getRequirements(startTime.toLocalDate().getDayOfWeek(), shiftType);
        this.site = site;
    }

    // constractor for loading from database (requirments are not loaded from WeeklyShiftRequirements)
    public ShiftDL(LocalDateTime startTime, LocalDateTime endTime, String shiftTypeString, Site site, 
                   Map<RoleDL, Integer> requirements, Map<RoleDL, List<EmployeeDL>> employeesAssignment) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.shiftType = ShiftType.valueOf(shiftTypeString.toUpperCase());
        this.employeesAssignment = employeesAssignment != null ? employeesAssignment : new HashMap<>();
        this.requirements = requirements != null ? requirements : new HashMap<>();
        this.site = site;
    }

    // Get the site for this shift
    public Site getSite() {
        return site;
    }

    // Assign an employee to a role in the shift
    public void assignEmployee(RoleDL role, EmployeeDL employee) {
        employeesAssignment.computeIfAbsent(role, k -> new ArrayList<>()).add(employee);
        decrementRequirement(role);
    }

    private void decrementRequirement(RoleDL role) {
        if (requirements.containsKey(role)) {
            int currentQuantity = requirements.get(role);
            if (currentQuantity > 0) {
                requirements.put(role, currentQuantity - 1);
            }
            if (requirements.get(role) <= 0) {
                requirements.remove(role);
            }
        }
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
    public Map<RoleDL, Integer> getRequirements() {
        // Use startTime.toLocalDate() to get the date for requirements
        return requirements;
    }

    // Check if the shift meets the requirements
    public boolean meetTheRequirements() {
        Map<RoleDL, Integer> requirements = getRequirements();
        for (RoleDL role : requirements.keySet()) {
            if (employeesAssignment.get(role) == null || employeesAssignment.get(role).size() < requirements.get(role)) {
                return false;
            }
        }
        return true;
    }

    // remove requirements for this shift
    public void removeFromRequirements(RoleDL role, int quantity) {
        if (requirements != null && requirements.get(role) != null) {
            int currentQuantity = requirements.get(role);
            if (currentQuantity >= quantity) {
                requirements.put(role, currentQuantity - quantity);
            } else {
                requirements.remove(role); // Remove role if quantity goes to zero or below
            }
        } else {
            throw new IllegalArgumentException("Role not found in requirements.");
        }
    }
     
    // Add to the requirements for this shift
    public void addToRequirements(RoleDL role, int quantity) {
        if (requirements != null) {
            int currentQuantity = requirements.get(role) != null ? requirements.get(role) : 0;
            if (currentQuantity == 0) {
                requirements.put(role, currentQuantity); // Initialize if not present
            }
            requirements.put(role, currentQuantity + quantity);
        } else {
            throw new IllegalArgumentException("Requirements not initialized for this shift.");
        }
    }

    public void setIntoRequirements(RoleDL role, int quantity) {
        if (requirements != null) {
            requirements.put(role, quantity);
        } else {
            throw new IllegalArgumentException("Requirements not initialized for this shift.");
        }
    }

    // Get the required number of employees for a specific role
    public int getRequiredEmployeeCount(RoleDL role) {
        if (requirements != null && requirements.get(role) != null) {
            return requirements.get(role);
        } else {
            return 0; // Role not found in requirements
        }
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
