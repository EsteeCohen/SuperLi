package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.EmployeeDL;
import employeeDev.src.domainLayer.EmployeeFacade;
import employeeDev.src.domainLayer.Enums.ShiftType;
import transportDev.src.main.entities.Site;
import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.domainLayer.RoleFacade;
import employeeDev.src.domainLayer.ShiftFacade;
import employeeDev.src.domainLayer.WeeklyShiftRequirements;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

public class AssigningService {
    private final ShiftFacade shiftFacade;
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;

    public AssigningService(ShiftFacade shiftFacade, EmployeeFacade employeeFacade, RoleFacade roleFacade) {
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
        this.roleFacade = roleFacade;
    }

    public void assignToShift(String employeeId, LocalDateTime startTime, String shiftType, String roleName,
                              Site site) {
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.assignToShift(employee, startTime, site, role);
    }

    public void unassignToShift(String employeeId, LocalDateTime startTime, Site site, String roleName) {
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.unassignToShift(employee, startTime, site, role);
    }

    public void setShiftRequirement(DayOfWeek day, ShiftType shift, String role, int quantity) {
        RoleDL roleDL = roleFacade.getRoleByName(role);
        if (roleDL == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.setWeeklyRequirements(day, shift, roleDL, quantity);
    }

    public void initializeRequirements() {
        WeeklyShiftRequirements.getInstance().setRequirementsToAll(roleFacade.getRoleByName("Shift Manager"), 0);
    }

    public void integrateTransportsIntoShiftAssignments(Site site) {
        RoleDL warehousemanRole = roleFacade.getRoleByName("Warehouseman");
        if (warehousemanRole == null) {
            throw new IllegalArgumentException("Warehouseman role not found");
        }
        shiftFacade.intergrateShiftToDeliveries(site, warehousemanRole);
    }
}
