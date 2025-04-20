package presentationLayer;

import java.util.Scanner;

import serviceLayer.AssigningService;
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
        System.out.println("=== weekly shift table ===");
        System.out.println(shiftService.getAllShift());
    }

    // we need to get the shift date and time from the shift number

    // public void assignEmployeeToShift() {
    //     boolean assignMore = true;

    //     while (assignMore) {
    //         System.out.print("enter employee ID: ");
    //         String employeeId = scanner.nextLine();
    //         System.out.print("enter shift number: ");
    //         int shiftNumber = scanner.nextInt();
    //         scanner.nextLine(); // Clear the buffer

    //         assigningService.assignToShift(employeeId, shiftNumber); // we need to get the shift date and time from the shift number
    //         System.out.println("Employee assigned to shift successfully!");

    //         System.out.print("Assign more? (yes/no): ");
    //         String response = scanner.nextLine().trim().toLowerCase();
    //         assignMore = response.equals("yes");
    //     }

    //     // Check if there are shifts with missing workers
    //     hasShiftsWithMissingWorkers();
    // }

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
