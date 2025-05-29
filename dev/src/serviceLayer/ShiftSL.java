package serviceLayer;

import domainLayer.EmployeeDL;
import domainLayer.Enums.ShiftType;
import domainLayer.RoleDL;
import domainLayer.ShiftDL;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftSL {
    private final LocalDateTime startTime;
    private final LocalDateTime endTime;
    private final ShiftType shiftType;
    private final Map<RoleSL, List<EmployeeSL>> employeesAssignment;

    public ShiftSL(ShiftDL shift) {
        this.startTime = shift.getStartTime();
        this.endTime = shift.getEndTime();
        this.shiftType = shift.getShiftType();
        this.employeesAssignment = new HashMap<>();
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
}
