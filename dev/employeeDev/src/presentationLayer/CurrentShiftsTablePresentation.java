package employeeDev.src.presentationLayer;

import java.util.List;
import java.util.Scanner;

import employeeDev.src.serviceLayer.ShiftService;
import employeeDev.src.serviceLayer.SiteService;
import transportDev.src.main.entities.Site;

public class CurrentShiftsTablePresentation extends Form {
    private ShiftService shiftService;
    private SiteService siteService;
    private Scanner scanner;

    public CurrentShiftsTablePresentation(ShiftService shiftService,SiteService siteService, Scanner scanner) {
        super("Weekly Shift Table");
        this.shiftService = shiftService;
        this.siteService = siteService;
        this.scanner = scanner;
    }

    public void showCurrentShiftsAndEmployees() {

        Site site = pickSite(); // Assuming admin is always available
        if (site == null) {
            System.out.println("Site selection cancelled.");
            return;
        }

        List<ShiftPL> shifts = shiftService.getWeeklyShifts(java.time.LocalDate.now(), site).stream()
            .map(shiftSL -> new ShiftPL(shiftSL))
            .toList();

        for (ShiftPL shift : shifts) {
            if (shift.getSite().equals(site)
                    && shift.getStartTime().isBefore(java.time.LocalDateTime.now())
                    && shift.getEndTime().isAfter(java.time.LocalDateTime.now())) {
                System.out.println("-----------------------------------");
                System.out.println("Shift Number: " + (shifts.indexOf(shift) + 1));
                System.out.println("Start Time: " + shift.getStartTime());
                System.out.println("End Time: " + shift.getEndTime());
                System.out.println("Shift Type: " + shift.getShiftType());
                System.out.println("Assigned Employees:");
                shift.getEmployeesAssignment().forEach((role, employees) -> {
                    System.out.println("  Role: " + role.getName());
                    employees.forEach(employee -> System.out.println("    - " + employee.getFullName() + " (ID: " + employee.getID() + ")"));
                });
            }
        }
    }

    private Site pickSite() {
        System.out.println("Available Sites:");
        int index = 1;
        for (Site site : siteService.getAllSites()) {
            System.out.println(index + ". " + site.getName() + " (ID: " + site.getId() + ")");
            index++;
        }
        System.out.println("Enter the number corresponding to the site or 'q' to cancel.");
        String siteString = UserInputManager.promptForString(scanner, "Site number: ", "Registration cancelled.", "q");
        if (siteString == null) return null;
        return siteService.getAllSites().get(Integer.parseInt(siteString) - 1);
    }

}
