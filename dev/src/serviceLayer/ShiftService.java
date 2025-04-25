package serviceLayer;

import domainLayer.AvailabilityDL;
import domainLayer.AvailabilityFacade;
import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import domainLayer.ShiftDL;
import domainLayer.ShiftFacade;
import domainLayer.Enums.ShiftType;

import java.time.DayOfWeek;
import java.time.LocalDate;
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

    public List<ShiftSL> getAllShift() {
        List<ShiftDL> allDlShifts  = shiftFacade.getAllShifts();
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    public void setAvailabilityOfEmployeeToShift(String employeeId, LocalDate date, String ShiftType, boolean available) {
        ShiftDL shift = shiftFacade.getShiftByDateAndType(date, ShiftType);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given date and type");
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

    public List<EmployeeDL> getAvailableEmployeesForShift(LocalDate date, ShiftType shiftType) {
        ShiftDL shift = shiftFacade.getShiftByDateAndType(date, shiftType.toString());
        return availabilityFacade.getAvailabilitiesForShift(shift);
    }
    
    public void createShiftsForWeek(LocalDate startDate) {
        List<EmployeeDL> employees = employeeFacade.getAllEmployees();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            createShift(date, ShiftType.MORNING, employees);
            createShift(date, ShiftType.EVENING, employees);
        }
    }

    private void createAvailabilityForShift(ShiftDL shift, EmployeeDL employee) {
        AvailabilityDL availability = new AvailabilityDL(shift, employee);
        availabilityFacade.addAvailability(availability);
    }

    private void createShift(LocalDate date, ShiftType shiftType, List<EmployeeDL> employees) {
        ShiftDL shift = new ShiftDL(date, shiftType);
        shiftFacade.addShift(shift);
        for (EmployeeDL employee : employees) {
            createAvailabilityForShift(shift, employee);
        }
    }

    public void createShiftsForNextWeek() {
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = getNextSunday(today);
        createShiftsForWeek(nextSunday);
    }

    private static LocalDate getNextSunday(LocalDate fromDate) {
        // Calculate how many days to add to get to the next Sunday
        int daysUntilSunday = DayOfWeek.SUNDAY.getValue() - fromDate.getDayOfWeek().getValue();
        if (daysUntilSunday <= 0) {
            daysUntilSunday += 7;
        }
        return fromDate.plusDays(daysUntilSunday);
    }
}
