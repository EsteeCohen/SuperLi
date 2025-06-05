package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.AvailabilityDL;
import employeeDev.src.domainLayer.AvailabilityFacade;
import employeeDev.src.domainLayer.DriverDL;
import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.Enums.ShiftType;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftDL;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.serviceLayer.Interfaces.DriverInfoInterface;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public class ShiftService implements DriverInfoInterface{
    private final ShiftFacade shiftFacade;
    private final AvailabilityFacade availabilityFacade;
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;

    public ShiftService(ShiftFacade shiftFacade, AvailabilityFacade availabilityFacade, EmployeeFacade employeeFacade, RoleFacade roleFacade) {
        this.shiftFacade = shiftFacade;
        this.availabilityFacade = availabilityFacade;
        this.employeeFacade = employeeFacade;
        this.roleFacade = roleFacade;
    }

    public boolean CheckIfThereAreShiftsThatAreNotAssigned(Site site, LocalDate startDate, LocalDate endDate) {
        return shiftFacade.checkIfThereAreShiftsThatAreNotFullyAssigned(site, startDate, endDate);
    }

    public List<ShiftSL> getAllShiftsWithMissingAssigns(Site site, LocalDate startDate, LocalDate endDate) {
        List<ShiftDL> allDlShifts = shiftFacade.getAllShiftsWithMissingAssigns(site, startDate, endDate);
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    public List<ShiftSL> getWeeklyShifts(LocalDate startDate, Site site) {
        List<ShiftDL> allDlShifts = shiftFacade.getWeeklyShifts(startDate, site);
        List<ShiftSL> allSlShifts = new ArrayList<>();
        for (ShiftDL dlShift : allDlShifts) {
            allSlShifts.add(new ShiftSL(dlShift));
        }
        return allSlShifts;
    }

    // Updated: now uses startTime and site
    public void setAvailabilityOfEmployeeToShift(String employeeId, LocalDateTime startTime, Site site, boolean available) {
        ShiftDL shift = shiftFacade.getShiftByStartTimeAndSite(startTime, site);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
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
    public List<EmployeeSL> getAvailableEmployeesForShift(LocalDateTime startTime, Site site) {
        ShiftDL shift = shiftFacade.getShiftByStartTimeAndSite(startTime, site);
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
        List<EmployeeDL> unassignedEmployees = shiftFacade.getAllUnassignedEmployeesForShift(shift.getStartTime(), shift.getSite());
        List<EmployeeSL> unassignedEmployeeSL = new ArrayList<>();
        for (EmployeeDL employee : unassignedEmployees) {
            unassignedEmployeeSL.add(new EmployeeSL(employee));
        }
        return unassignedEmployeeSL;
    }

    @Override
    public List<DriverSL> getAvailableDriversWithLicense(LocalDateTime time, Site site, LicenseType licenseType) {
        List<DriverDL> drivers = new ArrayList<>();
        RoleDL role = roleFacade.getRoleByName("Driver");
        if (role == null) {
            throw new IllegalArgumentException("Role 'Driver' not found");
        }
        ShiftDL shift = shiftFacade.getShiftAtTime(time, site);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given time and site");
        }
        List<EmployeeDL> assignedDrivers = shift.getEmployeesAssignment().get(role);
        // Filter the assigned drivers to only include those who are actually drivers
        if (assignedDrivers != null) {
            for (EmployeeDL employee : assignedDrivers) {
                if (employee.isDriver()) {
                    DriverDL driver = (DriverDL) employee;
                    if (driver.isLicensed(licenseType) && driver.isAvailableToDrive()) {
                        drivers.add(driver);
                    }
                }
            }
        }
        List<DriverSL> driverSLs = new ArrayList<>();
        for (DriverDL driver : drivers) {
            driverSLs.add(new DriverSL(driver));
        }
        return driverSLs;
    }

    public List<ShiftSL> getAllShiftFromSite(Site site) {
        Collection<ShiftDL> allShifts = shiftFacade.getAllShiftsFromSite(site);
        List<ShiftSL> allShiftSL = new ArrayList<>();
        for (ShiftDL shift : allShifts) {
            allShiftSL.add(new ShiftSL(shift));
        }
        return allShiftSL;
    }

    @Override
    public DriverSL getDriverById(String id) {
        EmployeeDL employee = employeeFacade.getEmployee(id);
        if (employee == null || !employee.isDriver()) {
            return null; // or throw an exception
        }
        DriverDL driver = (DriverDL) employee;
        return new DriverSL(driver);
    }

    @Override
    public List<DriverSL> getAllDrivers() {
        List<DriverDL> drivers = employeeFacade.getDrivers();
        List<DriverSL> driverSLs = new ArrayList<>();
        for (DriverDL driver : drivers) {
            driverSLs.add(new DriverSL(driver));
        }
        return driverSLs;
    }

    @Override
    public List<DriverSL> getDriversByLicenseType(LicenseType licenseType) {
        List<DriverSL> driverSLs = getAllDrivers();
        return driverSLs.stream()
                .filter(driver -> driver.getLicenseType().equals(licenseType))
                .toList();
    }

    @Override
    public void setAvailableToDrive(String driverID, boolean isAvailable) {
        employeeFacade.setAvailableToDrive(driverID, isAvailable);
    }

    @Override
    public void addDriver(String fullName, String password, String id, int wage, String wageType, int yearlySickDays, int yearlyDaysOff, String siteName, String phoneNumber, LicenseType licenseType) {
        employeeFacade.registerDriver(fullName, password, id, wage, wageType.charAt(0), yearlySickDays, yearlyDaysOff, siteName, phoneNumber, licenseType);
    }

    public boolean isEmployeeWithRoleOnShift(Site site, String employeeId, String roleName) {
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            return false; // Employee not found
        }
        RoleDL role = roleFacade.getRoleByName(roleName);
        if (role == null) {
            return false; // Role not found
        }
        List<ShiftDL> shifts = shiftFacade.getAllShiftsFromSite(site);
        for (ShiftDL shift : shifts) {
            if (shift.getEmployeesAssignment().containsKey(role)) {
                List<EmployeeDL> assignedEmployees = shift.getEmployeesAssignment().get(role);
                if (assignedEmployees != null && assignedEmployees.contains(employee)) {
                    return true;
                }
            }
        }
        return false; // Employee with the specified role is not on any shift at the site
        
    }

    public void unassignEmployeeFromShift(String employeeId, LocalDateTime startTime, Site site,String roleName) {
        ShiftDL shift = shiftFacade.getShiftByStartTimeAndSite(startTime, site);
        if (shift == null) {
            throw new IllegalArgumentException("Shift not found for the given start time and site");
        }
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        shiftFacade.unassignToShift(employee, startTime, site, roleFacade.getRoleByName(roleName));
    }
}
