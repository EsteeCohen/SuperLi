package employeeDev.src.presentationLayer;

import java.util.Scanner;

import employeeDev.src.serviceLayer.EmployeeService;

public class RegistrationPresentation extends Form {
    private EmployeeService employeeService;
    private Scanner scanner;

    public RegistrationPresentation(EmployeeService service, Scanner scanner) {
        super("Add New Employee");
        this.employeeService = service;
        this.scanner = scanner;
    }

    public void registerNewEmployee() {
        String name = UserInputManager.promptForString(scanner, "Name: ", "Registration cancelled.", "q");
        if (name == null) return;
        String password = UserInputManager.promptForString(scanner, "Password: ", "Registration cancelled.", "q");
        if (password == null) return;
        String id = UserInputManager.promptForString(scanner, "ID: ", "Registration cancelled.", "q");
        if (id == null) return;
        Integer salary = UserInputManager.promptForInt(scanner, "Wage: ", "Registration cancelled.", "q");
        if (salary == null) return;
        String wageType = UserInputManager.promptForWageType(scanner, "Registration cancelled.", "q");
        if (wageType == null) return;
        Integer yearlySickDays = UserInputManager.promptForInt(scanner, "Yearly Sick Days: ", "Registration cancelled.", "q");
        if (yearlySickDays == null) return;
        Integer yearlyDaysOff = UserInputManager.promptForInt(scanner, "Yearly Days Off: ", "Registration cancelled.", "q");
        if (yearlyDaysOff == null) return;

        employeeService.registerEmployee(name, password, id, salary, wageType, yearlySickDays, yearlyDaysOff);
        System.out.println("Registration completed successfully!");
    }
}
