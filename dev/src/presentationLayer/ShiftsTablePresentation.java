package presentationLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import serviceLayer.AssigningService;
import serviceLayer.EmployeeService;
import serviceLayer.ShiftService;

public class ShiftsTablePresentation extends Form {
    private ShiftService shiftService;
    private EmployeeService employeeService;
    private AssigningService assigningService;
    private Scanner scanner;
    private ArrayList<ShiftPL> shifts;

    public ShiftsTablePresentation(ShiftService service, Scanner scanner, AssigningService assigningService, EmployeeService employeeService) {
        super("Weekly Shift Table");
        this.assigningService = assigningService;
        this.employeeService = employeeService;
        this.shiftService = service;
        this.scanner = scanner;
        shifts = new ArrayList<>(shiftService.getAllShift().stream()
                                             .map(shiftSL -> new ShiftPL(shiftSL))
                                             .toList());
        shifts.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));
    }

    public void showShiftTable() {
        if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }

        for (ShiftPL shift : shifts) {
            System.out.println("-----------------------------------");
            System.out.println("Shift Number: " + (shifts.indexOf(shift) + 1)); // Display shift number
            System.out.println("Start Time: " + shift.getStartTime());
            System.out.println("End Time: " + shift.getEndTime());
            System.out.println("Shift Type: " + shift.getShiftType());
            System.out.println("Assigned Employees:");
            shift.getEmployeesAssignment().forEach((role, employees) -> {
                System.out.println("  Role: " + role.getName());
                employees.forEach(employee -> System.out.println("    - " + employee.getFullName() + " (ID: " + employee.getID() + ")"));
            });
            System.out.println("Available Employees:");
            shiftService.getAvailableEmployeesForShift(shift.getStartTime(), shift.getShiftType().toString()).forEach(employee -> System.out.println("  - " + employee.getFullName() + " (ID: " + employee.getId() + ")"));
            System.out.println("-----------------------------------");
        }
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
}
