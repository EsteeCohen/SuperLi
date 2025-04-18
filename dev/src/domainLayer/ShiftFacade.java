package domainLayer;

import domainLayer.Enums.ShiftType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class ShiftFacade {
    private Map<String, ShiftDL> shifts;

    private EmployeeFacade employeeFacade;
    private AvailabilityFacade availabilityFacade;

    private ShiftFacade() {
        this.shifts = new HashMap<String, ShiftDL>();
    }

    public List<ShiftDL> getAllShifts() {
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            shiftList.add(shift);
        }
        return shiftList;
    }

    public void assignToShift(EmployeeDL employee, LocalDate date, String shiftType, RoleDL role) {
        ShiftDL shift = shifts.get(date.toString() + shiftType);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given date and type");
        }
        shift.assignEmployee(role, employee);
    }

    public void unassignToShift(EmployeeDL employee, LocalDate date, String shiftType, RoleDL role) {
        ShiftDL shift = shifts.get(date.toString() + shiftType);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given date and type");
        }
        shift.unassignEmployee(role, employee);
    }
}
