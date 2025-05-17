package src.main.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import src.main.controllers.TransportController;
import src.main.entities.Site;
import src.main.entities.Transport;
import src.main.enums.ShippingZone;
import src.main.enums.TransportStatus;

public class TransportUI {
    private Scanner scanner;
    private TransportController transportController;
    
    // Constructor
    public TransportUI(TransportController transportController) {
        this.scanner = new Scanner(System.in);
        this.transportController = transportController;
    }
    
    // Methods
    public void start() {
        boolean exit = false;

        while (!exit) {
            displayMenu();
            int choice = getIntInput("Select an option: ");

            switch (choice) {
                case 1:
                    createTransport();
                    break;
                case 2:
                    viewTransport();
                    break;
                case 3:
                    viewAllTransports();
                    break;
                case 4:
                    viewTransportsByDate();
                    break;
                case 5:
                    viewTransportsByStatus();
                    break;
                case 6:
                    viewTransportsByZone();
                    break;
                case 7:
                    updateTransportsStatus();
                    break;
                case 8:
                    cancelTransport();
                    break;
                case 9:
                    changeDriver();
                    break;
                case 10:
                    changeTruck();
                    break;
                case 11:
                    addDestination();
                    break;
                case 12:
                    removeDestination();
                    break;
                case 0:
                    System.out.println("Returning to main menu...");
                    exit = true;
                    break;
                default:
                    System.out.println("⚠ Invalid selection, please try again.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n===== Transport Management =====");
        System.out.println("1. Create New Transport");
        System.out.println("2. View Transport");
        System.out.println("3. View All Transports");
        System.out.println("4. View by Date");
        System.out.println("5. View by Status");
        System.out.println("6. View by Shipping Zone");
        System.out.println("7. Update Status");
        System.out.println("8. Cancel Transport");
        System.out.println("9. Change Driver");
        System.out.println("10. Change Truck");
        System.out.println("11. Add Destination");
        System.out.println("12. Remove Destination");
        System.out.println("0. Return");
    }

    // Input helpers
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("⚠ Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clean buffer
        return input;
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private void createTransport() {
        // 1. Date
        String dateStr = getStringInput("Enter date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            if (date.isBefore(LocalDate.now())) {
                System.out.println("⚠ Cannot select a date in the past.");
                return;
            }
        } catch (Exception e) {
            System.out.println("⚠ Invalid date format.");
            return;
        }

        // 2. Time
        String timeStr = getStringInput("Enter time (HH:mm): ");
        LocalTime time;
        try {
            time = LocalTime.parse(timeStr);
        } catch (Exception e) {
            System.out.println("⚠ Invalid time format.");
            return;
        }

        // 3. Truck
        String truckId = getStringInput("Enter truck license number: ");

        // 4. Driver
        String driverId = getStringInput("Enter driver ID: ");

        // 5. Source site
        String sourceSiteId = getStringInput("Enter source site ID: ");

        // 6. Destinations
        List<String> destinationIds = new ArrayList<>();
        boolean more = true;
        while (more) {
            String destId = getStringInput("Enter destination site ID: ");
            destinationIds.add(destId);

            String answer = getStringInput("Add another destination? (yes/no): ");
            more = answer.equalsIgnoreCase("yes");
        }

        // Create transport
        Transport transport = transportController.createTransport(date, time, truckId, driverId, sourceSiteId, destinationIds);
        if (transport != null) {
            System.out.println("Transport created successfully.");
        }
        else {
            System.out.println("Error creating transport.");
        }
    }

    private void viewTransport() {
        int id = getIntInput("Enter transport ID: ");
        Transport t = transportController.getTransportById(id);
        if (t != null) {
            System.out.println(t);
        } else {
            System.out.println("Transport not found.");
        }
    }

    private void viewAllTransports() {
        List<Transport> transports = transportController.getAllTransports();
        if (transports.isEmpty()) {
            System.out.println("No transports in the system.");
        } else {
            System.out.println("\n--- List of all transports ---");
            for (Transport t : transports) {
                System.out.println(t);
                System.out.println("------------------------");
            }
        }
    }

    private void viewTransportsByDate() {
        String dateStr = getStringInput("Enter date (yyyy-MM-dd): ");
        LocalDate date;
        try {
            date = LocalDate.parse(dateStr);
            List<Transport> transports = transportController.getTransportsByDate(date);
            if (transports.isEmpty()) {
                System.out.println("No transports on this date.");
            } else {
                System.out.println("\n--- Transports on " + date + " ---");
                for (Transport t : transports) {
                    System.out.println(t);
                    System.out.println("------------------------");
                }
            }
        } catch (Exception e) {
            System.out.println("⚠ Invalid date format.");
        }
    }

    private void viewTransportsByStatus() {
        System.out.println("Select status:");
        for (int i = 0; i < TransportStatus.values().length; i++) {
            System.out.println((i + 1) + ". " + TransportStatus.values()[i]);
        }
        
        int statusChoice = getIntInput("Your choice: ");
        if (statusChoice < 1 || statusChoice > TransportStatus.values().length) {
            System.out.println("⚠ Invalid status selection.");
            return;
        }
        
        TransportStatus status = TransportStatus.values()[statusChoice - 1];
        List<Transport> transports = transportController.getTransportsByStatus(status);
        
        if (transports.isEmpty()) {
            System.out.println("No transports with status " + status);
        } else {
            System.out.println("\n--- Transports with status " + status + " ---");
            for (Transport t : transports) {
                System.out.println(t);
                System.out.println("------------------------");
            }
        }
    }

    private void viewTransportsByZone() {
        System.out.println("Select shipping zone:");
        for (int i = 0; i < ShippingZone.values().length; i++) {
            System.out.println((i + 1) + ". " + ShippingZone.values()[i]);
        }
        
        int zoneChoice = getIntInput("Your choice: ");
        if (zoneChoice < 1 || zoneChoice > ShippingZone.values().length) {
            System.out.println("⚠ Invalid zone selection.");
            return;
        }
        
        ShippingZone zone = ShippingZone.values()[zoneChoice - 1];
        List<Transport> transports = transportController.getTransportsByZone(zone);
        
        if (transports.isEmpty()) {
            System.out.println("No transports in zone " + zone);
        } else {
            System.out.println("\n--- Transports in zone " + zone + " ---");
            for (Transport t : transports) {
                System.out.println(t);
                System.out.println("------------------------");
            }
        }
    }

    private void updateTransportsStatus() {
        int id = getIntInput("Enter transport ID: ");
        Transport t = transportController.getTransportById(id);
        
        if (t == null) {
            System.out.println("Transport not found.");
            return;
        }
        
        System.out.println("Current status: " + t.getStatus());
        System.out.println("Select new status:");
        
        TransportStatus[] availableStatuses = t.getStatus().getNextPossibleStatuses();
        
        if (availableStatuses.length == 0) {
            System.out.println("No status changes available for this transport.");
            return;
        }
        
        for (int i = 0; i < availableStatuses.length; i++) {
            System.out.println((i + 1) + ". " + availableStatuses[i]);
        }
        
        int statusChoice = getIntInput("Your choice: ");
        if (statusChoice < 1 || statusChoice > availableStatuses.length) {
            System.out.println("⚠ Invalid status selection.");
            return;
        }
        
        TransportStatus newStatus = availableStatuses[statusChoice - 1];
        boolean success = transportController.updateTransportStatus(id, newStatus);
        
        if (success) {
            System.out.println("Status updated successfully.");
        } else {
            System.out.println("Error updating status.");
        }
    }

    private void changeDriver() {
        int transportId = getIntInput("Enter transport ID: ");
        String newDriverId = getStringInput("Enter new driver ID: ");
        
        boolean success = transportController.changeDriver(transportId, newDriverId);
        if (success) {
            System.out.println("Driver changed successfully.");
        } else {
            System.out.println("Error changing driver. Either the transport or driver does not exist, or the transport is not in a suitable status for modification.");
        }
    }

    private void changeTruck() {
        int transportId = getIntInput("Enter transport ID: ");
        String newTruckId = getStringInput("Enter new truck license number: ");
        
        boolean success = transportController.changeTruck(transportId, newTruckId);
        if (success) {
            System.out.println("Truck changed successfully.");
        } else {
            System.out.println("Error changing truck. Either the transport or truck does not exist, or the transport is not in a suitable status for modification.");
        }
    }

    private void addDestination() {
        int transportId = getIntInput("Enter transport ID: ");
        String siteId = getStringInput("Enter destination site ID to add: ");
        
        boolean success = transportController.addDestination(transportId, siteId);
        if (success) {
            System.out.println("Destination added successfully.");
        } else {
            System.out.println("Error adding destination. Either the transport or site does not exist, or the transport is not in a suitable status for modification.");
        }
    }

    private void removeDestination() {
        int transportId = getIntInput("Enter transport ID: ");
        Transport t = transportController.getTransportById(transportId);
        
        if (t == null) {
            System.out.println("Transport not found.");
            return;
        }
        
        List<Site> destinations = t.getDestinations();
        if (destinations == null || destinations.isEmpty()) {
            System.out.println("This transport has no destinations to remove.");
            return;
        }
        
        System.out.println("Current destinations:");
        for (int i = 0; i < destinations.size(); i++) {
            System.out.println((i + 1) + ". " + destinations.get(i).getName() + " (ID: " + destinations.get(i).getId() + ")");
        }
        
        int destChoice = getIntInput("Select destination to remove (number): ");
        if (destChoice < 1 || destChoice > destinations.size()) {
            System.out.println("⚠ Invalid selection.");
            return;
        }
        
        String siteId = destinations.get(destChoice - 1).getId();
        boolean success = transportController.removeDestination(transportId, siteId);
        
        if (success) {
            System.out.println("Destination removed successfully.");
        } else {
            System.out.println("Error removing destination. The transport may not be in a suitable status for modification.");
        }
    }

    private void cancelTransport() {
        int id = getIntInput("Enter transport ID to cancel: ");
        Transport t = transportController.getTransportById(id);
        
        if (t == null) {
            System.out.println("Transport not found.");
            return;
        }
        
        if (t.getStatus() == TransportStatus.CANCELLED || t.getStatus() == TransportStatus.COMPLETED) {
            System.out.println("Cannot cancel a transport that is already " + t.getStatus());
            return;
        }
        
        System.out.println("Are you sure you want to cancel transport #" + id + "? (yes/no)");
        String confirmation = getStringInput("Confirm: ");
        
        if (confirmation.equalsIgnoreCase("yes")) {
            boolean success = transportController.cancelTransport(id);
            if (success) {
                System.out.println("Transport cancelled successfully.");
            } else {
                System.out.println("Error cancelling transport.");
            }
        } else {
            System.out.println("Cancellation aborted.");
        }
    }
}
