package presentationLayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import serviceLayer.EmployeeService;

public class EmployeeUpdatePresentation {

    private final EmployeeService employeeService;
    private final Scanner scanner;

    public EmployeeUpdatePresentation(EmployeeService employeeService, Scanner scanner) {
        this.employeeService = employeeService;
        this.scanner = scanner;
    }

    public void updateEmployeeDetails() {
        EmployeePL employee = null;
        String wageType = "";
        System.out.println("=== Update Employee ===");
        while (true) {
            try {
                System.out.print("Search employee by ID: ");
                employee = new EmployeePL(employeeService.getEmployeeById(scanner.nextLine()));
                break;
            } catch (NullPointerException e) {
                System.out.println("Employee not found. Please try again.");
            }
        }
        System.out.println("Employee found: " + employee.getFullName() + " (ID: " + employee.getID() + ")");
        System.out.println(employee.toString());
        System.out.println("Enter new values (leave blank to keep current value):");
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("Full Name: ");
        String fullName = scanner.nextLine();
        System.out.print("Wage: ");
        int salary = readInt();
        while (true) {
            System.out.print("Wage Type: (enter 1 for Hourly, 2 for Monthly): ");
            int wageTypeChoice = readInt();
            switch (wageTypeChoice) {
                case 1:
                    wageType = "Hourly";
                    break;
                case 2:
                    wageType = "Monthly";
                    break;
                default:
                    break;
            }
            if (wageTypeChoice == 1 || wageTypeChoice == 2 || wageTypeChoice == 0) {
                break; // Exit the loop if a valid choice is made
            } else {
                System.out.println("Invalid input. Please enter 1 for Hourly or 2 for Monthly.");
            }
        }
        System.out.print("Yearly Sick Days: ");
        int yearlySickDays = readInt();
        System.out.print("Yearly Days Off: ");
        int yearlyDaysOff = readInt();
        Map<String, Object> attributes = new HashMap<>();
        if (!password.isEmpty()) {
            attributes.put("password", password);
        }
        if (!fullName.isEmpty()) {
            attributes.put("fullName", fullName);
        }
        if (salary != 0) {
            attributes.put("wage", salary);
        }
        if (!wageType.isEmpty()) {
            attributes.put("wageType", wageType);
        }
        if (yearlySickDays != 0) {
            attributes.put("yearlySickDays", yearlySickDays);
        }
        if (yearlyDaysOff != 0) {
            attributes.put("yearlyDaysOff", yearlyDaysOff);
        }
        employeeService.updateEmployeeAttributes(employee.getID(), attributes);
        System.out.println("Employee details updated successfully!");
    }

    public int readInt() {
        int input = 0;
        while (true) {
            try {
                input = scanner.nextLine().isEmpty() ? 0 : Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue; // Restart the loop to ask for input again
            }
            return input;
        }
    }
}
