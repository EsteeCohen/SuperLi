package employeeDev.src.presentationLayer;

import employeeDev.src.serviceLayer.AssigningService;
import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.ShiftService;
import employeeDev.src.serviceLayer.SiteService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import transportDev.src.main.entities.Site;

public class ShiftsTablePresentation extends Form {
    private final ShiftService shiftService;
    private final EmployeeService employeeService;
    private final SiteService siteService;    
    private final AssigningService assigningService;
    private final Scanner scanner;
    private ArrayList<ShiftPL> shifts;

    private static final LocalDate TODAY = LocalDate.now();
    private static final LocalDate NEXT_SUNDAY = TODAY.plusDays(7 - TODAY.getDayOfWeek().getValue() % 7);

    public ShiftsTablePresentation(ShiftService service, Scanner scanner, AssigningService assigningService, EmployeeService employeeService, SiteService siteService) {
        super("Weekly Shift Table");
        this.assigningService = assigningService;
        this.employeeService = employeeService;
        this.shiftService = service;
        this.siteService = siteService;
        this.scanner = scanner;
        
    }

    public void showEmployeeShift(String employeeId) {
        if (employeeId == null) return;
        shifts = siteService.getAllSites().stream().flatMap(site -> shiftService.getWeeklyShifts(NEXT_SUNDAY, site).stream()
        .map(shiftSL -> new ShiftPL(shiftSL))).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
        Map<String, List<ShiftPL>> siteToShifts = groupShiftsBySite();
        List<ShiftPL> employeeShifts = siteToShifts.values().stream()
            .flatMap(List::stream)
            .filter(shift -> shift.getEmployeesAssignment().values().stream()
                .flatMap(List::stream)
                .anyMatch(employee -> employee.getID().equals(employeeId)))
            .toList();

        if (employeeShifts.isEmpty()) {
            System.out.println("No shifts found for employee with ID: " + employeeId);
            return;
        }

        System.out.println("Shifts for Employee ID: " + employeeId);
        for (ShiftPL shift : employeeShifts) {
            System.out.println("-----------------------------------");
            System.out.println("Shift Number: " + (employeeShifts.indexOf(shift) + 1));
            System.out.println("Start Time: " + shift.getStartTime());
            System.out.println("End Time: " + shift.getEndTime());
            System.out.println("Shift Type: " + shift.getShiftType());
            System.out.println("Site: " + (shift.getSite() != null ? shift.getSite().getName() : "Unknown Site"));
            System.out.println("My roles in this shift:");
            shift.getEmployeesAssignment().forEach((role, employees) -> {
                employees.stream()
                    .filter(employee -> employee.getID().equals(employeeId))
                    .findFirst()
                    .ifPresent(employee -> System.out.println("  Role: " + role.getName() + " (ID: " + employee.getID() + ")"));
            });
        }
    }

    public void showShiftTable() {
        shifts = siteService.getAllSites().stream()
    .flatMap(site -> shiftService.getWeeklyShifts(NEXT_SUNDAY, site).stream()
        .map(shiftSL -> new ShiftPL(shiftSL)))
    .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
        Map<String, List<ShiftPL>> siteToShifts = groupShiftsBySite();
 if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }
        for (String siteName : siteToShifts.keySet()) {
            printSiteHeader(siteToShifts, siteName);
            List<ShiftPL> siteShifts = siteToShifts.get(siteName);
            siteShifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
            for (ShiftPL shift : siteShifts) {
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
                System.out.println("Shift Requirements:");
                if (shift.getShiftRoleRequirements() != null && shift.getShiftRoleRequirements() instanceof java.util.Map) {
                    ((java.util.Map<?, ?>)shift.getShiftRoleRequirements()).entrySet().forEach(entry -> {
                        System.out.println("  Role: " + ((java.util.Map.Entry<?, ?>)entry).getKey().toString() + ", Required: " + ((java.util.Map.Entry<?, ?>)entry).getValue());
                    });
                }
                System.out.println("Available Employees:");
                shiftService.getAvailableEmployeesForShift(shift.getStartTime(), shift.getSite()).forEach(employee -> {
                    boolean isAssigned = shift.getEmployeesAssignment().values().stream()
                        .flatMap(List::stream)
                        .anyMatch(e -> e.getID().equals(employee.getId()));
                    if(!isAssigned){
                        System.out.println("  - " + employee.getFullName() + " (ID: " + employee.getId() + ")");
                        System.out.println("    Roles: ");
                        List<RolePL> roles = employeeService.getEmployeeById(employee.getId()).getRoles().stream()
                            .map(roleSL -> new RolePL(roleSL))
                            .toList();
                        for (RolePL role : roles) {
                            if (role.getName().equals("Driver")){
                                employeeService.getAllDrivers().stream()
                                    .filter(driver -> driver.getId().equals(employee.getId()))
                                    .findFirst()
                                    .ifPresent(driver -> System.out.println("    - " + role.toString() + " (License: " + driver.getLicenseType().toString() + ")"));
                            }
                            else
                                System.out.println("      - " + role.toString());
                        }
                    }
                });
                System.out.println("-----------------------------------");
            }
        }
    }

    private void printSiteHeader(Map<String, List<ShiftPL>> siteToShifts, String siteName) {
        ShiftPL firstShift = siteToShifts.get(siteName).get(0);
        String siteAddress = firstShift.getSite() != null ? firstShift.getSite().getAddress() : "";
        System.out.println("==============================");
        System.out.println("Site: " + siteName + (siteAddress.isEmpty() ? "" : " (" + siteAddress + ")"));
        System.out.println("==============================");
     
    }

    private Map<String, List<ShiftPL>> groupShiftsBySite() {
        Map<String, List<ShiftPL>> siteToShifts = new TreeMap<>();
        for (ShiftPL shift : shifts) {
            String siteName = shift.getSite() != null ? shift.getSite().getName() : "Unknown Site";
            siteToShifts.computeIfAbsent(siteName, k -> new ArrayList<>()).add(shift);
        }
        return siteToShifts;
    }

    public void manageShiftOptions() {
        boolean continueManaging = true;

        while (continueManaging) {
            System.out.println("1. Assign Employee to Shift");
            System.out.println("2. Unassigned Employee Shifts");
            System.out.println("0. Exit");

            String choice = UserInputManager.promptForString(scanner, "Choose an option: ", "Invalid choice. Please try again.", "q");
            if (choice == null) return;

            switch (choice) {
                case "1":
                    assignEmployeeToShift();
                    break;
                case "2":
                    unassignedEmployeeFromShift();
                    break;
                case "0":
                    continueManaging = false;
                    System.out.println("Exiting shift management.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void unassignedEmployeeFromShift() {
        String employeeId = UserInputManager.promptForString(scanner, "Enter Employee ID to view unassigned shifts (or 'q' to cancel): ", "Operation cancelled.", "q");
        if (employeeId == null) return;
        showEmployeeShift(employeeId);
       shifts = siteService.getAllSites().stream().flatMap(site -> shiftService.getWeeklyShifts(NEXT_SUNDAY, site).stream()
        .map(shiftSL -> new ShiftPL(shiftSL))).collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
        Map<String, List<ShiftPL>> siteToShifts = groupShiftsBySite();
        List<ShiftPL> employeeShifts = siteToShifts.values().stream()
            .flatMap(List::stream)
            .filter(shift -> shift.getEmployeesAssignment().values().stream()
                .flatMap(List::stream)
                .anyMatch(employee -> employee.getID().equals(employeeId)))
            .toList();

        if (employeeShifts.isEmpty()) {
            System.out.println("No assigned shifts found for employee with ID: " + employeeId);
            return;
        }

        String shiftToUnassign = UserInputManager.promptForString(scanner, "Enter Shift number to unassign (or 'q' to cancel): ", "Operation cancelled.", "q");
        if (shiftToUnassign == null) return;
        ShiftPL selectedShift = employeeShifts.stream()
            .filter(shift -> shift.getStartTime().toString().equals(shiftToUnassign))
            .findFirst()
            .orElse(null);
        if (selectedShift == null) {
            System.out.println("No shift found with the provided number.");
            return;
        }
        String roleString = UserInputManager.promptForString(scanner, "Enter Role Name to unassign (or 'q' to cancel): ", "Operation cancelled.", "q");
        shiftService.unassignEmployeeFromShift(employeeId, selectedShift.getStartTime(), selectedShift.getSite(),roleString);
    }

    public void assignEmployeeToShift() {
        boolean assignMore = true;

        while (assignMore) {
            Integer shiftIndex = UserInputManager.promptForIndexFromList(
                scanner,"Enter shift number (or 'q' to cancel): ",
                shifts.size(),
                "Assignment cancelled.",
                "q"
            );
            if (shiftIndex == null) return;
            ShiftPL selectedShift = shifts.get(shiftIndex);

            String employeeId = UserInputManager.promptForString(
                scanner,
                "Enter employee ID (or 'q' to cancel): ",
                "Assignment cancelled.",
                "q"
            );
            if (employeeId == null) return;

            EmployeePL employee = new EmployeePL(employeeService.getEmployeeById(employeeId));
            List<String> roles = employee.getRoles();
            if (roles.isEmpty()) {
                System.out.println("This employee has no roles.");
                continue;
            }

            System.out.println("Available roles for the employee:");
            for (int i = 0; i < roles.size(); i++) {
                System.out.println((i + 1) + ". " + roles.get(i));
            }

            Integer roleIndex = UserInputManager.promptForIndexFromList(
                scanner,
                "Enter employee role number (or 'q' to cancel): ",
                roles.size(),
                "Assignment cancelled.",
                "q"
            );
            if (roleIndex == null) return;
            String roleName = roles.get(roleIndex);

            // Assign the employee to the shift
            assigningService.assignToShift(employeeId, selectedShift.getStartTime(), selectedShift.getShiftType().toString(), roleName,selectedShift.getSite());
            System.out.println("Employee assigned to shift successfully!");

            String response = UserInputManager.promptForString(
                scanner,
                "Assign more? (yes/no): ",
                "",
                "q"
            );
            if (response == null || response.equalsIgnoreCase("no")) {
                assignMore = false;
                System.out.println("Assignment process ended.");
            } else if (!response.equalsIgnoreCase("yes")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
        if(hasShiftsWithMissingWorkers()){
            handleMissingAvailability();
        }
    }

    public boolean hasShiftsWithMissingWorkers() {
        boolean hasMissingWorkersOverall = false;
        for (Site site : siteService.getAllSites()) {
            boolean hasMissingWorkers = shiftService.CheckIfThereAreShiftsThatAreNotAssigned(site, TODAY, NEXT_SUNDAY);
            if (hasMissingWorkers) {
                System.out.println("There are shifts with missing employees!");
                hasMissingWorkersOverall = true;
            } else {
                System.out.println("All shifts are fully staffed.");
            }
        }
        return hasMissingWorkersOverall;
    }


    private List<ShiftPL> getShiftsWithMissingWorkers() {
        List<ShiftPL> missingShifts = new ArrayList<>();
            for (Site site : siteService.getAllSites()) {
                List<ShiftPL> shiftsWithMissingWorkers = shiftService.getAllShiftsWithMissingAssigns(site, TODAY, NEXT_SUNDAY).stream()
                    .map(shiftSL -> new ShiftPL(shiftSL))
                    .toList();
                if (!shiftsWithMissingWorkers.isEmpty()) {
                    missingShifts.addAll(shiftsWithMissingWorkers);
                }
            }
        return missingShifts;
    }

    private void handleMissingAvailability() {
        List<ShiftPL> missingShifts = getShiftsWithMissingWorkers();
        List<EmployeePL> allEmployees = employeeService.getAllEmployees().values().stream().map(EmployeePL::new).toList();
        List<EmployeePL> unassignedEmployees = new ArrayList<>();
        for (EmployeePL employee : allEmployees) {
            boolean assigned = false;
            for (ShiftPL shift : missingShifts) {
                for (List<EmployeePL> emps : shift.getEmployeesAssignment().values()) {
                    if (emps.stream().anyMatch(e -> e.getID().equals(employee.getID()))) {
                        assigned = true;
                        break;
                    }
                }
                if (assigned) break;
            }
            if (!assigned) {
                unassignedEmployees.add(employee);
            }
        }
        if (unassignedEmployees.isEmpty()) {
            System.out.println("There are shifts with missing workers, but no unassigned employees are available.");
        } else {
            System.out.println("The following employees are not assigned to the missing shifts:");
            for (EmployeePL emp : unassignedEmployees) {
                System.out.println("- " + emp.getFullName() + " (ID: " + emp.getID() + ")");
            }
            System.out.println("Please assign these employees to the shifts with missing workers.");
            assignEmployeeToShift();
        }
    }
}
