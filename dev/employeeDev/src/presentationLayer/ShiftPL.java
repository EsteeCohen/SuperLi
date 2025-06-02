package employeeDev.src.presentationLayer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;
import employeeDev.src.domainLayer.Enums.ShiftType;
import employeeDev.src.serviceLayer.EmployeeSL;
import employeeDev.src.serviceLayer.RoleSL;
import employeeDev.src.serviceLayer.ShiftSL;

public class ShiftPL {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RolePL, List<EmployeePL>> employeesAssignment;
    private final Map<RolePL, Integer> shiftRoleRequirements;
    private final Site site;

    public ShiftPL(ShiftSL shift) {
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.shiftType = shift.getShiftType();
        this.employeesAssignment = new HashMap<>();
        for (Map.Entry<RoleSL, List<EmployeeSL>> entry : shift.getEmployeesAssignment().entrySet()) {
            RolePL role = new RolePL(entry.getKey());
            List<EmployeePL> employees = entry.getValue().stream()
                    .map(EmployeePL::new)
                    .toList();
            employeesAssignment.put(role, employees);
        }
        // Initialize shiftRoleRequirements, assuming shift.getShiftRoleRequirements() returns a Map<RoleSL, Integer>
        this.shiftRoleRequirements = new HashMap<>();
        if (shift.getShiftRoleRequirements() != null) {
            Map<RoleSL, Integer> roleRequirements = (Map<RoleSL, Integer>) shift.getShiftRoleRequirements();
            for (Map.Entry<RoleSL, Integer> entry : roleRequirements.entrySet()) {
                this.shiftRoleRequirements.put(new RolePL(entry.getKey()), entry.getValue());
            }
        }
        this.site = shift.getSite();
    }

    @Override
    public String toString() {
        return "[Start: " + startTime +
                ", End: " + endTime +
                ", " + (shiftType == ShiftType.EVENING ? "evening" : "morning") +
                ", Assigned Employees :" + employeesAssignment +
                ']';
    }

    public String toStringForAvailabilityForm(){
        return "[" + startTime + " - " + endTime + " " + (shiftType == ShiftType.EVENING ? "evening" : "morning") + "]";
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Map<RolePL, List<EmployeePL>> getEmployeesAssignment() {
        return employeesAssignment;
    }

    public ArrayList<ShiftPL> getShiftRoleRequirements() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShiftRoleRequirements'");
    }


}
