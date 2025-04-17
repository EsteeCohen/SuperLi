package src.presentationLayer;

import java.util.Scanner;

import src.serviceLayer.HRSystemUIService;

public class EmployeeSearchPresentation {
    private HRSystemUIService employeeService;
    private Scanner scanner;

    public EmployeeSearchPresentation(HRSystemUIService service, Scanner scanner) {
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void searchEmployee() {
        System.out.print("Enter employee ID to search: ");
        String id = scanner.nextLine();

        employeeService.printEmployeeDetails(id);
    }
}
