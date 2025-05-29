package presentationLayer;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domainLayer.Enums.ShiftType;
import serviceLayer.EmployeeSL;
import serviceLayer.RoleSL;
import serviceLayer.ShiftSL;

public class ShiftPL {

    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RolePL, List<EmployeePL>> employeesAssignment;

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


}
