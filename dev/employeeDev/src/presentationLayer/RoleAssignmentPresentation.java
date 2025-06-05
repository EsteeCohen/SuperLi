package employeeDev.src.presentationLayer;

import java.util.List;
import java.util.Scanner;

import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.RoleSL;
import employeeDev.src.serviceLayer.RoleService;

public class RoleAssignmentPresentation extends Form {
    private RoleService roleService;
    private EmployeeService employeeService;
    private Scanner scanner;

    public RoleAssignmentPresentation(EmployeeService employeeService, RoleService service, Scanner scanner) {
        super("Assign Role to Employee");
        this.roleService = service;
        this.employeeService = employeeService;
        this.scanner = scanner;
    }

    public void assignRoleToEmployee() {
        String employeeId = promptForEmployeeId();
        if (employeeId == null) return;
        if (!employeeService.isEmployeeExists(employeeId)) {
            System.out.println("Employee with ID: " + employeeId + " does not exist.");
            return;
        }

        List<String> roles = roleService.getAllRoles().stream()
                                        .map(RoleSL::getName)
                                        .toList();
        if (roles.isEmpty()) {
            System.out.println("No roles available.");
            return;
        }

        printRolesList(roles);

        Integer roleIndex = promptForRoleIndex(roles.size());
        if (roleIndex == null) return;

        String selectedRole = roles.get(roleIndex);
        employeeService.assignRoleToEmployee(employeeId, selectedRole);
        System.out.println("The role has been assigned to the employee.");
    }

    private void printRolesList(List<String> roles) {
        System.out.println("Available roles:");
        for (int i = 0; i < roles.size(); i++) {
            System.out.println((i + 1) + ". " + roles.get(i));
        }
    }

    private String promptForEmployeeId() {
        return UserInputManager.promptForString(scanner, "Enter employee ID (or 'q' to cancel): ", "Assignment cancelled.", "q");
    }

    private Integer promptForRoleIndex(int rolesCount) {
        return UserInputManager.promptForIndexFromList(scanner, "Enter the number of the role (or 'q' to cancel): ", rolesCount, "Assignment cancelled.", "q");
    }
}
