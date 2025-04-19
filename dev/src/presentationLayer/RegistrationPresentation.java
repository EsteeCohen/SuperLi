package presentationLayer;

import java.util.Scanner;

import serviceLayer.EmployeeService;

public class RegistrationPresentation {
    private EmployeeService employeeService;
    private Scanner scanner;

    public RegistrationPresentation(EmployeeService service, Scanner scanner) {
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void registerNewEmployee() {
        System.out.println("=== Add New Employee ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Wage: ");
        int salary = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer
        System.out.print("Wage Type: ");
        String wageType = scanner.nextLine();
        System.out.print("Yearly Sick Days: ");
        int yearlySickDays = scanner.nextInt();
        System.out.print("Yearly Days Off: ");
        int yearlyDaysOff = scanner.nextInt();
        scanner.nextLine(); // Clear the buffer

        // Use the wageType variable meaningfully
        System.out.println("Selected Wage Type: " + wageType);

        employeeService.registerEmployee(name,password, id, salary, wageType, yearlySickDays, yearlyDaysOff);
        System.out.println("Registration completed successfully!");
    }
}
