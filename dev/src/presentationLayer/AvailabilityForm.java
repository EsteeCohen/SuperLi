package presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
        System.out.println("=== availability form ===");

        // Calculate the date of the next Sunday
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = today.plusDays(7 - today.getDayOfWeek().getValue() % 7);

        // Fetch and display the list of work times starting from next Sunday
        ArrayList<ShiftPL> workTimes = new ArrayList<>(shiftService.getWeeklyShifts(nextSunday).stream()
            .map(shiftSL -> new ShiftPL(shiftSL))
            .toList());
        workTimes.sort(Comparator.comparing(ShiftPL::getDate, Comparator.naturalOrder()));
        System.out.println("shifts:");
        for (int i = 0; i < workTimes.size(); i++) {
            System.out.println((i + 1) + ". " + workTimes.get(i).toStringForAvailabilityForm());
        }

        // Get user input for selected work times
        System.out.println("choose your work times: (numbers separated by space)");
        String input = scanner.nextLine();
        String[] selectedNumbers = input.split(" ");

        // Convert selected numbers to work times
        for (String number : selectedNumbers) {
            int index = Integer.parseInt(number.trim()) - 1;
            if (index >= 0 && index < workTimes.size()) {
                LocalDate shiftDate = workTimes.get(index).getDate();
                String shiftType = workTimes.get(index).getShiftType().toString();
                boolean isAvailable = true; // Assuming availability is true by default
                shiftService.setAvailabilityOfEmployeeToShift(employeeId, shiftDate, shiftType, isAvailable);
            } else {
                System.out.println("incorrect number " + number);
            }
        }

        System.out.println("Availability has been set successfully!");
    }
}
