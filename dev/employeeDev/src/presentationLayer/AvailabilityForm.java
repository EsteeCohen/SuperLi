package presentationLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

import serviceLayer.ShiftService;

public class AvailabilityForm extends Form {
    private ShiftService shiftService;
    private Scanner scanner;

    public AvailabilityForm(Scanner scanner, ShiftService shiftService) {
        super("Availability Form");
        this.shiftService = shiftService;
        this.scanner = scanner;
    }

    public void showAvailabilityForm(String employeeId) {
        LocalDate today = LocalDate.now();
        LocalDate nextSunday = today.plusDays(7 - today.getDayOfWeek().getValue() % 7);

        ArrayList<ShiftPL> workTimes = new ArrayList<>(shiftService.getWeeklyShifts(nextSunday).stream()
            .map(shiftSL -> new ShiftPL(shiftSL))
            .toList());
        workTimes.sort(Comparator.comparing(ShiftPL::getStartTime, Comparator.naturalOrder()));

        if (workTimes.isEmpty()) {
            System.out.println("No shifts available.");
            return;
        }

        printShiftsList(workTimes);

        String input = getSelectedShiftsInput(workTimes);
        if (input == null) return;

        boolean anyValid = processSelectedShifts(input, workTimes, employeeId);

        if (anyValid) {
            System.out.println("Availability has been set successfully!");
        } else {
            System.out.println("No valid shifts selected. Availability not updated.");
        }
    }

    private void printShiftsList(ArrayList<ShiftPL> workTimes) {
        System.out.println("Shifts:");
        for (int i = 0; i < workTimes.size(); i++) {
            System.out.println((i + 1) + ". " + workTimes.get(i).toStringForAvailabilityForm());
        }
    }

    private boolean processSelectedShifts(String input, ArrayList<ShiftPL> workTimes, String employeeId) {
        boolean anyValid = false;
        for (String number : parseInputNumbers(input)) {
            try {
                int selectedIndex = Integer.parseInt(number) - 1;
                if (selectedIndex >= 0 && selectedIndex < workTimes.size()) {
                    ShiftPL selectedShift = workTimes.get(selectedIndex);
                    // Use startTime and shiftType for availability
                    shiftService.setAvailabilityOfEmployeeToShift(
                        employeeId,
                        selectedShift.getStartTime(),
                        selectedShift.getShiftType().toString(),
                        true
                    );
                    anyValid = true;
                } else {
                    System.out.println("Incorrect number: " + number);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + number);
            }
        }
        return anyValid;
    }

    private String[] parseInputNumbers(String input) {
        return input.trim().split("\\s+");
    }

    private String getSelectedShiftsInput(ArrayList<ShiftPL> workTimes) {
        System.out.println("Choose your work times: (numbers separated by space, or type 'q' to cancel)");
        return UserInputManager.getUserInputOrCancel(scanner, "Operation cancelled.", "q");
    }
}
