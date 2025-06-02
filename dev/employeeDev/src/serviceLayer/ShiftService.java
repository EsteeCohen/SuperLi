package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.AvailabilityDL;
import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.Enums.ShiftType;
import transportDev.src.main.entities.Site;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.ShiftFacade;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ShiftService {
    private ShiftFacade shiftFacade;
    private AvailabilityFacade availabilityFacade;
    private EmployeeFacade employeeFacade;

    public ShiftService(ShiftFacade shiftFacade, AvailabilityFacade availabilityFacade, EmployeeFacade employeeFacade) {
        this.shiftFacade = shiftFacade;
        this.availabilityFacade = availabilityFacade;
        this.employeeFacade = employeeFacade;
    }

    // checks if there are any shifts where not enough employees are available
    public boolean checkForProblematicShifts() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'checkShiftsWithMissingWorkers'");
    }

    public boolean CheckIfThereAreShiftsThatAreNotAssigned() {
        return shiftFacade.checkIfThereAreShiftsThatAreNotFullyAssigned();
    }

    public List<ShiftSL> getAllShift() {
        List<ShiftDL> allDlShifts  = shiftFacade.getAllShifts();
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    public List<ShiftSL> getWeeklyShifts(LocalDate startDate) {
        List<ShiftDL> allDlShifts = shiftFacade.getWeeklyShifts(startDate);
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    // Updated: now uses startTime and shiftType
    public void setAvailabilityOfEmployeeToShift(String employeeId, LocalDateTime startTime, String shiftType, boolean available) {
        ShiftDL shift = shiftFacade.getShiftByStartTimeAndType(startTime, shiftType);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and type");
        }
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        AvailabilityDL availability = availabilityFacade.getAvailabilityForEmployee(employee, shift);
        if (availability == null) {
            availability = new AvailabilityDL(shift, employee);
            availability.setAvailability(available);
            availabilityFacade.addAvailability(availability);
        } else {
            availability.setAvailability(available);
        }
    }

    // Updated: now uses startTime and shiftType
    public List<EmployeeSL> getAvailableEmployeesForShift(LocalDateTime startTime, String shiftType) {
        ShiftDL shift = shiftFacade.getShiftByStartTimeAndType(startTime, shiftType);
        List<EmployeeDL> availableEmployeesDL = availabilityFacade.getAvailabilitiesForShift(shift);
        List<EmployeeSL> availableEmployeesSL = new ArrayList<>();
        for (EmployeeDL employeeDL : availableEmployeesDL) {
            availableEmployeesSL.add(new EmployeeSL(employeeDL));
        }
        return availableEmployeesSL;
    }
    
    public void createShiftsForWeek(LocalDate startDate, Site site) {
        List<EmployeeDL> employees = employeeFacade.getAllEmployees();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            createShift(date, ShiftType.MORNING, employees, site);
            createShift(date, ShiftType.EVENING, employees, site);
        }
    }

    private void createAvailabilityForShift(ShiftDL shift, EmployeeDL employee) {
        AvailabilityDL availability = new AvailabilityDL(shift, employee);
        availabilityFacade.addAvailability(availability);
    }

    // Updated: createShift now creates startTime and endTime based on shiftType
    private void createShift(LocalDate date, ShiftType shiftType, List<EmployeeDL> employees, Site site) {
        LocalTime start, end;
        if (shiftType == ShiftType.MORNING) {
            start = LocalTime.of(8, 0);
            end = LocalTime.of(16, 0);
        } else { // EVENING
            start = LocalTime.of(16, 0);
            end = LocalTime.of(23, 59);
        }
        LocalDateTime startTime = LocalDateTime.of(date, start);
        LocalDateTime endTime = LocalDateTime.of(date, end);

        ShiftDL shift = new ShiftDL(startTime, endTime, shiftType,site);
        shiftFacade.addShift(shift);
        for (EmployeeDL employee : employees) {
            createAvailabilityForShift(shift, employee);
        }
    }

    public void createShiftsForNextWeek(Site site) {
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = getNextSunday(today);
        createShiftsForWeek(nextSunday, site);
    }

    private static LocalDate getNextSunday(LocalDate fromDate) {
        // Calculate how many days to add to get to the next Sunday
        int daysUntilSunday = DayOfWeek.SUNDAY.getValue() - fromDate.getDayOfWeek().getValue();
        if (daysUntilSunday <= 0) {
            daysUntilSunday += 7;
        }
        return fromDate.plusDays(daysUntilSunday);
    }

    public List<EmployeeSL> getAllUnassignedEmployeeForShift(ShiftSL shift) {
        List<EmployeeDL> unassignedEmployees = shiftFacade.getAllUnassignedEmployeesForShift(shift.getStartTime(), shift.getShiftType());
        List<EmployeeSL> unassignedEmployeeSL = new ArrayList<>();
        for (EmployeeDL employee : unassignedEmployees) {
            unassignedEmployeeSL.add(new EmployeeSL(employee));
        }
        return unassignedEmployeeSL;
    }
}
