package presentationLayer;

import java.util.List;
import java.util.Scanner;

import domainLayer.ShiftDL;
import serviceLayer.AssigningService;
import serviceLayer.ShiftSL;
import serviceLayer.ShiftService;

public class ShiftsTablePresentation {
    private ShiftService shiftService;
    private AssigningService assigningService;
    private Scanner scanner;

    public ShiftsTablePresentation(ShiftService service, Scanner scanner, AssigningService assigningService) {
        this.assigningService = assigningService;
        this.shiftService = service;
        this.scanner = scanner;
    }

    public void showShiftTable() {
        System.out.println("=== Weekly Shift Table ===");
        List<ShiftPL> shifts = shiftService.getAllShift().stream()
                                           .map(shiftSL -> new ShiftPL(shiftSL))
                                           .toList();

        if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }

        for (ShiftPL shift : shifts) {
            System.out.println("-----------------------------------");
            System.out.println("Date: " + shift.getDate());
            System.out.println("Shift Type: " + shift.getShiftType());
            System.out.println("Assigned Employees:");
            shift.getEmployeesAssignment().forEach((role, employees) -> {
                System.out.println("  Role: " + role.getName());
                employees.forEach(employee -> System.out.println("    - " + employee.getFullName() + " (ID: " + employee.getID() + ")"));
            });
            System.out.println("Available Employees:");
            shiftService.getAvailableEmployeesForShift(shift.getDate(), shift.getShiftType()).forEach(employee -> System.out.println("  - " + employee.getFullName() + " (ID: " + employee.getId() + ")"));
            System.out.println("-----------------------------------");
        }
    }

   // we need to get the shift date and time from the shift number

    public void assignEmployeeToShift() {
        boolean assignMore = true;

        while (assignMore) {
            System.out.print("Enter employee ID: ");
            String employeeId = scanner.nextLine();
            System.out.print("Enter shift number: ");
            int shiftNumber = scanner.nextInt();
            scanner.nextLine(); // Clear the buffer

            // Assign the employee to the shift
            // assigningService.assignToShift(employeeId, shiftNumber); // Uncomment when the method is implemented
            System.out.println("Employee assigned to shift successfully!");

            System.out.print("Assign more? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (response.equals("no")) {
                assignMore = false;
                System.out.println("Assignment process ended.");
            } else if (!response.equals("yes")) {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }

        // Check if there are shifts with missing workers
        hasShiftsWithMissingWorkers();
    }

    public boolean hasShiftsWithMissingWorkers() {
        boolean hasMissingWorkers = shiftService.checkForProblematicShifts();
        if (hasMissingWorkers) {
            System.out.println("There are shifts with missing workers.");
        } else {
            System.out.println("All shifts are fully staffed.");
        }
        return hasMissingWorkers;
    }
}
