package src.main.ui;

import java.util.List;
import java.util.Scanner;

import src.main.controllers.SiteController;
import src.main.entities.Site;

public class SiteUI {
    private Scanner scanner;
    private SiteController siteController;

    /**
     * Site Management UI constructor
     */
    public SiteUI(SiteController siteController) {
        this.scanner = new Scanner(System.in);
        this.siteController = siteController;
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
        
        Site site = siteController.addSite(id, name, address, contactPhone, contactName, zone);
        
        if (site != null) {
            System.out.println("Site added successfully!");
        } else {
            System.out.println("Error adding site. ID may already exist in the system.");
        }
    }

    /**
     * View a specific site
     */
    private void viewSite() {
        System.out.println("\n=== View Site ===");
        String id = getStringInput("Enter site ID: ");
        
        Site site = siteController.getSiteById(id);
        
        if (site != null) {
            displaySiteDetails(site);
        } else {
            System.out.println("Site not found in the system.");
        }
    }

    /**
     * View all sites
     */
    private void viewAllSites() {
        System.out.println("\n=== List of All Sites ===");
        
        List<Site> sites = siteController.getAllSites();
        
        if (sites.isEmpty()) {
            System.out.println("No sites in the system.");
            return;
        }
        
        for (Site site : sites) {
            displaySiteDetails(site);
            System.out.println("--------------------");
        }
    }

    /**
     * Search sites by shipping zone
     */
    private void searchSitesByZone() {
        System.out.println("\n=== Search Sites by Zone ===");
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
                System.out.println("Invalid selection. Showing Center zone sites as default.");
                zone = "CENTER";
        }
        
        List<Site> sites = siteController.getSitesByZone(zone);
        
        if (sites.isEmpty()) {
            System.out.println("No sites in this zone.");
            return;
        }
        
        System.out.println("Sites in zone " + zone + ":");
        for (Site site : sites) {
            displaySiteDetails(site);
            System.out.println("--------------------");
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

    /**
     * Get string input from user
     */
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
