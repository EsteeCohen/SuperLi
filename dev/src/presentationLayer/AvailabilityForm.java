package src.presentationLayer;

import java.util.List;
import java.util.Scanner;

import src.serviceLayer.HRSystemUIService;

public class AvailabilityForm {
    private HRSystemUIService hrService;
    private Scanner scanner;

    public AvailabilityForm(HRSystemUIService service, Scanner scanner) {
        this.hrService = service;
        this.scanner = scanner;
    }

    public void showAvailabilityForm(String employeeId) {
        System.out.println("=== availabillity form ===");

        // Fetch and display the list of work times
        List<String> workTimes = hrService.getWorkTimes();
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
                String selectedTime = workTimes.get(index);
                hrService.setAvailability(employeeId, selectedTime);
            } else {
                System.out.println("incorrect number " + number);
            }
        }

        System.out.println("Availability has been set successfully!");
    }
}
