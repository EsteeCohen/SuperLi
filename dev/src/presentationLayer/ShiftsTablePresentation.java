package presentationLayer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import serviceLayer.AssigningService;
import serviceLayer.EmployeeService;
import serviceLayer.ShiftService;

public class ShiftsTablePresentation {
    private ShiftService shiftService;
    private EmployeeService employeeService;
    private AssigningService assigningService;
    private Scanner scanner;
    private ArrayList<ShiftPL> shifts;

    public ShiftsTablePresentation(ShiftService service, Scanner scanner, AssigningService assigningService, EmployeeService employeeService) {
        this.assigningService = assigningService;
        this.employeeService = employeeService;
        this.shiftService = service;
        this.scanner = scanner;
        shifts = new ArrayList<>(shiftService.getAllShift().stream()
                                             .map(shiftSL -> new ShiftPL(shiftSL))
                                             .toList());
        shifts.sort(Comparator.comparing(ShiftPL::getDate, Comparator.naturalOrder()));
    }

    public void showShiftTable() {
        System.out.println("=== Weekly Shift Table ===");

        if (shifts.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }

        for (ShiftPL shift : shifts) {
            System.out.println("-----------------------------------");
            System.out.println("Shift Number: " + (shifts.indexOf(shift) + 1)); // Display shift number
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
            System.out.print("Enter shift number: ");
            int shiftNumber = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character
            ShiftPL selectedShift = shifts.get(shiftNumber - 1); // Assuming shiftNumber is 1-based index
            System.out.print("Enter employee ID: ");
            String employeeId = scanner.nextLine();
            EmployeePL employee =new EmployeePL(employeeService.getEmployeeById(employeeId));
            List<String> roles = employee.getRoles();
            System.out.println("Available roles for the employee: ");
            for (int i = 0; i < roles.size(); i++) {
                System.out.println((i + 1) + ". " + roles.get(i));
            }
            System.out.print("Enter employee role of the List: ");
            String roleName = roles.get(scanner.nextInt() - 1);
            scanner.nextLine(); // Consume the newline character

            // Assign the employee to the shift
            assigningService.assignToShift(employeeId, selectedShift.getDate(), selectedShift.getShiftType().toString(), roleName); // we need to get the shift date and time from the shift number
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
        boolean hasMissingWorkers = shiftService.CheckIfThereAreShiftsThatAreNotAssigned();
        if (hasMissingWorkers) {
            System.out.println("There are shifts with missing employees!");
        } else {
            System.out.println("All shifts are fully staffed.");
        }
        return hasMissingWorkers;
    }
}
