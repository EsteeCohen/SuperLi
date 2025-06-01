package employeeDev.src.presentationLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import employeeDev.src.serviceLayer.EmployeeService;

public class EmployeeUpdatePresentation extends Form {

    private final EmployeeService employeeService;
    private final Scanner scanner;

    public EmployeeUpdatePresentation(EmployeeService employeeService, Scanner scanner) {
        super("Update Employee");
        this.employeeService = employeeService;
        this.scanner = scanner;
    }

    public void updateEmployeeDetails() {
        EmployeePL employee = findEmployeeById();
        if (employee == null) return;

        System.out.println("Employee found: " + employee.getFullName() + " (ID: " + employee.getID() + ")");
        System.out.println(employee);

        Map<String, Object> attributes = collectUpdateAttributes();

        if (attributes.isEmpty()) {
            System.out.println("No changes made.");
            return;
        }

        employeeService.updateEmployeeAttributes(employee.getID(), attributes);
        System.out.println("Employee details updated successfully!");
    }

    private EmployeePL findEmployeeById() {
        while (true) {
            System.out.print("Search employee by ID (or 'q' to cancel): ");
            String id = UserInputManager.getUserInputOrCancel(scanner, "Update cancelled.", "q");
            if (id == null) return null;
            var emp = employeeService.getEmployeeById(id);
            if (emp != null) return new EmployeePL(emp);
            System.out.println("Employee not found. Please try again.");
        }
    }

    private Map<String, Object> collectUpdateAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        String password = UserInputManager.promptForString(scanner, "Password: ", "", "q");
        if (password != null && !password.isEmpty()) attributes.put("password", password);

        String fullName = UserInputManager.promptForString(scanner, "Full Name: ", "", "q");
        if (fullName != null && !fullName.isEmpty()) attributes.put("fullName", fullName);

        Integer wage = UserInputManager.promptForInt(scanner, "Wage: ", "", "q");
        if (wage != null && wage != 0) attributes.put("wage", wage);

        String wageType = UserInputManager.promptForWageType(scanner, "", "q");
        if (wageType != null && !wageType.isEmpty()) attributes.put("wageType", wageType);

        Integer yearlySickDays = UserInputManager.promptForInt(scanner, "Yearly Sick Days: ", "", "q");
        if (yearlySickDays != null && yearlySickDays != 0) attributes.put("yearlySickDays", yearlySickDays);

        Integer yearlyDaysOff = UserInputManager.promptForInt(scanner, "Yearly Days Off: ", "", "q");
        if (yearlyDaysOff != null && yearlyDaysOff != 0) attributes.put("yearlyDaysOff", yearlyDaysOff);

        return attributes;
    }
}
