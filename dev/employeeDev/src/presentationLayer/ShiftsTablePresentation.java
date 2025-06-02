package employeeDev.src.presentationLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import transportDev.src.main.services.DriverService;
import employeeDev.src.serviceLayer.AssigningService;
import employeeDev.src.serviceLayer.EmployeeService;
import employeeDev.src.serviceLayer.ShiftService;

public class ShiftsTablePresentation extends Form {
    private ShiftService shiftService;
    private EmployeeService employeeService;
    private AssigningService assigningService;
    private DriverService driverService;
    private Scanner scanner;
    private ArrayList<ShiftPL> shifts;

    public ShiftsTablePresentation(ShiftService service, Scanner scanner, AssigningService assigningService, EmployeeService employeeService, DriverService driverService) {
        super("Weekly Shift Table");
        this.assigningService = assigningService;
        this.employeeService = employeeService;
        this.shiftService = service;
        this.scanner = scanner;
        shifts = new ArrayList<>(shiftService.getAllShift().stream()
                                             .map(shiftSL -> new ShiftPL(shiftSL))
                                             .toList());
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
        this.driverService = driverService;
    }

    public void showShiftTable() {
        if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }

        Map<String, List<ShiftPL>> siteToShifts = groupShiftsBySite();
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
                shiftService.getAvailableEmployeesForShift(shift.getStartTime(), shift.getShiftType().toString()).forEach(employee -> {
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
                            if (role.getName() == "Driver"){
                                driverService.getDriverById(employee.getId())
                                    .ifPresent(driver -> System.out.println("    - " + role.toString() + " (License: " + driver.getLicenseType() + ")"));
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
            assigningService.assignToShift(employeeId, selectedShift.getStartTime(), selectedShift.getShiftType().toString(), roleName);
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
        hasShiftsWithMissingWorkers();
    }

    public boolean hasShiftsWithMissingWorkers() {
        boolean hasMissingWorkers = shiftService.CheckIfThereAreShiftsThatAreNotAssigned();
        if (hasMissingWorkers) {
            System.out.println("There are shifts with missing employees!");
        } else {
            System.out.println("All shifts are fully staffed.");
        }
        return hasMissingWorkers;
    }

    private boolean hasMissingWorkers() {
        boolean hasMissingWorkers = false;
        for (ShiftPL shift : shifts) {
            Map<RolePL, Integer> requirements = shift.getShiftRoleRequirements();
            Map<RolePL, List<EmployeePL>> assignments = shift.getEmployeesAssignment();
            for (Map.Entry<RolePL, Integer> entry : requirements.entrySet()) {
                int required = entry.getValue();
                int assigned = assignments.getOrDefault(entry.getKey(), new ArrayList<>()).size();
                if (assigned < required) {
                    hasMissingWorkers = true;
                    break;
                }
            }
            if (hasMissingWorkers) break;
        }
        return hasMissingWorkers;
    }

    private void handleMissingAvailability() {
        if (hasMissingWorkers()) {
            List<EmployeePL> allEmployees = employeeService.getAllEmployees().values().stream().map(EmployeePL::new).toList();
            List<EmployeePL> unassignedEmployees = new ArrayList<>();
            for (EmployeePL employee : allEmployees) {
                boolean assigned = false;
                for (ShiftPL shift : shifts) {
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
                System.out.println("The following employees are not assigned to any shift:");
                for (EmployeePL emp : unassignedEmployees) {
                    System.out.println("- " + emp.getFullName() + " (ID: " + emp.getID() + ")");
                }
                System.out.println("Please assign these employees to the shifts with missing workers.");
                assignEmployeeToShift();
            }
        } else {
            System.out.println("No shifts with missing workers found.");
        }
    }
}
