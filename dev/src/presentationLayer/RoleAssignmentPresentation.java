package presentationLayer;

import java.util.List;
import java.util.Scanner;

import serviceLayer.EmployeeService;
import serviceLayer.RoleSL;
import serviceLayer.RoleService;

public class RoleAssignmentPresentation {
    private RoleService roleService;
    private EmployeeService employeeService;
    private Scanner scanner;

    public RoleAssignmentPresentation(EmployeeService employeeService ,RoleService service, Scanner scanner) {
        this.roleService = service;
        this.employeeService = employeeService;
        this.scanner = scanner;
    }

    public void assignRoleToEmployee() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();
        List<String> roles = roleService.getAllRoles().stream()
                                        .map(RoleSL::getName)
                                        .toList();
        System.out.println("Available roles:");
        for (int i = 0; i < roles.size(); i++) {
            System.out.println((i + 1) + ". " + roles.get(i));
        }

        // Get user input for role selection
        System.out.print("Enter the number of the role: ");
        int roleNumber = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        if (roleNumber > 0 && roleNumber <= roles.size()) {
            String selectedRole = roles.get(roleNumber - 1);
            employeeService.assignRoleToEmployee(employeeId, selectedRole);
            System.out.println("The role has been assigned to the employee.");
        } else {
            System.out.println("Invalid role number.");
        }
    }
}
