package src.presentationLayer;

import java.util.Scanner;

import src.serviceLayer.HRSystemUIService;

public class RegistrationPresentation {
    private HRSystemUIService employeeService;
    private Scanner scanner;

    public RegistrationPresentation(HRSystemUIService service, Scanner scanner) {
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void registerNewEmployee() {
        System.out.println("=== Add New Employee ===");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("ID: ");
        String id = scanner.nextLine();
        System.out.print("Wage: ");
        double salary = scanner.nextDouble();
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

        employeeService.registerEmployee(name, id, salary, wageType, yearlySickDays, yearlyDaysOff);
        System.out.println("Registration completed successfully!");
    }
}
