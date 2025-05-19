package presentationLayer;

import java.util.Scanner;

public class UserInputManager {

    public static String getUserInputOrCancel(Scanner scanner, String cancelMessage, String cancelKeyword) {
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase(cancelKeyword)) {
            if (cancelMessage != null && !cancelMessage.isEmpty()) {
                System.out.println(cancelMessage);
            }
            return null;
        }
        return input;
    }

    public static String getUserInput(Scanner scanner) {
        return scanner.nextLine().trim();
    }

    public static String promptForString(Scanner scanner, String prompt, String cancelMessage, String cancelKeyword) {
        System.out.print(prompt);
        String input = getUserInputOrCancel(scanner, cancelMessage, cancelKeyword);
        if (input == null || input.isEmpty()) {
            System.out.println("Field cannot be empty. " + cancelMessage);
            return null;
        }
        return input;
    }

    public static Integer promptForInt(Scanner scanner, String prompt, String cancelMessage, String cancelKeyword) {
        while (true) {
            System.out.print(prompt);
            String input = getUserInputOrCancel(scanner, cancelMessage, cancelKeyword);
            if (input == null) return null;
            if (input.isEmpty()) {
                System.out.println("Field cannot be empty.");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or '" + cancelKeyword + "' to cancel.");
            }
        }
    }

    public static String promptForWageType(Scanner scanner, String cancelMessage, String cancelKeyword) {
        while (true) {
            System.out.print("Wage Type: (enter 1 for Hourly, 2 for Monthly, or '" + cancelKeyword + "' to cancel): ");
            String input = getUserInputOrCancel(scanner, cancelMessage, cancelKeyword);
            if (input == null) return null;
            if (input.equals("1")) return "Hourly";
            if (input.equals("2")) return "Monthly";
            System.out.println("Invalid input. Please enter 1 for Hourly, 2 for Monthly, or '" + cancelKeyword + "' to cancel.");
        }
    }

    public static Integer promptForIndexFromList(Scanner scanner, String prompt, int listSize, String cancelMessage, String cancelKeyword) {
        while (true) {
            System.out.print(prompt);
            String input = getUserInputOrCancel(scanner, cancelMessage, cancelKeyword);
            if (input == null) return null;
            if (input.isEmpty()) {
                System.out.println("Field cannot be empty.");
                continue;
            }
            try {
                int index = Integer.parseInt(input);
                if (index >= 1 && index <= listSize) {
                    return index - 1; // מחזיר אינדקס של הרשימה (0-based)
                } else {
                    System.out.println("Please enter a number between 1 and " + listSize + ", or '" + cancelKeyword + "' to cancel.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number or '" + cancelKeyword + "' to cancel.");
            }
        }
    }
}