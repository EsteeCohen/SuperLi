package presentationLayer;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import domainLayer.ShiftDL;
import domainLayer.Enums.ShiftType;
import serviceLayer.EmployeeSL;
import serviceLayer.RoleSL;
import serviceLayer.ShiftSL;

public class ShiftPL {

    private final LocalDate date;
    private final ShiftType shiftType;
    private final Map<RolePL, List<EmployeePL>> employeesAssignment;

    public ShiftPL(ShiftSL shift) {
        this.date = shift.getDate();
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
        return "ShiftPL{" +
                "date=" + date +
                ", shiftType=" + shiftType +
                ", employeesAssignment=" + employeesAssignment +
                '}';
    }

    public LocalDate getDate() {
        return date;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public Map<RolePL, List<EmployeePL>> getEmployeesAssignment() {
        return employeesAssignment;
    }


}
