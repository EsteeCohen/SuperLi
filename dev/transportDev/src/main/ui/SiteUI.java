package transportDev.src.main.ui;

import java.util.List;
import java.util.Scanner;

import transportDev.src.main.controllers.FacadeController;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.ShippingZone;

public class SiteUI {
    private Scanner scanner;
    private FacadeController facadeController;

    /**
     * Site Management UI constructor
     */
    public SiteUI(FacadeController facadeController) {
        this.scanner = new Scanner(System.in);
        this.facadeController = facadeController;
    }

    /**
     * Start Site Management interface
     */
    public void start() {
        boolean exit = false;
        
        while (!exit) {
            displayMenu();
            int choice = getIntInput("Select an option: ");
            
            switch (choice) {
                case 1:
                    addNewSite();
                    break;
                case 2:
                    viewSite();
                    break;
                case 3:
                    viewAllSites();
                    break;
                case 4:
                    searchSitesByZone();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option, please try again");
            }
        }
    }

    /**
     * Display Site Management menu
     */
    private void displayMenu() {
        System.out.println("\n=== Site Management ===");
        System.out.println("1. Add New Site");
        System.out.println("2. View Site");
        System.out.println("3. View All Sites");
        System.out.println("4. Search Sites by Zone");
        System.out.println("0. Return to Main Menu");
    }

    /**
     * Add a new site
     */
    private void addNewSite() {
        System.out.println("\n=== Add New Site ===");
        
        String id = getStringInput("Enter site ID: ");
        String name = getStringInput("Enter site name: ");
        String address = getStringInput("Enter address: ");
        String contactPhone = getStringInput("Enter contact phone: ");
        String contactName = getStringInput("Enter contact name: ");
        
        System.out.println("Select shipping zone:");
        System.out.println("1. North");
        System.out.println("2. Center");
        System.out.println("3. South");
        System.out.println("4. Jerusalem");
        
        int zoneChoice = getIntInput("Your choice: ");
        String zone;
        
        switch (zoneChoice) {
            case 1:
                zone = "NORTH";
                break;
            case 2:
                zone = "CENTER";
                break;
            case 3:
                zone = "SOUTH";
                break;
            case 4:
                zone = "JERUSALEM";
                break;
            default:
                System.out.println("Invalid selection. Setting default: Center");
                zone = "CENTER";
        }
        
        Site site = facadeController.addSite(id, name, address, contactPhone, contactName, zone);
        
        if (site != null) {
            System.out.println("Site added successfully!");
            displaySiteDetails(site);
        } else {
            System.out.println("Error adding site. Please check the input values and try again.");
        }
    }

    /**
     * View a specific site
     */
    private void viewSite() {
        String id = getStringInput("Enter site ID: ");
        Site site = facadeController.getSiteById(id);
        
        if (site != null) {
            displaySiteDetails(site);
        } else {
            System.out.println("Site not found.");
        }
    }

    /**
     * View all sites
     */
    private void viewAllSites() {
        List<Site> sites = facadeController.getAllSites();
        
        if (sites.isEmpty()) {
            System.out.println("No sites in the system.");
        } else {
            System.out.println("\n=== All Sites ===");
            for (Site site : sites) {
                displaySiteDetails(site);
                System.out.println("------------------------");
            }
        }
    }

    /**
     * Search sites by shipping zone
     */
    private void searchSitesByZone() {
        System.out.println("\nSelect shipping zone:");
        System.out.println("1. North");
        System.out.println("2. Center");
        System.out.println("3. South");
        System.out.println("4. Jerusalem");
        
        int zoneChoice = getIntInput("Your choice: ");
        String zone;
        
        switch (zoneChoice) {
            case 1:
                zone = "NORTH";
                break;
            case 2:
                zone = "CENTER";
                break;
            case 3:
                zone = "SOUTH";
                break;
            case 4:
                zone = "JERUSALEM";
                break;
            default:
                System.out.println("Invalid selection.");
                return;
        }
        
        List<Site> sites = facadeController.getSitesByZone(zone);
        
        if (sites == null || sites.isEmpty()) {
            System.out.println("No sites found in zone: " + zone);
        } else {
            System.out.println("\n=== Sites in zone: " + zone + " ===");
            for (Site site : sites) {
                displaySiteDetails(site);
                System.out.println("------------------------");
            }
        }
    }

    /**
     * Display site details
     */
    private void displaySiteDetails(Site site) {
        System.out.println("ID: " + site.getId());
        System.out.println("Name: " + site.getName());
        System.out.println("Address: " + site.getAddress());
        System.out.println("Contact Phone: " + site.getContactPhone());
        System.out.println("Contact Name: " + site.getContactName());
        System.out.println("Shipping Zone: " + site.getShippingZone());
    }

    /**
     * Get integer input from user
     */
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

    /**
     * Get string input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
