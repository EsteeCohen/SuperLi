package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.FacadeController;
import src.main.entities.Truck;
import src.main.entities.Driver;
import src.main.entities.User;

public class FleetUI {
    private Scanner scanner;
    private FacadeController facadeController;
    private String sessionId;
    
    // Fleet Management UI constructor
    public FleetUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
    }
    
    // Start Fleet Management interface
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            displayMainMenu();
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    if (canManageTrucks()) {
                        truckMenu();
                    } else {
                        showAccessDenied("to manage trucks");
                    }
                    break;
                case 2:
                    if (canManageDrivers()) {
                        driverMenu();
                    } else {
                        showAccessDenied("to manage drivers");
                    }
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }
    
    // Display main Fleet Management menu
    private void displayMainMenu() {
        System.out.println("\n=== Fleet Management ===");
        System.out.println("1. Truck Management");
        System.out.println("2. Driver Management");
        System.out.println("0. Return to Main Menu");
    }
    
    // Display Truck Management menu
    private void truckMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== Truck Management ===");
            System.out.println("1. Add New Truck");
            System.out.println("2. View Truck");
            System.out.println("3. View All Trucks");
            System.out.println("4. Search Trucks by License Type");
            System.out.println("0. Return to Previous Menu");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    if (facadeController.isAuthorized(sessionId, "CREATE", "TRUCK")) {
                        addNewTruck();
                    } else {
                        showAccessDenied("to add a truck");
                    }
                    break;
                case 2:
                    viewTruck();
                    break;
                case 3:
                    viewAllTrucks();
                    break;
                case 4:
                    searchTrucksByLicenseType();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }
    
    // Display Driver Management menu
    private void driverMenu() {
        boolean exit = false;
        
        while (!exit) {
            System.out.println("\n=== Driver Management ===");
            System.out.println("1. Add New Driver");
            System.out.println("2. View Driver");
            System.out.println("3. View All Drivers");
            System.out.println("4. Search Drivers by License Type");
            System.out.println("0. Return to Previous Menu");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    if (facadeController.isAuthorized(sessionId, "CREATE", "DRIVER")) {
                        addNewDriver();
                    } else {
                        showAccessDenied("to add a driver");
                    }
                    break;
                case 2:
                    viewDriver();
                    break;
                case 3:
                    viewAllDrivers();
                    break;
                case 4:
                    searchDriversByLicenseType();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }
    
    // Add a new truck
    private void addNewTruck() {
        System.out.println("\n=== Add New Truck ===");
        
        String regNumber = getStringInput("Enter registration number: ");
        String model = getStringInput("Enter model: ");
        
        double emptyWeight = getDoubleInput("Enter empty weight (kg): ");
        double maxWeight = getDoubleInput("Enter maximum allowed weight (kg): ");
        
        System.out.println("Select required license type:");
        System.out.println("1. C1 - License for trucks up to 12 tons");
        System.out.println("2. C - License for trucks up to 15 tons");
        System.out.println("3. CE - License for articulated/combined vehicles");
        System.out.println("4. C1E - License for light trucks with trailer");
        
        int licenseChoice = getIntInput("Your choice: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("Invalid selection. Setting default: C");
                licenseType = "C";
        }
        
        Truck newTruck = facadeController.addTruck(regNumber, model, String.valueOf(emptyWeight), String.valueOf(maxWeight), licenseType);
        
        if (newTruck != null) {
            System.out.println("Truck added successfully!");
            displayTruckDetails(newTruck);
        } else {
            System.out.println("Error adding truck. Please check the input values and try again.");
        }
    }
    
    private void viewTruck() {
        String regNumber = getStringInput("Enter truck registration number: ");
        List<Truck> trucks = facadeController.getAllTrucks();
        boolean found = false;
        
        for (Truck truck : trucks) {
            if (truck.getRegNumber().equals(regNumber)) {
                displayTruckDetails(truck);
                found = true;
                break;
            }
        }
        
        if (!found) {
            System.out.println("Truck not found.");
        }
    }
    
    private void viewAllTrucks() {
        List<Truck> trucks = facadeController.getAllTrucks();
        
        if (trucks.isEmpty()) {
            System.out.println("No trucks in the system.");
        } else {
            System.out.println("\n=== All Trucks ===");
            for (Truck truck : trucks) {
                displayTruckDetails(truck);
                System.out.println("------------------------");
            }
        }
    }
    
    private void searchTrucksByLicenseType() {
        System.out.println("Select license type to search for:");
        System.out.println("1. C1 - License for trucks up to 12 tons");
        System.out.println("2. C - License for trucks up to 15 tons");
        System.out.println("3. CE - License for articulated/combined vehicles");
        System.out.println("4. C1E - License for light trucks with trailer");
        
        int choice = getIntInput("Your choice: ");
        String licenseType;
        
        switch (choice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("Invalid selection.");
                return;
        }
        
        List<Truck> trucks = facadeController.getAllTrucks();
        boolean found = false;
        
        System.out.println("\n=== Trucks requiring " + licenseType + " license ===");
        for (Truck truck : trucks) {
            if (truck.getLicenseType().toString().equals(licenseType)) {
                displayTruckDetails(truck);
                System.out.println("------------------------");
                found = true;
            }
        }
        
        if (!found) {
            System.out.println("No trucks found requiring " + licenseType + " license.");
        }
    }
    
    private void addNewDriver() {
        System.out.println("\n=== Add New Driver ===");
        
        String id = getStringInput("Enter driver ID: ");
        String name = getStringInput("Enter full name: ");
        String phone = getStringInput("Enter phone number: ");
        
        System.out.println("Select driver's license type:");
        System.out.println("1. C1 - License for trucks up to 12 tons");
        System.out.println("2. C - License for trucks up to 15 tons");
        System.out.println("3. CE - License for articulated/combined vehicles");
        System.out.println("4. C1E - License for light trucks with trailer");
        
        int licenseChoice = getIntInput("Your choice: ");
        String licenseType;
        
        switch (licenseChoice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("Invalid selection. Setting default: C");
                licenseType = "C";
        }
        
        Driver newDriver = facadeController.addDriver(id, name, phone, licenseType);
        
        if (newDriver != null) {
            System.out.println("Driver added successfully!");
            displayDriverDetails(newDriver);
        } else {
            System.out.println("Error adding driver. Please check the input values and try again.");
        }
    }
    
    private void viewDriver() {
        String id = getStringInput("Enter driver ID: ");
        Driver driver = facadeController.getDriverById(id);
        
        if (driver != null) {
            displayDriverDetails(driver);
        } else {
            System.out.println("Driver not found.");
        }
    }
    
    private void viewAllDrivers() {
        List<Driver> drivers = facadeController.getAllDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("No drivers in the system.");
        } else {
            System.out.println("\n=== All Drivers ===");
            for (Driver driver : drivers) {
                displayDriverDetails(driver);
                System.out.println("------------------------");
            }
        }
    }
    
    private void searchDriversByLicenseType() {
        System.out.println("Select license type to search for:");
        System.out.println("1. C1 - License for trucks up to 12 tons");
        System.out.println("2. C - License for trucks up to 15 tons");
        System.out.println("3. CE - License for articulated/combined vehicles");
        System.out.println("4. C1E - License for light trucks with trailer");
        
        int choice = getIntInput("Your choice: ");
        String licenseType;
        
        switch (choice) {
            case 1:
                licenseType = "C1";
                break;
            case 2:
                licenseType = "C";
                break;
            case 3:
                licenseType = "CE";
                break;
            case 4:
                licenseType = "C1E";
                break;
            default:
                System.out.println("Invalid selection.");
                return;
        }
        
        List<Driver> drivers = facadeController.getDriversByLicenseType(licenseType);
        
        if (drivers == null || drivers.isEmpty()) {
            System.out.println("No drivers found with " + licenseType + " license.");
        } else {
            System.out.println("\n=== Drivers with " + licenseType + " license ===");
            for (Driver driver : drivers) {
                displayDriverDetails(driver);
                System.out.println("------------------------");
            }
        }
    }
    
    private void displayTruckDetails(Truck truck) {
        System.out.println("Registration Number: " + truck.getRegNumber());
        System.out.println("Model: " + truck.getModel());
        System.out.println("Empty Weight: " + truck.getEmptyWeight() + " kg");
        System.out.println("Maximum Weight: " + truck.getMaxWeight() + " kg");
        System.out.println("Required License: " + truck.getLicenseType());
    }
    
    private void displayDriverDetails(Driver driver) {
        System.out.println("ID: " + driver.getId());
        System.out.println("Name: " + driver.getName());
        System.out.println("Phone: " + driver.getPhone());
        System.out.println("License Type: " + driver.getLicenseType());
    }
    
    private boolean canManageTrucks() {
        return facadeController.isAuthorized(sessionId, "VIEW", "TRUCK");
    }
    
    private boolean canManageDrivers() {
        return facadeController.isAuthorized(sessionId, "VIEW", "DRIVER");
    }
    
    private void showAccessDenied(String action) {
        System.out.println("\nAccess Denied: You do not have permission " + action);
        System.out.println("Please contact your system administrator if you need this access.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private double getDoubleInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
    
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
