package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.Enums.ShiftType;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.ShiftDL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftSL {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RoleSL, List<EmployeeSL>> employeesAssignment;
    private final Map<RoleSL, Integer> shiftRoleRequirements;

    public ShiftSL(ShiftDL shift) {
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.shiftType = shift.getShiftType();
        this.employeesAssignment = new HashMap<>();
        this.shiftRoleRequirements = new HashMap<>();
        for (Map.Entry<RoleDL, Integer> entry : shift.getRequirements().entrySet()) {
            RoleSL role = new RoleSL(entry.getKey());
            shiftRoleRequirements.put(role, entry.getValue());
        }
        for (Map.Entry<RoleDL, List<EmployeeDL>> entry : shift.getEmployeesAssignment().entrySet()) {
            RoleSL role = new RoleSL(entry.getKey());
            List<EmployeeSL> employees = entry.getValue().stream()
                    .map(EmployeeSL::new)
                    .toList();
            employeesAssignment.put(role, employees);
        }
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

    public Map<RoleSL, List<EmployeeSL>> getEmployeesAssignment() {
        return employeesAssignment;
    }

    public Map<RoleSL, Integer> getShiftRoleRequirements() {
        return shiftRoleRequirements;
    }
}
