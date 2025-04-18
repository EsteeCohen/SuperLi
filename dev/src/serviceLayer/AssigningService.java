package serviceLayer;

import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import domainLayer.RoleDL;
import domainLayer.RoleFacade;
import domainLayer.ShiftFacade;
import java.time.LocalDate;

public class AssigningService {
    private final ShiftFacade shiftFacade;
    private final EmployeeFacade employeeFacade;
    private final RoleFacade roleFacade;

    public AssigningService(ShiftFacade shiftFacade, EmployeeFacade employeeFacade, RoleFacade roleFacade) {
        this.shiftFacade = shiftFacade;
        this.employeeFacade = employeeFacade;
        this.roleFacade = roleFacade;
    }

    public void assignToShift(String employeeId, LocalDate date, String shiftType, String roleName) {
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.assignToShift(employee, date, shiftType, role);
    }

    public void unassignToShift(String employeeId, LocalDate date, String shiftType, String roleName){
        RoleDL role = roleFacade.getRoleByName(roleName);
        EmployeeDL employee = employeeFacade.getEmployee(employeeId);
        if (employee == null) {
            throw new IllegalArgumentException("Employee not found");
        }
        if (role == null) {
            throw new IllegalArgumentException("Role not found");
        }
        shiftFacade.unassignToShift(employee, date, shiftType, role);
    }

}
