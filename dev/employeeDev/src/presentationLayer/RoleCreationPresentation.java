package employeeDev.src.presentationLayer;

import java.util.Scanner;
import employeeDev.src.serviceLayer.RoleService;

public class RoleCreationPresentation extends Form {
    private RoleService roleService;
    private Scanner scanner;

    public RoleCreationPresentation(RoleService service, Scanner scanner) {
        super("Create New Role");
        this.roleService = service;
        this.scanner = scanner;
    }

    public void createNewRole() {
        String role = UserInputManager.promptForString(scanner, "Enter the name of the new role (or 'q' to cancel): ", "Role creation cancelled.", "q");;
        if (role == null) return;

        boolean success = roleService.createRole(role);
        if (success) {
            System.out.println("The role has been successfully added.");
        } else {
            System.out.println("Failed to add the role. It might already exist.");
        }
    }
}
