package employeeDev.src.presentationLayer;

import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.SiteService;
import java.util.Scanner;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.LicenseType;

public class RegistrationPresentation extends Form {
    private final EmployeeService employeeService;
    private final SiteService siteService;
    private final Scanner scanner;

    public RegistrationPresentation(EmployeeService service,SiteService siteService, Scanner scanner) {
        super("Add New Employee");
        this.employeeService = service;
        this.siteService = siteService;
        this.scanner = scanner;
    }

    public void registerNewEmployee() {
        String name = UserInputManager.promptForString(scanner, "Name: ", "Registration cancelled.", "q");
        if (name == null) return;
        String password = UserInputManager.promptForString(scanner, "Password: ", "Registration cancelled.", "q");
        if (password == null) return;
        String id = UserInputManager.promptForString(scanner, "ID: ", "Registration cancelled.", "q");
        if (id == null) return;
        if (!employeeService.isEmployeeExists(id)) {
            System.out.println("Employee with ID: " + id + " does not exist.");
            return;
        }
        String phone = UserInputManager.promptForString(scanner, "Phone Number: ", "Registration cancelled.", "q");
        if (phone == null) return;
        printSiteList();
        String siteString = UserInputManager.promptForString(scanner, "Site number: ", "Registration cancelled.", "q");
        if (siteString == null) return;
        Site site = siteService.getAllSites().get(Integer.parseInt(siteString) - 1);
        System.out.println("Selected Site: " + site.getName());
        printRoleList();
        String roleString = UserInputManager.promptForString(scanner, "Role number: ", "Registration cancelled.", "q");
        if (roleString == null) return;
        String role = employeeService.getAllRoles()[Integer.parseInt(roleString) - 1];
        LicenseType licenseType = null;
        if( role.equals("Driver")){
            String license = UserInputManager.promptForLicenseType(scanner,employeeService.getAllLicensesType(), "License Type (A, B, C, D): ", "Registration cancelled.", "q");
            if (license == null) return;
            licenseType = employeeService.getAllLicensesType().get(Integer.parseInt(license) - 1);
        }
        Integer salary = UserInputManager.promptForInt(scanner, "Wage: ", "Registration cancelled.", "q");
        if (salary == null) return;
        String wageType = UserInputManager.promptForWageType(scanner, "Registration cancelled.", "q");
        if (wageType == null) return;
        Integer yearlySickDays = UserInputManager.promptForInt(scanner, "Yearly Sick Days: ", "Registration cancelled.", "q");
        if (yearlySickDays == null) return;
        Integer yearlyDaysOff = UserInputManager.promptForInt(scanner, "Yearly Days Off: ", "Registration cancelled.", "q");
        if (yearlyDaysOff == null) return;
        if (role.equals("Driver")) {
            employeeService.registerDriver(
                name,
                password,
                id,
                salary,
                wageType,
                yearlySickDays,
                yearlyDaysOff,
                site.getName(),
                phone,
                licenseType
            );
        }
        else{
        employeeService.registerEmployeeWithRole(
            name,
            password,
            id,
            salary,
            wageType,
            yearlySickDays,
            yearlyDaysOff,
            site.getName(),
            phone,
            role
        );
        }
        System.out.println("Registration completed successfully!");
    }

    private void printRoleList() {
        System.out.println("Available Roles:");
        int index = 1;
        for (String role : employeeService.getAllRoles()) {
            System.out.println(index + ". " + role);
            index++;
        }
        System.out.println("Enter the number corresponding to the role or 'q' to cancel.");
    }

    private void printSiteList() {
        System.out.println("Available Sites:");
        int index = 1;
        for (Site site : siteService.getAllSites()) {
            System.out.println(index + ". " + site.getName());
            index++;
        }
        System.out.println("Enter the number corresponding to the site or 'q' to cancel.");
    }
}
