import java.util.List;
import java.util.Scanner;

public class RoleAssignmentPresentation {
    private RoleService roleService;
    private Scanner scanner;

    public RoleAssignmentPresentation(RoleService service, Scanner scanner) {
        this.roleService = service;
        this.scanner = scanner;
    }

    public void assignRoleToEmployee() {
        System.out.print("Enter employee ID: ");
        String employeeId = scanner.nextLine();

        // Fetch and display roles with numbers
        List<String> roles = roleService.getAvailableRoles();
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
            roleService.assignRoleToEmployee(employeeId, selectedRole);
            System.out.println("The role has been assigned to the employee.");
        } else {
            System.out.println("Invalid role number.");
        }
    }
}
