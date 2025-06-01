package domainLayer;

import domainLayer.Enums.ShiftType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import serviceLayer.Interfaces.ITransportScheduleService;

public class ShiftFacade {
    private final Map<String, ShiftDL> shifts;
    private final ITransportScheduleService transportInterface;

    public ShiftFacade(ITransportScheduleService transportInterface) {
        this.shifts = new HashMap<>();
        this.transportInterface = transportInterface;
    }

    public List<ShiftDL> getAllShifts() {
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            shiftList.add(shift);
        }
        return shiftList;
    }

    public void assignToShift(EmployeeDL employee, LocalDateTime startTime, String shiftType, RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, shiftType));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and type");
        }
        shift.assignEmployee(role, employee);
    }

    public void unassignToShift(EmployeeDL employee, LocalDateTime startTime, String shiftType, RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, shiftType));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and type");
        }
        shift.unassignEmployee(role, employee);
    }

    public ShiftDL getShiftByStartTimeAndType(LocalDateTime startTime, String shiftType) {
        return shifts.get(getShiftKey(startTime, shiftType));
    }

    public void addShift(ShiftDL shift) {
        shifts.put(getShiftKey(shift.getStartTime(), shift.getShiftType().toString()), shift);
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
            LocalDate shiftDate = shift.getStartTime().toLocalDate();
            if (!shiftDate.isBefore(startDay) && !shiftDate.isAfter(endDay)) {
                weeklyShifts.add(shift);
            }
        }

        return weeklyShifts;
    }

    // Helper method to generate the key for the shifts map
    private String getShiftKey(LocalDateTime startTime, String shiftType) {
        return startTime.toString() + shiftType;
    }

    // Get the shift at a specific time
    public ShiftDL getShiftAtTime(LocalDateTime time) {
        for (ShiftDL shift : shifts.values()) {
            if (shift.getStartTime().isEqual(time) || (time.isAfter(shift.getStartTime()) && time.isBefore(shift.getEndTime()))) {
                return shift;
            }
        }
        return null; // No shift found at the specified time
    }

    // makes sure that every shift where a delivery is expected to arrive has at least one warehouseman required
    public void intergrateShiftToDeliveries(RoleDL warehousemanRole) {
        for (ShiftDL shift : shifts.values()) {
            if (transportInterface.areThereArivelesBetween(shift.getStartTime(), shift.getEndTime()) && shift.getRequiredEmployeeCount(warehousemanRole) < 1) {
                shift.addToRequirements(warehousemanRole, 1);
            }
        }
    }

}
