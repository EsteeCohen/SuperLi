package employeeDev.src.domainLayer;

import employeeDev.src.domainLayer.Enums.ShiftType;
import employeeDev.src.serviceLayer.Interfaces.ITransportScheduleService;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transportDev.src.main.entities.Site;

public class ShiftFacade {
    private final Map<String, ShiftDL> shifts;
    private final ITransportScheduleService transportInterface;
    private final EmployeeFacade employeeFacade;

    public ShiftFacade(ITransportScheduleService transportInterface, EmployeeFacade employeeFacade) {
        this.shifts = new HashMap<>();
        this.transportInterface = transportInterface;
        this.employeeFacade = employeeFacade;
    }

    public List<ShiftDL> getAllShiftsFromSite(Site site) {
        if (site == null) {
            throw new IllegalArgumentException("Site cannot be null");
        }
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            if (shift.getSite() != null && shift.getSite().equals(site)) {
                shiftList.add(shift);
            }
        }
        return shiftList;
    }

    public void assignToShift(EmployeeDL employee, LocalDateTime startTime, Site site, RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, site.getName()));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
        }
        shift.assignEmployee(role, employee);
    }

    public void unassignToShift(EmployeeDL employee, LocalDateTime startTime, Site site,  RoleDL role) {
        ShiftDL shift = shifts.get(getShiftKey(startTime, site.getName()));
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
        }
        shift.unassignEmployee(role, employee);
    }

    public ShiftDL getShiftByStartTimeAndType(LocalDateTime startTime, Site site) {
        return shifts.get(getShiftKey(startTime, site.getName()));
    }

    public void addShift(ShiftDL shift) {
        shifts.put(getShiftKey(shift.getStartTime(), shift.getSite().getName()), shift);
    }

    public void setWeeklyRequirements(DayOfWeek day, ShiftType shift, RoleDL role, int quantity) {
        WeeklyShiftRequirements.getInstance().setRequirements(day, shift, role, quantity);
    }

    public void setRequirementForShift(ShiftDL shift, RoleDL role, int quantity) {
        if (shift == null || role == null) {
            throw new IllegalArgumentException("Shift and role cannot be null");
        }
        shift.setIntoRequirements(role, quantity);
    }

    public boolean checkIfThereAreShiftsThatAreNotFullyAssigned(Site site, LocalDate startDate, LocalDate endDate) {
        if (site == null || startDate == null || endDate == null) {
            throw new IllegalArgumentException("Site and dates cannot be null");
        }
        List<ShiftDL> shiftList = getAllShiftsInRange(startDate, endDate, site);
        for (ShiftDL shift : shiftList) {
            if (!shift.meetTheRequirements()) {
                return true; // Found a shift that does not meet the requirements
            }
        }
        return false; // All shifts have at least one assigned employee
    }

    public List<ShiftDL> getAllShiftsInRange(LocalDate startDate, LocalDate endDate, Site site) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        List<ShiftDL> shiftList = new ArrayList<>();
        for (ShiftDL shift : shifts.values()) {
            LocalDate shiftDate = shift.getStartTime().toLocalDate();
            if (!shiftDate.isBefore(startDate) && !shiftDate.isAfter(endDate) && shift.getSite().equals(site)) {
                shiftList.add(shift);
            }
        }
        return shiftList;
    }

    public List<ShiftDL> getWeeklyShifts(LocalDate startDay, Site site) {
        List<ShiftDL> weeklyShifts = new ArrayList<>();
        LocalDate endDay = startDay.plusDays(6);

        for (ShiftDL shift : shifts.values()) {
            LocalDate shiftDate = shift.getStartTime().toLocalDate();
            if (!shiftDate.isBefore(startDay) && !shiftDate.isAfter(endDay) && shift.getSite().equals(site)) {
                weeklyShifts.add(shift);
            }
        }

        return weeklyShifts;
    }

    // Helper method to generate the key for the shifts map
    private String getShiftKey(LocalDateTime startTime, String siteName) {
        return startTime.toString() + siteName;
    }

    // Get the shift at a specific time
    public ShiftDL getShiftAtTime(LocalDateTime time, Site site) {
        for (ShiftDL shift : shifts.values()) {
            if ((shift.getStartTime().isEqual(time) || (time.isAfter(shift.getStartTime()) && time.isBefore(shift.getEndTime()))) && shift.getSite().equals(site)) {
                return shift;
            }
        }
        return null; // No shift found at the specified time
    }

    // makes sure that every shift where a delivery is expected to arrive has at least one warehouseman required
    public void intergrateShiftToDeliveries(Site site, RoleDL warehousemanRole) {
        for (ShiftDL shift : shifts.values()) {
            if (transportInterface.areThereArivelesBetween(shift.getStartTime(), shift.getEndTime(), site) && shift.getRequiredEmployeeCount(warehousemanRole) < 1) {
                shift.addToRequirements(warehousemanRole, 1);
            }
        }
    }

    public List<EmployeeDL> getAllUnassignedEmployeesForShift(LocalDateTime startTime, ShiftType shiftType) {
        List<EmployeeDL> unassigned = new ArrayList<>();
        // Find the shift by startTime and shiftType
        ShiftDL targetShift = null;
        for (ShiftDL shift : shifts.values()) {
            if (shift.getStartTime().isEqual(startTime) && shift.getShiftType() == shiftType) {
                targetShift = shift;
                break;
            }
        }
        if (targetShift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and type");
        }
        for (EmployeeDL employee : employeeFacade.getAllEmployees()) {
            if (!targetShift.isEmployeeAssigned(employee)) {
                unassigned.add(employee);
            }
        }
        return unassigned;
    }

}
