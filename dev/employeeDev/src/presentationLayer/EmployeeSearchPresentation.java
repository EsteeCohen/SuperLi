package employeeDev.src.presentationLayer;

import java.util.Scanner;
import employeeDev.src.serviceLayer.EmployeeService;

public class EmployeeSearchPresentation extends Form {
    private EmployeeService employeeService;
    private Scanner scanner;

    public EmployeeSearchPresentation(EmployeeService service, Scanner scanner) {
        super("Search Employee");
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void searchEmployee() {
        String id = promptForEmployeeId();
        if (id == null) return;
        printSearchResult(id);
    }

    private String promptForEmployeeId() {
        return UserInputManager.promptForString(
            scanner,
            "Enter employee ID to search (or 'q' to cancel): ",
            "Search cancelled.",
            "q"
        );
    }

    private void printSearchResult(String id) {
        var employee = employeeService.getEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
        } else {
            System.out.println(new EmployeePL(employee));
        }
    }
}
