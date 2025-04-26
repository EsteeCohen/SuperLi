package domainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

import domainLayer.Enums.ShiftType;

import java.util.HashMap;
import java.util.List;

public class ShiftFacade {
    private Map<String, ShiftDL> shifts;

    public ShiftFacade() {
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

    public ShiftDL getShiftByDateAndType(LocalDate date, String shiftType) {
        return shifts.get(date.toString() + shiftType);
    }

    public void addShift(ShiftDL shift) {
        shifts.put(shift.getDate().toString() + shift.getShiftType().toString(), shift);
    }

    public void setRequirements(DayOfWeek day, ShiftType shift, RoleDL role, int quantity) {
        WeeklyShiftRequirements.getInstance().setRequirements(day, shift, role, quantity);
    }

    public boolean checkIfThereAreShiftsThatAreNotFullyAssigned() {
        for (ShiftDL shift : shifts.values()) {
            if (!shift.meetTheRequirements()) {
                return true; // Found a shift that does not meet the requirements
            }
        }
        return false; // All shifts have at least one assigned employee
    }

    public List<ShiftDL> getWeeklyShifts(LocalDate startDay) {
        List<ShiftDL> weeklyShifts = new ArrayList<>();
        LocalDate endDay = startDay.plusDays(6); 

        for (ShiftDL shift : shifts.values()) {
            if (!shift.getDate().isBefore(startDay) && !shift.getDate().isAfter(endDay)) {
                weeklyShifts.add(shift);
            }
        }

        return weeklyShifts;
    }
}
