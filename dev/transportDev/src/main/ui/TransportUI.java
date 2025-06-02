package transportDev.src.main.ui;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import transportDev.src.main.controllers.FacadeController;
import transportDev.src.main.entities.*;
import transportDev.src.main.enums.ShippingZone;
import transportDev.src.main.enums.TransportStatus;

public class TransportUI {
    private Scanner scanner;
    private FacadeController facadeController;
    
    // For managing draft transports
    private TransportDraft currentDraft;
    
    public TransportUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
    }
    
    public void start() {
        boolean exit = false;

        while (!exit) {
            displayTransportMenu();
            int choice = getIntInput("Select option: ");

            switch (choice) {
                case 1:
                    createNewTransport();
                    break;
                case 2:
                    viewTransports();
                    break;
                case 3:
                    updateTransportStatus();
                    break;
                case 4:
                    manageTransportCargo();
                    break;
                case 5:
                    changeDriverOrTruck();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose a number from 0-5.");
                    pressEnterToContinue();
            }
        }
    }

    private void displayTransportMenu() {
        System.out.println("\n=== Transport Management ===");
        System.out.println();
        System.out.println("1. Create New Transport");
        System.out.println("2. View Transports");
        System.out.println("   - All Transports");
        System.out.println("   - By Date");
        System.out.println("   - By Status");
        System.out.println("   - By Shipping Zone");
        System.out.println("3. Update Transport Status");
        System.out.println("4. Manage Transport Cargo");
        System.out.println("5. Change Driver/Truck");
        System.out.println("0. Back to Main Menu");
        System.out.println();
    }

    private void createNewTransport() {
        System.out.println("\n=== Create New Transport ===");
        System.out.println();
        System.out.println("[Auto-save at each step - you can return to draft or continue from last step]");
        System.out.println();
        
        // Check if there's an existing draft
        if (currentDraft != null) {
            System.out.println("You have an existing draft.");
            currentDraft.displayProgress();
            if (confirmContinue("Continue with existing draft?")) {
                transportCreationFlow();
                return;
            } else {
                currentDraft = null;
            }
        }
        
        // Create new draft
        this.currentDraft = new TransportDraft();
        System.out.println("New draft created.");
        pressEnterToContinue();
        
        // Start the step-by-step process
        transportCreationFlow();
    }
    
    private void transportCreationFlow() {
        boolean completed = false;
        
        while (!completed && currentDraft != null) {
            clearScreen();
            displayCreationHeader();
            
            switch (currentDraft.getCurrentStep()) {
                case 0: // Basic Details
                    if (stepBasicDetails()) {
                        currentDraft.nextStep();
                        currentDraft.save();
                    }
                    break;
                case 1: // Route Planning
                    if (stepRoutePlanning()) {
                        currentDraft.nextStep();
                        currentDraft.save();
                    }
                    break;
                case 2: // Vehicle Assignment
                    if (stepVehicleAssignment()) {
                        currentDraft.nextStep();
                        currentDraft.save();
                    }
                    break;
                case 3: // Final Review
                    if (stepFinalReview()) {
                        completed = true;
                    }
                    break;
            }
            
            if (!completed && currentDraft != null) {
                displayNavigationMenu();
                int choice = getIntInput("👉 What would you like to do: ");
                
                switch (choice) {
                    case 1: // Continue
                        // Just continue to next iteration
                        break;
                    case 2: // Go back
                        if (currentDraft.getCurrentStep() > 0) {
                            currentDraft.previousStep();
                        } else {
                            System.out.println("⚠️ Already at first step.");
                            pressEnterToContinue();
                        }
                        break;
                    case 3: // Save and exit
                        currentDraft.save();
                        System.out.println("💾 Draft saved successfully!");
                        System.out.println("✅ You can resume later from Transport Menu.");
                        pressEnterToContinue();
                        return;
                    case 4: // Cancel
                        if (confirmContinue("Are you sure you want to discard all changes?")) {
                            currentDraft = null;
                            System.out.println("❌ Changes discarded.");
                            pressEnterToContinue();
                        }
                        return;
                    default:
                        System.out.println("❌ Invalid choice.");
                        pressEnterToContinue();
                }
            }
        }
        
        if (completed) {
            currentDraft = null; // Clear draft after successful completion
        }
    }
    
    private void displayCreationHeader() {
        System.out.println("🚛 TRANSPORT CREATION WIZARD");
        System.out.println("=".repeat(35));
        currentDraft.displayProgress();
        System.out.println("");
    }
    
    private void displayNavigationMenu() {
        System.out.println("\n🧭 NAVIGATION OPTIONS");
        System.out.println("─".repeat(20));
        System.out.println("1. ➡️  Continue to next step");
        System.out.println("2. ⬅️  Go back to previous step");
        System.out.println("3. 💾 Save draft and exit");
        System.out.println("4. ❌ Cancel (discard changes)");
        System.out.println("");
    }
    
    private boolean stepBasicDetails() {
        System.out.println("📅 STEP 1: BASIC DETAILS");
        System.out.println("─".repeat(25));
        
        if (currentDraft.getDate() != null) {
            System.out.println("📋 Current information:");
            System.out.println("Date: " + currentDraft.getDate().format(
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Time: " + currentDraft.getTime());
            
            if (confirmContinue("Keep current date and time?")) {
                return true;
            }
        }
        
        // Date input with validation
        LocalDate date = null;
        while (date == null) {
            String dateStr = getStringInput("📅 Enter transport date (DD/MM/YYYY): ");
            if (dateStr.isEmpty()) {
                System.out.println("❌ Date is required.");
                continue;
            }
            
            try {
                String[] parts = dateStr.split("/");
                if (parts.length == 3) {
                    int day = Integer.parseInt(parts[0]);
                    int month = Integer.parseInt(parts[1]);
                    int year = Integer.parseInt(parts[2]);
                    date = LocalDate.of(year, month, day);
                    
                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("⚠️ Cannot select a date in the past.");
                        date = null;
                        continue;
                    }
                    
                    if (date.isAfter(LocalDate.now().plusMonths(6))) {
                        System.out.println("⚠️ Cannot schedule transport more than 6 months ahead.");
                        date = null;
                        continue;
                    }
                } else {
                    throw new Exception("Invalid format");
                }
            } catch (Exception e) {
                System.out.println("❌ Invalid date format. Please use DD/MM/YYYY (e.g., 25/12/2024)");
            }
        }
        
        // Time input with validation
        LocalTime time = null;
        while (time == null) {
            String timeStr = getStringInput("🕐 Enter transport time (HH:MM): ");
            if (timeStr.isEmpty()) {
                System.out.println("❌ Time is required.");
                continue;
            }
            
            try {
                time = LocalTime.parse(timeStr);
                
                // Check if it's a reasonable time (between 6:00 and 22:00)
                if (time.isBefore(LocalTime.of(6, 0)) || time.isAfter(LocalTime.of(22, 0))) {
                    System.out.println("⚠️ Transport time should be between 06:00 and 22:00.");
                    if (!confirmContinue("Continue with this time anyway?")) {
                        time = null;
                        continue;
                    }
                }
                
            } catch (DateTimeParseException e) {
                System.out.println("❌ Invalid time format. Please use HH:MM (e.g., 14:30)");
            }
        }
        
        currentDraft.setDate(date);
        currentDraft.setTime(time);
        
        System.out.println("✅ Basic details saved!");
        System.out.println("📅 Date: " + date.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("🕐 Time: " + time);
        
        return true;
    }
    
    private boolean stepRoutePlanning() {
        System.out.println("🗺️ STEP 2: ROUTE PLANNING");
        System.out.println("─".repeat(25));
        
        if (currentDraft.getSourceSiteId() != null) {
            System.out.println("📋 Current route:");
            System.out.println("🏢 Source: " + getSiteName(currentDraft.getSourceSiteId()));
            if (currentDraft.getDestinationIds() != null && !currentDraft.getDestinationIds().isEmpty()) {
                System.out.println("🎯 Destinations:");
                for (String destId : currentDraft.getDestinationIds()) {
                    System.out.println("  → " + getSiteName(destId));
                }
            }
            if (confirmContinue("Keep current route?")) {
                return true;
            }
        }
        
        // Source site selection
        System.out.println("🏢 SELECTING SOURCE SITE");
        System.out.println("─".repeat(22));
        String sourceSiteId = selectSite("Choose the starting location for this transport:");
        if (sourceSiteId == null) {
            System.out.println("❌ Source site is required.");
            return false;
        }
        
        Site sourceSite = facadeController.getSiteById(sourceSiteId);
        ShippingZone transportZone = sourceSite.getShippingZone();
        
        System.out.println("✅ Source site selected: " + sourceSite.getName());
        System.out.println("🌐 Transport zone: " + transportZone);
        System.out.println("ℹ️ All destinations must be in the same zone (" + transportZone + ")");
        
        // Destinations selection
        System.out.println("\n🎯 SELECTING DESTINATIONS");
        System.out.println("─".repeat(22));
        List<String> destinationIds = new ArrayList<>();
        
        while (true) {
            String destId = selectSiteInZone("Select destination site:", transportZone, destinationIds);
            if (destId == null) {
                if (destinationIds.isEmpty()) {
                    System.out.println("⚠️ At least one destination is required.");
                    continue;
                } else {
                    break;
                }
            }
            
            destinationIds.add(destId);
            System.out.println("✅ Added destination: " + getSiteName(destId));
            System.out.println("📊 Total destinations: " + destinationIds.size());
            
            if (!confirmContinue("Add another destination?")) {
                break;
            }
        }
        
        currentDraft.setSourceSiteId(sourceSiteId);
        currentDraft.setDestinationIds(destinationIds);
        currentDraft.setTransportZone(transportZone);
        
        System.out.println("\n✅ Route planning completed!");
        displayRouteSummary();
        
        return true;
    }
    
    private void displayRouteSummary() {
        System.out.println("\n📍 ROUTE SUMMARY");
        System.out.println("─".repeat(15));
        System.out.println("🏢 Source: " + getSiteName(currentDraft.getSourceSiteId()));
        System.out.println("🌐 Zone: " + currentDraft.getTransportZone());
        System.out.println("🎯 Destinations:");
        for (int i = 0; i < currentDraft.getDestinationIds().size(); i++) {
            String destId = currentDraft.getDestinationIds().get(i);
            System.out.println("  " + (i + 1) + ". " + getSiteName(destId));
        }
    }
    
    private boolean stepVehicleAssignment() {
        System.out.println("🚛 STEP 3: VEHICLE ASSIGNMENT");
        System.out.println("─".repeat(29));
        
        if (currentDraft.getTruckId() != null) {
            System.out.println("📋 Current assignment:");
            System.out.println("🚛 Truck: " + getTruckName(currentDraft.getTruckId()));
            System.out.println("👨‍✈️ Driver: " + getDriverName(currentDraft.getDriverId()));
            if (confirmContinue("Keep current vehicle assignment?")) {
                return true;
            }
        }
        
        // Calculate required capacity
        double estimatedWeight = getEstimatedWeight();
        
        System.out.println("🚛 SELECTING TRUCK");
        System.out.println("─".repeat(16));
        System.out.println("📦 Estimated cargo weight: " + estimatedWeight + " kg");
        
        String truckId = selectAppropriaTruck(estimatedWeight);
        if (truckId == null) {
            System.out.println("❌ Truck selection is required.");
            return false;
        }
        
        Truck selectedTruck = facadeController.getTruckById(truckId);
        System.out.println("✅ Truck selected: " + selectedTruck.getRegNumber() + " (" + selectedTruck.getModel() + ")");
        System.out.println("⚖️ Capacity: " + selectedTruck.getMaxWeight() + " kg");
        System.out.println("🪪 Required license: " + selectedTruck.getLicenseType());
        
        // Select driver with matching license
        System.out.println("\n👨‍✈️ SELECTING DRIVER");
        System.out.println("─".repeat(17));
        System.out.println("🪪 Required license type: " + selectedTruck.getLicenseType());
        
        String driverId = selectAvailableDriver(selectedTruck.getLicenseType().toString());
        if (driverId == null) {
            System.out.println("❌ Driver selection is required.");
            return false;
        }
        
        Driver selectedDriver = facadeController.getDriverById(driverId);
        System.out.println("✅ Driver selected: " + selectedDriver.getName());
        System.out.println("🪪 License: " + selectedDriver.getLicenseType());
        
        currentDraft.setTruckId(truckId);
        currentDraft.setDriverId(driverId);
        
        System.out.println("\n✅ Vehicle assignment completed!");
        displayVehicleAssignmentSummary();
        
        return true;
    }
    
    private double getEstimatedWeight() {
        // Use service layer for business logic calculation
        return facadeController.calculateEstimatedWeight(currentDraft.getDestinationIds());
    }
    
    private void displayVehicleAssignmentSummary() {
        System.out.println("\n🚛 VEHICLE ASSIGNMENT SUMMARY");
        System.out.println("─".repeat(30));
        Truck truck = facadeController.getTruckById(currentDraft.getTruckId());
        Driver driver = facadeController.getDriverById(currentDraft.getDriverId());
        
        System.out.println("🚛 Truck: " + truck.getRegNumber() + " (" + truck.getModel() + ")");
        System.out.println("⚖️ Max capacity: " + truck.getMaxWeight() + " kg");
        System.out.println("👨‍✈️ Driver: " + driver.getName());
        System.out.println("🪪 License: " + driver.getLicenseType());
        System.out.println("🔗 License match: ✅ Compatible");
    }
    
    private boolean stepFinalReview() {
        System.out.println("📋 STEP 4: FINAL REVIEW & CONFIRMATION");
        System.out.println("─".repeat(38));
        
        displayCompleteSummary();
        
        System.out.println("\n❓ CONFIRMATION");
        System.out.println("─".repeat(13));
        if (!confirmContinue("Create this transport?")) {
            return false;
        }
        
        // Create the actual transport
        System.out.print("🔄 Creating transport");
        showLoadingAnimation();
        
        Transport transport = facadeController.createTransport(
            currentDraft.getDate(),
            currentDraft.getTime(),
            currentDraft.getTruckId(),
            currentDraft.getDriverId(),
            currentDraft.getSourceSiteId(),
            currentDraft.getDestinationIds()
        );
        
        if (transport != null) {
            displaySuccessMessage(transport);
            showPostCreationOptions(transport);
        } else {
            System.out.println("\n❌ TRANSPORT CREATION FAILED!");
            System.out.println("🚫 There was an error creating the transport.");
            System.out.println("💡 Please check all details and try again.");
            pressEnterToContinue();
            return false;
        }
        
        return true;
    }
    
    private void displayCompleteSummary() {
        System.out.println("📊 COMPLETE TRANSPORT SUMMARY");
        System.out.println("=".repeat(30));
        
        // Basic details
        System.out.println("📅 SCHEDULE");
        System.out.println("Date: " + currentDraft.getDate().format(
            java.time.format.DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));
        System.out.println("Time: " + currentDraft.getTime());
        
        // Route
        System.out.println("\n🗺️ ROUTE");
        System.out.println("Source: " + getSiteName(currentDraft.getSourceSiteId()));
        System.out.println("Zone: " + currentDraft.getTransportZone());
        System.out.println("Destinations:");
        for (int i = 0; i < currentDraft.getDestinationIds().size(); i++) {
            String destId = currentDraft.getDestinationIds().get(i);
            System.out.println("  " + (i + 1) + ". " + getSiteName(destId));
        }
        
        // Vehicle assignment
        System.out.println("\n🚛 VEHICLE ASSIGNMENT");
        Truck truck = facadeController.getTruckById(currentDraft.getTruckId());
        Driver driver = facadeController.getDriverById(currentDraft.getDriverId());
        System.out.println("Truck: " + truck.getRegNumber() + " (" + truck.getModel() + ")");
        System.out.println("Capacity: " + truck.getMaxWeight() + " kg");
        System.out.println("Driver: " + driver.getName());
        System.out.println("License: " + driver.getLicenseType());
    }
    
    private void displaySuccessMessage(Transport transport) {
        System.out.println("\n=== Transport Created Successfully! ===");
        System.out.println("Transport ID: #" + transport.getId());
        System.out.println("Scheduled for: " + transport.getDate().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " at " + transport.getTime());
        System.out.println("Status: " + getStatusDisplay(transport.getStatus()));
        System.out.println("Assigned truck: " + transport.getTruck().getRegNumber());
        System.out.println("Assigned driver: " + transport.getDriver().getName());
    }
    
    private void showPostCreationOptions(Transport transport) {
        System.out.println("\n=== What's Next? ===");
        System.out.println("1. Add items to this transport");
        System.out.println("2. View all transports");
        System.out.println("3. Create another transport");
        System.out.println("4. Manage this transport");
        System.out.println("0. Return to Transport Menu");
        
        int choice = getIntInput("Select option: ");
        switch (choice) {
            case 1:
                System.out.println("Add items functionality - Coming soon");
                pressEnterToContinue();
                break;
            case 2:
                viewAllTransports();
                break;
            case 3:
                createNewTransport();
                break;
            case 4:
                System.out.println("Transport management functionality - Coming soon");
                pressEnterToContinue();
                break;
            case 0:
            default:
                // Return to menu
                break;
        }
    }
    
    // View transports section
    private void viewTransports() {
        System.out.println("\n=== View Transports ===");
        System.out.println();
        System.out.println("1. All Transports");
        System.out.println("2. By Date");
        System.out.println("3. By Status");
        System.out.println("4. By Shipping Zone");
        System.out.println("0. Back");
        System.out.println();
        
        int choice = getIntInput("Select option: ");
        switch (choice) {
            case 1:
                viewAllTransports();
                break;
            case 2:
                viewTransportsByDate();
                break;
            case 3:
                viewTransportsByStatus();
                break;
            case 4:
                viewTransportsByZone();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid option.");
                pressEnterToContinue();
        }
    }
    
    private void viewAllTransports() {
        System.out.println("\n=== All Transports ===");
        
        List<Transport> transports = facadeController.getAllTransports();
        if (transports.isEmpty()) {
            System.out.println("No transports found in the system.");
            System.out.println("Create your first transport using the main menu.");
        } else {
            System.out.println("Found " + transports.size() + " transport(s):");
            System.out.println();
            
            for (int i = 0; i < transports.size(); i++) {
                Transport t = transports.get(i);
                System.out.println("#" + (i + 1) + " Transport ID: " + t.getId());
                displayTransportInfo(t);
                System.out.println("─".repeat(40));
            }
        }
        pressEnterToContinue();
    }

    // Helper methods for site/truck/driver selection
    private String selectSite(String prompt) {
        System.out.println("🏢 " + prompt);
        List<Site> sites = facadeController.getAllSites();
        if (sites.isEmpty()) {
            System.out.println("❌ No sites available in the system.");
            pressEnterToContinue();
            return null;
        }
        
        System.out.println("📍 Available sites:");
        for (int i = 0; i < sites.size(); i++) {
            Site site = sites.get(i);
            System.out.println((i + 1) + ". " + site.getName() + " (" + site.getShippingZone() + ")");
        }
        
        int choice = getIntInput("👉 Select site (number): ");
        if (choice > 0 && choice <= sites.size()) {
            return sites.get(choice - 1).getId();
        }
        
        System.out.println("❌ Invalid selection.");
        return null;
    }
    
    private String selectSiteInZone(String prompt, ShippingZone zone, List<String> excludeIds) {
        System.out.println("🏢 " + prompt);
        List<Site> sites = facadeController.getAllSites();
        List<Site> zoneSites = sites.stream()
            .filter(site -> site.getShippingZone() == zone)
            .filter(site -> !excludeIds.contains(site.getId()))
            .collect(java.util.stream.Collectors.toList());
        
        if (zoneSites.isEmpty()) {
            System.out.println("❌ No available sites in zone " + zone);
            System.out.println("ℹ️ All destinations must be in the same shipping zone.");
            return null;
        }
        
        System.out.println("📍 Available sites in " + zone + ":");
        for (int i = 0; i < zoneSites.size(); i++) {
            Site site = zoneSites.get(i);
            System.out.println((i + 1) + ". " + site.getName());
        }
        System.out.println("0. ❌ Skip/Cancel");
        
        int choice = getIntInput("👉 Select site (number, 0 to skip): ");
        if (choice == 0) {
            return null;
        }
        if (choice > 0 && choice <= zoneSites.size()) {
            return zoneSites.get(choice - 1).getId();
        }
        
        System.out.println("❌ Invalid selection.");
        return null;
    }
    
    private String selectAppropriaTruck(double requiredCapacity) {
        System.out.println("🚛 Finding suitable trucks...");
        System.out.println("📦 Minimum capacity required: " + requiredCapacity + " kg");
        
        List<Truck> suitableTrucks = facadeController.getAvailableTrucksWithCapacity(requiredCapacity);
        
        if (suitableTrucks.isEmpty()) {
            System.out.println("❌ No suitable trucks available.");
            System.out.println("💡 Try with lower weight requirement or check truck availability.");
            pressEnterToContinue();
            return null;
        }
        
        System.out.println("✅ Found " + suitableTrucks.size() + " suitable truck(s):");
        for (int i = 0; i < suitableTrucks.size(); i++) {
            Truck truck = suitableTrucks.get(i);
            System.out.println((i + 1) + ". 🚛 " + truck.getRegNumber() + 
                             " (" + truck.getModel() + ")");
            System.out.println("   ⚖️ Capacity: " + truck.getMaxWeight() + " kg");
            System.out.println("   🪪 License required: " + truck.getLicenseType());
            System.out.println("   📊 Available space: " + (truck.getMaxWeight() - requiredCapacity) + " kg");
        }
        
        int choice = getIntInput("👉 Select truck (number): ");
        if (choice > 0 && choice <= suitableTrucks.size()) {
            return suitableTrucks.get(choice - 1).getRegNumber();
        }
        
        System.out.println("❌ Invalid selection.");
        return null;
    }
    
    private String selectAvailableDriver(String requiredLicense) {
        System.out.println("👨‍✈️ Finding drivers with " + requiredLicense + " license...");
        
        List<Driver> suitableDrivers = facadeController.getAvailableDriversWithLicense(requiredLicense);
        
        if (suitableDrivers.isEmpty()) {
            System.out.println("❌ No available drivers with " + requiredLicense + " license.");
            System.out.println("💡 Check driver schedules or contact fleet management.");
            pressEnterToContinue();
            return null;
        }
        
        System.out.println("✅ Found " + suitableDrivers.size() + " available driver(s):");
        for (int i = 0; i < suitableDrivers.size(); i++) {
            Driver driver = suitableDrivers.get(i);
            System.out.println((i + 1) + ". 👨‍✈️ " + driver.getName() + 
                             " (ID: " + driver.getId() + ")");
            System.out.println("   🪪 License: " + driver.getLicenseType());
            System.out.println("   📞 Phone: " + driver.getPhone());
        }
        
        int choice = getIntInput("👉 Select driver (number): ");
        if (choice > 0 && choice <= suitableDrivers.size()) {
            return suitableDrivers.get(choice - 1).getId();
        }
        
        System.out.println("❌ Invalid selection.");
        return null;
    }

    // Additional helper methods and existing transport management
    /*
    private void manageSpecificTransport(Transport transport) {
        // This method is unused - commenting out to remove warnings
    }
    
    private void manageTransportItems(int transportId) {
        // This method is unused - commenting out to remove warnings  
    }
    */
    
    // Display and utility methods
    private void displayTransportInfo(Transport transport) {
        System.out.println("Transport Details:");
        System.out.println("ID: " + transport.getId());
        System.out.println("Date: " + transport.getDate().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("Time: " + transport.getTime());
        System.out.println("Status: " + getStatusDisplay(transport.getStatus()));
        System.out.println("Truck: " + getTruckName(transport.getTruck().getRegNumber()));
        System.out.println("Driver: " + getDriverName(transport.getDriver().getId()));
        System.out.println("Route:");
        System.out.println("  From: " + getSiteName(transport.getSourceSite().getId()));
        for (Site dest : transport.getDestinations()) {
            System.out.println("  To: " + getSiteName(dest.getId()));
        }
    }
    
    private String getStatusDisplay(TransportStatus status) {
        switch (status) {
            case PLANNING: return "Planning";
            case ACTIVE: return "Active";
            case COMPLETED: return "Completed";
            case CANCELLED: return "Cancelled";
            default: return status.toString();
        }
    }
    
    private String getSiteName(String siteId) {
        Site site = facadeController.getSiteById(siteId);
        return site != null ? site.getName() : "Unknown Site";
    }
    
    private String getTruckName(String truckId) {
        Truck truck = facadeController.getTruckById(truckId);
        return truck != null ? truck.getRegNumber() + " (" + truck.getModel() + ")" : "Unknown Truck";
    }
    
    private String getDriverName(String driverId) {
        Driver driver = facadeController.getDriverById(driverId);
        return driver != null ? driver.getName() : "Unknown Driver";
    }
    
    private void showLoadingAnimation() {
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(400);
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            // Handle interruption
        }
    }
    
    private void clearScreen() {
        // Simple clear screen simulation
        for (int i = 0; i < 3; i++) {
            System.out.println();
        }
    }
    
    // Input and confirmation methods
    private boolean confirmContinue(String message) {
        System.out.print(message + " (yes/no): ");
        String answer = scanner.nextLine().trim().toLowerCase();
        return answer.equals("yes") || answer.equals("y");
    }
    
    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    private void pressEnterToContinue() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    // Placeholder methods for features to be implemented
    private void viewTransportsByDate() {
        System.out.println("\nBy Date view - Coming soon");
        pressEnterToContinue();
    }
    
    private void viewTransportsByStatus() {
        System.out.println("\nBy Status view - Coming soon");
        pressEnterToContinue();
    }
    
    private void viewTransportsByZone() {
        System.out.println("\nBy Zone view - Coming soon");
        pressEnterToContinue();
    }
    
    private void updateTransportStatus() {
        System.out.println("\n=== Update Transport Status ===");
        System.out.println("Status update functionality - Coming soon");
        pressEnterToContinue();
    }
    
    private void manageTransportCargo() {
        System.out.println("\n=== Manage Transport Cargo ===");
        System.out.println("Cargo management functionality - Coming soon");
        pressEnterToContinue();
    }
    
    private void changeDriverOrTruck() {
        System.out.println("\n=== Change Driver/Truck ===");
        System.out.println("Driver/Truck change functionality - Coming soon");
        pressEnterToContinue();
    }
}

// Enhanced TransportDraft helper class
class TransportDraft {
    private LocalDate date;
    private LocalTime time;
    private String sourceSiteId;
    private List<String> destinationIds;
    private ShippingZone transportZone;
    private String truckId;
    private String driverId;
    private int currentStep = 0;
    private LocalTime lastModified;
    
    public TransportDraft() {
        this.lastModified = LocalTime.now();
        this.destinationIds = new ArrayList<>();
    }
    
    public void save() {
        this.lastModified = LocalTime.now();
        System.out.println("Draft auto-saved at " + lastModified.format(
            java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
    }
    
    public void nextStep() {
        if (currentStep < 3) {
            currentStep++;
            save();
        }
    }
    
    public void previousStep() {
        if (currentStep > 0) {
            currentStep--;
            save();
        }
    }
    
    public void displayProgress() {
        System.out.println("Progress Tracker:");
        String[] steps = {
            "Basic Details (Date, Time)",
            "Route Planning (Source, Destinations)", 
            "Vehicle Assignment (Truck, Driver)",
            "Final Review"
        };
        
        for (int i = 0; i < steps.length; i++) {
            String status = i < currentStep ? "[DONE]" : 
                           i == currentStep ? "[CURRENT]" : "[TODO]";
            System.out.println(status + " Step " + (i + 1) + ": " + steps[i]);
        }
        
        int progress = (int) ((currentStep / 3.0) * 100);
        System.out.println("Overall progress: " + progress + "%");
        System.out.println();
    }
    
    // Getters and setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    
    public String getSourceSiteId() { return sourceSiteId; }
    public void setSourceSiteId(String sourceSiteId) { this.sourceSiteId = sourceSiteId; }
    
    public List<String> getDestinationIds() { return destinationIds; }
    public void setDestinationIds(List<String> destinationIds) { this.destinationIds = destinationIds; }
    
    public ShippingZone getTransportZone() { return transportZone; }
    public void setTransportZone(ShippingZone transportZone) { this.transportZone = transportZone; }
    
    public String getTruckId() { return truckId; }
    public void setTruckId(String truckId) { this.truckId = truckId; }
    
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }
    
    public int getCurrentStep() { return currentStep; }
    
    public LocalTime getLastModified() { return lastModified; }
}
