package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.DriverController;
import src.main.controllers.TruckController;
import src.main.controllers.UserController;
import src.main.entities.Truck;
import src.main.entities.Driver;
import src.main.entities.User;

public class FleetUI {
    private Scanner scanner;
    private TruckController truckController;
    private DriverController driverController;
    private UserController userController;
    private String sessionId;
    
    // Fleet Management UI constructor
    public FleetUI(TruckController truckController, DriverController driverController, UserController userController) {
        this.scanner = new Scanner(System.in);
        this.truckController = truckController;
        this.driverController = driverController;
        this.userController = userController;
    }
    
    // Start Fleet Management interface
    public void start() {
//        this.sessionId = sessionId;
        
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
            System.out.println("5. Search Trucks by Capacity");
            System.out.println("0. Return to Previous Menu");
            
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    if (userController.isAuthorized(sessionId, "CREATE", "TRUCK")) {
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
                case 5:
                    searchTrucksByCapacity();
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
                    if (userController.isAuthorized(sessionId, "CREATE", "DRIVER")) {
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
        
// ------------- Missing string conversion!!! ---------------

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
        
        Truck newTruck = truckController.addTruck(regNumber, model, ""+emptyWeight, ""+maxWeight, licenseType);
        
        if (newTruck != null) {
            // Truck added successfully
            System.out.println("Truck added successfully!");
        } else {
            System.out.println("Error adding truck. Registration number may already exist in the system.");
        }
    }
    
    // View a specific truck
    private void viewTruck() {
        System.out.println("\n=== View Truck ===");
        String regNumber = getStringInput("Enter truck registration number: ");
        
        Truck truck = truckController.getTruckByRegNumber(regNumber);
        
        if (truck != null) {
            displayTruckDetails(truck);
        } else {
            System.out.println("Truck not found in the system.");
        }
    }
    
    // View all trucks
    private void viewAllTrucks() {
        System.out.println("\n=== List of All Trucks ===");
        
        List<Truck> trucks = truckController.getAllTrucks();
        
        if (trucks.isEmpty()) {
            System.out.println("No trucks in the system.");
            return;
        }
        
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    // Search trucks by license type
    private void searchTrucksByLicenseType() {
        System.out.println("\n=== Search Trucks by License Type ===");
        System.out.println("Select license type:");
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
        
        List<Truck> trucks = truckController.getTrucksByLicenseType(licenseType);
        
        if (trucks.isEmpty()) {
            System.out.println("No trucks with license type " + licenseType);
            return;
        }
        
        System.out.println("Trucks with license type " + licenseType + ":");
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    // Search trucks by cargo capacity
    private void searchTrucksByCapacity() {
        System.out.println("\n=== Search Trucks by Capacity ===");
        double minCapacity = getDoubleInput("Enter minimum cargo capacity (kg): ");
        
        List<Truck> trucks = truckController.getTrucksByMinCapacity(minCapacity);
        
        if (trucks.isEmpty()) {
            System.out.println("No trucks with capacity greater than " + minCapacity + " kg");
            return;
        }
        
        System.out.println("Trucks with capacity greater than " + minCapacity + " kg:");
        for (Truck truck : trucks) {
            displayTruckDetails(truck);
            System.out.println("--------------------");
        }
    }
    
    // Add a new driver
    private void addNewDriver() {
        System.out.println("\n=== Add New Driver ===");
        
        String id = getStringInput("Enter driver ID: ");
        String name = getStringInput("Enter full name: ");
        String phone = getStringInput("Enter phone number: ");
        
        System.out.println("Select license type:");
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
        
        Driver newDriver = driverController.addDriver(id, name, phone, licenseType);
        
        if (newDriver != null) {
            System.out.println("Driver added successfully!");
        } else {
            System.out.println("Error adding driver. ID may already exist in the system.");
        }
    }
    
    // View a specific driver
    private void viewDriver() {
        System.out.println("\n=== View Driver ===");
        String id = getStringInput("Enter driver ID: ");
        
        Driver driver = driverController.getDriverById(id);
        
        if (driver != null) {
            displayDriverDetails(driver);
        } else {
            System.out.println("Driver not found in the system.");
        }
    }
    
    // View all drivers
    private void viewAllDrivers() {
        System.out.println("\n=== List of All Drivers ===");
        
        List<Driver> drivers = driverController.getAllDrivers();
        
        if (drivers.isEmpty()) {
            System.out.println("No drivers in the system.");
            return;
        }
        
        for (Driver driver : drivers) {
            displayDriverDetails(driver);
            System.out.println("--------------------");
        }
    }
    
    // Search drivers by license type
    private void searchDriversByLicenseType() {
        System.out.println("\n=== Search Drivers by License Type ===");
        System.out.println("Select license type:");
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
        
        List<Driver> drivers = driverController.getDriversByLicenseType(licenseType);
        
        if (drivers.isEmpty()) {
            System.out.println("No drivers with license type " + licenseType);
            return;
        }
        
        System.out.println("Drivers with license type " + licenseType + ":");
        for (Driver driver : drivers) {
            displayDriverDetails(driver);
            System.out.println("--------------------");
        }
    }
    
    // Display truck details
    private void displayTruckDetails(Truck truck) {
        System.out.println("Registration Number: " + truck.getRegNumber());
        System.out.println("Model: " + truck.getModel());
        System.out.println("Empty Weight: " + truck.getEmptyWeight() + " kg");
        System.out.println("Maximum Weight: " + truck.getMaxWeight() + " kg");
        System.out.println("Required License: " + truck.getRequiredLicense());
    }
    
    // Display driver details
    private void displayDriverDetails(Driver driver) {
        System.out.println("ID: " + driver.getId());
        System.out.println("Name: " + driver.getName());
        System.out.println("Phone: " + driver.getPhone());
        System.out.println("License Type: " + driver.getLicenseType());
    }
    
    // Check if user can manage trucks
    private boolean canManageTrucks() {
        return userController == null || userController.isAuthorized(sessionId, "VIEW", "TRUCK");
    }
    
    // Check if user can manage drivers
    private boolean canManageDrivers() {
        return userController == null || userController.isAuthorized(sessionId, "VIEW", "DRIVER");
    }
    
    // Show access denied message
    private void showAccessDenied(String action) {
        System.out.println("Access denied. You do not have permission " + action + ".");
        System.out.println("Please contact a system administrator if you need this access.");
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // Get integer input from user
    private int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Clean buffer
        return input;
    }
    
    // Get double input from user
    private double getDoubleInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextDouble()) {
            System.out.println("Please enter a valid number.");
            System.out.print(prompt);
            scanner.next();
        }
        double input = scanner.nextDouble();
        scanner.nextLine(); // Clean buffer
        return input;
    }
    
    // Get string input from user
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
