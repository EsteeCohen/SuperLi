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
        String wageType = "";
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
        while(true){
            try{
            System.out.print("Wage Type: (enter 1 for Hourly, 2 for Monthly): ");
            int wageTypeChoice = scanner.nextInt();
            wageType =  wageTypeChoice == 1 ? "Hourly" : "Monthly";
            break;
            }
            catch (Exception e){
                System.out.println("Invalid input. Please enter 1 for Hourly or 2 for Monthly.");
                scanner.nextLine(); // Clear the invalid input
                continue; // Restart the loop to ask for input again
            }
        }
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
