package presentationLayer;

import java.util.Scanner;

import serviceLayer.RoleService;

public class RoleCreationPresentation {
    private RoleService roleService;
    private Scanner scanner;

    public RoleCreationPresentation(RoleService service, Scanner scanner) {
        this.roleService = service;
        this.scanner = scanner;
    }

    public void createNewRole() {
        System.out.print("Enter the name of the new role: ");
        String role = scanner.nextLine();

        roleService.createRole(role);
        System.out.println("The role has been successfully added.");
    }
}
