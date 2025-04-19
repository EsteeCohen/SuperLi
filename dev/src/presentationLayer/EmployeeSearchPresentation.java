package presentationLayer;

import java.util.Scanner;

import serviceLayer.EmployeeService;
import serviceLayer.HRSystemUIService;

public class EmployeeSearchPresentation {
    private EmployeeService employeeService;
    private Scanner scanner;

    public EmployeeSearchPresentation(EmployeeService service, Scanner scanner) {
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void searchEmployee() {
        System.out.print("Enter employee ID to search: ");
        String id = scanner.nextLine();

        System.out.println(employeeService.getEmployeeById(id).toString());
    }
}
