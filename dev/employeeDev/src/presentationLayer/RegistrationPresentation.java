package employeeDev.src.presentationLayer;

import java.util.Scanner;

import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.ShiftService;
import employeeDev.src.serviceLayer.SiteService;
import transportDev.src.main.entities.Site;

public class RegistrationPresentation extends Form {
    private EmployeeService employeeService;
    private ShiftService shiftService;
    private SiteService siteService;
    private Scanner scanner;

    public RegistrationPresentation(EmployeeService service,ShiftService shiftService,SiteService siteService, Scanner scanner) {
        super("Add New Employee");
        this.employeeService = service;
        this.shiftService = shiftService;
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
        printSiteList();
        String siteString = UserInputManager.promptForString(scanner, "Site number: ", "Registration cancelled.", "q");
        if (siteString == null) return;
        Site site = siteService.getAllSites().get(Integer.parseInt(siteString) - 1);
        Integer salary = UserInputManager.promptForInt(scanner, "Wage: ", "Registration cancelled.", "q");
        if (salary == null) return;
        String wageType = UserInputManager.promptForWageType(scanner, "Registration cancelled.", "q");
        if (wageType == null) return;
        Integer yearlySickDays = UserInputManager.promptForInt(scanner, "Yearly Sick Days: ", "Registration cancelled.", "q");
        if (yearlySickDays == null) return;
        Integer yearlyDaysOff = UserInputManager.promptForInt(scanner, "Yearly Days Off: ", "Registration cancelled.", "q");
        if (yearlyDaysOff == null) return;

        employeeService.registerEmployee(
            name,
            password,
            id,
            salary.intValue(),
            wageType,
            yearlySickDays.intValue(),
            yearlyDaysOff.intValue(),
            site.getId(),
            site.getName()
        );
        System.out.println("Registration completed successfully!");
    }

    private void printSiteList() {
        System.out.println("Available Sites:");
        int index = 1;
        for (Site site : siteService.getAllSites()) {
            System.out.println(index + ". " + site.getName() + " (ID: " + site.getId() + ")");
            index++;
        }
        System.out.println("Enter the number corresponding to the site or 'q' to cancel.");
    }
}
