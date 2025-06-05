package employeeDev.src.presentationLayer;

import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.ShiftService;
import employeeDev.src.serviceLayer.SiteService;
import java.util.Scanner;

public class RoleUnassignmentPresentation extends Form {
    private EmployeeService employeeService;
    private ShiftService shiftService;
    private SiteService siteService;
    private Scanner scanner;

    public RoleUnassignmentPresentation(EmployeeService employeeService,ShiftService shiftService,SiteService siteService, Scanner scanner) {
        super("Role Unassignment");
        this.employeeService = employeeService;
        this.shiftService = shiftService;
        this.siteService = siteService;
        this.scanner = scanner;
    }
    public void unassignRoleFromEmployee() {
        String employeeId = UserInputManager.promptForString(scanner, "Enter Employee ID to unassign role from: ", "Unassignment cancelled.", "q");
        if (employeeId == null) return;
        printEmployeeRoles(employeeId);
        String roleName = UserInputManager.promptForString(scanner, "Enter Role Name to unassign: ", "Unassignment cancelled.", "q");
        if (roleName == null) return;
        siteService.getAllSites().forEach(site -> {
            if(shiftService.isEmployeeWithRoleOnShift(site, employeeId, roleName)) {
            System.out.println("Cannot unassign role '" + roleName + "' from employee with ID: " + employeeId + " because they are currently assigned to a shift with this role.");
            return;
        }
        });
        boolean success = employeeService.unassignRoleFromEmployee(employeeId, roleName);
        if (success) {
            System.out.println("Role '" + roleName + "' successfully unassigned from employee with ID: " + employeeId);
        } else {
            if (roleName.equals("Driver"))
                System.out.println("Cannot unassign the 'Driver' role from a driver employee.");
            else
                System.out.println("Failed to unassign role. Please check the role name.");
        }
    }
    private void printEmployeeRoles(String employeeId) {
        EmployeePL employee = new EmployeePL(employeeService.getEmployeeById(employeeId));
        System.out.println("Employee ID: " + employeeId + " - " + employee.getFullName());
        System.out.println("Roles:");
        employeeService.getEmployeeById(employeeId).getRoles().forEach(role -> {
            RolePL rolePL = new RolePL(role);
            System.out.println("- " + rolePL);
        });
        if (employeeService.getEmployeeById(employeeId).getRoles().isEmpty()) {
            System.out.println("No roles assigned to this employee.");
        }
    }

}
