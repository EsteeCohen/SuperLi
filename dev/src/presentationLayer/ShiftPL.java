package presentationLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShiftPL {

    private final LocalDate date;
    private final ShiftType shiftType;
    private final Map<RoleSL, List<EmployeeSL>> employeesAssignment;

    public ShiftPL(ShiftDL shift) {
        this.date = shift.getDate();
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
}
