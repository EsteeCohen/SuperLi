package serviceLayer;

import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import domainLayer.RoleDL;
import domainLayer.RoleFacade;
import domainLayer.ShiftFacade;
import domainLayer.WeeklyShiftRequirements;
import domainLayer.Enums.ShiftType;
import serviceLayer.Interfaces.ITransportScheduleService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public class AssigningService {
    private final ShiftFacade shiftFacade;
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;
    private final ITransportScheduleService transportScheduleService;

    public AssigningService(ShiftFacade shiftFacade, EmployeeFacade employeeFacade, RoleFacade roleFacade,
                            ITransportScheduleService transportScheduleService) {
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
        this.roleFacade = roleFacade;
        this.transportScheduleService = transportScheduleService;
    }

    public void assignToShift(String employeeId, LocalDateTime startTime, String shiftType, String roleName) {
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.assignToShift(employee, startTime, shiftType, role);
    }

    public void unassignToShift(String employeeId, LocalDateTime startTime, String shiftType, String roleName){
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.unassignToShift(employee, startTime, shiftType, role);
    }

    public void setShiftRequirement(DayOfWeek day, ShiftType shift, String role, int quantity) {
        RoleDL roleDL = roleFacade.getRoleByName(role);
        if (roleDL == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.setRequirements(day, shift, roleDL, quantity);
    }

    public void initializeRequirements(){
        WeeklyShiftRequirements.getInstance().setRequirementsToAll(roleFacade.getRoleByName("Shift Manager"), 0);
    }

    public void integrateTransportsIntoShiftAssignments(LocalDateTime startDate, LocalDateTime endDate) {        
        List<TransportScheduleSL> upcomingTransports = transportScheduleService.getUUpcomingDeliveries(startDate, endDate);

        // ודא שתפקידי המפתח קיימים במערכת
        RoleDL driverRole = roleFacade.getRoleByName("Driver");
        if (driverRole == null) {
            System.err.println("Error: 'Driver' role not found in HR system. Cannot assign drivers to transports.");
            return;
        }
        RoleDL warehousemanRole = roleFacade.getRoleByName("Warehouseman");
        if (warehousemanRole == null) {
            System.err.println("Warning: 'Warehouseman' role not found in HR system. Deliveries might not be covered by a dedicated warehouseman.");
        }

        for (TransportScheduleSL transport : upcomingTransports) {
             {
                try {
                    assignDriverToTransportShift(
                        transport.getAssignedDriverId(),
                        transport.getArrivalTime(),
                        transport.getDurationMinutes(),
                        transport.getTransportId(),
                        driverRole
                    );
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Failed to assign pre-assigned driver for transport " + transport.getTransportId() + ": " + e.getMessage(), e);
                }
            }
        }
        if (!upcomingTransports.isEmpty()) {
            setShiftRequirement(
                upcomingTransports.get(0).getArrivalTime().getDayOfWeek(),
                ShiftType.EVENING,
                warehousemanRole.getName(),
                upcomingTransports.size()
            );// TO-DO: shift time should be dynamic based on transport times
        }
    }

    /**
     * מתודת עזר פנימית: משבצת נהג ספציפי למשמרת נהיגה התואמת הובלה.
     * כוללת בדיקות זמינות.
     * @param driverId מזהה הנהג.
     * @param shiftStartDateTime תאריך ושעת תחילת המשמרת (תחילת ההובלה).
     * @param durationMinutes משך המשמרת/ההובלה.
     * @param transportId מזהה ההובלה לשיוך.
     * @param driverRole אובייקט התפקיד 'נהג'.
     * @throws IllegalArgumentException אם הנהג לא נמצא, או אינו זמין.
     */
    private void assignDriverToTransportShift(String driverId, LocalDateTime shiftStartDateTime, int durationMinutes,
                                              String transportId, RoleDL driverRole) {
        EmployeeDL driver = employeeFacade.getEmployee(driverId);
        if (driver == null) {
            throw new IllegalArgumentException("Driver with ID " + driverId + " not found in HR system.");
        }

        // שיבוץ הנהג למשמרת Driving
        // הנחה: ShiftType.DRIVING קיים או מוגדר באנאם שלכם.
        assignToShift(driverId, shiftStartDateTime, ShiftType.EVENING.name(), driverRole.getName());// TO-DO: shift time should be dynamic based on transport times
    }
}
