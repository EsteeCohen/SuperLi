package presentationLayer;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import serviceLayer.ShiftService;

public class AvailabilityForm {
    private ShiftService shiftService;
    private Scanner scanner;

    public AvailabilityForm(Scanner scanner, ShiftService shiftService) {
        this.shiftService = shiftService;
        this.scanner = scanner;
    }

    public void showAvailabilityForm(String employeeId) {
        System.out.println("=== availabillity form ===");

        // Fetch and display the list of work times
        List<ShiftPL> workTimes = shiftService.getAllShift().stream()
            .map(shiftSL -> new ShiftPL(shiftSL))
            .toList();
        System.out.println("shifts:");
        for (int i = 0; i < workTimes.size(); i++) {
            System.out.println((i + 1) + ". " + workTimes.get(i));
        }

        // Get user input for selected work times
        System.out.println("choose your work times: (numbers separated by space')");
        String input = scanner.nextLine();
        String[] selectedNumbers = input.split(" ");

        // Convert selected numbers to work times
        for (String number : selectedNumbers) {
            int index = Integer.parseInt(number.trim()) - 1;
            if (index >= 0 && index < workTimes.size()) {
                String selectedTime = workTimes.get(index).toString();
                // Assuming selectedTime contains the shift details in a format that can be parsed
                LocalDate shiftDate = LocalDate.parse(selectedTime.split(" ")[0]); // Extract date from selectedTime
                String shiftType = selectedTime.split(" ")[1]; // Extract shift type from selectedTime
                boolean isAvailable = true; // Assuming availability is true by default
                shiftService.setAvailabilityOfEmployeetoShift(employeeId, shiftDate, shiftType, isAvailable);
            } else {
                System.out.println("incorrect number " + number);
            }
        }

        System.out.println("Availability has been set successfully!");
    }
}
